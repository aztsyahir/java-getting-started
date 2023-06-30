package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    public String login(HttpSession session) {
        session.invalidate();
         return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
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
    public String home(HttpSession session){
       if(session.getAttribute("fullname") != null){ 
            return "user/home"; 
        }else{ 
            System.out.println("Session expired or invalid");
            return "login"; 
        } 
    }
    @GetMapping("/menu")
    public String menu(){
        return "user/menu";
    }
    @GetMapping("/userregister")
    public String userregister(){
        return "user/userregister";
    }
    @GetMapping("/bakerregister")
    public String bakerregister(){
        return "admin/bakerregister";
    }
   
    
 @PostMapping("/login") 
    public String Loginpage(HttpSession session, @ModelAttribute("login") customer customer, User user, Model model, baker baker) { 

        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.createStatement(); 
            String sql ="SELECT usersid, fullname,email, password FROM users"; 
            final var resultSet = statement.executeQuery(sql); 
            

            String returnPage = ""; 
 
            while (resultSet.next()) { 
                int usersid = resultSet.getInt("usersid");
                String email = resultSet.getString("email"); 
                String fullname = resultSet.getString("fullname");
                String password = resultSet.getString("password");
                String usertype = user.getUsertype();  
                
                //if they choose customer
                if (usertype.equals("customers")){
                    if (email.equals(customer.getEmail()) && password.equals(customer.getPassword())) { 
                    // session.setAttribute("fullname",customer.getName());
                    session.setAttribute("fullname",fullname);
                    session.setAttribute("usersid",usersid);
                    System.out.println("fullname : "+fullname);
                    System.out.println("usersid: "+usersid);
                    System.out.println("usertype: "+usertype);
                    returnPage = "redirect:/home"; 
                    break; 
                } else { 
                    returnPage = "/login"; 
                } 
                }
                //if they choose employee
                
                else if (usertype.equals("admin")){
                    if (email.equals(baker.getEmail()) && password.equals(baker.getPassword())) { 
                    session.setAttribute("fullname",customer.getName());
                    session.setAttribute("usersid",usersid);
                    returnPage = "redirect:/homeadmin"; 
                    break; 
                } else { 
                    returnPage = "/login"; 
                } 
                }
                else{
                    System.out.println("email does not match password");
                }
            }
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            return "/login"; 
        } 
 
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
        SpringApplication.run(MainController.class, args);
        
    }
}
