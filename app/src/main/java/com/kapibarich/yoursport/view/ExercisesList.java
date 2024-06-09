package com.kapibarich.yoursport.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kapibarich.yoursport.R;
import com.kapibarich.yoursport.model.Exercise;
import com.kapibarich.yoursport.viewmodel.ExercisesListViewModel;
import com.kapibarich.yoursport.viewmodel.ExercisesAdapter;

import java.util.List;

// В MVVM  MainActivity - это экран View, а взаимодействует с ним
public class ExercisesList extends AppCompatActivity {

    private RecyclerView recyclerViewNotes; // ЧТобы отображать элементы нужен класс Адаптер
    private FloatingActionButton buttonAddExercise;
    private FloatingActionButton buttonDailyPlan;
    private FloatingActionButton buttonProfile;
    private Intent intent;
    private ExercisesAdapter exercisesAdapter;
    private ExercisesListViewModel viewModel;

    Handler handler = new Handler(Looper.getMainLooper()); // Держит ссылку на главный поток

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);
        // viewModel = new MainViewModel(getApplication()); // При перевороте viewModel создастся заново. ПОэтому так неверно создавать
        viewModel = new ViewModelProvider(this).get(ExercisesListViewModel.class); // Такая реализация позволит пережить переворот экрана
        viewModel.setIdPerson(getIntent().getIntExtra("idPerson", 0));
        viewModel.getCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                Toast.makeText(
                        ExercisesList.this,
                        String.valueOf(count),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        initView();

        exercisesAdapter = new ExercisesAdapter();
        exercisesAdapter.setOnNoteClickListener(new ExercisesAdapter.onExerciseClickListener() { // данная реадизация замениться вместо интерфейса
            @Override
            public void onExerciseClick(Exercise exercise) {
                viewModel.showCount();
            }
        });

        recyclerViewNotes.setAdapter(exercisesAdapter);
        // Подпишемся на изменения в таблице note LiveData
        viewModel.getNotes().observe(ExercisesList.this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exercisesAdapter.setNotes(exercises); // Измененные данные notes установятся в адаптер
            }
        });

        buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Add_Exercise.newIntent(ExercisesList.this);
                startActivity(intent);
            }
        });


        // Чтобы реагировать на свайп или на перемещение объектов, нужен экземпляр класса ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0, // Не будем реагировать на перемещение
                        ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
                ) { // Параметр - CallBack = абстрактный класс, необходимо передать одну из реализаций
                    @Override
                    // DragDirs - направление перемещения SwipeDirs - направление свайпа
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false; // Срабатывает при перемещении, оставляем пустой
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        // ViewHolder - содержит номер позиции по которой произведен Swiped
                        int position = viewHolder.getAdapterPosition();
                        // Нужно получать id из адаптера, т.к. он отображается на экране
                        Exercise exercise = exercisesAdapter.getNotes().get(position);
                        viewModel.remove(exercise);
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes); // Перемещение прикрепили к recyclerViewNotes

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ProfileActivity.getIntent(ExercisesList.this, viewModel.getIdPerson()));
            }
        });

        buttonDailyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DailyPlanActivity.getIntent(ExercisesList.this, viewModel.getIdPerson()));
            }
        });
    }

    private void initView() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddExercise = findViewById(R.id.buttonAddExercise);
        buttonDailyPlan = findViewById(R.id.buttonDailyPlan);
        buttonProfile = findViewById(R.id.buttonProfile);
    }

    public static Intent getIntent(Context context, int idPerson){
        Intent intent = new Intent(context, ExercisesList.class);
        intent.putExtra("idPerson", idPerson);
        return intent;
    }
}
