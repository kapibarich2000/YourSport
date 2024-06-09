package com.kapibarich.yoursport.model;

// Чтобы данный клас хранился в базе данных room, его нужно пометить аннотацией Entity

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {
    // Необходим первичный ключ, чтобы каждая запись была уникальна в таблице
    @PrimaryKey(autoGenerate = true) // Установим автоматическую генерацию id
    private int id;
    private String name;
    private String description;
    private String muscle;
    private int difficulty;

    public Exercise(int id, String name, String description, String muscle, int difficulty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.muscle = muscle;
        this.difficulty = difficulty;
    }
    @Ignore
    public Exercise(String name, String description, String muscle, int difficulty) {
        this(0, name, description,muscle,difficulty);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMuscle() {
        return muscle;
    }

    public int getDifficulty() {
        return difficulty;
    }
}

