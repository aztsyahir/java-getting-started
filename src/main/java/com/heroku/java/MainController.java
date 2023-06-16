package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;

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
        // if(session.getAttribute("custname") !=null){
        //     return "user/home";
        // }else{
        // return "login";
        // }
        return "login";
    }
    @GetMapping("/logout")
    public String logout(){
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
    @GetMapping("/bakerregister")
    public String bakerregister(){
        return "admin/bakerregister";
    }
    @GetMapping("/custprofile")
    public String custprofile(){
        return "user/custprofile";
    }
    
//      @PostMapping("/login")
//   String homepage(HttpSession session, @ModelAttribute("login") User user,customer cust,baker baker){
//     try (Connection connection = dataSource.getConnection()) {
//       final var statement = connection.createStatement();

//       var resultSet = statement.executeQuery("SELECT email, password FROM customer;");
//       System.out.println("Role : " + user.getRadio());

//       if(user.getRadio().equals("customer")){
//         resultSet = statement.executeQuery("SELECT email, password FROM customer;");
//       }else{
//         resultSet = statement.executeQuery("SELECT email, password FROM baker;");
//       }
//       String returnPage = "";
//       System.out.println("User url params : " + user);
//       while (resultSet.next()) {
//         String email = resultSet.getString("email");
//         String pwd = resultSet.getString("password");
         
//           if (user.getCustemail().equals(email) && user.getCustpassword().equals(pwd)) {

//             session.setAttribute("custemail", user.getCustemail());
            
//             returnPage = "redirect:/home";
//             break;
//           } else {
//             returnPage = "redirect:/";
//           }

//         }
//       connection.close();
//       return returnPage;

//     } 
//     catch (SQLException sqe) {
//       System.out.println("Error Code = " + sqe.getErrorCode());
//       System.out.println("SQL state = " + sqe.getSQLState());
//       System.out.println("Message = " + sqe.getMessage());
//       System.out.println("printTrace /n");
//       sqe.printStackTrace();

//       return "redirect:/";
//     } 
//     catch (Throwable t) {
//       System.out.println("message : " + t.getMessage());
//       return "redirect:/";
//     }
// }


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
