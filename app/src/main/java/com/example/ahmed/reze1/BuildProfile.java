package com.example.ahmed.reze1;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Html;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.ahmed.reze1.helper.PrefManager;

        import java.io.FileNotFoundException;
        import java.io.InputStream;

public class BuildProfile extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip,btnNext;
    private PrefManager prefManager;
    private String fbname;
    private String fbpicurl;
    private TextView user_namae;
    public static final int PICK_IMAGE = 1;

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

        setContentView(R.layout.activity_welcome);
        Bundle inBundle = getIntent().getExtras();
        fbname = inBundle.get("fbname").toString();
        fbpicurl = inBundle.get("fbpicurl").toString();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);



        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.build_profile1,
                R.layout.build_profile2,
                R.layout.build_profile3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });


    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
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
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            }
            if(position == layouts.length-2){
                user_namae = (TextView)findViewById(R.id.user_build_name);
                user_namae.setText(fbname);
                new DownloadImage((ImageView)findViewById(R.id.profile_upload_image)).execute(fbpicurl);

                Spinner spinnerCity = (Spinner) findViewById(R.id.spinner_city);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapterSpinnerCity = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_city, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapterSpinnerCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinnerCity.setAdapter(adapterSpinnerCity);


                Spinner spinnerCarrer = (Spinner) findViewById(R.id.spinner_carrer);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapterSpinnerCarrer = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_carrer, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapterSpinnerCarrer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinnerCarrer.setAdapter(adapterSpinnerCarrer);

            }
            else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

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
            if(position == layouts.length-2){
                user_namae = (TextView)findViewById(R.id.user_build_name);
                user_namae.setText(fbname);
                new DownloadImage((ImageView)findViewById(R.id.profile_upload_image)).execute(fbpicurl);
                Spinner spinnerCity = (Spinner) findViewById(R.id.spinner_city);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapterSpinnerCity = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_city, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapterSpinnerCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinnerCity.setAdapter(adapterSpinnerCity);


                Spinner spinnerCarrer = (Spinner) findViewById(R.id.spinner_carrer);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapterSpinnerCarrer = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.spinner_carrer, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapterSpinnerCarrer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinnerCarrer.setAdapter(adapterSpinnerCarrer);
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

}

