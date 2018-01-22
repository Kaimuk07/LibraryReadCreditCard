package com.example.ocecreditcardlibrary.module;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.ocecreditcardlibrary.config.Global;
import com.example.ocecreditcardlibrary.util.Card;
import com.example.ocecreditcardlibrary.util.ResultService;
import com.example.ocecreditcardlibrary.util.Success;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanthimp on 13/12/2560.
 */

public class CallService {
    private String api = "TEXT_DETECTION";
    private Feature feature;
    private CallBackService callBackService;

    public CallService() {
        setFeature();
    }

    public void setCallBackService(CallBackService callBackService) {
        this.callBackService = callBackService;
    }

    public void setFeature() {
        feature = new Feature();
        feature.setType(api);
        feature.setMaxResults(100);
    }

    private List<Feature> setFeatureList() {
        List<Feature> featureList = new ArrayList<>();
        featureList.add(feature);
        return featureList;
    }

    private List<AnnotateImageRequest> setAnnotateImageRequests(Bitmap bitmap) {
        List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();
        AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
        annotateImageReq.setFeatures(setFeatureList());
        annotateImageReq.setImage(getImageEncodeImage(bitmap));
        annotateImageRequests.add(annotateImageReq);
        return annotateImageRequests;
    }

    public void callCloudVision(final Bitmap bitmap) {
        callBackService.loading();
        final List<AnnotateImageRequest> annotateImageRequests = setAnnotateImageRequests(bitmap);
        new AsyncTask<Object, Void, ResultService>() {
            @Override
            protected ResultService doInBackground(Object... params) {
                ResultService resultService = new ResultService();

                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer = new VisionRequestInitializer(Global.getInstance().getCloudVisionKey());

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                    Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    resultService.setSuccess(convertResponseToSuccess(response));
                    return resultService;
                } catch (GoogleJsonResponseException e) {
                    resultService.setFailed("failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    resultService.setFailed("failed to make API request because of other IOException " + e.getMessage());
                } catch (Exception e) {
                    resultService.setFailed("failed exception");
                    e.printStackTrace();
                }
                resultService.setFailed("Cloud Vision API request failed. Check logs for details.");
                return resultService;

            }

            protected void onPostExecute(ResultService result) {
                if (result != null) {
                    if (result.getSuccess() != null) {
                        Card card = new Card();
                        card.setArrayList(result.getSuccess().getArrayList());
                        card.setMessage(result.getSuccess().getMessage());
                        card.cutSting();
                        callBackService.success(card);
                    }
                }
                callBackService.failed(result.getFailed());
            }
        }.execute();
    }

    @NonNull
    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        // Just in case it's a format that Android understands but Cloud Vision
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }

    private Success convertResponseToSuccess(BatchAnnotateImagesResponse response) throws Exception {
        AnnotateImageResponse imageResponses = response.getResponses().get(0);
        List<EntityAnnotation> entityAnnotations;
        ArrayMap<String, String> fullTextAnnotation = (ArrayMap<String, String>) imageResponses.get("fullTextAnnotation");
        ArrayList<EntityAnnotation> arrayList = (ArrayList<EntityAnnotation>) imageResponses.get("textAnnotations");
        String message = "";
        for (EntityAnnotation entity : arrayList) {
            if (message.equals("")) {
                message = entity.getDescription();
            } else {
                message = message + "\n" + entity.getDescription();
            }
        }

        Success success = new Success();
        success.setArrayList(arrayList);
        success.setMessage(message);
        return success;
    }

    interface CallBackService {
        void success(Card card);

        void failed(String message);

        void loading();
    }
}
