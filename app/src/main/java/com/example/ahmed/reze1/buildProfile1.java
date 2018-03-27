package com.example.ahmed.reze1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ahmed.reze1.R;
import java.io.InputStream;

/**
 * Created by tifaa on 27/03/18.
 */

public class buildProfile1 extends AppCompatActivity {
    private TextView user_namae;
    private String fbname;
    private String fbpicurl;
    public buildProfile1(String fbname,String fbpicurl){
        this.fbname = fbname;
        this.fbpicurl = fbpicurl;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.build_profile1);
    }
    public void init(View view){
        user_namae = (TextView) view.findViewById(R.id.user_build_name);
        user_namae.setText(fbname);
        new DownloadImage((ImageView)findViewById(R.id.profile_upload_image)).execute(fbpicurl);
    }
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }

    }
}
