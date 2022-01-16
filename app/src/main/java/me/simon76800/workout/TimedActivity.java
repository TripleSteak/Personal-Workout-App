package me.simon76800.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.simon76800.workout.item.RepExercise;
import me.simon76800.workout.item.TimedExercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static me.simon76800.workout.SummaryActivity.curExercise;
import static me.simon76800.workout.SummaryActivity.needsRest;

public class TimedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timed);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));

        final TextView name = findViewById(R.id.exercise_name);
        TextView description = findViewById(R.id.exercise_description);
        final TimedActivity instance = this;

        int timerVal = 0;

        if (needsRest) {
            needsRest = false;
            name.setText(R.string.rest_time);

            HomeActivity.speak("Rest for " + HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getRest() + " seconds.");
            timerVal = HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getRest();

            String descriptionString = "";
            int nextSet = SummaryActivity.completedSets + 1;
            boolean nextExercise = false;

            if (nextSet >= HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getSets())
                nextExercise = true;

            if (curExercise + (nextExercise ? 1 : 0) >= HomeActivity.EXERCISES[HomeActivity.curWorkout].size())
                descriptionString = getResources().getString(R.string.almost_done);
            else if (nextExercise)
                descriptionString = "NEXT UP:\n\n" + HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise + 1).getWorkoutName();
            else
                descriptionString = "NEXT UP:\n\n" + HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getWorkoutName();

            description.setText(descriptionString);
        } else {
            needsRest = true;

            name.setText(HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getWorkoutName());

            String descriptionText = HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getWeight() + " LBS\n\n" + HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getDescription();
            description.setText(descriptionText);

            timerVal = ((TimedExercise) HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise)).getTime();
            HomeActivity.speak(HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getWorkoutName() + " for " + ((TimedExercise) HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise)).getTime() + " seconds.");
        }

        final int timerStart = timerVal;

        final TextView timerView = findViewById(R.id.timer);
        timerView.setText(String.valueOf(timerVal));

        final boolean switchSides = needsRest;
        Thread timerThread = new Thread() {
            @Override
            public void run() {
                int timerCount = timerStart;
                while (timerCount != 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        // No action required
                    }

                    timerCount--;
                    timerView.setText(String.valueOf(timerCount));

                    if (timerCount <= 5 && timerCount > 0)
                        HomeActivity.speak(String.valueOf(timerCount));
                }

                if (switchSides && HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).isBilateral()) {
                    HomeActivity.speak("Switch sides.");

                    timerCount = timerStart;
                    while (timerCount != 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            // No action required
                        }

                        timerCount--;
                        timerView.setText(String.valueOf(timerCount));

                        if (timerCount <= 5 && timerCount > 0)
                            HomeActivity.speak(String.valueOf(timerCount));
                    }
                }

                if (!needsRest || HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getRest() == 0) {

                    SummaryActivity.completedSets++;
                    if (SummaryActivity.completedSets >= HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getSets()) {
                        SummaryActivity.completedSets = 0;
                        curExercise++;
                    }

                    if (curExercise >= HomeActivity.EXERCISES[HomeActivity.curWorkout].size()) {
                        Intent intent = new Intent(instance, FinishActivity.class);

                        startActivity(intent);
                        finish();

                        return;
                    }

                    Intent intent = null;
                    if (HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise) instanceof RepExercise)
                        intent = new Intent(instance, UntimedActivity.class);
                    else intent = new Intent(instance, TimedActivity.class);

                    if(HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise).getRest() == 0) needsRest = false;

                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(instance, TimedActivity.class);

                    needsRest = true;
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }
}
