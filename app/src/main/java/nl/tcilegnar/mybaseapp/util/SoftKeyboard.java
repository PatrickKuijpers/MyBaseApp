package nl.tcilegnar.mybaseapp.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A utility to show or hide the keyboard in a simpel way. Preferably use the methods that accept a view, rather than an
 * activity: {@link #hide(View)} and {@link #show(View)}.
 *
 * If you cannot pass the 'currentViewInFocus' (eg. because there's several possible), then pass the current activity.
 * Then the currentViewInFocus will be requested from this activity
 */
@SuppressWarnings("unused")
public class SoftKeyboard {
    /** Use {@link #show(View)} instead of this method, if possible */
    public static void show(@Nullable Activity activity) {
        if (activity == null) {
            return;
        }
        show(activity.getCurrentFocus());
    }

    public static void show(View currentViewInFocus) {
        if (currentViewInFocus == null) {
            return;
        }
        currentViewInFocus.requestFocus(); // To be sure the view has focus, which is not always the case (yet)
        showKeyboard(currentViewInFocus);
    }

    private static void showKeyboard(@NonNull View currentViewInFocus) {
        // Don't use SHOW_FORCED because then the keyboard cannot be hidden anymore
        getInputMethodManager(currentViewInFocus.getContext()).showSoftInput(currentViewInFocus,
                InputMethodManager.SHOW_IMPLICIT);
    }

    /** Use {@link #hide(View)} instead of this method, if possible */
    public static void hide(@Nullable Activity activity) {
        if (activity == null) {
            return;
        }
        hide(activity.getCurrentFocus());
    }

    public static void hide(View currentViewInFocus) {
        if (currentViewInFocus == null) {
            return;
        }
        hideKeyboard(currentViewInFocus);
    }

    private static void hideKeyboard(@NonNull View currentViewInFocus) {
        IBinder windowToken = currentViewInFocus.getWindowToken();
        getInputMethodManager(currentViewInFocus.getContext()).hideSoftInputFromWindow(windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
