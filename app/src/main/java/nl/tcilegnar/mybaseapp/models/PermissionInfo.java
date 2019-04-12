package nl.tcilegnar.mybaseapp.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link PermissionInfo} contains a set of permissions and a requestCode, which can be used to request permissions for
 * a specific feature. This can either be a single permission (to request WIFI state) or a group of permissions (eg.
 * camera + microphone to be requested for a video call feature). This class also contains a constant list of all
 * PermissionInfo's used by this app.
 */
public class PermissionInfo {
    public static final PermissionInfo WRITE_EXTERNAL_STORAGE = new PermissionInfo(1,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    public static final PermissionInfo ALL = new PermissionInfo(999, WRITE_EXTERNAL_STORAGE);

    private int requestCode;
    private List<String> permissions = new ArrayList<>();

    private PermissionInfo(int requestCode, PermissionInfo... permissionInfos) {
        this.requestCode = requestCode;
        for (PermissionInfo permissionInfo : permissionInfos) {
            permissions.addAll(Arrays.asList(permissionInfo.getPermissions()));
        }
    }

    private PermissionInfo(int requestCode, String permission) {
        this.requestCode = requestCode;
        this.permissions.add(permission);
    }

    public String[] getPermissions() {
        return permissions.toArray(new String[]{});
    }

    public int getRequestCode() {
        return requestCode;
    }
}
