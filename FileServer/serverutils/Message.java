package serverutils;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    String command;
    boolean byId;
    String name;
    int iD;
    byte[] contents;

    int responseCode;

    public Message(int responseCode) {
        this.responseCode = responseCode;
    }
    public Message(int responseCode, byte[] contents) {
        this.responseCode = responseCode;
        this.contents = contents;
    }

    public Message(int responseCode, int iD) {
        this.iD = iD;
        this.responseCode = responseCode;
    }

    public Message(String command) {
        this.command = command;
    }
    public Message(String command, String name) {
        this.command = command;
        this.byId = false;
        this.name = name;
    }

    public Message(String command, int iD) {
        this.command = command;
        this.byId = true;
        this.iD = iD;
    }

    public Message(String command, String name, byte[] contents) {
        this.command = command;
        this.name = name;
        this.byId = false;
        this.contents = contents;
    }
    public Message(String command,int iD, byte[] contents) {
        this.command = command;
        this.byId = true;
        this.iD = iD;
        this.contents = contents;
    }

    public String getCommand() {
        return command;
    }

    public boolean isById() {
        return byId;
    }

    public String getName() {
        return name;
    }

    public int getiD() {
        return iD;
    }

    public byte[] getContents() {
        return contents;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
