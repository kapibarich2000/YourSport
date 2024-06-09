package com.kapibarich.yoursport.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kapibarich.yoursport.model.DataBaseYourSport;
import com.kapibarich.yoursport.model.Exercise;

import java.util.List;

public class ExercisesListViewModel extends AndroidViewModel {

    private DataBaseYourSport dataBaseYourSport;
    private int count = 0;
    private MutableLiveData<Integer> countLD = new MutableLiveData<>(); // Для самостоятельной установки значения
    private int idPerson;

    public ExercisesListViewModel(@NonNull Application application) {
        // application - будем использовать при создании БД
        super(application);
        dataBaseYourSport = DataBaseYourSport.getInstance(application);
    }
    public void remove(Exercise exercise){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataBaseYourSport.exerciseDao().remove(exercise.getId());
            }
        });
        thread.start();
    }

    public void showCount(){
        count ++;
        countLD.setValue(count);
    }

    public LiveData<Integer> getCount(){
        return countLD;
    }

    public LiveData<List<Exercise>> getNotes(){
        return dataBaseYourSport.exerciseDao().getAllExercises();
    }

    public void addToDailyPlan(Context context, Exercise exercise){
        Toast.makeText(context, "Добавлен в дневной план", Toast.LENGTH_SHORT).show();
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public int getIdPerson() {
        return idPerson;
    }
}
