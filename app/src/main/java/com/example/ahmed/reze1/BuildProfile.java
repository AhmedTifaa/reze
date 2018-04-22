package com.example.ahmed.reze1;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.graphics.drawable.BitmapDrawable;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.example.ahmed.reze1.helper.PrefManager;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.io.ByteArrayOutputStream;
        import java.io.InputStream;
        import java.util.HashMap;
        import java.util.Map;

public class BuildProfile extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    public int[] layouts;
    private Button btnNext;
    private PrefManager prefManager;
    private String fbname;
    private String fbpicurl;
    private TextView user_namae;
    public RadioGroup radioGroupptl;
    public RadioGroup radioGroupr;
    public RequestQueue requestQueue;
    public EditText address;
    public EditText phone;
    public RadioButton radioButtonptl;
    public RadioButton radioButtonr;
    public ImageView user_img;
    public String user_id;
    public Spinner spinnerCarrer;
    public Spinner spinnerCity;
    public static final int PICK_IMAGE = 1;
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            //launchHomeScreen();
            //finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_buildprofile);
        Bundle inBundle = getIntent().getExtras();
        fbname = inBundle.get("fbname").toString();
        fbpicurl = inBundle.get("fbpicurl").toString();
        user_id = inBundle.get("id").toString();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        requestQueue = Volley.newRequestQueue(this);
        //btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        progress = new ProgressDialog(BuildProfile.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        //btnSkip.setVisibility(View.GONE);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.build_profile1,
                /*R.layout.build_profile2,
                R.layout.build_profile3*/};

        // adding bottom dots
        //addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
//        btnSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //launchHomeScreen();
//            }
//        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(),spinnerCarrer.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(getBaseContext(),spinnerCity.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
//                Bitmap per_img = ((BitmapDrawable) user_img.getDrawable()).getBitmap();
//                new UploadImage(per_img,(System.currentTimeMillis()/100)+"").execute();
                //radioButtonptl = (RadioButton)findViewById(radioGroupptl.getCheckedRadioButtonId());
                //radioButtonr = (RadioButton)findViewById(radioGroupr.getCheckedRadioButtonId());
                //Toast.makeText(getBaseContext(),address.getText(),Toast.LENGTH_LONG).show();
                //Toast.makeText(getBaseContext(),phone.getText(),Toast.LENGTH_LONG).show();
               // Toast.makeText(getBaseContext(),radioButtonptl.getText(),Toast.LENGTH_LONG).show();
               // Toast.makeText(getBaseContext(),radioButtonr.getText(),Toast.LENGTH_LONG).show();
              // Toast.makeText(getBaseContext(),validate()+"",Toast.LENGTH_LONG).show();
                if(!validate()){


                }
                else{
                    progress.show();
                    radioButtonptl = (RadioButton)findViewById(radioGroupptl.getCheckedRadioButtonId());
                    radioButtonr = (RadioButton)findViewById(radioGroupr.getCheckedRadioButtonId());
                    Bitmap per_img = ((BitmapDrawable) user_img.getDrawable()).getBitmap();
                    new UploadImage(per_img,(System.currentTimeMillis()/100)+"").execute();
                    StringRequest request = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/rcp.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getBaseContext(),"test",Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                            progress.dismiss();
                            //hideDialog();
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                //Toast.makeText(getBaseContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                if(jsonObject.getString("msg").equals("done")){
                                    Intent intent = new Intent(BuildProfile.this,BuildProfile1.class);
                                    intent.putExtra("id",user_id);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                   // Toast.makeText(getBaseContext(),response.toString(),Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parameters  = new HashMap<String, String>();

                            parameters.put("city",spinnerCity.getSelectedItem().toString());
                            parameters.put("career",spinnerCarrer.getSelectedItem().toString());
                            parameters.put("address",address.getText().toString());
                            if(phone.getVisibility() == View.GONE){

                            }else{
                                parameters.put("phone",phone.getText().toString());
                            }

                            parameters.put("ptl",radioButtonptl.getText().toString());
                            parameters.put("id",user_id);

                            return parameters;
                        }
                    };
                    requestQueue.add(request);

                }

            }
        });


    }



    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(BuildProfile.this, MainActivity.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {

            //Toast.makeText(getBaseContext(),viewPager.getCurrentItem()+"",Toast.LENGTH_LONG).show();
            viewPager.getAdapter().notifyDataSetChanged();
            //addBottomDots(position);
             //btnNext.setVisibility(View.GONE);
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                //btnSkip.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            //Toast.makeText(getBaseContext(),"5050",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            //Toast.makeText(getBaseContext(),"12",Toast.LENGTH_LONG).show();
        }
    };
    public void gallary(View view){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select App");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imgSelectedUri = data.getData();
            ((ImageView)findViewById(R.id.profile_upload_image)).setImageURI(imgSelectedUri);
        }
    }
    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends android.support.v4.view.PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            user_img = (ImageView)view.findViewById(R.id.profile_upload_image);
            //Toast.makeText(getBaseContext(),layouts.length+"    "+position,Toast.LENGTH_LONG).show();
            if(position == 0){
                //Toast.makeText(getBaseContext(),position+"",Toast.LENGTH_LONG).show();
                user_namae = (TextView)view.findViewById(R.id.user_build_name);
                user_namae.setText(fbname);
                //((ImageView)findViewById(R.id.profile_upload_image)).setImageResource(R.drawable.default_avatar);
                //Toast.makeText(getBaseContext(),fbpicurl,Toast.LENGTH_LONG).show();
                if (fbpicurl.equals("null")){
                    //Toast.makeText(getBaseContext(),"1",Toast.LENGTH_LONG).show();
                    ((ImageView)view.findViewById(R.id.profile_upload_image)).setImageResource(R.drawable.default_avatar);

                }
                else{
                    //Toast.makeText(getBaseContext(),"2",Toast.LENGTH_LONG).show();
                    new DownloadImage((ImageView)view.findViewById(R.id.profile_upload_image)).execute(fbpicurl);
                    ((EditText)view.findViewById(R.id.phone_compelete)).setVisibility(View.VISIBLE);
                }

                spinnerCity = (Spinner) view.findViewById(R.id.spinner_city);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapterSpinnerCity = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_city, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapterSpinnerCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinnerCity.setAdapter(adapterSpinnerCity);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       // Toast.makeText(getBaseContext(),spinnerCity.getSelectedItemPosition()+"",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                spinnerCarrer = (Spinner) view.findViewById(R.id.spinner_carrer);
                radioGroupptl = (RadioGroup)view.findViewById(R.id.rgptl);
                radioGroupr = (RadioGroup)view.findViewById(R.id.rtp);
                address = (EditText) view.findViewById(R.id.c_address);
                phone = (EditText)view.findViewById(R.id.phone_compelete);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapterSpinnerCarrer = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_carrer, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapterSpinnerCarrer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinnerCarrer.setAdapter(adapterSpinnerCarrer);
                spinnerCarrer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       // Toast.makeText(getBaseContext(),spinnerCarrer.getSelectedItemPosition()+"",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
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
    public class UploadImage extends AsyncTask<Void,Void,Void>{
        Bitmap image;
        String imgName;

        public UploadImage(Bitmap image,String imgName){
           this.image = image;
           this.imgName = imgName;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            final String enI = android.util.Base64.encodeToString(byteArrayOutputStream.toByteArray(), android.util.Base64.DEFAULT);
            StringRequest request = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/upload.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getBaseContext(),"test",Toast.LENGTH_LONG).show();
                   // Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();

                    //hideDialog();
                    try {
                        JSONObject jsonObject;
                        jsonObject = new JSONObject(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                       // Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters  = new HashMap<String, String>();

                    parameters.put("image",enI.toString());
                    parameters.put("img_name",imgName.toString());
                    parameters.put("id",user_id.toString());
                    return parameters;
                }
            };
            requestQueue.add(request);
//            ArrayList<NameValuePair> data = new ArrayList<>();
//            data.add(new BasicNameValuePair("image",enI));
//            data.add(new BasicNameValuePair("img_name",imgName));
//            data.add(new BasicNameValuePair("id",user_id));
//            HttpParams httpParams = httpRequest();
//            HttpClient httpClient = new DefaultHttpClient(httpParams);
//            HttpPost httpPost = new HttpPost("");
//            try {
//                httpPost.setEntity(new UrlEncodedFormEntity(data));
//                httpClient.execute(httpPost);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
           // Toast.makeText(getBaseContext(),"image sent",Toast.LENGTH_LONG).show();
        }
    }
