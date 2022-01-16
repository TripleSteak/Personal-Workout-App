package me.simon76800.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

public class FinishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));

        HomeActivity.finishWorkout();
    }
}
