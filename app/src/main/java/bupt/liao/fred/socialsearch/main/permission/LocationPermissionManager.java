package bupt.liao.fred.socialsearch.main.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import javax.inject.Inject;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.main.MainActivity;

/**
 * Created by Fred.Liao on 2017/12/9.
 * Email:fredliaobupt@qq.com
 * Description: A class to acquire Permission
 */

public class LocationPermissionManager {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    AppCompatActivity activity;
    int requestId;
    String permission;
    boolean finishActivity;

    public LocationPermissionManager(AppCompatActivity activity, int requestId,
                                     String permission, boolean finishActivity){
        this.activity = activity;
        this.requestId = requestId;
        this.permission = permission;
        this.finishActivity = finishActivity;
    }

    /**
     * Requests the fine location permission. If a rationale with an additional explanation should
     * be shown to the user, displays a dialog that triggers the request.
     */
    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Display a dialog with rationale.
            PermissionRationalDialog dialog = PermissionRationalDialog.newInstance(requestId, finishActivity);
            dialog.show(activity.getFragmentManager(), "Permission Dialog");
        } else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);
        }
    }

    /**
     * Checks if the result contains a PERMISSION_GRANTED result for a
     * permission from a runtime permissions request.
     */
    public boolean isPermissionGranted(String[] grantPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

}
