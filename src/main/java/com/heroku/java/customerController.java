package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
// import java.util.ArrayList;
// import java.util.Map;

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
    public String AddCustomer(HttpSession session, @ModelAttribute("userregister") customer cust){

        try{
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO customer(custname, custemail, custphonenum, custaddress, custpassword) VALUES (?, ?, ?, ?, ?)";
            final var statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getcustname());
            statement.setString(2, customer.getcustemail());
            statement.setString(3, customer.getcustphonenum());
            statement.setString(4, customer.getcustaddress());
            statement.setString(5, customer.getcustpassword());

            statement.executeUpdate();

            connection.close();
            return "redirect/login";
        }
    }

}
