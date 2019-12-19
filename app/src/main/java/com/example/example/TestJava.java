package com.example.example;

public class TestJava {

    public static void main(String[] args) {
        Name.Gender gender = new Name.Gender();
    }
}

class Name {
    String x;

    public static int y = 1;

    public Name(String x) {
        this.x = x;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {

        this.x = x;
        Gender g = new Gender();
    }

    static class Gender{

    }
}
