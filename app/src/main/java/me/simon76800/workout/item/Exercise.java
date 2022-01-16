package me.simon76800.workout.item;

public abstract class Exercise {
    protected final String workoutName;
    protected final String description;
    protected final int weight;
    protected final int sets;
    protected final int rest;
    protected final boolean bilateral;

    protected Exercise(String workoutName, String description, int weight, int sets, int rest, boolean bilateral) {
        this.workoutName = workoutName;
        this.description = description;
        this.weight = weight;
        this.sets = sets;
        this.rest = rest;
        this.bilateral = bilateral;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public int getSets() {
        return sets;
    }

    public int getRest() {
        return rest;
    }

    public boolean isBilateral() {
        return bilateral;
    }
}
