package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TimeOfTheTestRule implements TestRule {

    private static final Logger LOG = LoggerFactory.getLogger(TimeOfTheTestRule.class);

    private final Map<String, Long> testsTimes;

    public TimeOfTheTestRule(Map<String, Long> testsTimes) {
        this.testsTimes = testsTimes;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        long startTimeMills = System.nanoTime();
        try {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    base.evaluate();
                }
            };
        } finally {
            long endTimeMills = System.nanoTime();
            LOG.info(String.format("The %s test ran for %s ns", description.getMethodName(),
                    endTimeMills - startTimeMills));
            testsTimes.put(description.getMethodName(), endTimeMills - startTimeMills);
        }
    }
}