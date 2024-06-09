package com.kapibarich.yoursport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kapibarich.yoursport.R;
import com.kapibarich.yoursport.viewmodel.AuthorizationViewModel;

public class Authorization extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonSignUp;

    private AuthorizationViewModel authorizationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        initViews();

        authorizationViewModel = new ViewModelProvider(this).get(AuthorizationViewModel.class);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SingUp.newIntent(Authorization.this));
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singIn();
            }
        });
        authorizationViewModel.getGoodAuthorization().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean goodAuthorization) {
                if (goodAuthorization)
                    startActivity(ExercisesList.getIntent(Authorization.this, authorizationViewModel.getIdPerson())); // editTextName.getText().toString().trim()
            }
        });
        authorizationViewModel.getTryOfAuthorization().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer countOfTry) {
                if(countOfTry > 0)
                    showMessage("Неверное имя пользователя или пароль");
            }
        });

    }

    private void singIn() {
        String userName = editTextName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (userName.isEmpty() || password.isEmpty())
            showMessage("Введите имя пользователя и пароль");
        else
            authorizationViewModel.singIn(userName, password);
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
    }

    private void showMessage(String message) {
        Toast.makeText(
                Authorization.this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}