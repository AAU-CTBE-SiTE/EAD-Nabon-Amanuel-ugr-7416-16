package com.craft;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Student {
    private int id;
    private String name;
    private int age;

    public Student() {
        System.out.println("Student Constructor Called");
    }

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        System.out.println("Student Parameterized Constructor Called");
    }

    public void getHello() {
        System.out.println("Hello Springers");
    }

    public void getDetails() {
        System.out.println("Student Details " + this);
    }
}
