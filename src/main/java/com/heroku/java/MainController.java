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

    @GetMapping("/catalogue")
    public String catalogue(HttpSession session) {
        if (session.getAttribute("custid") != null) {
            return "user/catalogue";
        } else {
            System.out.println("Session expired or invalid");
            return "login";
        }
    }

    @GetMapping("/faqs")
    public String faqs() {
        return "user/faqs";
    }

    // @GetMapping("/login")
    // public String login() {
    //     return "login";
    // }

    @GetMapping("/menu")
    public String menu(HttpSession session) {
        // if (session.getAttribute("fullname") != null) {
            return "user/menu";
        // } else {
        //     System.out.println("Session expired or invalid");
        //     return "login";
        // }
    }

    @GetMapping("/customerregister")
    public String custregister() {
        return "user/customerregister";
    }

    

    @GetMapping("/convert")
    String convert(Map<String, Object> model) {
        RelativisticModel.select();

        final var result = java.util.Optional
                .ofNullable(System.getenv().get("ENERGY"))
                .map(Amount::valueOf)
                .map(energy -> "E=mc^2: " + energy + " = " + energy.to(SI.KILOGRAM))
                .orElse("ENERGY environment variable is not set!");

        model.put("result", result);
        return "convert";
    }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);

    }
}
