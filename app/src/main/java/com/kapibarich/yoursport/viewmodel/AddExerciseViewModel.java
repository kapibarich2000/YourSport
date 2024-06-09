package com.kapibarich.yoursport.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kapibarich.yoursport.model.DataBaseYourSport;
import com.kapibarich.yoursport.model.ExerciseDao;
import com.kapibarich.yoursport.model.Exercise;

public class AddExerciseViewModel extends AndroidViewModel {
    private DataBaseYourSport dataBaseYourSport;
    private ExerciseDao exerciseDao; // Как и верхнее, но не придется постоянно писать notesDao
    private MutableLiveData<Boolean> shouldClose = new MutableLiveData<>();

    public LiveData<Boolean> getShouldClose() {
        return shouldClose;
    }

    public AddExerciseViewModel(@NonNull Application application) {
        super(application);
        // Нужно воспользоваться паттерном Singleton к базе данных, иначе на главный экран изменения не придут
        dataBaseYourSport = DataBaseYourSport.getInstance(application);
        exerciseDao = DataBaseYourSport.getInstance(application).exerciseDao();
    }

    public void saveExercise(Exercise exercise){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                exerciseDao.add(exercise);
                //shouldClose.setValue(true); // setValue можем вызывать только из главного потока
                shouldClose.postValue(true);
            }
        });
        thread.start();
    }
}
