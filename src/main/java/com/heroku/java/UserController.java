package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.jscience.physics.amount.Amount;
import org.jscience.physics.model.RelativisticModel;
import javax.measure.unit.SI;

@SpringBootApplication
@Controller
public class UserController {
    private final DataSource dataSource;

    @Autowired
    public UserController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/about")
    public String about(){
        return "user/about";
    }
    @GetMapping("/catalogue")
    public String catalogue(){
        return "user/catalogue";
    }
    @GetMapping("/faqs")
    public String faqs(){
        return "user/faqs";
    }
    @GetMapping("/feedback")
    public String feedback(){
        return "user/feedback";
    }
    @GetMapping("/home")
    public String home(){
        return "user/home";
    }
    @GetMapping("/menu")
    public String menu(){
        return "user/menu";
    }
    @GetMapping("/userregister")
    public String userregister(){
        return "user/userregister";
    }
    @GetMapping("/adminregister")
    public String adminregister(){
        return "adminregister";
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

    public static void main(String[] args) {
        SpringApplication.run(UserController.class, args);
        
    }
}
