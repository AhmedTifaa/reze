package com.example.ahmed.reze1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lal.adhish.gifprogressbar.GifView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class BuildNetwork extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    public int[] layouts;
    private Button btnNext;
    private Button btnSkip;
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
    public ListView listView ;
    public ArrayList<String> StoreContacts ;
    public ArrayAdapter<String> arrayAdapter ;
    public Cursor cursor ;
    public String name, phonenumber ;
    public ProgressDialog progress;
    public String result;
    public ListView suggestlist;
    public ArrayList<String>values;
    public ArrayAdapter<String> stringArrayAdapter;
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

        setContentView(R.layout.activity_buildnetwork);
        Bundle inBundle = getIntent().getExtras();
        user_id = inBundle.get("user_id").toString();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        requestQueue = Volley.newRequestQueue(this);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        //btnSkip.setVisibility(View.GONE);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.build_network1,};
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuildNetwork.this,MainActivity.class);
                intent.putExtra("id",user_id);
                startActivity(intent);
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new network().execute();
//                Intent intent = new Intent(BuildNetwork.this,MainActivity.class);
//                intent.putExtra("id",user_id);
//                startActivity(intent);
//                finish();

            }
        });


    }



    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
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
            suggestlist = (ListView) view.findViewById(R.id.contacts_list);
            values = new ArrayList<>();

            progress = new ProgressDialog(BuildNetwork.this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();

            StoreContacts = new ArrayList<String>();
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
            while (cursor.moveToNext()) {

                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phonenumber = phonenumber.replaceAll("\\s+","");
                if (phonenumber.charAt(0) == '+'){
                    phonenumber =   phonenumber.substring(2);
                }
                StoreContacts.add(phonenumber);
            }
            Toast.makeText(getBaseContext(),StoreContacts.toString(),Toast.LENGTH_LONG).show();

            cursor.close();

            StringRequest request = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/contacts.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progress.dismiss();
                    //Toast.makeText(getBaseContext(),"test",Toast.LENGTH_LONG).show();

                    Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                    if (response.equals("skip")){
                        return;
                    }
                    try {
                        JSONObject jsonObject;
                        //jsonObject = new JSONObject(response);
                        JSONArray jsonArray = new JSONArray(response);
                        jsonObject = new JSONObject(jsonArray.get(0).toString());
                        //Toast.makeText(getBaseContext(),jsonObject.get("name").toString(),Toast.LENGTH_LONG).show();

                        for (int i = 0;i<jsonArray.length();i++){
                            values.add(jsonArray.get(i).toString());
                        }

                        //Toast.makeText(getBaseContext(),values.toString(),Toast.LENGTH_LONG).show();
                        contactAddapter contactAddapter = new contactAddapter(BuildNetwork.this,R.layout.item_friend,values);
                        //ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(BuildNetwork.this,R.layout.item_friend,values);

                        suggestlist.setAdapter(contactAddapter);
//                        suggestlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view,
//                                                    int position, long id) {
//                              final TextView ss = (TextView)view.findViewById(R.id.id);
//                              ss.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                   Toast.makeText(BuildNetwork.this,ss.getText() , Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//                            }
//                        });
                        //Toast.makeText(getBaseContext(),jsonArray.get(0).toString(),Toast.LENGTH_LONG).show();

                       /* if(jsonObject.getString("msg").equals("done")){
//                                Intent intent = new Intent(BuildProfile2.this,BuildNetwork.class);
//                                intent.putExtra("user_id",user_id);
//                                startActivity(intent);
//                                finish();
                        }
                        else {
                            //Toast.makeText(getBaseContext(),response.toString(),Toast.LENGTH_LONG).show();
                        }*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(getBaseContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

                    }
                    //hideDialog();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters  = new HashMap<String, String>();
                    for (int i = 0;i< StoreContacts.size();i++){
                        parameters.put("contact"+i,StoreContacts.get(i));
                    }
                    parameters.put("id",user_id);


                    return parameters;
                }
            };
            requestQueue.add(request);
            //new getFriendsContact().execute();
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
    public class network extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest request = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/networkstate.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();

                    //hideDialog();
                    try {
                        JSONObject jsonObject;
                        jsonObject = new JSONObject(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters  = new HashMap<String, String>();

                    parameters.put("snet","1");
                    parameters.put("id",user_id);
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }



}

