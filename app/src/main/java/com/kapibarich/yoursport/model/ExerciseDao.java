package com.kapibarich.yoursport.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {
    // Dao - Date Access object

    // Здесь определяем методы, через которые будем взаимодействовать с БД
    @Query("SELECT * FROM exercises")
    LiveData<List<Exercise>> getAllExercises(); // получение всех заметок. Указываем базовую колекцию List, т.к. не знаем, что вернет room
    // getNotes возвращает объект LiveData, на него можно подписаться в приложении, то сможем автоматически реагировать на изменения
    // Если указать LiveData, то room автоматически будет выполнять запрос в новом потоке

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Exercise exercise); // Добавляет заметку в базу
    // ABORT - операция будет прервана
    // REPLACE - изменяет уже существующую заметку
    // IGNORE - операция будет проигнорирована

    @Query("DELETE FROM exercises WHERE id = :id")
    void remove(int id);

}
