package client;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import serverutils.*;

public class Main {

    static DataInputStream input;
    static DataOutputStream output;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Client started!");
        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Enter action (1 - get a file, 2 - save a file, 3 - delete a file):");
        String choice = scanner.nextLine().replaceAll("\n", "");
        if (choice.equals("exit")) {
            Serializer.serializeAndWrite(new Message("EXIT"), output);
            System.out.println("The request was sent.");
            socket.close();
            return;
        }
        switch (choice) {
            case "1" -> getFile();
            case "2" -> saveFile();
            case "3" -> deleteFile();
        }
        socket.close();
    }
    private static String getFileName() {
        System.out.println("Enter name of the file:");
        return scanner.nextLine().replaceAll("\n", "");
    }
    private static void getFile() throws IOException, ClassNotFoundException {

        System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id):");
        int choice = Integer.parseInt(scanner.nextLine().replaceAll("\n", ""));
        switch (choice) {
            case 1 -> {
                String fileName = getFileName();
                Serializer.serializeAndWrite(new Message("GET", fileName), output);
            }
            case 2 -> {
                System.out.println("Enter id:");
                int iD = Integer.parseInt(scanner.nextLine().replaceAll("\n", ""));
                Serializer.serializeAndWrite(new Message("GET", iD), output);
            }
        }
        System.out.println("The request was sent.");
        Message response = Serializer.readAndDeserialize(input);
        switch (response.getResponseCode()) {
            case 404 -> {
                System.out.println("The response says that this file is not found!");
            }
            case 200 -> {
                System.out.println("The file was downloaded! Specify a name for it:");
                String saveName = scanner.nextLine().replaceAll("\n", "");
                Path path = Paths.get("./src/client/data/" + saveName);
                Files.write(path, response.getContents());
                System.out.println("File saved on the hard drive!");
            }
        }
    }
    private static void saveFile() throws IOException, ClassNotFoundException {
        String fileName = getFileName();
        Path path = Paths.get("./src/client/data/" + fileName);
        byte[] fileContents = Files.readAllBytes(path);
        System.out.println("Enter name of the file to be saved on server:");
        String serverFileName = scanner.nextLine();
        if (serverFileName.equals("")) serverFileName = fileName;
        Message message = new Message("PUT", serverFileName, fileContents);
        Serializer.serializeAndWrite(message, output);
        System.out.println("The request was sent.");
        Message response = Serializer.readAndDeserialize(input);
        switch (response.getResponseCode()) {
            case 403 -> {
                System.out.println("The response says that creating the file was forbidden!");
            }
            case 200 -> {
                System.out.println("Response says that file is saved! ID = " + response.getiD());
            }
        }
    }
    private static void deleteFile() throws IOException, ClassNotFoundException {
        System.out.println("Do you want to delete the file by name or by id (1 - name, 2 - id):");
        int choice = Integer.parseInt(scanner.nextLine().replaceAll("\n", ""));
        switch (choice) {
            case 1 -> {
                String fileName = getFileName();
                Serializer.serializeAndWrite(new Message("DELETE", fileName), output);
            }
            case 2 -> {
                System.out.println("Enter id:");
                int iD = Integer.parseInt(scanner.nextLine().replaceAll("\n", ""));
                Serializer.serializeAndWrite(new Message("DELETE", iD), output);
            }
        }
        System.out.println("The request was sent.");
        Message response = Serializer.readAndDeserialize(input);
        switch (response.getResponseCode()) {
            case 404 -> {
                System.out.println("The response says that the file was not found!");
            }
            case 200 -> {
                System.out.println("The response says that the file was successfully deleted!");
            }
        }
    }
}
