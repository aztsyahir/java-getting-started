package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;



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
    public String AddCake(HttpSession session,  @ModelAttribute("cakeregister")Products products,  @RequestParam("proimgs") MultipartFile proimgs,Cakes cake, Cupcakes cupcake){

        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO products(proname,protype,proprice,proimg) VALUES(?,?,?,?)";
            final var statement = connection.prepareStatement(sql);

            String proname = products.getProname();
            String protype = products.getProtype();
            int proprice = products.getProprice();

            System.out.println(cake.getCakesize());
            System.out.println(cupcake.getCuptoppings());
            
            statement.setString(1, proname);
            statement.setString(2, protype);
            statement.setInt(3, proprice );
            statement.setBytes(4,proimgs.getBytes());
            statement.executeUpdate();
            
            System.out.println("product name : "+proname);
            System.out.println("type : "+protype);
            System.out.println("product price : RM"+proprice);
            System.out.println("proimg: "+proimgs.getBytes());

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
         return "redirect:/staffmenu";
    }

      @GetMapping("/staffmenu")
      public String productList(HttpSession session, Model model ,Cakes cake,Cupcakes cupcake) {
        // String staffsrole = (String) session.getAttribute("staffsid");
          List<Cakes> cakes = new ArrayList<>();
          List<Cupcakes> cupcakes = new ArrayList<>();
        try {
          System.out.println("pass connection first");
          Connection connection = dataSource.getConnection();
          String sql = "SELECT proid,proname,protype,proprice,proimg FROM products ORDER BY proname";
          final var statement = connection.createStatement();
          final var resultSet = statement.executeQuery(sql);
        
          System.out.println("pass connection 2");
        
          while(resultSet.next()){
              int proid = resultSet.getInt("proid");
              String proname = resultSet.getString("proname");
              String protype = resultSet.getString("protype");
              int proprice = resultSet.getInt("proprice");  

              System.out.println("product name : " +proname);

              byte[] proimgBytes = resultSet.getBytes("proimg");
              String proimgBase64 = Base64.getEncoder().encodeToString(proimgBytes);
              String proimage = "data:image/jpeg;base64,"+proimgBase64;


              //debug
              System.out.println("product name : " +proname);
              System.out.println("product id from db : "+proid);

              if (protype.equals("cake")){
                  String sql2 = "SELECT cakesize FROM cakes WHERE proid=?";
                  final var statement2 = connection.prepareStatement(sql2);
                  statement2.setInt(1,proid);
                  final var resultSet2 = statement2.executeQuery();
                  System.out.println("cake here <<<<<<");

                  if (resultSet2.next()) {
                  int cakesize = resultSet2.getInt("cakesize");
                  
                  Cakes Cake = new Cakes(proid, proname, protype, proprice,null, null, proimage, cakesize);
                  cakes.add(Cake);
                  System.out.println("cakesize here>>>>>");
                  System.out.println("cake id  2 : "+proid);

                  }
                
              }
                  if (protype.equals("cupcake")) {
                      String sql3 = "SELECT cuptoppings FROM cupcakes WHERE proid=?";
                      final var statement3 = connection.prepareStatement(sql3);
                      statement3.setInt(1, proid);
                      final var resultSet3 = statement3.executeQuery();
                      System.out.println("cupcake here<<<<<<");

      
                      if (resultSet3.next()) {
                          String cuptoppings = resultSet3.getString("cuptoppings");
                          Cupcakes Cupcake = new Cupcakes(proid, proname, protype, proprice, null, null, proimage, cuptoppings);
                          cupcakes.add(Cupcake);
                          System.out.println("cuptopping here>>>>>>");

                      }
                  }

                }
          
          model.addAttribute("cakes", cakes);
          model.addAttribute("cupcakes", cupcakes);

          connection.close();
        
        } catch (Exception e) {
          // TODO: handle exception
        }
        return "admin/staffmenu";
      }
      
}
