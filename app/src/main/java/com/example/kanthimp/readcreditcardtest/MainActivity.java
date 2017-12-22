package com.example.kanthimp.readcreditcardtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ocecreditcardlibrary.module.Camera;
import com.example.ocecreditcardlibrary.util.Card;


public class MainActivity extends AppCompatActivity implements Camera.CallBack {

    Camera camera;
    Button takePicture;
    ImageView imageView;
    TextView idCard;
    TextView expDate;
    TextView txtFailed;
    TextView txtSuccess;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePicture = (Button) findViewById(R.id.takePicture);
        imageView = (ImageView) findViewById(R.id.imageView);
        idCard = (TextView) findViewById(R.id.idCard);
        expDate = (TextView) findViewById(R.id.expDate);
        txtFailed = (TextView) findViewById(R.id.txtFailed);
        txtSuccess = (TextView) findViewById(R.id.txtSuccess);


        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = new Camera(MainActivity.this, MainActivity.this).openCamera();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.onActivityResultCamera(requestCode, resultCode, data);
    }

    @Override
    public void checkResultCameraSuccess(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void checkResultCameraFailed(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void loading() {
        showLoading();

    }


    @Override
    public void failed(String message) {
        hideLoading();
        txtFailed.setText(message);
    }

    @Override
    public void success(Card card) {
        hideLoading();
        txtSuccess.setText(card.getMessage());
        idCard.setText(card.getIdCard());
        expDate.setText(card.getExpDate());

    }

    private void showLoading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading ....");
        progressDialog.show();
    }

    private void hideLoading(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
