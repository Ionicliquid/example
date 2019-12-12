package com.example.example;

public class TestJava {

    public static void main(String[] args) {
        Name n = new Name("liqiao");
        Name m = n;
        Name y = n;
        y.setX("xxxx");
        System.out.println(m.getX());
    }
}

class Name {
    String x;

    public Name(String x) {
        this.x = x;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }
}
