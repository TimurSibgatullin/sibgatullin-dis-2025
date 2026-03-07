package org.example.spring.component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/index")
@RestController
public class IndexController {
    public IndexController() {
    }

    @GetMapping()
    public String homePage() {
        return "<html><head><meta charset='utf-8'/><title>INDEX</title></head><body>" +
                "<h1>Добро пожаловать на главную страницу!</h1>" +
                "<p>Это главная страница, обслуживаемая IndexController.</p>" +
                "</body></html>";
    }
}

