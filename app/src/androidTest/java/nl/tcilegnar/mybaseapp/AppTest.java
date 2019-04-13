package nl.tcilegnar.mybaseapp;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AppTest {
    @Test
    public void getContext_ShouldBeApplicationContext() {
        // Arrange

        // Act
        Context actualContext = App.getContext();

        // Assert
        assertEquals(getApplicationContext(), actualContext);
    }

    private Context getApplicationContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    }
}