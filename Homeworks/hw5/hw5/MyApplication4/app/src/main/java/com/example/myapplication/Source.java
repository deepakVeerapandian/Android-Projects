package com.example.myapplication;

public class Source {
    String id;
    String name;

    public Source() {
    }

    @Override
    public String toString() {
        return name;
    }
    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
}
