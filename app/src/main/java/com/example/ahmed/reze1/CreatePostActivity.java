package com.example.ahmed.reze1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.ahmed.reze1.api.post.PostResponse;
import com.example.ahmed.reze1.app.AppConfig;
import com.example.ahmed.reze1.helper.VolleyCustomRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog dialog = null;
    private List<Image> selectedImages;
    private ArrayList<String> encodedImages;

    EditText postTextView;
    ImageView imageView;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postTextView = findViewById(R.id.new_post_desc);
        imageView = findViewById(R.id.new_post_image);
        postButton = findViewById(R.id.post_btn);

        imageView.setOnClickListener(this);
        postButton.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating your post");
    }


    private void openImagePicker(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ImagePicker.create(CreatePostActivity.this)
                                .folderMode(true) // folder mode (false by default)
                                .toolbarFolderTitle("Folder") // folder selection title
                                .toolbarImageTitle("Tap to select")
                                .start();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_btn:
                if (validPost()){
                    performUpload();
                }
                break;
            case R.id.new_post_image:
                openImagePicker();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            selectedImages = ImagePicker.getImages(data);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validPost(){

        if (postTextView.getText().toString().length() > 0 || (selectedImages != null && selectedImages.size() > 0)){
            return true;
        }

        return false;
    }

    private void performUpload(){
        dialog.show();
        encodedImages = new ArrayList<>();
        if (encodedImages.size() > 0){
            for (Image image: selectedImages) {
                Bitmap bm = null;
                bm = BitmapFactory.decodeFile(image.getPath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                encodedImages.add(encodedImage);
            }

            HashMap<String, ArrayList<String>> jsonMap = new HashMap<>();
            jsonMap.put("imageList", encodedImages);
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.18:80/reze/user_post.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley response", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    PostResponse postResponse = new PostResponse();
                    //todo add username
                    postResponse.setUsername("ahmed ali");
                    postResponse.setCreatedAt(jsonObject.getString("createdAt"));
                    postResponse.setText(jsonObject.getString("text"));
                    Intent intent = new Intent();
                    intent.putExtra("post", postResponse);
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley error", "onErrorResponse: " + error.getMessage());
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                if (encodedImages.size() > 0){
                    for (String value : encodedImages) {
                        map.put(encodedImages.indexOf(value) + "", value);
                    }
                    map.put("images_size", encodedImages.size() + "");
                }

                map.put("method", "create_post");
                map.put("userId", getSharedPreferences(AppConfig.SHARED_PREFERECE_NAME, MODE_PRIVATE)
                        .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "1"));
                if (postTextView.getText().toString().length() > 0){
                    map.put("post_text", postTextView.getText().toString());
                }

                return map;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
