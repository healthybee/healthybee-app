package com.app.healthybee.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.app.healthybee.R;

/**
 * Created by root on 1/11/18.
 */

public class MyCustomProgressDialog {
    private static ProgressDialog dialog;

    public static void showDialog(Context context, String message){
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.circular_progress_dialog));
        dialog.show();


    }

    public static void dismissDialog(){
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void showAlertDialogMessage(Context context, String title, String dialogContent)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(dialogContent);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
