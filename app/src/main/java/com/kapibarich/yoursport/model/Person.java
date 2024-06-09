package com.kapibarich.yoursport.model;

// Чтобы данный клас хранился в базе данных room, его нужно пометить аннотацией Entity

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "persons")
public class Person {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName;
    private String password;
    private int weight;
    private int age;
    private int height;
    private String gender;

    public Person(int id, String userName, String password, int weight, int height, String gender, int age) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    @Ignore
    // Чтобы room не видел данный конструктор, иначе он не сможет выбрать по какому содавать запись в БД
    public Person(String userName, String password, int weight, int height, String gender, int age) {
        this(0, userName, password, weight, height, gender, age);
    }



    public String getPassword() {
        return password;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }
}
