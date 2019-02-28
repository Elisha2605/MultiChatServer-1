package com.Alvin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChatServer {

    Map<Socket,DataOutputStream> map = new HashMap<Socket, DataOutputStream>();

    public static void main(String[] args) {

        new ChatServer().runServer();

    }

    private void updateClients(String message){

        for (Map.Entry<Socket, DataOutputStream> entry: map.entrySet()){

            try {
                entry.getValue().writeBytes(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void runServer() {

        try {
            ServerSocket serverSocket = new ServerSocket(1337);

            while (true) {

                System.out.println("Venter forbindelse");
                Socket socket = serverSocket.accept();
                map.put(socket, new DataOutputStream(socket.getOutputStream()));
                System.out.println("Forbundet til Klient");
                ClientHandler clientHandler = new ClientHandler(socket);


                Thread thread1 = new Thread(clientHandler);
                thread1.start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ClientHandler implements Runnable{

        Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                Scanner scanner = new Scanner(socket.getInputStream());

                while (true){
                    String message;
                    message = scanner.nextLine();
                    System.out.println(message); // blokerer


                    updateClients(message);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
