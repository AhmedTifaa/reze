package app.reze.ahmed.reze1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.reze.ahmed.reze1.GUI.CustomButton;
import app.reze.ahmed.reze1.GUI.CustomEditText;
import app.reze.ahmed.reze1.GUI.CustomTextView;

import app.reze.ahmed.reze1.app.AppConfig;
import com.facebook.*;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Login extends AppCompatActivity {
    CustomButton btnSignUp;
    private CustomEditText name,password;
    private CustomButton btnSignIn;
    private RequestQueue requestQueue;
    public static String URL_LOGIN = "https://rezetopia.com/app/login.php";
    private StringRequest request;
    private LoginButton fblogin;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ProgressDialog pDialog;
    private Profile profile;
    private Object fbobject;

    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        fblogin = (LoginButton) findViewById(R.id.login_button);
        fblogin.setReadPermissions("public_profile");
        fblogin.setReadPermissions("email");
        fblogin.setReadPermissions("user_friends");
        fblogin.setReadPermissions("user_birthday");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // If using in a fragment
        /*fblogin.setFragment(this);*/
        // Callback registration*/
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        profile = Profile.getCurrentProfile();
                        //Toast.makeText(getBaseContext(),profile.getName(),Toast.LENGTH_LONG).show();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                       /* try {

                                            //String email = object.getString("email");
                                            //String birthday = object.getString("birthday"); // 01/31/1980 format
                                            fbRegister(profile.getName(),object.getString("email"),object.getString("birthday"),profile.getProfilePictureUri(200,200).toString());

                                            //Toast.makeText(getBaseContext(),email+" "+birthday,Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }*/


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        btnSignUp=(CustomButton)findViewById(R.id.btnRegister);
        name = (CustomEditText) findViewById(R.id.edName);
        password = (CustomEditText) findViewById(R.id.edPassword);
        btnSignIn = (CustomButton) findViewById(R.id.btnSignIn);
        requestQueue = Volley.newRequestQueue(this);
        CustomTextView tvForgetPassword=(CustomTextView)findViewById(R.id.txtvForgetPass);
        tvForgetPassword.setPaintFlags(tvForgetPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //finish();
            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //finish();
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {


                } else {

                    request = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject;
                                Log.i("login_response", response);
                                //JSONArray jsonArray = new JSONArray(response);
                                jsonObject = new JSONObject(response);
                                //Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit();
                                editor.putString(AppConfig.LOGGED_IN_USER_ID_SHARED, jsonObject.getString("id"))
                                        .putString(AppConfig.LOGGED_IN_USER_NAME_SHARED, jsonObject.getString("name")).apply();

                                if (jsonObject.getString("msg").equals("user")) {
                                   // Toast.makeText(getApplicationContext(), jsonObject.getString("state"), Toast.LENGTH_SHORT).show();
                                    if(jsonObject.getString("state").equals("0")){
                                        Intent intent = new Intent(getApplicationContext(), BuildProfile.class);
                                        intent.putExtra("fbname", jsonObject.getString("name"));
                                        intent.putExtra("fbpicurl", "null");
                                        intent.putExtra("id",jsonObject.getString("id"));
                                        startActivityForResult(intent, 0);
                                        finish();
                                    }
                                    else if(jsonObject.getString("state").equals("1")){
                                        Intent intent = new Intent(getApplicationContext(), BuildProfile1.class);
                                        intent.putExtra("fbname", jsonObject.getString("name"));
                                        intent.putExtra("fbpicurl", "null");
                                        intent.putExtra("id",jsonObject.getString("id"));
                                        startActivityForResult(intent, 0);
                                        finish();
                                    }
                                    else if(jsonObject.getString("state").equals("2")){
                                        Intent intent = new Intent(getApplicationContext(), BuildProfile2.class);
                                        intent.putExtra("fbname", jsonObject.getString("name"));
                                        intent.putExtra("fbpicurl", "null");
                                        intent.putExtra("id",jsonObject.getString("id"));
                                        startActivityForResult(intent, 0);
                                        finish();
                                    }
                                    else if(jsonObject.getString("state").equals("3")){
                                        if(jsonObject.getString("snet").equals("0")){
                                          //  Toast.makeText(getBaseContext(),jsonObject.getString("id"),Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), BuildNetwork.class);
                                            intent.putExtra("id",jsonObject.getString("id"));
                                            startActivityForResult(intent, 0);
                                            finish();
                                        }
                                        else{
                                           // Toast.makeText(getBaseContext(),jsonObject.getString("id"),Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("id",jsonObject.getString("id"));
                                            intent.putExtra("type", jsonObject.getString("msg"));
                                            startActivityForResult(intent, 0);
                                            finish();
                                        }

                                    }

                                } else if (jsonObject.getString("msg").equals("no")) {
                                  //  Toast.makeText(getBaseContext(), R.string.wronglogin, Toast.LENGTH_LONG).show();
                                } else if (jsonObject.getString("msg").equals("vendor")){
                                   // Toast.makeText(getBaseContext(), "vendor", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("id",jsonObject.getString("id"));
                                    intent.putExtra("type", jsonObject.getString("msg"));
                                    startActivityForResult(intent, 0);
                                    finish();
                                }
                                else {
                                   // Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                          //  Toast.makeText(getBaseContext(), R.string.checkingNetwork, Toast.LENGTH_LONG).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("mail", name.getText().toString());
                            hashMap.put("login", "login");
                            hashMap.put("password", password.getText().toString());
                            return hashMap;
                        }
                    };

                    requestQueue.add(request);
                }
            }
        });
    }
//    public void fbRegister(final String name, final String mail, final String birthdate, final String imgUrl){
//
//        showDialog();
//        StringRequest request = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/fbregister.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                hideDialog();
//                //Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
//                try {
//                    JSONObject jsonObject;
//                    jsonObject = new JSONObject(response);
//                    //Toast.makeText(getBaseContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
//                    if(jsonObject.getString("msg").equals("done")){
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("id"),Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
//                        intent.putExtra("fbname",name);
//                        intent.putExtra("fbpicurl",imgUrl);
//                        intent.putExtra("id",jsonObject.getString("id"));
//                        startActivityForResult(intent, 0);
//                        finish();
//                    }else if(jsonObject.getString("msg").equals("This mail is already exsist you can log in")){
//                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
//                        intent.putExtra("fbname",name);
//                        intent.putExtra("fbpicurl",imgUrl);
//                        intent.putExtra("id",jsonObject.getString("id"));
//                        startActivityForResult(intent, 0);
//                        finish();
//
//                        //Toast.makeText(getBaseContext(),R.string.exsistEmail,Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Toast.makeText(getBaseContext(),response.toString(),Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> parameters  = new HashMap<String, String>();
//
//                parameters.put("name",name);
//                parameters.put("mail",mail);
//                parameters.put("birthday",birthdate);
//                parameters.put("img_url",imgUrl);
//
//                return parameters;
//            }
//        };
//        requestQueue.add(request);
//    }
    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validate() {
        boolean valid = true;

        String userName = name.getText().toString();
        String userPassword = password.getText().toString();

        if (userName.isEmpty()) {
            name.setError("enter your mail Address");
            valid = false;
        } else {
            name.setError(null);
        }

        if (userPassword.isEmpty() || password.length() < 4) {
            password.setError("More Than 4 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}


