package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
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

@Controller
public class bakerController {
  private final DataSource dataSource;

  @Autowired
  public bakerController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  // @PostMapping("/bakerregister")
  // public String AddBaker(HttpSession session, @ModelAttribute("bakerregister") baker bake) {

  //   try {
  //     Connection connection = dataSource.getConnection();
  //     String sql = "INSERT INTO baker(bakername, bakeremail, bakerphonenumber, bakeraddress, bakerpassword) VALUES (?, ?, ?, ?, ?)";
  //     final var statement = connection.prepareStatement(sql);

  //     statement.setString(1, bake.getBakername());
  //     statement.setString(2, bake.getBakeremail());
  //     statement.setString(3, bake.getBakerphonenumber());
  //     statement.setString(4, bake.getBakeraddress());
  //     statement.setString(5, bake.getBakerpassword());

  //     statement.executeUpdate();

  //     connection.close();
  //     return "redirect:/login";
  //   } catch (SQLException sqe) {
  //     System.out.println("Error Code = " + sqe.getErrorCode());
  //     System.out.println("SQL state = " + sqe.getSQLState());
  //     System.out.println("Message = " + sqe.getMessage());
  //     System.out.println("printTrace /n");
  //     sqe.printStackTrace();

  //     return "redirect:/bakerregister";
  //   } catch (Exception e) {
  //     System.out.println("E message : " + e.getMessage());
  //     return "redirect:/bakerregister";
  //   }
  // }


}
