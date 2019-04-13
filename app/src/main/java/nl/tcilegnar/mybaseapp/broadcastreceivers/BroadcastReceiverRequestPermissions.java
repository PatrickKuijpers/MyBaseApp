package nl.tcilegnar.mybaseapp.broadcastreceivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import nl.tcilegnar.mybaseapp.interfaces.CallbackRequestPermissions;

public class BroadcastReceiverRequestPermissions extends BroadcastReceiver {
    public static final String ACTION_REQUEST_PERMISSION = "REQUEST_PERMISSION";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String PERMISSIONS = "PERMISSIONS";
    public static final String GRANT_RESULTS = "GRANT_RESULTS";

    private final CallbackRequestPermissions callbackRequestPermissions;

    public BroadcastReceiverRequestPermissions(Activity activity,
                                               CallbackRequestPermissions callbackRequestPermissions) {
        this.callbackRequestPermissions = callbackRequestPermissions;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_REQUEST_PERMISSION);
        activity.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_REQUEST_PERMISSION)) {
            int requestCode = intent.getIntExtra(REQUEST_CODE, -1);
            String[] permissions = intent.getStringArrayExtra(PERMISSIONS);
            int[] grantResult = intent.getIntArrayExtra(GRANT_RESULTS);
            callbackRequestPermissions.onRequestPermissionsResult(requestCode, permissions, grantResult);
        }
    }
}
