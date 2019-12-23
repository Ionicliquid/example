package com.example.example.design_pattern;

import android.util.Log;

public class O3 implements O {

    private O o;

    public O3(O o) {
        this.o = o;
    }

    @Override
    public void subscribe(String name) {
        System.out.println("name = "+name);
        o.subscribe(O3.class.getSimpleName());
    }
}
