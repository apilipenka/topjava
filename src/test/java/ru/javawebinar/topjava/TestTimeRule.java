package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTimeRule implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(TestTimeRule.class);
    private final TestsTimesContainer testsTimes;

    public TestTimeRule(TestsTimesContainer testsTimes) {
        this.testsTimes = testsTimes;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTimeMills = System.nanoTime();
                base.evaluate();
                long endTimeMills = System.nanoTime();
                log.info(String.format("\n%s - %s ns", description.getMethodName(),
                        endTimeMills - startTimeMills));

                if (description.getMethodName().length() > testsTimes.getMaxTestNameLength()) {
                    testsTimes.setMaxTestNameLength(description.getMethodName().length());
                }

                testsTimes.getTestsTimes().put(description.getMethodName(), endTimeMills - startTimeMills);
            }
        };
    }
}