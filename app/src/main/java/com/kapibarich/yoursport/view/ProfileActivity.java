package com.kapibarich.yoursport.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kapibarich.yoursport.R;
import com.kapibarich.yoursport.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel viewModel;
    private TextView textViewNamePersonProfile;
    private TextView textViewWeightPersonProfile;
    private TextView textViewHeightPersonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class); // Такая реализация позволит пережить переворот экрана
        viewModel.setIdPerson(getIntent().getIntExtra("idPerson", 0));

        viewModel.getPersonIsFind().observe(ProfileActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean personIsFind) {
                if(personIsFind)
                    fillFields();
            }
        });

    }

    private void initView() {
        textViewNamePersonProfile = findViewById(R.id.textViewNamePersonProfile);
        textViewWeightPersonProfile = findViewById(R.id.textViewWeightPersonProfile);
        textViewHeightPersonProfile = findViewById(R.id.textViewHeightPersonProfile);
    }

    static public Intent getIntent(Context context, int idPerson) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("idPerson", idPerson);
        return intent;
    }

    private void fillFields(){
        textViewNamePersonProfile.setText(viewModel.getPersonUserName());
        textViewWeightPersonProfile.setText(String.valueOf(viewModel.getPersonWeight()));
        textViewHeightPersonProfile.setText(String.valueOf(viewModel.getPersonHeight()));
    }
}