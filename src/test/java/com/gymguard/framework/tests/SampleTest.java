package com.gymguard.framework.tests;

import com.gymguard.framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.gymguard.framework.base.TestListener;

@Listeners(TestListener.class)
public class SampleTest extends BaseTest {

    @Test
    public void failingTest_shouldTriggerScreenshot() {
        // Intentionally fail so the listener captures a screenshot
        Assert.fail("Intentional failure to exercise TestListener screenshot capture");
    }
}