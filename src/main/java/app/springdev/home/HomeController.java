package app.springdev.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Mustache Example");
        model.addAttribute("name", "Spring Boot");
        return "index";  // index.mustache
    }
}
