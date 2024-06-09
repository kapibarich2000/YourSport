package com.kapibarich.yoursport.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kapibarich.yoursport.model.DataBaseYourSport;
import com.kapibarich.yoursport.model.Person;

import java.util.List;

public class AuthorizationViewModel extends AndroidViewModel {

    private DataBaseYourSport dataBaseYourSport;
    private MutableLiveData<Integer> tryOfAuthorization = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> goodAuthorization = new MutableLiveData<>();
    private int idPerson;

    public LiveData<Integer> getTryOfAuthorization() {
        return tryOfAuthorization;
    }

    public LiveData<Boolean> getGoodAuthorization() {
        return goodAuthorization;
    }

    public AuthorizationViewModel(@NonNull Application application) {
        super(application);
        dataBaseYourSport = DataBaseYourSport.getInstance(application);
    }

    public void singIn(String userName, String password){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer counter = tryOfAuthorization.getValue();
                List<Person> person = dataBaseYourSport.personDao().getPersonsByUserNameAndPassword(userName, password);
                if(person.size()>0){
                    goodAuthorization.postValue(true);
                    idPerson = person.get(0).getId();
                }
                else{
                    counter = counter + 1;
                    tryOfAuthorization.postValue(counter);
                }
            }
        });
        thread.start();
    }

    public int getIdPerson() {
        return idPerson;
    }
}
