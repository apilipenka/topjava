package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTimeRule extends TestWatcher {
    private static final Logger log = LoggerFactory.getLogger(TestTimeRule.class);
    private final TestsTimesContainer testsTimes;

    long startTimeMills;

    public TestTimeRule(TestsTimesContainer testsTimes) {
        this.testsTimes = testsTimes;
    }


    @Override
    protected void starting(Description description) {
        startTimeMills = System.nanoTime();
    }

    @Override
    protected void finished(Description description) {
        long endTimeMills = System.nanoTime();
        log.info(String.format("\n%s - %s ns", description.getMethodName(),
                endTimeMills - startTimeMills));

        if (description.getMethodName().length() > testsTimes.getMaxTestNameLength()) {
            testsTimes.setMaxTestNameLength(description.getMethodName().length());
        }
        testsTimes.getTestsTimes().put(description.getMethodName(), endTimeMills - startTimeMills);
    }
}