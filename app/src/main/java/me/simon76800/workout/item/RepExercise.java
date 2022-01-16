package me.simon76800.workout.item;

public class RepExercise extends Exercise {
    private final int reps;

    public RepExercise(String workoutName, String description, int weight, int sets, int reps, int rest, boolean bilateral) {
        super(workoutName, description, weight, sets, rest, bilateral);

        this.reps = reps;
    }

    public int getReps() {
        return reps;
    }
}
