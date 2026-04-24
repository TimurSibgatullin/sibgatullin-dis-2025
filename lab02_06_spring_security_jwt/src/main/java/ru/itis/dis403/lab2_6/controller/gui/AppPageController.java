package ru.itis.dis403.lab2_6.controller.gui;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.itis.dis403.lab2_6.dto.AuthRequest;
import ru.itis.dis403.lab2_6.dto.UserDto;
import ru.itis.dis403.lab2_6.service.JWTService;
import ru.itis.dis403.lab2_6.service.UserDetailImpl;

@Controller
public class AppPageController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public AppPageController(AuthenticationManager authenticationManager, JWTService jwtService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/app")
    public String appPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetailImpl userDetails) {
            String username = userDetails.getUsername();
            model.addAttribute("username", username);
            return "app";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/app")
    public String app(@RequestBody AuthRequest request, Model model) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);
        System.out.println("token " + token);

        model.addAttribute("jwt_token", token);

        return "app";
    }
    @GetMapping("/api/me")
    public UserDto me() {
        UserDetailImpl user = (UserDetailImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return new UserDto(user.getUsername());
    }
}
