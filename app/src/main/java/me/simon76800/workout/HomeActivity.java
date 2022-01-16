package me.simon76800.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.simon76800.workout.item.Exercise;
import me.simon76800.workout.item.RepExercise;
import me.simon76800.workout.item.TimedExercise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public static int curWorkout; // today's workout
    public static String today;

    /*
    Numerical values for all possible workout types
     */
    public static final int UPPER_STRENGTH = 0;
    public static final int LOWER_STRENGTH = 1;
    public static final int HIIT = 2;
    public static final int TOTAL_BODY_STRENGTH = 3;
    public static final int CARDIO = 4;

    @SuppressWarnings("unchecked")
    public static final ArrayList<Exercise>[] EXERCISES = new ArrayList[5];

    public static HomeActivity instance;

    public static TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));

        instance = this;

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        populateExercises();

        /*
        Get current day
         */
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        today = format.format(ldt);

        /*
        Load save information
         */
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        curWorkout = sharedPref.getInt(getString(R.string.cur_workout_key), HIIT);
        String nextDay = sharedPref.getString(getString(R.string.next_day_key), today);

        Button beginButton = findViewById(R.id.begin_button);
        TextView restText = findViewById(R.id.rest_text);

        Log.i("Workout", "Today is " + today + ", next workout is " + nextDay + " (workout type " + curWorkout + ")");

        if (nextDay.compareToIgnoreCase(today) <= 0) { // workout scheduled for today
            beginButton.setVisibility(View.VISIBLE);
            restText.setVisibility(View.GONE);

            beginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(instance, SummaryActivity.class);
                    startActivity(intent);
                    instance.finish();
                }
            });
        } else {
            beginButton.setVisibility(View.GONE);
            restText.setVisibility(View.VISIBLE);
        }
    }

    public void populateExercises() {
        //**************************************************************************
        //
        //                          LIST OF EXERCISES
        //
        //**************************************************************************

        // Warm up
        TimedExercise ARM_CIRCLES = new TimedExercise("Arm Circles", "Slowly circle your arms, straightened, around the sides of your body as far as you can. Alternate forwards and backwards.", 0, 3, 15, 15, true); // 2:15
        TimedExercise BAND_SHOULDER_ROTATIONS = new TimedExercise("Band Shoulder Rotations", "Grab onto and stretch a resistance band with both hands. Place it at waist height, and slowly bring the band over your body until it reaches waist height behind you. Bring it back and repeat.", 0, 3, 30, 15, false); // 2:15
        TimedExercise PRISONER_ROTATIONS = new TimedExercise("Prisoner Rotations", "Lay down on your belly and bring your arms out to the side, straightened. Slowly bring your arms to a touch in front of you, and then behind you. Squeeze your shoulder blades while performing this warmup.", 0, 3, 20, 15, false); // 1:45

        RepExercise CAT_CAMEL = new RepExercise("Cat Camel", "On your hands and knees, push your lower back as far out as possible. Then, push your abdomen as close to the ground as possible.", 0, 2, 12, 25, false); // 2:00
        RepExercise REACH_ROLL_LIFT = new RepExercise("Reach, Roll, Lift", "Go on all fours on your knees, and shift back so that you're sitting on your calves. Gently stretch one arm out along the floor, turn it palm up, and reach for the sky. Complete both arms to count as a rep.", 0, 3, 8, 25, false); // 4:30

        RepExercise LEG_SWINGS = new RepExercise("Leg Swings", "With your other leg and back straightened, hold onto a wall and swing your active leg as far forwards and backwards as you can, while maintaining an unbent knee.", 0, 3, 12, 15, true); // 3:00
        RepExercise FIRE_HYDRANTS = new RepExercise("Fire Hydrants", "With your hands and knees on the ground, lift up your leg fully to the side while maintaining a ninety degree bend at the knees. Repeat on the other side for a full rep.", 0, 3, 8, 15, false); // 3:00
        RepExercise QUAD_PULL_BACK = new RepExercise("Walking Pull-Backs", "Walk in a direction, but pull your leg back to touch your bottom every step. One iteration of both legs counts as one rep.", 0, 3, 8, 15, false); // 3:00

        RepExercise DEAD_BUG = new RepExercise("Dead Bug", "Lie down flat on the floor, facing up. Slowly raise one straightened arm to the ceiling while simultaneously raising the knee of the opposite leg. Try to keep your lower back touching the ground. Complete both sides for a rep.", 0, 3, 6, 25, false); // 4:30

        TimedExercise HIP_ROTATIONS = new TimedExercise("Hip Rotations", "With your hands on your hips, circle them in both directions as far as you can.", 0, 2, 15, 15, true); // 2:30
        TimedExercise KNEE_ROTATIONS = new TimedExercise("Knee Rotations", "Place your hands on your knees, and rotate them in both directions slowly.", 0, 2, 15, 15, true); // 2:30

        TimedExercise HIGH_KNEES = new TimedExercise("High Knees", "Lightly jog around in a small circle, making sure to raise your knees to your chest each step.", 0, 3, 30, 15, false); // 2:15

        // Stretches/Cool down
        TimedExercise CROSS_BODY_SHOULDER_STRETCH = new TimedExercise("Cross-Body Shoulder Stretch", "Straighten your arm across your body and pull back gently with the other arm.", 0, 2, 25, 15, true); // 2:25
        TimedExercise FOREARM_STRETCH = new TimedExercise("Forearm Stretch", "Straighten your arm palm-up, and use your other hand to gently pull down your palm and bend your wrist.", 0, 2, 25, 15, true); // 2:25
        TimedExercise OVERHEAD_TRICEP_STRETCH = new TimedExercise("Overhead Tricep Stretch", "Place your hand on your upper back, raise your elbow up high, and gently push down on your elbow with your other hand.", 0, 2, 25, 15, true); // 2:25

        TimedExercise COBRA_STRETCH = new TimedExercise("Cobra Stretch", "Enter pushup position and bring your abdomen to the floor. You should feel a stretch in your core.", 0, 2, 25, 15, false); // 1:20

        TimedExercise SIDE_SPLIT = new TimedExercise("Side Split", "Straighten your legs directly to your left and right. Whilst maintaining straightened legs and unflexed knees, go as low as possible.", 0, 2, 25 , 15, false); // 1:20
        TimedExercise BUTTERFLY_STRETCH = new TimedExercise("Butterfly Stretch", "Sit as if you were about to cross your legs, but instead place your feet together sole to sole. Lower your knees as close as you can to the ground.", 0, 2, 25, 15, false); // 1:20
        TimedExercise FRONT_SPLIT = new TimedExercise("Front Split", "Straighten your legs directly to your front and back. Whilst maintaining straightened legs and unflexed knees, go as low as possible.", 0, 2, 25, 15, true); // 1:20

        TimedExercise SITTING_TOE_TOUCH = new TimedExercise("Sitting Toe Touch", "Grab onto your toes while sitting down with your legs straightened and together.", 0, 2, 25, 15, false);

        // Push-ups
        RepExercise DIAMOND_PUSHUP_FEET_ELEVATED = new RepExercise("Diamond Pushups, Feet Elevated", "Enter standard pushup position, but with hands close together (thumbs and index fingers should touch to form a diamond). Place feet on elevated surface.", 0, 3, 8, 60, false); // 4:30
        RepExercise SIDE_OBLIQUE_PUSHUP = new RepExercise("Side Oblique Pushups", "Lay down on your side and, using your hip as a fulcrum, push yourself up with your higher arm.", 0, 3, 12, 60, true); // 6:00
        RepExercise INCHWORM_PUSHUP = new RepExercise("Inchworm Pushups", "From a standing position, reach down until your arms are far enough forward to enter a pushup. Afterward, while maintaining your hands on the ground, step forwards until your feet reach your hands.", 0, 3, 8, 60, false); // 4:30
        RepExercise WIDE_CLAP_PUSHUP = new RepExercise("Wide Clap Pushups", "Perform standard pushups with hands widened, so that the elbows form 90 degree bends, and clap between reps. Perform slowly.", 0, 3, 8, 60, false); // 5:00

        // Pull-ups
        RepExercise WIDE_GRIP_PULLUP = new RepExercise("Wide Grip Pullups", "Grip the pullup bar overhand with hands further apart than shoulder distance and pull upward until chin passes the bar. Try to keep shoulders and neck relaxed.", 0, 5, 3, 75, false); // 6:30
        RepExercise CHIN_UP = new RepExercise("Chin Ups", "Grip the pullup bar underhand with hands shoulder-width apart and pull upward until chin passes the bar. Try to keep shoulders and neck relaxed.", 0, 5, 4, 75, false); // 7:30

        // Holds
        TimedExercise WIDE_OVERHAND_HANG = new TimedExercise("Wide Overhand Hang", "Hang from a pullup bar with a wide overhand grip.", 0, 3, 20, 75, false); // 5:45

        // Rows
        RepExercise RENEGADE_ROWS = new RepExercise("Renegade Rows", "Enter pushup position with two dumbbells. For each rep, perform rows with each dumbbell sequentially.", 15, 3, 8, 60, false); // 6:00

        // Dumbbell raises
        RepExercise PRONE_LAT_RAISE = new RepExercise("Prone Lateral Raises", "Lie flat on your belly with your arms perpendicular to the side. While keeping the arms straight, lift upward away from floor as far as possible.", 5, 3, 16, 45, false); // 4:30
        RepExercise PRONE_LAT_RAISE_W_CIRCLES = new RepExercise("Prone Lateral Raises w/ Circles", "Perform standard prone lateral raises with small arm circles for 5 seconds during each rep.", 2, 3, 4, 30, false); // 1:15
        RepExercise L_RAISES = new RepExercise("L Raises", "Create an L-shape with your arms, holding a dumbbell in each hand. Rotate the dumbbells so that your arms form vertical L-shapes upward, then back to parallel.", 10, 3, 10, 60, false); // 5:30
        RepExercise DUMBBELL_FLIES = new RepExercise("Dumbbell Flies", "Lie down flat facing up, with your arms straightened. Hold two dumbbells with an underhand grip, and, with your arms straightened, raise them to the ceiling so that they almost touch.", 15, 3, 10, 45, false); // 5:45
        RepExercise REVERSE_FLIES = new RepExercise("Reverse Flies", "In a bent-forward standing position, start with your dummbells in your hands, together below your shoulders. Raise them in opposing directions while trying to maintain straightened arms.", 10, 3, 10, 45, false); // 5:30

        // Arm twists
        RepExercise SIDE_LYING_LATERAL_ARM_TWIST = new RepExercise("Side Lying Lateral Arm Twists", "Lay down on your side and raise a straightened arm into the air. Slowly bring it down to the ground, and turn your knuckles toward your head. Then, bring your arm back up to the perpendicular, twisting your arm so that your knuckles face your feet.", 5, 2, 12, 45, true); // 3:30
        RepExercise SIDE_LYING_ELBOW_TWIST = new RepExercise("Side Lying Elbow Twists", "Lay down on your side with your free arm's elbow attached to your waist. Hold a dumbbell with your forearm pointing upward and twist your arm downward until the dumbbell is almost touching the floor. Bring back up to the perpendicular and repeat.", 5, 3, 12, 45, true); // 3:00

        // Dips
        RepExercise CHEST_DIPS = new RepExercise("Chest Dips", "Place your hands on two bars slightly wider than shoulder width apart, lower yourself until your arms reach a 90 degree formation, and raise yourself back up.", 0, 3, 5, 75, false); // 8:45

        // Leg raises
        RepExercise VERT_LEG_LIFTS = new RepExercise("Vertical Leg Lifts", "Stand up straight against a cushioned surface and grab onto two bars. Lift your feet off the floor and, keeping legs straightened and together, raise to a 90 degree from your back.", 0, 3, 12, 60, false); // 4:30

        // Squats
        RepExercise DEEP_SQUAT = new RepExercise("Deep Squat", "Place your feet shoulder width apart and squat all the way down. When coming back up, push your feet apart against the ground.", 0, 3, 20, 75, false); // 8:00
        RepExercise DEEP_LUNGE = new RepExercise("Deep Lunges", "Take a large step forward and bend all the way down, so that both legs form a ninety degree angle (one shin parallel to ground, one perpendicular). Return to standing, and repeat for the other leg for a full rep. Hold one dumbbell in each hand.", 15, 2, 18, 90, false); // 7:30

        TimedExercise TIMED_JUMP_SQUATS = new TimedExercise("Timed Jump Squats",  "Perform standard squats as quickly as you can, while adding an explosive jump in between reps", 0, 1, 30, 0, false); // 0:30

        // Bridges
        RepExercise HIP_BRIDGES = new RepExercise("Hip Bridges", "Lie down facing up on the ground, and lift your hip up off the floor. Your back, hips, and thighs should create a straight line perpendicular to your shins. Hold for about 3 seconds, and lower back down. This counts as one rep.", 0, 3, 12, 60,false); // 5:00

        // Core
        RepExercise RUSSIAN_TWIST = new RepExercise("Russian Twists", "In a V-sit position with straightened legs, hold a ball at abdomen level and bring it to the ground on both sides of your body.", 15, 3, 12, 30, false); // 4:00
        RepExercise CROSS_LEG_SIT_UP = new RepExercise("Cross-Legged Sit Ups", "Perform a standard sit up, but with your legs crossed. You do not need to reach ninety degrees– having your lower back off the ground is enough.", 0, 3, 24, 45, false); // 6:30
        RepExercise LYING_WINDSHIELD_WIPERS = new RepExercise("Lying Windshield Wipers", "Lying down, raise your legs to 90 degrees. Spread your arms out straight to your sides, and rotate your legs fully to one side at a time.", 0, 2, 12, 45, false); // 4:00

        // Timed Running
        TimedExercise TIMED_RUN_SPRINT = new TimedExercise("Running Sprints", "Keep torso straight, head still and relaxed, and shoulders steady and relaxed. Bring knees high and maintain 90 degree bend in elbows.", 0, 1, 60, 0, false); // 1:00
        TimedExercise TIMED_JOG = new TimedExercise("Running Jogs", "Keep torso straight, head still and relaxed, and houlders steady and relaxed. Bring knees high and maintain 90 degree bend in elbows.", 0, 1, 90, 0, false); // 1:30
        TimedExercise TIMED_WALK = new TimedExercise("Timed Walk", "Walk for a minimum amount of time to cool down from running", 0, 1, 120, 0, false); // 2:00

        // High intensity
        TimedExercise TIMED_BURPEES = new TimedExercise("Timed Burpees", "From a standing position, alternate between jumping as high as you can and dropping to the ground for a full push-up.", 0, 1, 30, 0, false); // 1:00
        TimedExercise JUMPING_JACKS = new TimedExercise("Jumping Jacks", "While jumping, alternate between landing with feet together and arms down low to feet apart and arms up high, as fast as you can.", 0, 1, 30, 0, false); // 1:00
        TimedExercise MOUNTAIN_CLIMBERS = new TimedExercise("Mountain Climbers", "In a pushup position, alternate high knees with a slight bounce each time.", 0, 1, 30, 0, false);
        TimedExercise BICYCLE_CRUNCHES = new TimedExercise("Bicycle Crunches", "Lie flat on the ground, face up. Bring your knees up so that your thighs are perpendicular to the floor, and place your hands behind your head. Alternate elbow and knee contact in a cycling motion.", 0, 1, 30, 0, false); // 0:30

        // Low intensity
        TimedExercise STATIONARY_JOG = new TimedExercise("Stationary Jog", "Jog lightly on the spot– the purpose of this exercise is to keep you active during a rest.", 0, 1, 30, 0, false); // 0:30

        // Miscellaneous
        RepExercise STAIRCASE_BICEP_CURL = new RepExercise("Staircase Bicep Curls", "Walk up and down flights of stairs while doing bicep curls (one in each hand).", 15, 3, 8, 45, false); // 4:30

        // Cardio
        RepExercise RUNNING = new RepExercise("Running", "Keep torso straight, head still and relaxed, and shoulders steady and relaxed. Land on balls of your feet. This is a steady-state cardio exercise, run for as long as you can.", 0, 1, 1, 0, false); // no time

        // Artificial rest
        TimedExercise REST_30_SEC = new TimedExercise("Rest", "", 0, 1, 30, 0, false); // 0;30
        TimedExercise REST_60_SEC = new TimedExercise("Rest", "", 0, 1, 60, 0, false); // 1:00

        /*
        Populate array lists with appropriate exercises (change monthly, hard-coded)
         */
        for (int i = 0; i < EXERCISES.length; i++) EXERCISES[i] = new ArrayList<>();

        /*
        Upper body strength exercises
         */
        EXERCISES[UPPER_STRENGTH].add(ARM_CIRCLES);
        EXERCISES[UPPER_STRENGTH].add(BAND_SHOULDER_ROTATIONS);
        EXERCISES[UPPER_STRENGTH].add(PRISONER_ROTATIONS);
        EXERCISES[UPPER_STRENGTH].add(CAT_CAMEL);

        EXERCISES[UPPER_STRENGTH].add(SIDE_OBLIQUE_PUSHUP);
        EXERCISES[UPPER_STRENGTH].add(SIDE_LYING_ELBOW_TWIST);
        EXERCISES[UPPER_STRENGTH].add(L_RAISES);
        EXERCISES[UPPER_STRENGTH].add(DUMBBELL_FLIES);
        EXERCISES[UPPER_STRENGTH].add(REVERSE_FLIES);
        EXERCISES[UPPER_STRENGTH].add(WIDE_OVERHAND_HANG);

        EXERCISES[UPPER_STRENGTH].add(RUSSIAN_TWIST);
        EXERCISES[UPPER_STRENGTH].add(CROSS_LEG_SIT_UP);

        EXERCISES[UPPER_STRENGTH].add(CROSS_BODY_SHOULDER_STRETCH);
        EXERCISES[UPPER_STRENGTH].add(FOREARM_STRETCH);
        EXERCISES[UPPER_STRENGTH].add(OVERHEAD_TRICEP_STRETCH);
        EXERCISES[UPPER_STRENGTH].add(COBRA_STRETCH);


        /*
        Lower body strength exercises
         */
        EXERCISES[LOWER_STRENGTH].add(LEG_SWINGS);
        EXERCISES[LOWER_STRENGTH].add(FIRE_HYDRANTS);
        EXERCISES[LOWER_STRENGTH].add(QUAD_PULL_BACK);

        EXERCISES[LOWER_STRENGTH].add(DEEP_SQUAT);
        EXERCISES[LOWER_STRENGTH].add(DEEP_LUNGE);
        EXERCISES[LOWER_STRENGTH].add(HIP_BRIDGES);

        EXERCISES[LOWER_STRENGTH].add(VERT_LEG_LIFTS);
        EXERCISES[LOWER_STRENGTH].add(RUSSIAN_TWIST);

        EXERCISES[LOWER_STRENGTH].add(SIDE_SPLIT);
        EXERCISES[LOWER_STRENGTH].add(BUTTERFLY_STRETCH);
        EXERCISES[LOWER_STRENGTH].add(FRONT_SPLIT);
        EXERCISES[LOWER_STRENGTH].add(COBRA_STRETCH);

        /*
        High intensity interval training exercises
         */
        EXERCISES[HIIT].add(ARM_CIRCLES);
        EXERCISES[HIIT].add(HIP_ROTATIONS);
        EXERCISES[HIIT].add(KNEE_ROTATIONS);
        EXERCISES[HIIT].add(HIGH_KNEES);

        EXERCISES[HIIT].add(TIMED_BURPEES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(MOUNTAIN_CLIMBERS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_JUMP_SQUATS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(BICYCLE_CRUNCHES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_BURPEES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(MOUNTAIN_CLIMBERS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_JUMP_SQUATS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(BICYCLE_CRUNCHES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_BURPEES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(MOUNTAIN_CLIMBERS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_JUMP_SQUATS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(BICYCLE_CRUNCHES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_BURPEES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(MOUNTAIN_CLIMBERS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_JUMP_SQUATS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(BICYCLE_CRUNCHES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_BURPEES);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(MOUNTAIN_CLIMBERS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(TIMED_JUMP_SQUATS);
        EXERCISES[HIIT].add(REST_30_SEC);
        EXERCISES[HIIT].add(BICYCLE_CRUNCHES);
        EXERCISES[HIIT].add(REST_30_SEC);

        EXERCISES[HIIT].add(CROSS_BODY_SHOULDER_STRETCH);
        EXERCISES[HIIT].add(OVERHEAD_TRICEP_STRETCH);
        EXERCISES[HIIT].add(SIDE_SPLIT);
        EXERCISES[HIIT].add(FRONT_SPLIT);

        /*
        Total body strength building exercises
         */
        EXERCISES[TOTAL_BODY_STRENGTH].add(BAND_SHOULDER_ROTATIONS);
        EXERCISES[TOTAL_BODY_STRENGTH].add(REACH_ROLL_LIFT);
        EXERCISES[TOTAL_BODY_STRENGTH].add(FIRE_HYDRANTS);
        EXERCISES[TOTAL_BODY_STRENGTH].add(DEAD_BUG);

        EXERCISES[TOTAL_BODY_STRENGTH].add(WIDE_CLAP_PUSHUP);
        EXERCISES[TOTAL_BODY_STRENGTH].add(PRONE_LAT_RAISE_W_CIRCLES);
        EXERCISES[TOTAL_BODY_STRENGTH].add(RENEGADE_ROWS);
        EXERCISES[TOTAL_BODY_STRENGTH].add(SIDE_LYING_LATERAL_ARM_TWIST);
        EXERCISES[TOTAL_BODY_STRENGTH].add(DEEP_SQUAT);

        EXERCISES[TOTAL_BODY_STRENGTH].add(CROSS_LEG_SIT_UP);
        EXERCISES[TOTAL_BODY_STRENGTH].add(LYING_WINDSHIELD_WIPERS);

        EXERCISES[TOTAL_BODY_STRENGTH].add(OVERHEAD_TRICEP_STRETCH);
        EXERCISES[TOTAL_BODY_STRENGTH].add(SIDE_SPLIT);
        EXERCISES[TOTAL_BODY_STRENGTH].add(COBRA_STRETCH);
        EXERCISES[TOTAL_BODY_STRENGTH].add(SITTING_TOE_TOUCH);

        /*
        Steady state cardio exercises
         */
        EXERCISES[CARDIO].add(ARM_CIRCLES);
        EXERCISES[CARDIO].add(HIP_ROTATIONS);
        EXERCISES[CARDIO].add(KNEE_ROTATIONS);
        EXERCISES[CARDIO].add(HIGH_KNEES);

        EXERCISES[CARDIO].add(RUNNING);
        EXERCISES[CARDIO].add(TIMED_WALK);

        EXERCISES[CARDIO].add(SIDE_SPLIT);

    }

    public static void finishWorkout() {
        LocalDateTime ldt = LocalDateTime.now().plusDays(curWorkout == CARDIO || curWorkout == LOWER_STRENGTH ? 2 : 1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        String nextDay = format.format(ldt);

        curWorkout++;
        if (curWorkout > CARDIO) curWorkout = UPPER_STRENGTH;

        SharedPreferences sharedPref = instance.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(instance.getString(R.string.cur_workout_key), curWorkout);
        editor.putString(instance.getString(R.string.next_day_key), nextDay);
        editor.apply();
    }

    public static int convertToPx(float dp, Context context) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void speak(String toSpeak) {
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
