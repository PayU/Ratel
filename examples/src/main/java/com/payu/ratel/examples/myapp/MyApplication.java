package com.payu.ratel.examples.myapp;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.payu.ratel.Discover;
import com.payu.ratel.config.EnableServiceDiscovery;
import com.payu.ratel.examples.calculator.CalculatorService;

@Configuration
@EnableServiceDiscovery
@Component
public class MyApplication{
  
  @Discover
  private CalculatorService calc;
  
  private boolean running;
  
  public static void main(String[] args) {
      SpringApplication app = new SpringApplication(MyApplication.class);
      app.setWebEnvironment(false);
      ConfigurableApplicationContext ctx = app.run(args);
      
      MyApplication myApp = ctx.getBean(MyApplication.class);
      myApp.playConsoleGame();
      
      ctx.close();
  }

  private void playConsoleGame() {
    running = true;
    Scanner scanner = new Scanner(System.in);
    int a, b, sum;
    String quotient;
    
    while (running) {
      System.out.println("Please enter two numbers separated by whitespace");
      a = scanner.nextInt();
      b = scanner.nextInt();
      
      sum = calc.add(a, b);
      
      try {
        quotient = String.valueOf(calc.divide(a, b));
      } catch (ArithmeticException e) {
          //Ratel will transport exception thrown by the remote service implementation
          quotient = "<undefined>";
      }
      
      System.out.printf("Sum of your numbers is %d\nInteger quotient is %s\n\n\n", sum, quotient);;
      
      System.out.println("Try again (y/n)?");
      running = scanner.next("[yY]|[nN]").equalsIgnoreCase("y");
    }
    
  }
  
}

