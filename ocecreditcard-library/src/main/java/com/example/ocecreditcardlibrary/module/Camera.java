package com.example.ocecreditcardlibrary.module;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.ocecreditcardlibrary.config.Global;
import com.example.ocecreditcardlibrary.util.Card;

/**
 * Created by kanthimp on 13/12/2560.
 */

public class Camera implements CallService.CallBackService {
    private Context context;
    private CallBack callBack;
    private CallService callService;

    public Camera(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        callService = new CallService();
        callService.setCallBackService(this);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void openCamera(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Global.getInstance().getRequestCamera()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {

            }
        }
    }

    public Camera openCamera() {
        if (checkPermissionCameraOpen()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ((Activity) context).startActivityForResult(intent, Global.getInstance().getRequestCamera());
        } else {

        }
        return this;
    }

    private boolean checkPermissionCameraOpen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.CAMERA},
                        Global.getInstance().getRequestCamera());
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public Bitmap checkResultCamera(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (requestCode == Global.getInstance().getRequestCamera() && resultCode == ((Activity) context).RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        return bitmap;
    }

    public void onActivityResultCamera(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (requestCode == Global.getInstance().getRequestCamera() && resultCode == ((Activity) context).RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            setcallCloudVision(bitmap);
            callBack.checkResultCameraSuccess(bitmap);
        } else {
            callBack.checkResultCameraFailed(requestCode, resultCode, data);
        }
    }

    private void setcallCloudVision(Bitmap bitmap) {
        callService.callCloudVision(bitmap);
    }

    @Override
    public void loading() {
        callBack.loading();
    }

    @Override
    public void success(Card card) {
        callBack.success(card);
    }

    @Override
    public void failed(String message) {
        callBack.failed(message);
    }



    public interface CallBack {
        public void checkResultCameraSuccess(Bitmap bitmap);

        public void checkResultCameraFailed(int requestCode, int resultCode, Intent data);

        public void loading();

        public void failed(String message);

        public void success(Card card);


    }


}
