package com.craft;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Student {
    private int id;
    private String name;
    private int age;
    private Address address;
    private List<Subject> subjects;

    public Student() {
        System.out.println("Student Constructor Called");
    }

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        System.out.println("Student Parameterized Constructor Called");
    }

    public Student(int id, String name, int age, Address address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public void getHello() {
        System.out.println("Hello Springers");
    }

    public void getDetails() {
        System.out.println("Student Details " + this);
        if (subjects != null) {
            int index = 1;
            for (Subject subject : subjects) {
                System.out.println("Subject " + index + ": " + subject.getSubjectName());
                index++;
            }
        }
    }
}
