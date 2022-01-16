package me.simon76800.workout.item;

public class TimedExercise extends Exercise {
    private final int time;

    public TimedExercise(String workoutName, String description, int weight, int sets, int time, int rest, boolean bilateral) {
        super(workoutName, description, weight, sets, rest, bilateral);

        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
