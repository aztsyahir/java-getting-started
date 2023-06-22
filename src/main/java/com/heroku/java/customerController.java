package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
// import java.util.ArrayList;
// import java.util.Map;
import java.sql.SQLException;

// import org.jscience.physics.amount.Amount;
// import org.jscience.physics.model.RelativisticModel;
// import javax.measure.unit.SI;

@Controller
public class customerController {
    private final DataSource dataSource;

    @Autowired
    public customerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }


     @PostMapping("/userregister")
    public String addAccount(HttpSession session, @ModelAttribute("userregister") customer cust, User user) {
    try {
      Connection connection = dataSource.getConnection();
      String sql1= "INSERT INTO users (fullname, email, password) VALUES (?,?,?)";
      final var statement1 = connection.prepareStatement(sql1);
      statement1.setString(1, user.getName());
      statement1.setString(2, user.getEmail());
      statement1.setString(3, user.getPassword());

      statement1.executeUpdate();

    //   Get id from database for sql 2 from sql 1
      String sql = "SELECT * FROM users where email=?";
      final var stmt = connection.prepareStatement(sql);
      stmt.setString(1, user.getEmail());
      final var resultSet = stmt.executeQuery();
      //id user
      int id_db = 0;
      while(resultSet.next()){
        id_db = resultSet.getInt("usersid");
      }

      System.out.println("id database : " + id_db);
      
      String sql2= "INSERT INTO customer (usersid, phonenumber, address) VALUES (?,?,?)";
      final var statement2 = connection.prepareStatement(sql2);
      statement2.setInt(1, id_db);
      statement2.setString(2, cust.getPhonenumber());
      statement2.setString(3, cust.getAddress());

      statement2.executeUpdate();

      System.out.println("phonenumber: "+cust.getPhonenumber());

      connection.close();
      return "redirect:/";

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/userregister";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/userregister";
    }
    }
  

    //  @PostMapping("/login") 
    // public String Loginpage(HttpSession session, @ModelAttribute("login") customer customer, User user, Model model, baker baker) { 

    //     try {
    //         Connection connection = dataSource.getConnection();
    //         final var statement = connection.createStatement(); 
    //         String sql ="SELECT usersid, fullname,email, password FROM users"; 
    //         final var resultSet = statement.executeQuery(sql); 
            

    //         String returnPage = ""; 
 
    //         while (resultSet.next()) { 
    //             int usersid = resultSet.getInt("usersid");
    //             String username = resultSet.getString("fullname"); 
    //             String password = resultSet.getString("password");
    //             String usertype = user.getUsertype();  
                
    //             //if they choose custoke
    //             if (usertype.equals("custradio")){
    //                 if (username.equals(customer.getEmail()) && password.equals(customer.getPassword())) { 
    //                 session.setAttribute("fullname",customer.getName());
    //                 session.setAttribute("usersid",usersid);
    //                 System.out.println("usersid: "+usersid);
    //                 returnPage = "redirect:/home"; 
    //                 break; 
    //             } else { 
    //                 returnPage = "/login"; 
    //             } 

    //             //if they choose employee
    //             }
    //             else if (usertype.equals("admin")){
    //                 if (username.equals(baker.getEmail()) && password.equals(baker.getPassword())) { 
    //                 session.setAttribute("fullname",customer.getName());
    //                 returnPage = "redirect:/homeadmin"; 
    //                 break; 
    //             } else { 
    //                 returnPage = "/login"; 
    //             } 
    //             }
    //             else{
    //                 System.out.println("Username does not match password");
    //             }
    //         }
    //         return returnPage; 
 
    //     } catch (Throwable t) { 
    //         System.out.println("message : " + t.getMessage()); 
    //         return "/login"; 
    //     } 
 
    // }


  
 
}
