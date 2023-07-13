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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.sql.*;
import javax.sql.DataSource;
// import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
//import java.sql.SQLException;
import java.util.List;

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
    public String staffregister(HttpSession session) {
        // int staffsid = (int) session.getAttribute("staffsid");
        // System.out.println("staff id :" + staffsid);
        return "admin/staffregister";
    }

    @GetMapping("/stafforder")
    public String stafforder(HttpSession session) {
        if (session.getAttribute("fullname") != null) {
            return "admin/stafforder";
        } else {
            System.out.println("Session expired or invalid");
            return "login";
        }

    }

    @GetMapping("/stafflist")
    public String staffList(HttpSession session, Model model) {

        List<staff> staffs = new ArrayList<>();
        // Retrieve the logged-in staff's role from the session
        String staffsrole = (String) session.getAttribute("staffsrole");
        System.out.println("staffrole stafflist : " + staffsrole);
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT staffsid, staffsname, staffsemail, staffspassword, staffsrole FROM staffs WHERE staffsrole=?;";
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, "baker");
            final var resultSet = statement.executeQuery();
            System.out.println("pass try stafflist >>>>>");

            while (resultSet.next()) {
                int userid = resultSet.getInt("staffsid");
                String fullname = resultSet.getString("staffsname");
                String email = resultSet.getString("staffsemail");
                String password = resultSet.getString("staffspassword");
                String role = resultSet.getString("staffsrole");
                System.out.println("role while" + email);
                System.out.println("role while" + fullname);

                staffs.add(new staff(userid, fullname, email, password, role));
                model.addAttribute("staffs", staffs);
                model.addAttribute("isAdmin", staffsrole != null && staffsrole.equals("admin")); // Add isAdmin flag to the modelF

            }

            return "admin/stafflist";
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as desired (e.g., show an error message)
            return "error";
        }
        
    }

    @GetMapping("/deletestaff/")
    public String deleteStaff(@RequestParam("staffsid") int staffsid, HttpSession session) {
        // Retrieve the logged-in staff's role from the session
        String staffsrole = (String) session.getAttribute("staffsrole");
        System.out.println("delete staff : " + staffsrole);
        System.out.println(staffsid);
        if (staffsrole != null && staffsrole.equals("admin")) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM staffs WHERE staffsid = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, staffsid);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Deletion successful
                    return "redirect:/stafflist"; // Redirect back to the staff list
                } else {
                    // Deletion failed
                    return "redirect:/stafflist"; // Redirect to an error page or show an error message
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as desired (e.g., show an error message)
                return "admin/stafflist"; // Redirect to an error page or show an error message
            }
        }

        // Redirect to an error page or back to the staff list
        return "redirect:/stafflist";
    }

    @PostMapping("/staffregister")
    public String addAccountStaff(HttpSession session, @ModelAttribute("staffregister") staff staff) {
        try {
            Connection connection = dataSource.getConnection();
            String sql1 = "INSERT INTO staffs (staffsname, staffsemail, staffspassword, staffsrole) VALUES (?,?,?,?)";
            final var statement1 = connection.prepareStatement(sql1);

            String fullname = staff.getFullname();
            String email = staff.getEmail();
            String password = staff.getPassword();
            System.out.println("password : " + password);
            System.out.println("fullname : " + fullname);
            System.out.println("email : " + email);

            statement1.setString(1, fullname);
            statement1.setString(2, email);
            statement1.setString(3, passwordEncoder.encode(password));
            statement1.setString(4, "admin");
          //  statement1.setInt(5, (int) session.getAttribute("staffsid"));

            statement1.executeUpdate();

            connection.close();
            return "redirect:/login";

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

    @GetMapping("/staffprofile")
    public String viewprofilestaff(HttpSession session, Model model) {
        String fullname = (String) session.getAttribute("staffsname");
        int userid = (int) session.getAttribute("staffsid");
        String staffrole = (String) session.getAttribute("staffsrole");
        System.out.println("staff fullname : " + fullname);
        System.out.println("staff id : " + userid);
        System.out.println("staff role : " + staffrole);

        if (fullname != null) {
            try {
                Connection connection = dataSource.getConnection();
                final var statement = connection.prepareStatement(
                        "SELECT  staffsname, staffsemail, staffspassword,staffsrole FROM staffs WHERE staffsid = ?");
                statement.setInt(1, userid);
                final var resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String fname = resultSet.getString("staffsname");
                    String email = resultSet.getString("staffsemail");
                    String password = resultSet.getString("staffspassword");
                    String staffsrole = resultSet.getString("staffsrole");
                    // debug
                    System.out.println("fullname from db = " + fname);

                    staff staffprofile = new staff(userid, fname, email, password, staffsrole);

                    model.addAttribute("staffprofile", staffprofile);
                    System.out.println("fullname :" + staffprofile.getFullname());
                    // Return the view name for displaying staff details --debug
                    System.out.println("Session staffprofile : " + model.getAttribute("staffprofile"));

                }
                return "staffprofile";
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return "/login";
        }
        return "/login";

    }

    // Update Profile staff
    @PostMapping("/staffupdate")
    public String updatestaff(HttpSession session, @ModelAttribute("staffprofile") staff staff, Model model) {
        int staffsid = (int) session.getAttribute("staffsid");
        String staffsrole = (String) session.getAttribute("staffsrole");

        String staffsname = staff.getFullname();
        String staffsemail = staff.getEmail();
        String staffspassword = staff.getPassword();

        // debug
        System.out.println("id update = " + staffsid);
        System.out.println("role update = " + staffsrole);

        try {
            Connection connection = dataSource.getConnection();
            String sql1 = "UPDATE staffs SET staffsname=? ,staffsemail=?, staffsrole=? WHERE staffsid=?";
            final var statement = connection.prepareStatement(sql1);

            statement.setString(1, staffsname);
            statement.setString(2, staffsemail);
            statement.setString(3, staffsrole);
            statement.setInt(4, staffsid);
            statement.executeUpdate();
            System.out.println("debug= " + staffsid + " " + staffsname + " " + staffsrole + " " + staffsemail);

            // Check if the staff has entered a new password
            if (!staffspassword.isEmpty()) {
                // Hash the new password
                String newpassword = passwordEncoder.encode(staffspassword);

                // Update the staff's password in the database
                String sql2 = "UPDATE staffs SET staffspassword=? WHERE staffsid=?";
                final var passwordStatement = connection.prepareStatement(sql2);
                passwordStatement.setString(1, newpassword);
                passwordStatement.setInt(2, staffsid);
                passwordStatement.executeUpdate();

            }

            String returnPage = "staffprofile";
            return returnPage;

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            System.out.println("error");
            return "redirect:/staffprofile";
        }
    }

    // delete controller
    @GetMapping("/deletestaff")
    public String deleteProfileCust(HttpSession session, Model model) {
        String fullname = (String) session.getAttribute("staffsname");
        int userid = (int) session.getAttribute("staffsid");

        if (fullname != null) {
            try (Connection connection = dataSource.getConnection()) {

                // Delete user record
                final var deleteStaffStatement = connection.prepareStatement("DELETE FROM staffs WHERE staffsid=?");
                deleteStaffStatement.setInt(1, userid);
                int userRowsAffected = deleteStaffStatement.executeUpdate();

                if (userRowsAffected > 0) {
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
        // Username is null or deletion failed, handle accordingly (e.g., redirect to an
        // error page)
        return "/staff/stafforder";
    }

    @GetMapping("/staffmenu")
    // public String staffmenu(HttpSession session,cake cake,Model model){
    public String staffmenu() {

        // try(Connection connection = dataSource.getConnection()) {

        // final var statement = connection.createStatement();
        // final var resultSet = statement.executeQuery("SELECT caketype, cakeprice,
        // cakesize , cakeimg FROM cake ORDER BY cakeid;");

        // // int row = 0;
        // ArrayList<cake> cakes = new ArrayList<>();
        // while (resultSet.next()) {
        // String caketype = resultSet.getString("caketype");
        // String cakeprice = resultSet.getString("cakeprice");
        // Integer cakesize = resultSet.getInt("cakesize");
        // byte[] cakeimg = resultSet.getBytes("cakeimg");
        // cake Cake = new cake(caketype,cakeprice,cakesize,cakeimg);
        // cakes.add(Cake);
        // }
        // model.addAttribute("staffmenu", cake);
        // connection.close();
        return "admin/staffmenu";
              
            // } catch (Exception e) {
            //   TODO: handle exception
            //   return "redirect/login";
            // }

      

    }

    @GetMapping("/cakeregister")
    public String cakeregister() {
        return "admin/cakeregister";
    }

    @GetMapping("/stafforderhistory")
    public String stafforderhistory() {
        return "admin/stafforderhistory";
    }


}
