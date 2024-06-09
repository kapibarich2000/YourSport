package com.kapibarich.yoursport.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kapibarich.yoursport.model.DataBaseYourSport;
import com.kapibarich.yoursport.model.Person;

import java.util.List;

public class DailyPlanViewModel extends AndroidViewModel {
    DataBaseYourSport dataBaseYourSport;
    int idPerson;
    private Person person;
    private MutableLiveData<Boolean> personIsFind = new MutableLiveData<>();

    public DailyPlanViewModel(@NonNull Application application) {
        super(application);
        dataBaseYourSport = DataBaseYourSport.getInstance(application);
    }
    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Person> persons = dataBaseYourSport.personDao().getPersonsById(idPerson);
                if (persons.size() > 0){
                    person = persons.get(0);
                    personIsFind.postValue(true);
                }
            }
        });
        thread.start();
    }

    public MutableLiveData<Boolean> getPersonIsFind() {
        return personIsFind;
    }

    public int getPersonAge(){
        if (person != null)
            return person.getAge();
        else
            return 0;
    }

    public int getPersonWeight(){
        if (person != null)
            return person.getWeight();
        else
            return 0;
    }

    public int getPersonHeight(){
        if (person != null)
            return person.getHeight();
        else
            return 0;
    }

}
