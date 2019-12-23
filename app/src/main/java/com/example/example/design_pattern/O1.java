package com.example.example.design_pattern;

public class O1 implements O{

    private O o;

    public O1(O o) {
        this.o = o;
    }

    @Override
    public void subscribe(String name) {
        System.out.println("name = "+name);
        o.subscribe(O1.class.getSimpleName());
    }
}
