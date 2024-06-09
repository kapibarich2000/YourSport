package com.kapibarich.yoursport.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kapibarich.yoursport.R;
import com.kapibarich.yoursport.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises = new ArrayList<>(); // Для работы с БД ArrayList меняем на List. new ArrayList реализует интерфейс List
    private onExerciseClickListener onExerciseClickListener; // переменная интерфейсного типа. Из Main нужно передавать реализацию интерфейса onNoteClickListener

    public void setOnNoteClickListener(ExercisesAdapter.onExerciseClickListener onNoteClickListener) {
        this.onExerciseClickListener = onNoteClickListener;
    }

    public void setNotes(List<Exercise> exercises) {
        this.exercises = exercises; // Просто устанавливаем новое значение, не зная, что добавился новый элемент
        notifyDataSetChanged();
    }

    @NonNull
    @Override   // RecyclerView.ViewHolder заменим на собственную реализацю
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {    // нужен для создания View
        View view = LayoutInflater.from(parent.getContext()).inflate( // Получили View макета note_item.xml. Вызовется 10-12 раз
                R.layout.exercise_item,
                parent,
                false); // Преобразовать xml файл во View элемент
        return new ExerciseViewHolder(view);
    }

    @Override
    // NoteViewHolder = ViewHolder - содержит все ссылки на View элементы, с которыми можно работать
    public void onBindViewHolder(@NonNull ExerciseViewHolder viewHolder, int position) { // для отображения элемента: в начале и при прокрутке
        Exercise exercise = exercises.get(position); // Какое упражнение нужно отобразить
        viewHolder.textViewExerciseName.setText(exercise.getName());
        viewHolder.textViewExerciseDescription.setText(exercise.getDescription());

        int colorResId;
        switch (exercise.getDifficulty()) {
            case 0:
                colorResId = android.R.color.holo_green_light;
                break;
            case 1:
                colorResId = android.R.color.holo_orange_light;
                break;
            default:
                colorResId = android.R.color.holo_red_light;
                break;
        }
        // Конекст можно получить у любого View элемента, либо у объекта ViewGroup
        int color = ContextCompat.getColor(viewHolder.itemView.getContext(), colorResId);
        viewHolder.linearLayoutParent.setBackgroundColor(color);

        // Адаптер отвечает за работу с View элементами, никогда не должен работать с базой данных, всю реализацию нужно вынести в Activity через interface
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onExerciseClickListener != null)
                    onExerciseClickListener.onExerciseClick(exercise);
            }
        });

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    } // Возвращает количество элементов в отображаемой коллекции


    class ExerciseViewHolder extends RecyclerView.ViewHolder { // ViewHolder
        private LinearLayout linearLayoutParent;
        private TextView textViewExerciseName;
        private TextView textViewExerciseDescription;
        private ImageView imageViewExercisePhoto;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutParent = itemView.findViewById(R.id.linearLayoutParent);
            textViewExerciseName = itemView.findViewById(R.id.textViewExerciseName);
            textViewExerciseDescription = itemView.findViewById(R.id.textViewExerciseDescription);
            imageViewExercisePhoto = itemView.findViewById(R.id.imageViewExercisePhoto);
        }
    }

    public List<Exercise> getNotes() {
        return new ArrayList<Exercise>(exercises);
    }

    public interface onExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }
}
