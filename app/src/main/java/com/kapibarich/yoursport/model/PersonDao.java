package com.kapibarich.yoursport.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM persons")
    LiveData<List<Person>> getAllPersons();

    @Query("SELECT * FROM persons WHERE userName = :username AND password = :password")
    List<Person> getPersonsByUserNameAndPassword(String username, String password);

    @Query("SELECT * FROM persons WHERE id = :id")
    List<Person> getPersonsById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Person person);

    @Query("DELETE FROM persons WHERE id = :id")
    void removeById(int id);

}
