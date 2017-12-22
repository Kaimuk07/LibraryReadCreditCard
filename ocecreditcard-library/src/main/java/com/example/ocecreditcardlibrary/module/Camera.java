package com.example.ocecreditcardlibrary.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

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

    public Camera openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(intent, Global.requestCamera);
        return this;
    }

    public Bitmap checkResultCamera(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (requestCode == Global.requestCamera && resultCode == ((Activity) context).RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        return bitmap;
    }

    public void onActivityResultCamera(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if (requestCode == Global.requestCamera && resultCode == ((Activity) context).RESULT_OK) {
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
