package nl.tcilegnar.mybaseapp.interfaces;

import androidx.annotation.NonNull;

public interface CallbackRequestPermissions {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] requestedPermissions,
                                    @NonNull int[] grantResults);
}
