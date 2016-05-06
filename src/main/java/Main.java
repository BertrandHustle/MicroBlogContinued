import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.halt;

public class Main {

    //think of this as a database
    static HashMap<String, User> userList = new HashMap<>();
    static ArrayList<String> messages = new ArrayList<>();


    public static void main(String[] args){

        //gets webroot
        Spark.get(
                "/",
                (request, response) -> {

                    //contains user

                    HashMap hash = new HashMap();

                    if(!request.session().attributes().contains("userName")) {

                        //returns index

                        return new ModelAndView(hash, "index.mustache");

                    } else {

                        //returns new messages page

                        String userName = request.session().attribute("userName");

                        hash.put("userName", userName);
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

                    User currentUser = userList.get(userName);

                    if (currentUser.getPassword().equals(password)){

                        //might be PROBLEM
                        request.session().attribute("userName", userName);
                        request.session().attribute("password", password);

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

                    if (request.session().attributes().contains("userName")) {
                        //User user = request.session().attribute(currentUser);
                        //Message m = new Message(request.queryParams("message"));

                        String m = request.queryParams("message");

                        hash.put("userName", request.session().attribute("userName"));
                        hash.put("messages", messages);
                        messages.add(m);

                    } else {

                    /*kicks back to home if user hasn't been created (
                    i.e. if new session tries to access
                    */

                        response.redirect("/");
                        halt();

                    }

                    return new ModelAndView(hash, "messages.mustache");
                },

                new MustacheTemplateEngine()
        );

        //test cases

        User Don = new User("Don", "SCDP");
        User Peggy = new User("Peggy", "Ted");
        User Pete = new User("Pete", "Money");

        userList.put("Don", Don);
        userList.put("Peggy", Peggy);
        userList.put("Pete", Pete);

    }

}
