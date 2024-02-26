package ru.javawebinar.topjava;

import java.util.HashMap;
import java.util.Map;

public class TestsTimesContainer {

    private final Map<String, Long> testsTimes = new HashMap<>();
    private int maxTestNameLength;

    public int getMaxTestNameLength() {
        return maxTestNameLength;
    }

    public void setMaxTestNameLength(int maxTestNameLength) {
        this.maxTestNameLength = maxTestNameLength;
    }

    public Map<String, Long> getTestsTimes() {
        return testsTimes;
    }

    public void add(String testName, long duration) {
        testsTimes.put(testName, duration);
    }
}
