package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String AddCustomer(HttpSession session, @ModelAttribute("userregister")customer cust){

        try{
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO customer(custname, custemail, custphonenum, custaddress, custpassword) VALUES (?, ?, ?, ?, ?)";
            final var statement = connection.prepareStatement(sql);

            statement.setString(1, cust.getCustname());
            statement.setString(2, cust.getCustemail());
            statement.setString(3, cust.getCustphonenum());
            statement.setString(4, cust.getCustaddress());
            statement.setString(5, cust.getCustpassword());

            statement.executeUpdate();

            connection.close();
            return "redirect:/";
        }catch (SQLException sqe) {
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

}
