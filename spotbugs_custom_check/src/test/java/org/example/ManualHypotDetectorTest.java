package org.example;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.test.SpotBugsRule;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;

public class ManualHypotDetectorTest {
    @Rule
    public SpotBugsRule spotbugs = new SpotBugsRule();

    @Test
    public void testGoodCase() {
        doTest("GoodCase.class", 0);
    }

    @Test
    public void testBadCase() {
        doTest("BadCase.class", 1);
    }

    @Test
    public void testBadCase2() {
        doTest("BadCase2.class", 1);
    }

    private void doTest(String testName, int count) {
        Path path = Paths.get("target/test-classes", "org.example".replace('.', '/'), testName);
        BugCollection bugCollection = spotbugs.performAnalysis(path);

        BugInstanceMatcher bugTypeMatcher = new BugInstanceMatcherBuilder()
                .bugType("MATH_USE_HYPOT").build();
        assertThat(bugCollection, containsExactly(count, bugTypeMatcher));
    }
}
