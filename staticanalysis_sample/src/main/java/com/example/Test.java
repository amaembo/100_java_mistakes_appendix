package com.example;

public class Test {
  public static void main(String[] args) {
    double x = Double.parseDouble(args[0]);
    double y = Double.parseDouble(args[1]);
    System.out.println(Math.sqrt(x * x + y * y));
  }
}