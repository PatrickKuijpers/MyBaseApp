package nl.tcilegnar.mybaseapp.util;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import java.nio.ByteBuffer;
import java.util.Arrays;

import nl.tcilegnar.mybaseapp.R;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ResTest {
    @Test
    public void getAppResources_ShouldBeResourcesFromApplicationContext() {
        // Arrange

        // Act
        Resources actualResources = Res.getApplicationResources();

        // Assert
        assertEquals(getApplicationResources(), actualResources);
    }

    @Test
    public void getString_ShouldContainSameStringAsGetStringFromApplicationResources() {
        // Arrange

        // Act
        String actualString = Res.getString(R.string.test_string);

        // Assert
        String expectedString = getApplicationResources().getString(R.string.test_string);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void getString_WithArgsForFormatting_ShouldContainSameStringAsGetStringFromApplicationResourcesIncludingArgsFormattedIn() {
        // Arrange
        String argString = "TEST";

        // Act
        String actualString = Res.getString(R.string.test_string_format, argString);

        // Assert
        String expectedStringNotFormatted = getApplicationResources().getString(R.string.test_string_format);
        String expectedString = String.format(expectedStringNotFormatted, argString);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void getStringArray_ShouldContainSameStringArrayAsGetStringArrayFromApplicationResources() {
        // Arrange

        // Act
        String[] actualStringArray = Res.getStringArray(R.array.test_array);

        // Assert
        String[] expectedStringArray = getApplicationResources().getStringArray(R.array.test_array);
        assertArrayEquals(expectedStringArray, actualStringArray);
    }

    @Test
    public void getInt_ShouldContainSameIntegerAsGetIntegerFromApplicationResources() {
        // Arrange

        // Act
        Integer actualInteger = Res.getInt(R.integer.test_int);

        // Assert
        Integer expectedInteger = getApplicationResources().getInteger(R.integer.test_int);
        assertEquals(expectedInteger, actualInteger);
    }

    @Test
    public void getDimension_ShouldContainSameDimenAsGetDimensionFromApplicationResources() {
        // Arrange

        // Act
        float actualDimension = Res.getDimension(R.dimen.test_dimen);

        // Assert
        float expectedDimension = getApplicationResources().getDimension(R.dimen.test_dimen);
        assertEquals(expectedDimension, actualDimension, 0);
    }

    @Test
    public void getDimensionPixelSize_ShouldContainSamePixelSizeAsGetDimensionPixelSizeFromApplicationResources() {
        // Arrange

        // Act
        int actualPixels = Res.getDimensionPixelSize(R.dimen.test_dimen);

        // Assert
        int expectedPixels = getApplicationResources().getDimensionPixelSize(R.dimen.test_dimen);
        assertEquals(expectedPixels, actualPixels);
    }

    @Test
    public void getBoolean_ShouldContainSameBooleanAsGetBooleanFromApplicationResources() {
        // Arrange

        // Act
        boolean bool = Res.getBoolean(R.bool.test_bool);

        // Assert
        boolean expectedBool = getApplicationResources().getBoolean(R.bool.test_bool);
        assertEquals(expectedBool, bool);
    }

    @Test
    public void getColor_ShouldContainSameColorAsGetColorFromApplicationContext() {
        // Arrange

        // Act
        int actualColorInt = Res.getColor(R.color.test_color);

        // Assert
        int expectedColorInt = ContextCompat.getColor(getApplicationContext(), R.color.test_color);
        assertEquals(expectedColorInt, actualColorInt);
    }

    @Test
    @Ignore("Tried to make a unittest for this method, but failed... Why are both different?")
    public void getDrawable_ShouldContainSameDrawableAsGetDrawableFromApplicationContext() {
        // Arrange

        // Act
        Drawable actualDrawable = Res.getDrawable(R.drawable.test_drawable);

        // Assert
        Drawable expectedDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.test_drawable);
        assertEquals(expectedDrawable, actualDrawable);
    }

    @Test
    @Ignore("Tried to make a unittest for this method, but failed... Why are both different?")
    public void getBitmap_ShouldContainSameBitmapAsBitmapDecodedFromBitmapFactory() {
        // Arrange

        // Act
        Bitmap actualBitmap = Res.getBitmap(R.drawable.test_drawable);

        // Assert
        Bitmap expectedBitmap = BitmapFactory.decodeResource(getApplicationResources(), R.drawable.test_drawable);
        assertBitmaps(expectedBitmap, actualBitmap);
    }

    @Test
    @Ignore("Tried to make a unittest for this method, but failed... Why are both different?")
    public void getBitmapFromVectorDrawable_ShouldContainSameBitmapAsBitmapDecodedFromBitmapFactory() {
        // Arrange

        // Act
        Bitmap actualBitmap = Res.getBitmapFromVectorDrawable(R.drawable.test_vector_drawable);

        // Assert
        Bitmap expectedBitmap = BitmapFactory.decodeResource(getApplicationResources(),
                R.drawable.test_vector_drawable);
        assertBitmaps(expectedBitmap, actualBitmap);
    }

    private void assertBitmaps(Bitmap expectedBitmap, Bitmap actualBitmap) {
        //        assertEquals("1", expectedBitmap.getByteCount(), actualBitmap.getByteCount());
        assertEquals("2", expectedBitmap.getDensity(), actualBitmap.getDensity());
        assertEquals("3", expectedBitmap.getHeight(), actualBitmap.getHeight());
        assertEquals("4", expectedBitmap.getWidth(), actualBitmap.getWidth());
        assertEquals("5", expectedBitmap.getConfig(), actualBitmap.getConfig());

        ByteBuffer expectedBytes = ByteBuffer.allocate(expectedBitmap.getHeight() * expectedBitmap.getRowBytes());
        expectedBitmap.copyPixelsToBuffer(expectedBytes);

        ByteBuffer actualBytes = ByteBuffer.allocate(actualBitmap.getHeight() * actualBitmap.getRowBytes());
        actualBitmap.copyPixelsToBuffer(actualBytes);

        assertTrue(Arrays.equals(expectedBytes.array(), actualBytes.array()));
        // Onderstaande controles werken mogelijk niet zoals verwacht:
        //        assertTrue(expectedBitmap.sameAs(actualBitmap));
        //        assertTrue(expectedBitmap.equals(actualBitmap));
        //        assertEquals(expectedBitmap, actualBitmap);
    }

    private Resources getApplicationResources() {
        return getApplicationContext().getResources();
    }

    private Context getApplicationContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    }
}