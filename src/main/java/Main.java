import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    public static void main(String[] args){

        Spark.get(
                "/",
                (request, response) -> {

                    //contains user

                    HashMap hash = new HashMap();

                    if(request.session().attributes().contains("user")){
                        hash.put("user", request.session().attribute("user"));
                    }

                    return new ModelAndView(hash, "index.html");

                },
                new MustacheTemplateEngine()
        );

    }

}
