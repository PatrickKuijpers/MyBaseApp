package nl.tcilegnar.mybaseapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.ArrayRes;
import androidx.annotation.BoolRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.Locale;

import nl.tcilegnar.mybaseapp.App;

public class Res {
    public static String getString(@StringRes int resId) {
        return getApplicationResources().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... args) {
        return String.format(Locale.getDefault(), Res.getString(resId), args);
    }

    public static String[] getStringArray(@ArrayRes int resId) {
        return getApplicationResources().getStringArray(resId);
    }

    public static int getInt(@IntegerRes int resId) {
        return getApplicationResources().getInteger(resId);
    }

    /** The dimension in dp */
    @Dimension
    public static float getDimension(@DimenRes int resId) {
        return getApplicationResources().getDimension(resId);
    }

    public static int getDimensionPixelSize(@DimenRes int resId) {
        return getApplicationResources().getDimensionPixelSize(resId);
    }

    public static boolean getBoolean(@BoolRes int resId) {
        return getApplicationResources().getBoolean(resId);
    }

    @ColorInt
    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    public static Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    public static Bitmap getBitmap(@DrawableRes int resId) {
        return BitmapFactory.decodeResource(getApplicationResources(), resId);
    }

    /** Cannot use {@link #getBitmap} to get the bitmap of a Vector drawable. */
    public static Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable = getDrawable(drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    protected static Resources getApplicationResources() {
        return getContext().getResources();
    }

    private static Context getContext() {
        return App.getContext();
    }
}
