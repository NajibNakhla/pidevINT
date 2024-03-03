package tests;

import Entities.User;
import Services.UserService;
import Tools.PassSecurity;

import java.io.IOException;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) throws IOException {
        User u1= new User("Ahmed","Ben Ali","ahmedbenali@gmail.com","123456");
        User u2= new User("Rania","Ben Ali","raniabenali@gmail.com","123456789");
        User u3= new User("Ramond","Ramond","ramond2@gmail.com","123456");
        User u4= new User("Kyle","Stevenson","KyleStevenson@gmail.com","123456789");
        User a =new User("aya","boukhris","ayaboukh@gmail.com","123");
        User user =new User();
        UserService us = new UserService();
        PassSecurity ps =new PassSecurity();
        //us.addEntity(u1);
       // us.addEntity(u2);
        //us.register(u3);
        //us.register(a);
      //  us.addEntity(u4);
        System.out.println(us.getAllData());
        u2.setFirstName("Rénée");
        u2.setLastName("Rénéin");
        u2.setEmail("reneereneein@gmail.com");
       // us.updateEntity2(30,u2);

        System.out.println(us.getAllData());

        Scanner sc = new  Scanner(System.in);
        /*System.out.println("First Name : ");*/
        String fname ="aya" ;
        user.setFirstName(fname);
        System.out.println("Last Name : ");
        String lname = "boukhris";
        user.setLastName(lname);
        System.out.println("Email : ");
        String email = "boukrisaya@gmail.com";
        user.setEmail(email);
        System.out.println("Password : ");
        String pwd = "Aya12345";
        user.setPassword(pwd);
       // us.register(user);
       // us.comparePass("boukrisaya@gmail.com","Aya12345");
      // System.out.println(us.login("boukrisaya@gmail.com","Aya12345"));
       /* byte[] salt= ps.generateSalt();
      String pass1= ps.hashPassword("12345TETE",salt);
        System.out.println(pass1);
        String pass2= ps.hashPassword("12345TETE",salt);
        System.out.println(pass2);
        System.out.println(pass1.equals(pass2)?true:false);*/
        //




    }

}


