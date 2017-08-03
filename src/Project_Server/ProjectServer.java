package Project_Server;

// ProjectServer
// Programmer: Easy Group
// Last Modified: 9/24/16
import ProjectResources.AMathConstants;
import ProjectResources.BoardTiles.BoardTile;
import ProjectResources.PlayTiles.PlayTile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ThreadFactory;

public class ProjectServer extends Application implements AMathConstants {
    private ServerSocket serverSocket;
    private Socket player1;
    private Socket player2;
    private ObjectOutputStream outP1;
    private ObjectOutputStream outP2;
    private ObjectInputStream inP1;
    private ObjectInputStream inP2;
    private DataInputStream inP1Data;
    private DataInputStream inP2Data;
    private DataOutputStream outP1Data;
    private DataOutputStream outP2Data;

    TextArea taLog = new TextArea();
    private boolean isConnected, isGameContinued;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) {

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(taLog), 500, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project Server");
        primaryStage.show();

        isConnected = false;


        primaryStage.setOnCloseRequest(event -> {
            isConnected = false;
            isGameContinued = false;

            try {
                inP1.close();
                inP2.close();
                outP1.close();
                outP2.close();
                player1.close();
                player2.close();
                serverSocket.close();
            }
            catch (Exception e) {}

            System.exit(0);
        });


        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(2705);
                Platform.runLater(() -> {
                    taLog.appendText(new Date() + ": Server started at socket 2705\n");
                });

                Platform.runLater(() -> {
                    taLog.appendText(new Date() + ": Waiting for players to join\n");
                });

                player1 = serverSocket.accept();
                Platform.runLater(() -> {
                    taLog.appendText(new Date() + ": player1 " + player1.getInetAddress().getHostAddress() + " joined\n");
                });


                outP1 = new ObjectOutputStream(player1.getOutputStream());
                outP1Data = new DataOutputStream(player1.getOutputStream());
                outP1Data.writeInt(AMathConstants.PLAYER1);
                outP1.flush();

                inP1 = new ObjectInputStream(player1.getInputStream());
                inP1Data = new DataInputStream(player1.getInputStream());
                Platform.runLater(() -> {
                    taLog.appendText("Stream for player1 is set up..\n");
                });

                outP1.writeObject("You are player 1");
                outP1.flush();

                outP1.writeObject("Wait for player 2 to join...");
                outP1.flush();

                PlayTile[] playTiles = (PlayTile[]) inP1.readObject();
                Platform.runLater(() -> {
                    taLog.appendText(new Date() + ": take the bag from Player 1\n");
                });


                player2 = serverSocket.accept();
                Platform.runLater(() -> {
                    taLog.appendText(new Date() + ": player2 " + player2.getInetAddress().getHostAddress() + " joined\n");
                });

                outP2 = new ObjectOutputStream(player2.getOutputStream());
                outP2Data = new DataOutputStream(player2.getOutputStream());
                outP2Data.writeInt(AMathConstants.PLAYER2);
                outP2.flush();

                inP2 = new ObjectInputStream(player2.getInputStream());
                inP2Data = new DataInputStream(player2.getInputStream());
                Platform.runLater(() -> {
                    taLog.appendText("Stream for player2 is set up..\n");
                });

                outP2.writeObject("you are player 2");
                outP2.flush();

                //PlayTile playTile = (PlayTile) inP1.readObject();
                //outP2.writeObject(playTile);

                outP2.writeObject(playTiles);
                outP2.flush();
                Platform.runLater(() -> {
                    taLog.appendText(new Date() + ": send the bag to Player 2\n");
                });

                playTiles = (PlayTile[]) inP2.readObject();
                outP1.writeObject(playTiles);
                outP1.flush();

                isConnected = true;
                isGameContinued = true;
                
                outP1.writeObject("Game started...");
                outP2.writeObject("Game started...");

                new Thread(new ProcessingGame(inP1, outP2, inP1Data, outP2Data, player1.getInetAddress().getHostAddress())).start();
                new Thread(new ProcessingGame(inP2, outP1, inP2Data, outP1Data, player2.getInetAddress().getHostAddress())).start();
                //new Thread(new Chatting(inP1, outP2, player1.getInetAddress().getHostAddress())).start();
                //new Thread(new Chatting(inP2, outP1, player2.getInetAddress().getHostAddress())).start();
                //new Thread(new Playing(inP1, outP2, player1.getInetAddress().getHostAddress())).start();
                //new Thread(new Playing(inP2, outP1, player2.getInetAddress().getHostAddress())).start();
            }
            catch (Exception ex) {

            }
        }).start();
    }

    class ProcessingGame implements Runnable {
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private DataInputStream inputStreamData;
        private DataOutputStream outputStreamData;
        private String ip;

        ProcessingGame(ObjectInputStream in, ObjectOutputStream out,
                       DataInputStream inData, DataOutputStream outData, String ip) {
            this.inputStream = in;
            this.outputStream = out;
            this.inputStreamData = inData;
            this.outputStreamData = outData;
            this.ip = ip;
        }

        @Override
        public void run() {
            while (isConnected) {
                try {
                    int code = inputStreamData.readInt();
                    if (code == CHAT) {
                        String message = inputStream.readObject().toString();
                        System.out.println("Receive: " +  message);

                        outputStreamData.writeInt(CHAT);
                        outputStream.writeObject(ip + ": " + message);
                        System.out.println("Write: " +  message);
                        outputStream.flush();
                    }
                    else if (code == PLAY) {
                        BoardTile[] boardTiles = (BoardTile[]) inputStream.readObject();
                        System.out.println("Receive: Board");

                        outputStreamData.writeInt(PLAY);
                        outputStream.writeObject(boardTiles);
                        System.out.println("Write: Board");
                        outputStream.flush();

                        PlayTile[] playTiles = (PlayTile[]) inputStream.readObject();
                        System.out.println("Receive: Bag");

                        outputStream.writeObject(playTiles);
                        System.out.println("Write: Bag");
                        outputStream.flush();

                        int score = inputStreamData.readInt();
                        System.out.println("Receive: Score");
                        outputStreamData.writeInt(score);
                        System.out.println("Receive: Score");
                        outputStreamData.flush();
                    }
                }
                catch (Exception ex) {
                }
            }

            try {
                player1.close();
                player2.close();
            }
            catch (Exception e) {}

            taLog.appendText("Sockets are disconnected\n");

            return;
        }
    }
}





