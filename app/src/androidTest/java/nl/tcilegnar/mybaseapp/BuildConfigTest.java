package nl.tcilegnar.mybaseapp;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ConstantConditions")
public abstract class BuildConfigTest {
    protected static final String APPLICATION_ID_BASE = "nl.tcilegnar.mybaseapp";

    @Test
    public void BuildConfig_ApplicationId_ApplicationIdBaseShouldNeverChange() {
        // Arrange

        // Act
        String actualApplicationId = BuildConfig.APPLICATION_ID;

        // Assert
        assertTrue(actualApplicationId.contains(APPLICATION_ID_BASE));
    }

    @Test
    public abstract void BuildConfig_ApplicationId_SuffixDependsOnProductFlavor();

    @Test
    public abstract void BuildConfig_ApplicationId_SuffixDependsOnBuildType();

    @Test
    public void BuildConfig_ApplicationId_ApplicationIdContainsAppContextPackageName() {
        // Arrange

        // Act
        String actualApplicationId = BuildConfig.APPLICATION_ID;

        // Assert
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertTrue(actualApplicationId.contains(appContext.getPackageName()));
    }
}
