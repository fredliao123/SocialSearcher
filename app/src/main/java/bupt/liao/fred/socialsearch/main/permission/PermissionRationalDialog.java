package bupt.liao.fred.socialsearch.main.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import bupt.liao.fred.socialsearch.R;
import bupt.liao.fred.socialsearch.main.MainActivity;

/**
 * Created by Fred.Liao on 2018/1/1.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class PermissionRationalDialog extends DialogFragment{
    private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";

    private static final String ARGUMENT_FINISH_ACTIVITY = "finish";

    private boolean mFinishActivity = false;

    IPermissionDialogController controller = null;

    /**
     * Creates a new instance of a dialog displaying the rationale for the use of the location
     * permission.
     * The permission is requested after clicking 'ok'.
     *
     * @param requestCode    Id of the request that is used to request the permission. It is
     *                       returned to the
     * @param finishActivity Whether the calling Activity should be finished if the dialog is
     *                       cancelled.
     */
    public static PermissionRationalDialog newInstance(int requestCode, boolean finishActivity) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
        arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
        PermissionRationalDialog dialog = new PermissionRationalDialog();
        dialog.setArguments(arguments);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        final int requestCode = arguments.getInt(ARGUMENT_PERMISSION_REQUEST_CODE);
        mFinishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY);
        if(getActivity() instanceof IPermissionDialogController){
            controller = (IPermissionDialogController) getActivity();
        }
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.permission_rationale_location)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // After click on Ok, request the permission.
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                requestCode);
                        // Do not finish the Activity while requesting permission.
                        mFinishActivity = false;
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // After click on No, show a snack bar to inform the consequence.
                        //And set Near category to GONE
                        if(controller != null){
                            controller.dialogCancelled();
                            controller.showSnackBar(getString(R.string.location_permission_denied));
                        }
                    }
                })
                .create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mFinishActivity) {
            Toast.makeText(getActivity(),
                    R.string.permission_required_toast,
                    Toast.LENGTH_SHORT)
                    .show();
            getActivity().finish();
        }
    }
}
