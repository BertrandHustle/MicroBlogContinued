import java.util.ArrayList;

/**
 * User class
 */
public class User {

    //properties
    String name;
    String password;
    static ArrayList<String> messages = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
