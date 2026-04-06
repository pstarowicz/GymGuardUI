package com.gymguard.framework.tests;

import com.gymguard.framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTest extends BaseTest {

    @Test
    public void failingTest_shouldTriggerScreenshot() {
        // Intentionally fail so the listener captures a screenshot
        Assert.fail("Intentional failure to exercise TestListener screenshot capture");
    }
}