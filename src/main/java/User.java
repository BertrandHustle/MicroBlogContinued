import java.util.ArrayList;

/**
 * User class
 */
public class User {

    //properties
    String name;
    ArrayList<String> messages;

    //constructor

    public User(String name, ArrayList<String> messages) {
        this.name = name;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }
}
