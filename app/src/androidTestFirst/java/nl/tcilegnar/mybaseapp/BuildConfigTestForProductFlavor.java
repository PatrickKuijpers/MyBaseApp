package nl.tcilegnar.mybaseapp;

import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ConstantConditions")
public abstract class BuildConfigTestForProductFlavor extends BuildConfigTest {
    protected static final String APPLICATION_ID_WITH_FLAVOR = APPLICATION_ID_BASE + ".first";

    @Override
    public void BuildConfig_ApplicationId_SuffixDependsOnProductFlavor() {
        // Arrange

        // Act
        String actualApplicationId = BuildConfig.APPLICATION_ID;

        // Assert
        assertTrue(actualApplicationId.contains(APPLICATION_ID_WITH_FLAVOR));
    }
}