/*
    class Chatting implements Runnable {

        private ObjectInputStream chatInputStream;
        private ObjectOutputStream chatOutputStream;
        private String ip;

        Chatting(ObjectInputStream inP1, ObjectOutputStream outP2, String inputIP) {
            this.chatInputStream = inP1;
            this.chatOutputStream = outP2;
            this.ip = inputIP;
        }

        @Override
        public void run() {
            while (isConnected) {
                try {
                    String message = chatInputStream.readObject().toString();
                    System.out.println("Receive: " +  message);
                    chatOutputStream.writeObject(ip + ": " + message);
                    System.out.println("Write: " +  message);
                    chatOutputStream.flush();
                }
                catch (Exception ex) {
                    //ex.printStackTrace();
                    try {
                        chatOutputStream.writeObject(ip + " is disconnected..");
                    }
                    catch (Exception e) {}

                    System.out.println(ip + " is disconnected..");
                    isConnected = false;
                }
            }

            try {
                chatInputStream.close();
                chatOutputStream.close();
                player1.close();
                player2.close();
            }
            catch (Exception e) {}

            taLog.appendText("Sockets are disconnected\n");

            return;
        }
    }

    class Playing implements Runnable {
        private ObjectInputStream playInputStream;
        private ObjectOutputStream playOutputStream;
        private String ip;

        Playing(ObjectInputStream inP1, ObjectOutputStream outP2, String inputIP) {
            this.playInputStream = inP1;
            this.playOutputStream = outP2;
            this.ip = inputIP;
        }


        @Override
        public void run() {
            while (isGameContinued) {
                try {
                    BoardTile[] boardTiles = (BoardTile[]) playInputStream.readObject();
                    System.out.println("Receive: Board");
                    playOutputStream.writeObject(boardTiles);
                    System.out.println("Write: Board");
                    playOutputStream.flush();
                }
                catch (Exception ex) {
                    //ex.printStackTrace();
                    try {
                        playOutputStream.writeObject(ip + " is disconnected..");
                    }
                    catch (Exception e) {}

                    System.out.println(ip + " is disconnected..");
                    isGameContinued = false;
                }
            }

            try {
                playInputStream.close();
                playOutputStream.close();
                player1.close();
                player2.close();
            }
            catch (Exception e) {}

            taLog.appendText("Sockets are disconnected\n");

            return;
        }
    }
    */