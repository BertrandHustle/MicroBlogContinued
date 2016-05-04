import java.util.ArrayList;

/**
 * User class
 */
public class User {

    //holds user-created messages
    public ArrayList<String> list = new ArrayList<>();

    //properties
    String name;

    public User(String name) {
        this.name = name;
    }
}
