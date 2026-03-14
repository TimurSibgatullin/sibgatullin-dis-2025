package org.example.controllers;

import org.example.model.*;
import org.example.service.PersonService;
import org.example.service.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final PhoneService phoneService;

    public PersonController(PersonService personService, PhoneService phoneService) {
        this.personService = personService;
        this.phoneService = phoneService;
    }

    @GetMapping
    public String persons(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "persons";
    }

    @GetMapping("/add")
    public String addPersonForm() {
        return "person_add";
    }

    @PostMapping("/add")
    public String addPerson(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String role
    ) {

        Phone phoneEntity = new Phone();
        phoneEntity.setNumber(phone);
        phoneService.save(phoneEntity);

        Person person;

        if ("ADMIN".equals(role)) {
            person = new Admin();
        } else {
            person = new Client();
        }

        person.setName(name);
        person.setPhone(phoneEntity);

        personService.save(person);

        return "redirect:/persons";
    }
}