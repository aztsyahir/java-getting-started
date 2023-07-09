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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/about")
    public String about() {
        return "user/about";
    }

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

    // @PostMapping("/login")
    // public String Loginpage(HttpSession session, @ModelAttribute("login") customer cust, staff staff, Model model) {
    //     String returnPage = null;
    //     try {
    //         Connection connection = dataSource.getConnection();
    //         final var statement = connection.createStatement();
    //         // final var statement2 = connection.createStatement();
    //         String sql = "SELECT staffsid, staffsname,staffsemail, staffspassword,staffsrole FROM staffs";
    //         // String sql2 ="SELECT custsId, staffsname,staffsemail,
    //         // staffspassword,staffsrole FROM staffs";
    //         final var resultSet = statement.executeQuery(sql);
    //         // final var resultSet2 = statement2.executeQuery(sql2);

    //         while (resultSet.next()) {
    //             int staffsid = resultSet.getInt("staffsid");
    //             String staffsname = resultSet.getString("staffsname");
    //             String staffsemail = resultSet.getString("staffsemail");
    //             String staffspassword = resultSet.getString("staffspassword");
    //             String staffsrole = resultSet.getString("staffsrole");
    //             // int custid = resultSet.getInt("custid");
    //             // String custname = resultSet.getString("custname");
    //             // String custemail = resultSet.getString("custemail");
    //             // String custpassword = resultSet.getString("custpassword");

    //             // if they're admin
    //             if (staffsrole.equals("admin")) {
    //                 if (staffsemail.equals(staff.getStaffsemail())
    //                         && passwordEncoder.matches(staff.getStaffspassword(),staffspassword)) {

    //                     session.setAttribute("staffsname", staffsname);
    //                     session.setAttribute("staffsid", staffsid);
    //                     // debug
    //                     System.out.println("admin name : " + staffsname);
    //                     System.out.println("admin id: " + staffsid);
    //                     System.out.println("admin role: " + staffsrole);
    //                     returnPage = "redirect:/staffmenu";
    //                     break;
    //                 } else {
    //                     System.out.println("debug admin");
    //                     returnPage = "login";
    //                 }
    //             }

    //             // if they're baker
    //             else if (staffsrole.equals("baker")) {
    //                 if (staffsemail.equals(staff.getStaffsemail())
    //                         && staffspassword.equals(staff.getStaffspassword())) {

    //                     session.setAttribute("staffsname", staffsname);
    //                     session.setAttribute("staffsid", staffsid);
    //                     // debug
    //                     System.out.println("baker name : " + staffsname);
    //                     System.out.println("baker id: " + staffsid);
    //                     System.out.println("baker role: " + staffsrole);
    //                     returnPage = "redirect:/staffmenu";
    //                     break;
    //                 } else {
    //                     System.out.println("debug baker");
    //                     returnPage = "login";
    //                 }
    //             }
    //             // if they're customer

    //             else {
    //                 // if (custemail.equals(cust.getCustemail()) &&
    //                 // custpassword.equals(cust.getCustpassword())) {

    //                 // session.setAttribute("custemail",custemail);
    //                 // session.setAttribute("custid",custid);
    //                 // //debug
    //                 // System.out.println("baker name : "+custname);
    //                 // System.out.println("baker id: "+custid);
    //                 // System.out.println("baker role: customer");
    //                 // returnPage = "redirect:/catalogue";
    //                 // break;
    //                 // } else {
    //                 returnPage = "login";
    //                 // System.out.println("email does not match password");
    //                 // }
    //             }
    //         }
    //         return returnPage;

    //     } catch (Throwable t) {
    //         System.out.println("message : " + t.getMessage());
    //         System.out.println("debug failure");
    //         return "login";
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

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);

    }
}
