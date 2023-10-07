package org.example;

class BadCase2 {
    void method(double x, double y) {
        double sq1 = x * x;
        double sq2 = y * y;
        System.out.println(Math.sqrt(sq1 + sq2));
    }
}
