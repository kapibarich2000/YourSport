package com.kapibarich.yoursport.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kapibarich.yoursport.model.DataBaseYourSport;
import com.kapibarich.yoursport.model.Person;

public class SingUpViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> shouldClose = new MutableLiveData<>();
    private DataBaseYourSport dataBaseYourSport;

    public LiveData<Boolean> getShouldClose() {
        return shouldClose;
    }

    public SingUpViewModel(@NonNull Application application) {
        super(application);
        dataBaseYourSport = DataBaseYourSport.getInstance(application);
    }

    public void createPerson(Person person){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataBaseYourSport.personDao().add(person);
                shouldClose.postValue(true);
            }
        });
        thread.start();
    }

    public void deletePersonById(int id){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataBaseYourSport.personDao().removeById(id);
            }
        });
        thread.start();
    }
}
