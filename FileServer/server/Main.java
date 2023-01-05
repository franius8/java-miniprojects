package server;

import serverutils.Message;
import serverutils.Serializer;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;

public class Main {
    static DataInputStream input;
    static DataOutputStream output;
    static Random random = new Random();

    static HashMap<Integer, String> files = null;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Server started!");
        String address = "127.0.0.1";
        int port = 23456;
        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));

        while (true) {
            Socket socket = server.accept();
            input = new DataInputStream(socket.getInputStream());
            output  = new DataOutputStream(socket.getOutputStream());
            Message message = Serializer.readAndDeserialize(input);
            if (message.getCommand().equals("EXIT")) {
                if (files != null) {
                    Serializer.serialize(files, "./src/server/data/files.txt");
                }
                server.close();
                return;
            }
            if (files == null) {
                try {
                    files = (HashMap<Integer, String>) Serializer.deserialize("./src/server/data/files.txt");
                } catch (Exception e) {
                    files = new HashMap<Integer, String>();
                }
            }
            switch (message.getCommand()) {
                case "GET" -> getFile(message);
                case "PUT" -> putFile(message);
                case "DELETE" -> deleteFile(message);
            }
        }
    }
    private static void getFile(Message message) throws IOException {
        String fileName = getFileName(message);
        Path path = Paths.get(System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator + fileName);
        try {
            byte[] content = Files.readAllBytes(path);
            Message outputMessage = new Message(200, content);
            Serializer.serializeAndWrite(outputMessage, output);
        } catch (IOException e) {
           Serializer.serializeAndWrite(new Message(404), output);
        }
    }
    private static void putFile(Message message) throws IOException {
        String fileName = message.getName();
        Path path = Paths.get("./src/server/data/" + fileName);
        byte[] content = message.getContents();
        int iD = random.nextInt(9999);
        if (!Files.exists(path)) {
            try {
                Files.write(path, content);
                files.put(iD, fileName);
                System.out.println(files.toString());
                Serializer.serializeAndWrite(new Message(200, iD), output);
            } catch (IOException e) {
                Serializer.serializeAndWrite(new Message(403), output);
            }
        } else {
            Serializer.serializeAndWrite(new Message(403), output);
        }
    }
    private static void deleteFile(Message message) throws IOException {
        String fileName = getFileName(message);
        Path path = Paths.get(System.getProperty("user.dir") +
                File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator + fileName);
        try {
            Files.delete(path);
            Serializer.serializeAndWrite(new Message(200), output);
        } catch (IOException e) {
            Serializer.serializeAndWrite(new Message(404), output);
        }
    }
    private static String getFileName(Message message) {
        if (message.isById()) {
            int id = message.getiD();
            return files.get(id);
        } else {
            return message.getName();
        }
    }
}