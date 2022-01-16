package me.simon76800.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.simon76800.workout.item.Exercise;
import me.simon76800.workout.item.RepExercise;
import me.simon76800.workout.item.TimedExercise;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {
    public static int curExercise;
    public static int completedSets;
    public static boolean needsRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));

        final LinearLayout parent = findViewById(R.id.summary_scroll);
        parent.removeAllViews();
        boolean firstElement = true;

        for (Exercise exercise : HomeActivity.EXERCISES[HomeActivity.curWorkout]) {
            if (!firstElement) {
                View line = new View(this);
                line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                line.setBackgroundColor(ContextCompat.getColor(this, R.color.colorFaintGrey));

                parent.addView(line);
            } else firstElement = false;

            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layout.setLayoutParams(params);
            layout.setPadding(HomeActivity.convertToPx(10, this), HomeActivity.convertToPx(5, this), HomeActivity.convertToPx(10, this), HomeActivity.convertToPx(5, this));
            layout.setOrientation(LinearLayout.VERTICAL);

            TextView titleText = new TextView(this);
            titleText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            titleText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            titleText.setTextSize(20);
            titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
            titleText.setText(exercise.getWorkoutName());

            TextView bodyText = new TextView(this);
            bodyText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            bodyText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            bodyText.setTextSize(18);

            String bodyString = "   ";
            if (exercise instanceof RepExercise)
                bodyString += exercise.getSets() + " x " + ((RepExercise) exercise).getReps() + " reps" + (exercise.getWeight() == 0 ? "" : " @ " + exercise.getWeight() + "lbs");
            else
                bodyString += (exercise.getSets() <= 1 ? "" : exercise.getSets() + " x ") + ((TimedExercise) exercise).getTime() + " seconds" + (exercise.getWeight() == 0 ? "" : " @ " + exercise.getWeight() + "lbs");
            bodyText.setText(bodyString);

            layout.addView(titleText);
            layout.addView(bodyText);

            parent.addView(layout);
        }

        final SummaryActivity instance = this;

        Button beginButton = new Button(this);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(HomeActivity.convertToPx(200, this), HomeActivity.convertToPx(60, this));
        buttonParams.setMargins(0, HomeActivity.convertToPx(20, this), 0, HomeActivity.convertToPx(20, this));
        beginButton.setLayoutParams(buttonParams);
        beginButton.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        beginButton.setTextSize(26);
        beginButton.setTypeface(beginButton.getTypeface(), Typeface.BOLD);
        beginButton.setBackground(getDrawable(R.drawable.rounded_rectangle));
        beginButton.setText(R.string.do_it);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curExercise = 0;
                completedSets = 0;
                needsRest = false;

                Intent intent = null;
                if (HomeActivity.EXERCISES[HomeActivity.curWorkout].get(curExercise) instanceof RepExercise)
                    intent = new Intent(instance, UntimedActivity.class);
                else intent = new Intent(instance, TimedActivity.class);

                startActivity(intent);
                finish();
            }
        });
        parent.addView(beginButton);
    }
}
