package nl.tcilegnar.mybaseapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class App extends Application {

    // Probably the only location in the app where static context is allowed: https://stackoverflow
    // .com/questions/40573279/static-context-saved-in-application-class-and-used-in-a-singleton-toast-builder
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
