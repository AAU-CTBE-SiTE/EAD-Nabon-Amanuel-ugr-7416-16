package com.craft;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beansConfig.xml");

        // Object & Collections Dependency Injection
        Student student = context.getBean("student", Student.class);
        System.out.println(student);
        student.getDetails();

        // Bean Scopes Demonstration
        Student student1 = context.getBean("student", Student.class);
        Student student2 = context.getBean("student", Student.class);

        // Check if both references point to the SAME instance (singleton scope)
        System.out.println("Same instance (singleton): " + (student1 == student2));
        student1.setName("John Doe Updated");
        System.out.println("student2 name reflects change: " + student2.getName());
    }
}
