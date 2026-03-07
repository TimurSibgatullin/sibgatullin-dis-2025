package org.example.spring.component;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/home")
@RestController
public class HomeController {
    public HomeController() {
    }

    @GetMapping()
    public String homePage() {
        return "<html><head><meta charset='utf-8'/><title>HOME</title></head><body>\n" +
                "<h1>Добро пожаловать домой странник</h1>\n" +
                "<p>Будь как дома путник, я ни в чём не откажу</p>\n" +
                "</body></html>";
    }
}
