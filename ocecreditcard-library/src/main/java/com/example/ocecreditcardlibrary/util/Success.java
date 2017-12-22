package com.example.ocecreditcardlibrary.util;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.ArrayList;

/**
 * Created by kanthimp on 14/12/2560.
 */

public class Success {
    private String message;
    private ArrayList<EntityAnnotation> arrayList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<EntityAnnotation> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<EntityAnnotation> arrayList) {
        this.arrayList = arrayList;
    }
}
