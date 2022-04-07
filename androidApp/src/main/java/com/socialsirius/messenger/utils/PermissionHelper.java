package com.socialsirius.messenger.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {



    public static boolean requestReadContactPermission(Activity activity, int permissionRequest) {
        if (activity == null) {
            return false;
        }
        if (!checkReadContactPermissionGranted(activity)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    permissionRequest);
            return false;
        }
        return true;
    }



    public static boolean checkReadContactPermissionGranted(Activity activity) {
        int permissionCheckWriteExternal = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS);
        return permissionCheckWriteExternal == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissionsOnlyForCamera(Activity activity, int requestCode) {
        List<String> list = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.CAMERA);

        if (list.size() > 0) {
            String[] perms = new String[list.size()];
            ActivityCompat.requestPermissions(activity, list.toArray(perms), requestCode);
            return false;
        } else
            return true;
    }


    public static boolean checkPermissionsForCamera(Activity activity, int requestCode) {
        List<String> list = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (list.size() > 0) {
            String[] perms = new String[list.size()];
            ActivityCompat.requestPermissions(activity, list.toArray(perms), requestCode);
            return false;
        } else
            return true;
    }


    public static boolean checkPermissionsForAudio(Activity activity, int requestCode) {
        List<String> list = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.RECORD_AUDIO);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (list.size() > 0) {
            String[] perms = new String[list.size()];
            ActivityCompat.requestPermissions(activity, list.toArray(perms), requestCode);
            return false;
        } else
            return true;
    }


    public static boolean checkPermissionsForStorage( Activity activity, int requestCode) {
        List<String> list = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (list.size() > 0) {
            String[] perms = new String[list.size()];
            ActivityCompat.requestPermissions(activity, list.toArray(perms), requestCode);
            return false;
        } else
            return true;
    }

    public static boolean checkPermissionsForFullIntent( Activity activity, int requestCode) {
        List<String> list = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.USE_FULL_SCREEN_INTENT) != PackageManager.PERMISSION_GRANTED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                list.add(Manifest.permission.USE_FULL_SCREEN_INTENT);
            }
        if (list.size() > 0) {
            String[] perms = new String[list.size()];
            ActivityCompat.requestPermissions(activity, list.toArray(perms), requestCode);
            return false;
        } else
            return true;
    }




    public static boolean requestLocationPermission(Activity activity, int permissionRequest) {
        if (activity == null) {
            return false;
        }
        if (!isLocationPermissionGranted(activity)) {
        /*    AlertUtils.showAlertWithOk(activity, App.getContext().getString(R.string.map_alert_request_title),
                    App.getContext().getString(R.string.map_alert_request_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {*/
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    permissionRequest);
            //       }
            //    });
            return false;
        }
        return true;
    }


    public static boolean isLocationPermissionGranted(Activity activity) {
        if (activity == null) {
            return false;
        }
        int permissionCoarceLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFineLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionCoarceLocation == PackageManager.PERMISSION_GRANTED && permissionFineLocation == PackageManager.PERMISSION_GRANTED;
    }


    public static boolean isWriteReadStoragePermissionGranted(Activity activity) {
        if (activity == null) {
            return false;
        }
        int permissionWriteLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionReadLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionWriteLocation == PackageManager.PERMISSION_GRANTED && permissionReadLocation == PackageManager.PERMISSION_GRANTED;
    }


    public static boolean requestWriteReadPermission(Activity activity, int permissionRequest) {
        if (activity == null) {
            return false;
        }
        if (!isWriteReadStoragePermissionGranted(activity)) {
        /*    AlertUtils.showAlertWithOk(activity, App.getContext().getString(R.string.write_read_alert_request_title),
                    App.getContext().getString(R.string.write_read_alert_request_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {*/
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    permissionRequest);
            //   }
            //     });
            return false;
        }
        return true;
    }


    public interface OnRequestPermissionListener {
        void onRequestSuccess();

        void onRequestFail();
    }


    public static void onRequestPermissionsResult(int requestCode, int myRequstCode,
                                                  @NonNull String permissions[], @NonNull int[] grantResults,
                                                  OnRequestPermissionListener onRequstPermissionListener) {
        if (requestCode == myRequstCode) {
            boolean showDialog = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showDialog = false;
                }
            }
            if (showDialog) {
                if (onRequstPermissionListener != null) {
                    onRequstPermissionListener.onRequestSuccess();
                }
            } else {
                if (onRequstPermissionListener != null) {
                    onRequstPermissionListener.onRequestFail();
                }
            }
        }

    }


}
