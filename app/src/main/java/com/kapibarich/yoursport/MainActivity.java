package com.kapibarich.yoursport;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kapibarich.yoursport.viewmodel.ExercisesAdapter;
import com.kapibarich.yoursport.viewmodel.ExercisesListViewModel;

// В MVVM  MainActivity - это экран View, а взаимодействует с ним
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes; // ЧТобы отображать элементы нужен класс Адаптер
    private FloatingActionButton buttonAddNote;
    private ExercisesAdapter exercisesAdapter;
    private ExercisesListViewModel viewModel;

    Handler handler = new Handler(Looper.getMainLooper()); // Держит ссылку на главный поток

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}