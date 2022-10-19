package models;

public class RandomEngine {

    public int randomBetween(int a, int b) {
        return a + (int)(Math.random()*(b-a));
    }
}
