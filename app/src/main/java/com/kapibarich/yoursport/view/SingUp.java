package com.kapibarich.yoursport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kapibarich.yoursport.R;
import com.kapibarich.yoursport.model.Person;
import com.kapibarich.yoursport.viewmodel.SingUpViewModel;

public class SingUp extends AppCompatActivity {

    private EditText editTextNameSingUp;
    private EditText editTextPasswordSingUp;
    private EditText editTextWeightSingUp;
    private EditText editTextHeightSingUp;
    private EditText editTextAgeSingUp;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMan;
    private RadioButton radioButtonWomen;
    private Button buttonCreateNewUser;


    private SingUpViewModel singUpViewModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        initViews();
        radioButtonMan.setChecked(true);

        singUpViewModel = new ViewModelProvider(this).get(SingUpViewModel.class);

        singUpViewModel.getShouldClose().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showMessage(getResources().getString(R.string.successfullyCreatePerson));
                finish();
            }
        });

        buttonCreateNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPerson();
            }
        });
    }

    private void createPerson() {
        String name = editTextNameSingUp.getText().toString().trim();
        String password = editTextPasswordSingUp.getText().toString().trim();
        String weight_str = editTextWeightSingUp.getText().toString().trim();
        String height_str = editTextHeightSingUp.getText().toString().trim(); //Integer.parseInt(
        String age_str = editTextAgeSingUp.getText().toString().trim();
        String gender = getGender();

        if (checkFields(name, password, weight_str, height_str, gender, age_str))
            singUpViewModel.createPerson(new Person(name, password, Integer.parseInt(weight_str), Integer.parseInt(height_str), gender, Integer.parseInt(age_str)));
    }

    private boolean checkFields(String name, String password, String weight, String height, String gender, String age) {
        if (name.isEmpty()) {
            showMessage("Заполните имя");
            return false;
        }
        if (password.isEmpty()) {
            showMessage("Заполните пароль");
            return false;
        }
        if (weight.isEmpty()) {
            showMessage("Заполните вес");
            return false;
        }
        if (height.isEmpty()) {
            showMessage("Заполните рост");
            return false;
        }
        if (age.isEmpty()) {
            showMessage("Заполните возраст");
            return false;
        }
        if (gender.isEmpty()) {
            return false;
        }
        return true;
    }

    private String getGender() {
        if (radioButtonMan.isChecked()) {
            return radioButtonMan.getText().toString();
        } else if (radioButtonWomen.isChecked()) {
            return radioButtonWomen.getText().toString();
        }
        return "";
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SingUp.class);
        return intent;
    }

    private void initViews() {
        editTextNameSingUp = findViewById(R.id.editTextNameSingUp);
        editTextPasswordSingUp = findViewById(R.id.editTextPasswordSingUp);
        editTextWeightSingUp = findViewById(R.id.editTextWeightSingUp);
        editTextAgeSingUp = findViewById(R.id.editTextAgeSingUp);
        editTextHeightSingUp = findViewById(R.id.editTextHeightSingUp);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMan = findViewById(R.id.radioButtonMan);
        radioButtonWomen = findViewById(R.id.radioButtonWomen);
        buttonCreateNewUser = findViewById(R.id.buttonCreateNewUser);
    }

    private void showMessage(String message) {
        Toast.makeText(
                SingUp.this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

}