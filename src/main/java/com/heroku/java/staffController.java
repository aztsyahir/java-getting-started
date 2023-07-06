package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class staffController {
  private final DataSource dataSource;

  @Autowired
  public staffController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @GetMapping("/staffregister")
  public String staffregister(){
      return "admin/staffregister";
  }

  @GetMapping("/stafforder")
    public String stafforder(HttpSession session){
       if(session.getAttribute("fullname") != null){ 
            return "admin/stafforder"; 
        }else{ 
            System.out.println("Session expired or invalid");
            return "login"; 
        } 

    }

    @PostMapping("/staffregister")
    public String addAccountStaff(HttpSession session, @ModelAttribute("staffregister")  staff staff) {
    try {
      Connection connection = dataSource.getConnection();
      String sql1= "INSERT INTO staffs (staffsname, staffsemail, staffspassword, staffsrole) VALUES (?,?,?,?)";
      final var statement1 = connection.prepareStatement(sql1);

      String staffsname = staff.getStaffsname();
      String staffsemail = staff.getStaffsemail();
      String staffspassword = staff.getStaffspassword();    

      statement1.setString(1, staffsname);
      statement1.setString(2, staffsemail);
      statement1.setString(3, passwordEncoder.encode(staffspassword));
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

      return "redirect:/staffregister";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/staffregister";
    }
    }

//   @GetMapping("/staffprofile")
//     public String viewprofilestaff(HttpSession session, Model model)
//     {
//         String staffsname = (String) session.getAttribute("fullname");
//         int staffsid = (int) session.getAttribute("staffsid");
//         System.out.println("staff fullname : "+staffsname);
//          System.out.println("staff id : "+staffsid);

//         if(staffsname != null){
//             try{
//                 Connection connection = dataSource.getConnection();
//                 final var statement = connection.prepareStatement
//                 ( "SELECT  staffssname, staffsemail, staffspassword, staffsrole FROM staffs WHERE staffsid = ?");
//                 statement.setInt(1, staffsid);
//                  final var resultSet = statement.executeQuery();

//                 while(resultSet.next()){
//                     String fname = resultSet.getString("fullname");
//                     String email = resultSet.getString("email");
//                     String password = resultSet.getString("password");
//                     String usertype = resultSet.getString("usertype");

//                     //debug
//                     System.out.println("fullname from db = "+fname);

//                     User staffprofile = new User(fullname,email,password,usertype);


//                     model.addAttribute("staffprofile", staffprofile);
//                     System.out.println("fullname :"+ staffprofile.fullname);
//                     // Return the view name for displaying staff details --debug
//                     System.out.println("Session staffprofile : " + model.getAttribute("staffprofile"));

//                 }
//                return "staffprofile";
//             }
//         catch (SQLException e) {
//             e.printStackTrace();
//             }
//             }else{
//                 return "/login";
//             }
//             return "/login";
 
// }

//Update Profile staff
        @PostMapping("/updatestaff") 
        public String updatestaff(HttpSession session, @ModelAttribute("staffprofile") Model model, User user) { 

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
                
            String returnPage = "staffprofile"; 
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "redirect:/login"; 
        } }

        //delete controller
        @GetMapping("/deletestaff")
        public String deleteProfileCust(HttpSession session, Model model) {
            String fullname = (String) session.getAttribute("fullname");
            int userid = (int) session.getAttribute("usersid");

            if (fullname != null) {
                try (Connection connection = dataSource.getConnection()) {
                    // Delete staff record
                    // final var deletestaffStatement = connection.prepareStatement("DELETE FROM staff WHERE usersid=?");
                    // deletestaffStatement.setInt(1, userid);
                    // int staffRowsAffected = deletestaffStatement.executeUpdate();

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
            return "/staff/stafforder";
        }

        @GetMapping("/staffmenu")
          // public String staffmenu(HttpSession session,cake cake,Model model){
          public String staffmenu(){

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
        // model.addAttribute("staffmenu", cake);
        // connection.close();
        return "admin/staffmenu";
              
            // } catch (Exception e) {
            //   // TODO: handle exception
            //   return "redirect/login";
            // }

          
        }
        @GetMapping("/cakeregister")
        
        public String cakeregister()
        {
            return "admin/cakeregister";
        }


}
