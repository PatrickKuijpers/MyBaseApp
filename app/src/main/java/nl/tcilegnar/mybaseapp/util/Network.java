package nl.tcilegnar.mybaseapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import java.lang.reflect.Method;

import nl.tcilegnar.mybaseapp.App;
import nl.tcilegnar.mybaseapp.R;
import nl.tcilegnar.mybaseapp.util.loggers.Log;

import static nl.tcilegnar.mybaseapp.util.loggers.Log.Cat.PERMISSIONS;

/** TODO: improve network connection detect: https://developer.android.com/training/basics/network-ops/managing.html */
@SuppressWarnings("unused")
public class Network {
    private static final String TAG = Log.getTag(Network.class.getSimpleName());
    private static final int RETRY_TOTAL_TIME = 5000;
    private static final int RETRY_INTERVAL = 250;

    public boolean hasConnection() {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Requests {@link #hasConnection()} but optionally retries several times. Retrying network calls like this should
     * NOT be done on the UI thread! Call {@link #hasConnectionWithRetryOnBackground(Context,
     * NetworkConnectionCallback)} on UI thread instead
     */
    public boolean hasConnection(boolean shouldRetryOnCurrentThread) {
        boolean hasConnection = hasConnection();
        if (!hasConnection && shouldRetryOnCurrentThread) {
            hasConnection = hasConnectionWithRetry();
        }
        return hasConnection;
    }

    /**
     * Requests {@link #hasConnection()} on a background thread and returns the result to {@link
     * NetworkConnectionCallback}
     */
    public void hasConnectionWithRetryOnBackground(final Context context, final NetworkConnectionCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                final boolean hasConnection = hasConnectionWithRetry();
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.hasConnection(hasConnection);
                    }
                });
            }
        }).start();
    }

    private boolean hasConnectionWithRetry() {
        // Retry is only useful when either wifi or mobile data is enabled
        // TODO (PK): is this also the case for invalid network error messages onResume?
        if (!isWifiEnabled() && !isMobileDataEnabled()) {
            return false;
        }

        boolean hasConnection = false;
        try {
            int numberOfRetries = RETRY_TOTAL_TIME / RETRY_INTERVAL;
            for (int i = 0; i < numberOfRetries; i++) {
                Thread.sleep(RETRY_INTERVAL);

                hasConnection = hasConnection();
                if (hasConnection) {
                    break;
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            // TODO:           new ExceptionHandler(TAG).printStackTrace(e);
        }
        return hasConnection;
    }

    public boolean isWifiEnabled() {
        WifiManager wifi = getWifiManager();
        return wifi != null && wifi.isWifiEnabled();
    }

    @SuppressWarnings("unchecked")
    public boolean isMobileDataEnabled() {
        boolean isMobileDataEnabled = false;
        try {
            ConnectivityManager connectivityManager = getConnectivityManager();
            Class cmClass = Class.forName(connectivityManager.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            isMobileDataEnabled = (Boolean) method.invoke(connectivityManager);
        }
        catch (Exception e) {
            // Some problem accessible private API
        }
        return isMobileDataEnabled;
    }

    private boolean isWifiConnected() {
        return isConnected(ConnectivityManager.TYPE_WIFI);
    }

    private boolean isMobileDataConnected() {
        return isConnected(ConnectivityManager.TYPE_MOBILE);
    }

    private boolean isConnected(int networkType) {
        NetworkInfo networkInfo = getNetworkInfo(networkType);
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean isWifiConnectedOrConnecting() {
        return isConnectedOrConnecting(ConnectivityManager.TYPE_WIFI);
    }

    public boolean isMobileDataConnectedOrConnecting() {
        return isConnectedOrConnecting(ConnectivityManager.TYPE_MOBILE);
    }

    private boolean isConnectedOrConnecting(int networkType) {
        NetworkInfo networkInfo = getNetworkInfo(networkType);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public String getCurrentWifiNetworkName() {
        WifiManager wifiManager = getWifiManager();
        try {
            return wifiManager != null ? wifiManager.getConnectionInfo().getSSID() : Res.getString(R.string.unknown);
        }
        catch (SecurityException e) {
            Log.w(TAG, "Could not retrieve WiFi network name: probably the permission was not granted", PERMISSIONS);
            e.printStackTrace();
            // TODO:           new ExceptionHandler(TAG).printStackTrace(e);
            return Res.getString(R.string.unknown);
        }
    }

    private NetworkInfo getNetworkInfo(int networkType) {
        ConnectivityManager connectivityManager = getConnectivityManager();
        return connectivityManager != null ? connectivityManager.getNetworkInfo(networkType) : null;
    }

    private NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager = getConnectivityManager();
        return connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private WifiManager getWifiManager() {
        return (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public interface NetworkConnectionCallback {
        void hasConnection(boolean hasConnection);
    }
}
