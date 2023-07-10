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
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
// import java.util.ArrayList;
// import java.util.Map;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// import org.jscience.physics.amount.Amount;
// import org.jscience.physics.model.RelativisticModel;
// import javax.measure.unit.SI;
@SpringBootApplication
@Controller

public class productsController {
    private final DataSource dataSource;

    @Autowired
    public productsController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/cakeregister")

    public String cakeregister() {
        return "admin/cakeregister";
    }

    @PostMapping("/cakeregister")
    public String AddCake(HttpSession session,  @ModelAttribute("cakeregister")Products products,Cakes cake, Cupcakes cupcake){

        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO products(proname,protype,proprice,proimg) VALUES(?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String proname = products.getProname();
            String protype = products.getProtype();
            int proprice = products.getProprice();
            byte[] proimg = products.getProimg();
            System.out.println(cake.getCakesize());
            System.out.println(cupcake.getCuptoppings());
            
            statement.setString(1, proname);
            statement.setString(2, protype);
            statement.setInt(3, proprice );
            statement.setBytes(4,proimg );
            statement.executeUpdate();
            System.out.println("product name : "+proname);
            System.out.println("type : "+protype);
            System.out.println("product price : RM"+proprice);
            System.out.println("proimg: "+proimg);

             //   Get id from database for sql 2 from sql 1
      String sql1 = "SELECT * FROM products where proname=?";
      final var stmt = connection.prepareStatement(sql1);
      stmt.setString(1, products.getProname());
      final var resultSet = stmt.executeQuery();
      int id_pro = 0;
      while(resultSet.next()){
        id_pro = resultSet.getInt("proid");
      }
      System.out.println("id product from database : "+id_pro);
      System.out.println("protype : " + protype);
      if(protype.equals("cake")){
        String sql2 = "INSERT INTO cakes (proid,cakesize) VALUES (?,?)";
        final var statement2 = connection.prepareStatement(sql2);
       
        int cakesize = cake.getCakesize();

        statement2.setInt(1, id_pro);
        statement2.setInt(2, cakesize);
        statement2.executeUpdate();
        System.out.println("cake size : "+cakesize);
      }
      
        if(protype.equals("cupcake")){
            System.out.println("cupcake here");
        
        System.out.println("Cupcake here 2");
        String sql3 = "INSERT INTO cupcakes (proid,cuptoppings) VALUES (?,?)";
        final var statement3 = connection.prepareStatement(sql3);

        String cuptopping = cupcake.getCuptoppings();
        // String cuptopping = ;
        System.out.println("product id : " + id_pro);
        System.out.println("cupcake topping : " + cuptopping);   

        statement3.setInt(1, id_pro);
        statement3.setString(2, cuptopping);
        statement3.executeUpdate();
        System.out.println("cupcake topping 2: "+cuptopping);
      }
      connection.close();
        
        } catch (Exception e) {
            // TODO: handle exception
            return "redirect:/cakeregister";
        }
         return "redirect:/cakeregister";
    }

    @GetMapping("/staffmenu")
    public String productList(HttpSession session, Model model ,Cakes cake,Cupcakes cupcake) {
       String staffsrole = (String) session.getAttribute("staffsid");

      try {
        List<Cakes> cakes = new ArrayList<>();
         List<Cupcakes> cupcakes = new ArrayList<>();

        Connection connection = dataSource.getConnection();

        final var statement = connection.createStatement();
        final var resultSet = statement.executeQuery( "SELECT p.proid, p.proname, p.protype, p.proprice, p.proimg FROM products p ");

        if(resultSet.next()){
            int proid = resultSet.getInt("proid");
            String proname = resultSet.getString("proname");
            String protype = resultSet.getString("protype");
            int proprice = resultSet.getInt("proprice");
            byte[] proimg = resultSet.getBytes("proimg");

            //debug
            System.out.println("product name : " +proname);

            if (protype.equals("cake")){
                
            }

        }

        

        model.addAttribute("cakes", cakes);
        model.addAttribute("cupcakes", cupcakes);

      } catch (Exception e) {
        // TODO: handle exception
      }
      return "staffmenu";
    }
    
}
