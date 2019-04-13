package nl.tcilegnar.mybaseapp.util.loggers;

import android.database.Cursor;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Set;

public class ContentLogger {
    public static void logBundle(String TAG, Bundle bundle) {
        if (bundle == null) {
            Log.v(TAG, "bundle is null");
            return;
        }

        Set<String> keys = bundle.keySet();
        Iterator<String> it = keys.iterator();
        Log.v(TAG, "================= bundle start");
        while (it.hasNext()) {
            String key = it.next();
            Log.d(TAG, "[" + key + "=" + bundle.get(key) + "]");
        }
        Log.v(TAG, "================= bundle end");
    }

    public static void logCursor(String TAG, Cursor cursor) {
        if (cursor != null) {
            int numberOfEntries = cursor.getCount();
            Log.v(TAG, "Cursor size: " + numberOfEntries);
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                Log.v(TAG, "================= New cursor entry");
                for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
                    Log.v(TAG, "- " + cursor.getColumnName(columnIndex) + ": " + cursor.getString(columnIndex));
                }
            }
            if (numberOfEntries > 0) {
                Log.v(TAG, "================= End cursor entries");
            }
        }
    }
}
