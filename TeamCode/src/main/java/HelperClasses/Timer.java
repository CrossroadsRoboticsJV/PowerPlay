package HelperClasses;

public class Timer {

    long startTime = System.currentTimeMillis();

    public long getTime() {
        return System.currentTimeMillis() - startTime;
    }

    public void resetTimer() {
        startTime = System.currentTimeMillis();
    }

}
