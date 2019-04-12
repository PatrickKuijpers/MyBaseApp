package nl.tcilegnar.mybaseapp.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import nl.tcilegnar.mybaseapp.App;
import nl.tcilegnar.mybaseapp.broadcastreceivers.BroadcastReceiverRequestPermissions;
import nl.tcilegnar.mybaseapp.interfaces.CallbackRequestPermissions;
import nl.tcilegnar.mybaseapp.models.PermissionInfo;
import nl.tcilegnar.mybaseapp.util.loggers.Log;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static nl.tcilegnar.mybaseapp.util.loggers.Log.Cat.PERMISSIONS;

/**
 * The permissionRequester simplifies requesting permissions and requesting their status. Simply call {@link
 * #requestPermissionsAndCallbackIfAllGranted} with a {@link PermissionRequestListener} and implement its return-methods
 * for handling all possible usecases. These include all permissions granted, permissions partially granted or all
 * permissions denied (incl. do-not-ask-again). A request dialog will be shown (when an activity is supplied) whenever
 * necessary.
 *
 * Take note that (sub)permissions are requested and checked separately, while uses can only grant or deny
 * permission-groups: https://developer.android.com/guide/topics/permissions/requesting.html#perm-groups Note: since
 * Android O behaviour of permission requests were changed due to an Android bug below Android O versions:
 * https://developer.android.com/about/versions/oreo/android-8.0-changes.html#rmp A request for these permissions on
 * Android O will return '_DENIED', while previous Android versions would return '_GRANTED' (unfairly). These
 * permissions can still be granted by turning off & on the permission manually via the Android settings Also, a request
 * for denied (sub)permissions will grant them automatically if the permission-group was already accepted (and no dialog
 * will be shown)
 */
public class PermissionRequester {
    private static final String TAG = Log.getTag(PermissionRequester.class, PERMISSIONS);

    private BroadcastReceiverRequestPermissions broadcastPermissions;

    /**
     * If all the permissions are already granted, the callback 'allPermissionsGranted' will be performed directly and
     * 'true' will be returned. If not all are granted, a permission dialog is shown first and the callback is called
     * after the dialog is finished.
     *
     * @param activity       - required to ask for permissions (the activity which should display the request dialog).
     *                       If null, then no request is asked
     * @param callback       - the callback AFTER the permissions were requested
     * @param permissionInfo - can be used to get the permissions to request & requestCode
     * @return 'true' if all permissions were granted INITIALLY, 'false' if any was denied. This state will be different
     * AFTER the permission request was made AND all permission were granted
     */
    public boolean requestPermissionsAndCallbackIfAllGranted(@Nullable Activity activity,
                                                             PermissionRequestListener callback,
                                                             PermissionInfo permissionInfo) {
        boolean areAllPermissionsAccepted = areAllPermissionsGranted(permissionInfo);
        if (!areAllPermissionsAccepted) {
            requestPermissions(activity, callback, permissionInfo);
        }
        else {
            callback.allPermissionsGranted();
        }
        return areAllPermissionsAccepted;
    }

