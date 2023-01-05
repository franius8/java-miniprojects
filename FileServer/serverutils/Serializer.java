package serverutils;

import java.io.*;

public class Serializer {

    public static void serialize(Object obj, String fileName) throws IOException {
        File file = new File(fileName);
        file.delete();
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

    public static void serializeAndWrite (Message message, OutputStream output) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeObject(message);
    }

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    public static Message readAndDeserialize (InputStream input) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(input);
        return (Message) ois.readObject();
    }
}
