package me.simon76800.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.simon76800.workout.item.RepExercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UntimedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_untimed);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));

        TextView name = findViewById(R.id.exercise_name);
        TextView description = findViewById(R.id.exercise_description);
        Button doneButton = findViewById(R.id.done_button);

        name.setText(HomeActivity.EXERCISES[HomeActivity.curWorkout].get(SummaryActivity.curExercise).getWorkoutName());

        final UntimedActivity instance = this;
        String descriptionText = ((RepExercise) (HomeActivity.EXERCISES[HomeActivity.curWorkout].get(SummaryActivity.curExercise))).getReps() + " REPS\n" + HomeActivity.EXERCISES[HomeActivity.curWorkout].get(SummaryActivity.curExercise).getWeight() + " LBS\n\n" + HomeActivity.EXERCISES[HomeActivity.curWorkout].get(SummaryActivity.curExercise).getDescription();
        description.setText(descriptionText);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(instance, TimedActivity.class);

                SummaryActivity.needsRest = true;
                startActivity(intent);
                finish();
            }
        });

        HomeActivity.speak(HomeActivity.EXERCISES[HomeActivity.curWorkout].get(SummaryActivity.curExercise).getWorkoutName() + " " + ((RepExercise) HomeActivity.EXERCISES[HomeActivity.curWorkout].get(SummaryActivity.curExercise)).getReps() + " reps.");
    }
}
