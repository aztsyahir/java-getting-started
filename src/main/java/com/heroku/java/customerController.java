package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
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

      @Autowired
  private BCryptPasswordEncoder passwordEncoder;


     @PostMapping("/customerregister")
    public String addAccount(HttpSession session, @ModelAttribute("customerregister") customer cust,User user) {
    try {
      Connection connection = dataSource.getConnection();
      String sql= "INSERT INTO customers (custname, custemail, custpassword, custaddress,custphone) VALUES (?,?,?,?,?)";
      final var statement1 = connection.prepareStatement(sql);

      String custsname = cust.getFullname();
      String custsemail = cust.getEmail();
      String custspassword = cust.getPassword();
      String custsaddress = cust.getCustaddress();
      int custsphone = cust.getCustphone();
      
      statement1.setString(1, custsname);
      statement1.setString(2, custsemail);
      statement1.setString(3, passwordEncoder.encode(custspassword));
      statement1.setString(4, custsaddress);
      statement1.setInt(5, custsphone);
      statement1.executeUpdate();

      //debug
      System.out.println("phonenumber: "+custsphone);

      connection.close();
      return "redirect:/login";

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
        String fullname = (String) session.getAttribute("custname");
        int userid = (int) session.getAttribute("custid");

        if(fullname != null){
            try{
                Connection connection = dataSource.getConnection();
                final var statement = connection.prepareStatement
                ( "SELECT* FROM customers WHERE custsid=?");
                statement.setInt(1, userid);
                 final var resultSet = statement.executeQuery();

                while(resultSet.next()){
                    String custname = resultSet.getString("custname");
                    String custemail = resultSet.getString("custemail");
                    String custpassword = resultSet.getString("custpassword");
                    String custaddress = resultSet.getString("custaddress");
                    int custphone = resultSet.getInt("custphone");

                    //debug
                    System.out.println("fullname from db = "+custname);

                    customer custprofile = new customer( userid,custname, custemail, custpassword, custaddress, custphone) ;


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
        public String updateCust(HttpSession session, @ModelAttribute("custprofile") customer cust, Model model) { 

            int custid = (int) session.getAttribute("custid");

            String custname = cust.getFullname();
            String custemail = cust.getEmail();
            String custaddress = cust.getCustaddress();
            String custpassword = cust.getPassword();
            int custphone = cust.getCustphone();
        
            try { 
            Connection connection = dataSource.getConnection();
            String sql = "UPDATE customers SET custname=? ,custemail=?, custpassword=?, custaddress=?, custphone=? WHERE custid=?";
            
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, custid);

            statement.setString(1, custname);
            statement.setString(2, custemail);
            statement.setString(3, custpassword);
            statement.setString(4, custaddress);
            statement.setInt(5, custphone);
            statement.executeUpdate();
            System.out.println("debug= "+custname+" "+custemail+" "+custpassword);
            System.out.println("id database : " + custid);
                
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
            String custemail = (String) session.getAttribute("custemail");
            int custid = (int) session.getAttribute("custid");

            if (custemail != null) {
                try (Connection connection = dataSource.getConnection()) {
                    // Delete customer record
                    final var deleteCustomerStatement = connection.prepareStatement("DELETE FROM customer WHERE usersid=?");
                    deleteCustomerStatement.setInt(1, custid);
                    int customerRowsAffected = deleteCustomerStatement.executeUpdate();

                    //debug delete
                    System.out.println("done delete");
                    
                    if (customerRowsAffected > 0) {
                        // Deletion successful
                        // You can redirect to a success page or perform any other desired actions
                        session.invalidate();
                        return "redirect:/";
                    } else {
                        // Deletion failed
                        // You can redirect to an error page or perform any other desired actions
                        System.out.println("Delete Failed");
                        return "redirect:/custprofile";
                    }
                } catch (SQLException e) {
                    // Handle any potential exceptions (e.g., log the error, display an error page)
                    e.printStackTrace();

                    // Deletion failed
                    // You can redirect to an error page or perform any other desired actions
                    System.out.println("Error");
                }
            }
            // fullname is null or deletion failed, handle accordingly (e.g., redirect to an error page)
            return "/user/catalogue";
        }

        @GetMapping("/cart")
         public String cart(){
        return "user/cart";
    }

    @GetMapping("/paymentFileUpload")
         public String paymentFileUpload(){
        return "user/paymentFileUpload";
    }

    @GetMapping("/orderHistory")
         public String orderHistory(){
        return "user/orderHistory   ";
    }

    }
 
    
