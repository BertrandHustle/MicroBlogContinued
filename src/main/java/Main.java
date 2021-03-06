import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.HashMap;
import java.util.Random;

import static spark.Spark.halt;

public class Main {

    //think of this as a database
    static HashMap<String, User> userList = new HashMap<>();

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
                        User currentUser = userList.get(userName);

                        hash.put("userName", userName);
                        hash.put("messages", currentUser.getMessages());

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

                try {

                    if (currentUser.getPassword().equals(password)) {

                        //might be PROBLEM
                        request.session().attribute("userName", userName);
                        request.session().attribute("password", password);

                        response.redirect("/");

                    } else {

                        response.redirect("/");

                    }

                } catch (NullPointerException npe){
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

                    if(request.session().attributes().contains("userName")) {

                        return new ModelAndView(hash, "messages.mustache");

                    } else {

                        return new ModelAndView(hash, "index.mustache");

                    }
                },

                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-messages",
                (request, response) -> {

                    HashMap hash = new HashMap();

                    if (request.session().attributes().contains("userName")) {
                        //User user = request.session().attribute(currentUser);

                        User currentUser = userList.get(request.session().attribute("userName"));

                        Message m = new Message(request.queryParams("message"));

                        hash.put("userName", request.session().attribute("userName"));
                        hash.put("messages", currentUser.getMessages());

                        //FIX?
                        currentUser.getMessages().add(m.message);

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

        Spark.post(
                "/delete-messages",
                (request, response) -> {

                    User currentUser = userList.get(request.session().attribute("userName"));
                    currentUser.getMessages().remove(Integer.parseInt(request.queryParams("messageID")) - 1);

                    response.redirect("/");
                    halt();
                    return "";
                }
        );Spark.post(
                "/edit-messages",
                (request, response) -> {

                    User currentUser = userList.get(request.session().attribute("userName"));
                    currentUser.getMessages().remove(Integer.parseInt(request.queryParams("messageIDedit")) - 1);

                    Message m = new Message(request.queryParams("text"));
                    currentUser.getMessages().add(m.message);

                    response.redirect("/");
                    halt();
                    return "";
                }

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
