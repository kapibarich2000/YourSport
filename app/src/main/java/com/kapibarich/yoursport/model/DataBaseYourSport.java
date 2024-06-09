package com.kapibarich.yoursport.model;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Exercise.class, Person.class}, version = 4, exportSchema = false)
public abstract class DataBaseYourSport extends RoomDatabase {

    private static DataBaseYourSport instance = null;
    private static final String DB_NAME = "YourSport.db";

    public static DataBaseYourSport getInstance(Application application){
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application,
                    DataBaseYourSport.class,
                    DB_NAME
            ).fallbackToDestructiveMigration().build(); //.fallbackToDestructiveMigration() // удаляет имеющиеся данные из базы
            // allowMainThreadQueries - позволяет обойти ограничение по бращению к БД в главном потоке. НО в РЕАЛЬНЫХ приложениях так НЕ НАДО !
        }
        return instance;
    }

    public abstract ExerciseDao exerciseDao(); // Появится зеленая стрелочка (реализация сгенерированная room) после первого запска

    public abstract PersonDao personDao();

}
