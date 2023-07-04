package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
 import java.util.ArrayList;
 import java.util.Map;
import java.sql.SQLException;
//import java.util.ArrayList;

// import org.jscience.physics.amount.Amount;
// import org.jscience.physics.model.RelativisticModel;
// import javax.measure.unit.SI;

@Controller
public class bakerController {
  private final DataSource dataSource;

  @Autowired
  public bakerController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping("/bakerregister")
  public String bakerregister(){
      return "admin/bakerregister";
  }

  @GetMapping("/bakerorder")
    public String bakerorder(HttpSession session){
       if(session.getAttribute("fullname") != null){ 
            return "admin/bakerorder"; 
        }else{ 
            System.out.println("Session expired or invalid");
            return "login"; 
        } 

    }

    @PostMapping("/bakerregister")
    public String addAccount(HttpSession session, @ModelAttribute("bakerregister")  User user) {
    try {
      Connection connection = dataSource.getConnection();
      String sql1= "INSERT INTO users (fullname, email, password, usertype) VALUES (?,?,?,?)";
      final var statement1 = connection.prepareStatement(sql1);
      statement1.setString(1, user.getName());
      statement1.setString(2, user.getEmail());
      statement1.setString(3, user.getPassword());
      statement1.setString(4, "baker");

      statement1.executeUpdate();

      connection.close();
      return "redirect:/";

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/bakerregister";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/bakerregister";
    }
    }

  @GetMapping("/bakerprofile")
    public String viewprofilebaker(HttpSession session, Model model)
    {
        String fullname = (String) session.getAttribute("fullname");
        int usersid = (int) session.getAttribute("usersid");
        System.out.println("fullname : "+fullname);
         System.out.println("user id : "+usersid);

        if(fullname != null){
            try{
                Connection connection = dataSource.getConnection();
                final var statement = connection.prepareStatement
                ( "SELECT  fullname, email, password,usertype FROM users WHERE usersid = ?");
                statement.setInt(1, usersid);
                 final var resultSet = statement.executeQuery();

                while(resultSet.next()){
                    String fname = resultSet.getString("fullname");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String usertype = resultSet.getString("usertype");

                    //debug
                    System.out.println("fullname from db = "+fname);

                    User bakerprofile = new User(fullname,email,password,usertype);


                    model.addAttribute("bakerprofile", bakerprofile);
                    System.out.println("fullname :"+ bakerprofile.fullname);
                    // Return the view name for displaying baker details --debug
                    System.out.println("Session bakerprofile : " + model.getAttribute("bakerprofile"));

                }
               return "bakerprofile";
            }
        catch (SQLException e) {
            e.printStackTrace();
            }
            }else{
                return "/login";
            }
            return "/login";
 
}

//Update Profile baker
        @PostMapping("/updatebaker") 
        public String updateBaker(HttpSession session, @ModelAttribute("bakerprofile") Model model, User user) { 

            String fullname = user.getName();
            String email = user.getEmail();
            String password = user.getPassword();
            String usertype = user.getUsertype();
        
            try { 
            Connection connection = dataSource.getConnection();
            String sql1 = "UPDATE users SET fullname=? ,email=?, password=?, usertype=? WHERE email=?";
            final var statement = connection.prepareStatement(sql1);

            statement.setString(1, fullname);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, usertype);
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
      
            statement.executeUpdate();
                
            String returnPage = "bakerprofile"; 
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "redirect:/login"; 
        } }

        //delete controller
        @GetMapping("/deletebaker")
        public String deleteProfileCust(HttpSession session, Model model) {
            String fullname = (String) session.getAttribute("fullname");
            int userid = (int) session.getAttribute("usersid");

            if (fullname != null) {
                try (Connection connection = dataSource.getConnection()) {
                    // Delete baker record
                    // final var deleteBakerStatement = connection.prepareStatement("DELETE FROM baker WHERE usersid=?");
                    // deleteBakerStatement.setInt(1, userid);
                    // int bakerRowsAffected = deleteBakerStatement.executeUpdate();

                    // //debug delete
                    // System.out.println("hehe");
                    
                    // Delete user record
                    final var deleteUserStatement = connection.prepareStatement("DELETE FROM users WHERE usersid=?");
                    deleteUserStatement.setInt(1, userid);
                    int userRowsAffected = deleteUserStatement.executeUpdate();

                    if ( userRowsAffected > 0) {
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
            return "/baker/bakerorder";
        }

        @GetMapping("/bakermenu")
          // public String bakermenu(HttpSession session,cake cake,Model model){
          public String bakermenu(){

        //     try(Connection connection = dataSource.getConnection()) {
              
        //       final var statement = connection.createStatement();
        //       final var resultSet = statement.executeQuery("SELECT caketype, cakeprice, cakesize , cakeimg FROM cake ORDER BY cakeid;");

        //       // int row = 0;
        // ArrayList<cake> cakes = new ArrayList<>();
        // while (resultSet.next()) {
        //   String caketype = resultSet.getString("caketype");
        //   String cakeprice = resultSet.getString("cakeprice");
        //   Integer cakesize = resultSet.getInt("cakesize");
        //   byte[] cakeimg = resultSet.getBytes("cakeimg");
        //   cake Cake = new cake(caketype,cakeprice,cakesize,cakeimg);
        //   cakes.add(Cake);
        // }
        // model.addAttribute("bakermenu", cake);
        // connection.close();
        return "admin/bakermenu";
              
            // } catch (Exception e) {
            //   // TODO: handle exception
            //   return "redirect/login";
            // }

          
        }
      


}
