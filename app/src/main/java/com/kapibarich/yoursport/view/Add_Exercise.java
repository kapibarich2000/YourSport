package com.kapibarich.yoursport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.kapibarich.yoursport.model.Exercise;
import com.kapibarich.yoursport.viewmodel.AddExerciseViewModel;
import com.kapibarich.yoursport.R;


public class Add_Exercise extends AppCompatActivity {

    private EditText editTextExerciseName;
    private EditText editTextExerciseDescription;
    private Spinner spinnerTypesOfExercises;
    private RadioButton radioButtonLow;
    private RadioButton radioButtonMedium;
    private RadioButton radioButtonHigh;
    private Button buttonSaveExercise;
    private Handler handler = new Handler(Looper.getMainLooper());

    private AddExerciseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        initViews();
        viewModel = new ViewModelProvider(this).get(AddExerciseViewModel.class);
        viewModel.getShouldClose().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldClose) {
                if (shouldClose)
                    finish();
            }
        });

        buttonSaveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {saveNote();}
        });
    }

    private void initViews() {
        editTextExerciseName = findViewById(R.id.editTextExerciseName);
        editTextExerciseDescription = findViewById(R.id.editTextExerciseDescription);
        spinnerTypesOfExercises = findViewById(R.id.spinnerTypesOfExercises);
        radioButtonLow = findViewById(R.id.radioButtonLow);
        radioButtonMedium = findViewById(R.id.radioButtonMedium);
        radioButtonHigh = findViewById(R.id.radioButtonHigh);
        buttonSaveExercise = findViewById(R.id.buttonSaveExercise);
    }

    private void saveNote() {
        String name = editTextExerciseName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.warning_empty_exercise_text, Toast.LENGTH_SHORT).show();
            return;
        }
        String description = editTextExerciseDescription.getText().toString().trim();
        String muscle = spinnerTypesOfExercises.getSelectedItem().toString();
        int difficulty = getDifficulty();
        Exercise exercise = new Exercise(name,description,muscle,difficulty);
        // Когда мы работаем с данными (запись, изменение, редактирование), тогда необходимо работать с базой данных
        viewModel.saveExercise(exercise);

    }

    private int getDifficulty() {
        int priority;
        if (radioButtonLow.isChecked())
            priority = 0;
        else if (radioButtonMedium.isChecked())
            priority = 1;
        else
            priority = 2;
        return priority;
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, Add_Exercise.class);
    }

}