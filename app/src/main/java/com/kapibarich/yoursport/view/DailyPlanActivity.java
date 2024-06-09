package com.kapibarich.yoursport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;

import java.io.FileInputStream;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kapibarich.yoursport.R;
import com.kapibarich.yoursport.viewmodel.DailyPlanViewModel;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DailyPlanActivity extends AppCompatActivity {
    private DailyPlanViewModel viewModel;
    private EditText editTextX1;
    private EditText editTextX2;
    private EditText editTextX3;
    private EditText editTextAgeDailyPlan;
    private EditText editTextHihtDailyPlan;
    private EditText editTextWeightDailyPlan;
    private Button buttonNNTest;
    private Spinner spinnerTypeOfTrainingDailyPlan;

    private Interpreter tflite;
    private ByteBuffer tflitemodel;
    private EditText txtValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_plan);
        initViews();
        viewModel = new ViewModelProvider(this).get(DailyPlanViewModel.class);
        viewModel.setIdPerson(getIntent().getIntExtra("idPerson", 0));

        try {
            try {
                tflitemodel = loadModelFile(this.getAssets(), "model.tflite");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            tflite = new Interpreter(tflitemodel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        buttonNNTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doInference();
            }
        });


        viewModel.getPersonIsFind().observe(DailyPlanActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean personIsFind) {
                if (personIsFind)
                    fillFields();
            }
        });
    }

    private void initViews() {
        editTextX1 = findViewById(R.id.editTextX1);
        editTextX2 = findViewById(R.id.editTextX2);
        editTextX3 = findViewById(R.id.editTextX3);
        editTextAgeDailyPlan = findViewById(R.id.editTextAgeDailyPlan);
        editTextHihtDailyPlan = findViewById(R.id.editTextHihtDailyPlan);
        editTextWeightDailyPlan = findViewById(R.id.editTextWeightDailyPlan);
        buttonNNTest = findViewById(R.id.buttonNNTest);
        spinnerTypeOfTrainingDailyPlan = findViewById(R.id.spinnerTypeOfTrainingDailyPlan);
    }

    private void fillFields() {
        editTextAgeDailyPlan.setText(String.valueOf(viewModel.getPersonAge()));
        editTextHihtDailyPlan.setText(String.valueOf(viewModel.getPersonHeight()));
        editTextWeightDailyPlan.setText(String.valueOf(viewModel.getPersonWeight()));
    }

    static public Intent getIntent(Context context, int idPerson) {
        Intent intent = new Intent(context, DailyPlanActivity.class);
        intent.putExtra("idPerson", idPerson);
        return intent;
    }

    private ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(String.valueOf(fileDescriptor));
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void doInference() {
        int x1 = Integer.parseInt(editTextX1.getText().toString());
        int x2 = Integer.parseInt(editTextX2.getText().toString());
        int x3 = Integer.parseInt(editTextX3.getText().toString());
        int weight = Integer.parseInt(editTextWeightDailyPlan.getText().toString());
        int height = Integer.parseInt(editTextHihtDailyPlan.getText().toString());
        int age = Integer.parseInt(editTextAgeDailyPlan.getText().toString());
        int typeOfTraining = getTypeOfTraining();

        ArrayList<Integer> inputVal = new ArrayList<>();
        inputVal.add(x1);
        inputVal.add(x2);
        inputVal.add(x3);
        inputVal.add(weight);
        inputVal.add(height);
        inputVal.add(age);
        inputVal.add(typeOfTraining);

        ByteBuffer outputVal = ByteBuffer.allocateDirect(4);
        outputVal.order(ByteOrder.nativeOrder());
        tflite.run(inputVal, outputVal);
        outputVal.rewind();
        Integer result = outputVal.getInt();
        showInference(inputVal, result);
    }

    private int getTypeOfTraining() {
        String type = spinnerTypeOfTrainingDailyPlan.getSelectedItem().toString();
        if (type.equals("Сила"))
            return 0;
        else if (type.equals("Гипертрофия"))
            return 1;
        else    // "Выносливость"
            return 2;
    }

    private void showInference(ArrayList<Integer> inputVal, Integer result) {
        int typeOfTraining = inputVal.get(6);
        int summ = inputVal.get(0) + inputVal.get(1) + inputVal.get(2);
        if (typeOfTraining == 0) {
            if (summ > 8) {
                showMessage("Следует повысить сложность");
                return;
            } else if (summ < 3) {
                showMessage("Следует понизить сложность");
                return;
            } else if (summ >= 3 && summ <= 8) {
                showMessage("Так держать");
                return;
            }
        } else if (typeOfTraining == 1) {
            if (summ > 36) {
                showMessage("Следует повысить сложность");
                return;
            } else if (summ < 18) {
                showMessage("Следует понизить сложность");
                return;
            } else if (summ >=  18 && summ <= 36) {
                showMessage("Так держать");
                return;
            }
        } else if (typeOfTraining == 2) {
            if (summ > 90) {
                showMessage("Следует повысить сложность");
                return;
            } else if (summ < 20) {
                showMessage("Следует понизить сложность");
                return;
            } else if (summ >= 20 && summ <= 90) {
                showMessage("Так держать");
                return;
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(
                DailyPlanActivity.this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}