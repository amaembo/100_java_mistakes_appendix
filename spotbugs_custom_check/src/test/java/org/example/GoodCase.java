package org.example;

class GoodCase {
    void method(double x, double y) {
        System.out.println(Math.hypot(x, y));
        System.out.println(Math.sqrt(x * x - y * y));
        System.out.println(Math.sqrt(x * y + y * y));
    }
}