    public boolean areAllPermissionsGranted(PermissionInfo permissionInfo) {
        for (String permission : permissionInfo.getPermissions()) {
            if (!isPermissionGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(App.getContext(), permission) == PERMISSION_GRANTED;
    }

    private void requestPermissions(@Nullable Activity activity, PermissionRequestListener callback,
                                    PermissionInfo permissionInfo) {
        // TODO (PK): maybe we don't need to filter the requiredPermissions, just request all:
        // (do-not-ask-again won't be asked anyway, what about already granted permissions)?

        String[] requiredPermissions = permissionInfo.getPermissions();
        String[] permissionsToRequest = getPermissionsToRequest(requiredPermissions);
        if (permissionsToRequest.length > 0) {
            if (activity == null) {
                Log.i(TAG, "Cannot request permissions: activity == null");
                // TODO (PK): callback.notAllPermissionsGranted();
                return; // No activity? Then don't start a request, because we cannot show a permission dialog (and
                // probably don't want to anyway)!
            }
            initBroadcast(activity, callback, permissionInfo);
            forceRequestPermissions(activity, permissionInfo); // With result in the activity + broadcast as
            // initialized above
        }
        else {
            callback.allPermissionsGranted();
        }
    }

    private void forceRequestPermissions(Activity activity, PermissionInfo permissionInfo) {
        for (String permissionToRequest : permissionInfo.getPermissions()) {
            Log.i(TAG, "requesting permission: " + permissionToRequest);
        }
        ActivityCompat.requestPermissions(activity, permissionInfo.getPermissions(), permissionInfo.getRequestCode());
    }

    /**
     * @return all denied permissions. Special cases will be handled by Android automatically: - Permission(group)s for
     * which the user selected 'do not ask again' will be ignored, and will not be shown to the user in a request pop-up
     * - Permissions in an already granted permission group will be added to this group automatically, without a request
     * pop-up shown to the user
     */
    private String[] getPermissionsToRequest(String... requiredPermissions) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : requiredPermissions) {
            Log.d(TAG, "getPermissionToRequest: " + permission);
            if (!isPermissionGranted(permission)) {
                permissionsToRequest.add(permission);
                Log.d(TAG, "add permission to request: " + permission);
            }
        }
        return permissionsToRequest.toArray(new String[]{});
    }

    private void initBroadcast(final Activity activity, final PermissionRequestListener callback,
                               final PermissionInfo permissionInfo) {
        broadcastPermissions = new BroadcastReceiverRequestPermissions(activity, new CallbackRequestPermissions() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] requestedPermissions,
                                                   @NonNull int[] grantResults) {
                Log.d(this, "onRequestPermissionsResult: requestCode = " + requestCode);
                if (requestCode == permissionInfo.getRequestCode()) {
                    if (hasGrantedAllRequestedPermissions(requestedPermissions, grantResults)) {
                        if (areAllPermissionsGranted(permissionInfo)) {
                            Log.d(TAG, "allPermissionsGranted");
                            callback.allPermissionsGranted();
                        }
                        else {
                            // Even if all requested permissions are granted, it's possible there's a permission
                            // which was previously denied and 'do-not-ask-again'. For that reason we also check whether
                            // 'allRequiredPermissionsAreRequested'. If not, the not all are granted!
                            Log.d(TAG, "anyPermissionDeniedAndDoNotAskAgain");
                            callback.anyPermissionDeniedAndDoNotAskAgain();
                        }
                    }
                    else {
                        Log.d(TAG, "not all requested permissions were granted");
                        callback.notAllPermissionsGranted();
                    }
                    unregisterBroadcast(activity);
                }
            }
        });
    }

    private boolean hasGrantedAllRequestedPermissions(String[] requestedPermissions, int[] grantResults) {
        boolean hasSameNumberOfResultsAsRequestedPermissions = requestedPermissions.length == grantResults.length;

        boolean allAccepted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PERMISSION_GRANTED) {
                allAccepted = false;
                break;
            }
        }
        return hasSameNumberOfResultsAsRequestedPermissions && allAccepted;
    }

    private void unregisterBroadcast(Activity activity) {
        if (broadcastPermissions != null) {
            activity.unregisterReceiver(broadcastPermissions);
        }
    }

    /**
     * Do this at the start of the app if you'd like to request everything at once just after the first app start. Still
     * we need to request permissions on every location where they are required, in case they were added in a later
     * version, or when a user has manually turned off the permission in the app settings.
     */
    public void requestAllRequiredAppPermissions(Activity activity) {
        forceRequestPermissions(activity, PermissionInfo.ALL);
    }

    public interface PermissionRequestListener {
        void allPermissionsGranted();

        void notAllPermissionsGranted();

        void anyPermissionDeniedAndDoNotAskAgain();
    }
}
