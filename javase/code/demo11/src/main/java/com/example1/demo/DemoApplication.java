package com.example1.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);


        String abc = new String("abc");
        Object o = abc;
        Integer i  =  (Integer) o;


    }

}
