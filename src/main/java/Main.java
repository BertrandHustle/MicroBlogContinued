import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.halt;

public class Main {

    public static void main(String[] args){

        //get method for main (home) page

        Spark.get(
                "/",
                (request, response) -> {

                    //contains user

                    HashMap hash = new HashMap();

                    if(request.session().attributes().contains("user")){
                        //creates user and puts them into hashmap
                        User user = request.session().attribute("user");
                        hash.put("user", request.session().attribute("user"));
                        return new ModelAndView(hash, "messages.html");

                    } else {
                        User user = request.session().attribute("user");
                        hash.put("user", user);
                        return new ModelAndView(hash, "index.html");
                    }

                },
                new MustacheTemplateEngine()
        );

        //post method for creating user

        Spark.post(
                "/create-user",
                (request, response) -> {

                    HashMap hash = new HashMap();

                    request.session().attribute("user", new User(request.queryParams("name")));

                    response.redirect("/");
                    halt();

                    return "";
                }
            );

        Spark.post(
                "/create-messages",
                (request, response) -> {

                    HashMap hash = new HashMap();

                    /*kicks back to home if user hasn't been created (
                    i.e. if new session tries to access
                    */

                    if(request.session().attributes().contains("user")){
                        //creates message
                        Message message = new Message(request.session().attribute("message"));
                        hash.put("message", request.session().attribute("message"));
                    }

                    response.redirect("/");
                    halt();

                    return new ModelAndView(hash, "messages.html");
                },
                new MustacheTemplateEngine()
        );

    }

}
