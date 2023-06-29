package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
@SpringBootApplication
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
      String sql1= "INSERT INTO users (fullname, email, password, usertype) VALUES (?,?,?,?)";
      final var statement1 = connection.prepareStatement(sql1);
      statement1.setString(1, user.getName());
      statement1.setString(2, user.getEmail());
      statement1.setString(3, user.getPassword());
      statement1.setString(4, "customer");

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
  
    @GetMapping("/custprofile")
    public String viewprofile(HttpSession session, Model model, customer cust)
    {
        String fullname = (String) session.getAttribute("fullname");
        int usersid = (int) session.getAttribute("usersid");

        if(fullname != null){
            try{
                Connection connection = dataSource.getConnection();
                final var statement = connection.prepareStatement
                ( "SELECT  users.fullname, users.email, users.password,users.usertype, customer.phonenumber, customer.address FROM users JOIN customer ON (users.usersid = customer.usersid) WHERE users.usersid = ?");
                statement.setInt(1, usersid);
                 final var resultSet = statement.executeQuery();

                while(resultSet.next()){
                    String fname = resultSet.getString("fullname");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String usertype = resultSet.getString("usertype");
                    String phonenumber = resultSet.getString("phonenumber");
                    String address = resultSet.getString("address");

                    //debug
                    System.out.println("fullname from db = "+fname);

                    customer custprofile = new customer(fullname,email,password,usertype,phonenumber,address);


                    model.addAttribute("custprofile", custprofile);
                    System.out.println("fullname :"+ custprofile.fullname);
                    // Return the view name for displaying customer details --debug
                    System.out.println("Session custprofile : " + model.getAttribute("custprofile"));

                }
               return "custprofile";
            }
        catch (SQLException e) {
            e.printStackTrace();
            }
            }else{
                return "/login";
            }
            return "/login";
 
}


 //Update Profile Customer
        @PostMapping("/updatecust") 
        public String updateCust(HttpSession session, @ModelAttribute("custprofile") customer cust, Model model, User user) { 

            String fullname = cust.getName();
            String email = cust.getEmail();
            String password = cust.getPassword();
            String phonenum = cust.getPhonenumber();
            String address = cust.getAddress();
        
            try { 
            Connection connection = dataSource.getConnection();
            String sql1 = "UPDATE users SET fullname=? ,email=?, password=?, usertype=? WHERE email=?";
            final var statement = connection.prepareStatement(sql1);

            statement.setString(1, fullname);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, "customer");
            statement.setString(5, email);
            statement.executeUpdate();
            System.out.println("debug= "+fullname+" "+email+" "+password);

            String sql = "SELECT * FROM users where fullname=?";
            final var stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getName());
            final var resultSet = stmt.executeQuery();
            int id_db = 0;
            while(resultSet.next()){
            id_db = resultSet.getInt("usersid");
            }
            System.out.println("id database : " + id_db);
      
            String sql2= "UPDATE customer SET phonenumber=?, address=? WHERE usersid=?";
            final var statement2 = connection.prepareStatement(sql2);
            // statement2.setInt(1, id_db);
            statement2.setString(1, phonenum);
            statement2.setString(2, address);
            statement2.setInt(3,id_db);
           // statement2.executeUpdate();
            System.out.println("debug= "+phonenum+" "+address+" "+id_db);
            statement2.executeUpdate();
                
            String returnPage = "custprofile"; 
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "redirect:/login"; 
        } }

        //delete controller
        @GetMapping("/deletecust")
        public String deleteProfileCust(HttpSession session, customer customer,Model model) {
            String fullname = (String) session.getAttribute("fullname");
            int userid = (int) session.getAttribute("usersid");

            if (fullname != null) {
                try (Connection connection = dataSource.getConnection()) {
                    // Delete customer record
                    final var deleteCustomerStatement = connection.prepareStatement("DELETE FROM customer WHERE usersid=?");
                    deleteCustomerStatement.setInt(1, userid);
                    int customerRowsAffected = deleteCustomerStatement.executeUpdate();

                    //debug delete
                    System.out.println("hehe");
                    
                    // Delete user record
                    final var deleteUserStatement = connection.prepareStatement("DELETE FROM users WHERE usersid=?");
                    deleteUserStatement.setInt(1, userid);
                    int userRowsAffected = deleteUserStatement.executeUpdate();

                    if (customerRowsAffected > 0 && userRowsAffected > 0) {
                        // Deletion successful
                        // You can redirect to a success page or perform any other desired actions
                        session.invalidate();
                        return "redirect:/";
                    } else {
                        // Deletion failed
                        // You can redirect to an error page or perform any other desired actions
                        System.out.println("Delete Failed");
                    }
                } catch (SQLException e) {
                    // Handle any potential exceptions (e.g., log the error, display an error page)
                    e.printStackTrace();

                    // Deletion failed
                    // You can redirect to an error page or perform any other desired actions
                    System.out.println("Error");
                }
            }
            // Username is null or deletion failed, handle accordingly (e.g., redirect to an error page)
            return "/user/home";
        }

    }
 
    
