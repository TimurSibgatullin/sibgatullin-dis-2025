package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.model.Phone;
import org.example.service.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    private final PhoneService phoneService;

    public IndexController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping("/")
    public String index(Model model) {

        List<Phone> phones = phoneService.findAll();

        model.addAttribute("phones", phones);

        return "index";
    }

}