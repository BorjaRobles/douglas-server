package douglas.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    // Redirect to index page
    @RequestMapping(path = "/")
    public String index() {
        return "redirect:index.html";
    }

}