//    private HttpParams httpRequest(){
//      HttpParams httpParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpParams,100 * 30);
//        HttpConnectionParams.setSoTimeout(httpParams,100 * 30);
//        return httpParams;
//    }
public boolean validate() {
    boolean valid = true;

    String addressV = address.getText().toString();
    String mobile = phone.getText().toString();

    if (addressV.isEmpty()) {
        address.setError(getResources().getString(R.string.validate_address));
        valid = false;
    } else {
        address.setError(null);
    }

    if (mobile.isEmpty()) {
        if(phone.getVisibility() == View.GONE){

        }else{
            phone.setError(getResources().getString(R.string.validate_phone));
            valid = false;
        }

    } else {
        phone.setError(null);
    }
    if (spinnerCarrer.getSelectedItemPosition() == 0) {
        Toast.makeText(getBaseContext(),R.string.validate_career,Toast.LENGTH_LONG).show();
        TextView errorText = (TextView)spinnerCarrer.getSelectedView();
        errorText.setError(getResources().getString(R.string.validate_career));
        valid = false;
    } else {
        TextView errorText = (TextView)spinnerCarrer.getSelectedView();
        errorText.setError(null);
    }
    if (spinnerCity.getSelectedItemPosition() == 0) {
        Toast.makeText(getBaseContext(),R.string.validate_city,Toast.LENGTH_LONG).show();
        TextView errorText = (TextView)spinnerCity.getSelectedView();
        errorText.setError(getResources().getString(R.string.validate_city));
        valid = false;
    } else {
        TextView errorText = (TextView)spinnerCarrer.getSelectedView();
        errorText.setError(null);
    }

    return valid;
}

}

