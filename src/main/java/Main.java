import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.halt;

public class Main {

    static HashMap<String, User> userList = new HashMap<>();
    static ArrayList<Message> messages = new ArrayList<>();
    static String currentUser;

    public static void main(String[] args){

        //gets webroot
        Spark.get(
                "/",
                (request, response) -> {

                    //contains user

                    HashMap hash = new HashMap();

                    if(!userList.containsKey(currentUser)) {

                        //returns index
                        return new ModelAndView(hash, "index.mustache");


                    } else {

                        //returns new messages page

                        return new ModelAndView(hash, "messages.mustache");
                    }

                },
                new MustacheTemplateEngine()
        );

        //post method for creating user
        Spark.post(
                "/create-user",
                (request, response) -> {

                    //creates new user
                    User user = new User(request.queryParams("name"),
                            request.queryParams("password"));

                    //puts new user in userList
                    userList.put(request.queryParams("name"), user);

                    //redirects to home
                    response.redirect("/");
                    halt();

                    return "";
                }
        );

        //login
        Spark.post(
                "/login",

                (request, response) -> {

                    String userName = request.queryParams("loginName");
                    String password = request.queryParams("loginPassword");

                    User user = userList.get(userName);

                    if (user.getPassword().equals(password) ){

                        currentUser = request.queryParams("loginName");
                        response.redirect("/");

                    } else {

                        response.redirect("/");

                    }

                    return "";

                }
        );

        //logout
        Spark.get(
                "/logout",
                (request, response) -> {
                    request.session().invalidate();
                    response.redirect("/");
                    halt();
                    return "";
                }
        );

        Spark.get(
                "/create-messages",
                (request, response) -> {

                    HashMap hash = new HashMap();

                    return new ModelAndView(hash, "messages.mustache");

                },

                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-messages",
                (request, response) -> {

                    HashMap hash = new HashMap();

                    if (request.session().attributes().contains("user")) {
                        //creates message

                        Message m = new Message(request.queryParams("message"));

                        hash.put("user", request.session().attribute("user"));
                        messages.add(m);

                    }

                    //todo: THIS NEEDS (ELSE)

                    /*kicks back to home if user hasn't been created (
                    i.e. if new session tries to access
                    */


                    response.redirect("/");
                    halt();

                    return "";
                }
        );



    }

}
