package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JacksonInject.Value;

import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.jscience.physics.amount.Amount;
import org.jscience.physics.model.RelativisticModel;
import javax.measure.unit.SI;

@SpringBootApplication
@Controller
public class MainController {
    private final DataSource dataSource;

    @Autowired
    public MainController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        session.invalidate();
        return "user/home";
    }

    @GetMapping("/about")
    public String about() {
        return "user/about";
    }
<<<<<<< HEAD

    @GetMapping("/catalogue")
    public String catalogue() {
        // if (session.getAttribute("fullname") != null) {
            return "user/catalogue";
        // } else {
        //     System.out.println("Session expired or invalid");
        //     return "login";
        // }
    }

    @GetMapping("/faqs")
    public String faqs() {
        return "user/faqs";
    }

    @GetMapping("/payment")
    public String payment() {
        return "user/payment";
    }
=======
>>>>>>> 32d661f1f917dbb3b31839d96d424984d36c3c63
    
    @GetMapping("/customerregister")
    public String custregister() {
        return "user/customerregister";
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);

    }
}
