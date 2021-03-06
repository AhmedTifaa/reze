package app.reze.ahmed.reze1;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import app.reze.ahmed.reze1.GUI.CustomButton;
import app.reze.ahmed.reze1.GUI.CustomEditText;
import app.reze.ahmed.reze1.helper.DateDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {
    private static final String TAG = Registration.class.getSimpleName();
    private CustomButton btnRegister;

    private CustomEditText inputFullName;
    private CustomEditText inputMobile;
    private CustomEditText inputEmail;
    private CustomEditText inputDateOfBirth;
    private CustomEditText inputPassword;
    private CustomEditText inputRepassword;
    private CustomEditText birthdate;
    private ProgressDialog pDialog;
    private  CustomButton btnLogin;
    private CheckBox checkBox;
    private Calendar myCalendar;
    RequestQueue requestQueue;
    public static String URL_REGISTER = "https://rezetopia.com/app/register.php";
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Spinner spinner = (Spinner) findViewById(R.eventId.spinner);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.object_array, R.layout.spinner_item);

        //adapter.setDropDownViewResource(R.layout.spinner_item);
//        myCalendar = Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//
//        };
        //spinner.setAdapter(adapter);
        inputFullName = (CustomEditText) findViewById(R.id.edName);
        inputMobile=(CustomEditText)findViewById(R.id.edPhone);
        inputEmail = (CustomEditText) findViewById(R.id.edEmail);
        inputDateOfBirth=(CustomEditText)findViewById(R.id.edDate);
        inputPassword = (CustomEditText) findViewById(R.id.edPassword);
        inputRepassword=(CustomEditText)findViewById(R.id.edConfirmPassword);
        checkBox = (CheckBox)findViewById(R.id.checkbox);
        btnRegister = (CustomButton) findViewById(R.id.btnRegister);
        btnLogin = (CustomButton) findViewById(R.id.btnLogin);
        birthdate = (CustomEditText)findViewById(R.id.edDate);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        progress = new ProgressDialog(Registration.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivityForResult(intent, 0);
                //finish();
            }
        });
        // Session manager

        // SQLite database handler

        // Check if user is already logged in or not
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                validate();
                if (!validate()) {


                } else {
                    progress.show();
                    StringRequest request = new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getBaseContext(),"test",Toast.LENGTH_LONG).show();
                            //Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();

                            //hideDialog();
                            progress.dismiss();
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                               // Toast.makeText(getBaseContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                if (jsonObject.getString("msg").equals("done")) {
                                   // Toast.makeText(getApplicationContext(), jsonObject.getString("eventId"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                    intent.putExtra("fbname", inputFullName.getText().toString());
                                    intent.putExtra("fbpicurl", "null");
                                    intent.putExtra("id", jsonObject.getString("id"));
                                    startActivityForResult(intent, 0);
                                    finish();
                                } else if (jsonObject.getString("msg").equals("This mail is already exsist you can log in")) {
                                  //  Toast.makeText(getBaseContext(), R.string.exsistEmail, Toast.LENGTH_LONG).show();
                                } else {
                                  //  Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
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
                            Map<String, String> parameters = new HashMap<String, String>();

                            parameters.put("name", inputFullName.getText().toString());
                            parameters.put("mobile", inputMobile.getText().toString());
                            parameters.put("mail", inputEmail.getText().toString());
                            parameters.put("birthday", inputDateOfBirth.getText().toString());
                            parameters.put("password", inputPassword.getText().toString());

                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                }
            }
        });


    }
//    private void updateLabel() {
//        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        birthdate.setText(sdf.format(myCalendar.getTime()));
//    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public boolean validate() {
        boolean valid = true;

        String name = inputFullName.getText().toString();
        String mobile = inputMobile.getText().toString();
        String email = inputEmail.getText().toString();
        String date_of_birth = inputDateOfBirth.getText().toString();
        String password =inputPassword.getText().toString();
        String reEnterPassword =inputRepassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            inputFullName.setError("at least 3 characters");
            valid = false;
        } else {
            inputFullName.setError(null);
        }

        if (date_of_birth.isEmpty()) {
            inputDateOfBirth.setError("Enter your birthday date");
            valid = false;
        } else {
            inputDateOfBirth.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=11) {
            inputMobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            inputMobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            inputRepassword.setError("Password Do not match");
            valid = false;
        } else {
            inputRepassword.setError(null);
        }
        if(!checkBox.isChecked()){
            checkBox.setError("Confirm Terms Please");
            valid = false;
        } else{
            checkBox.setError(null);
        }

        return valid;
    }


    public void onStart(){
        super.onStart();

        inputDateOfBirth=(CustomEditText)findViewById(R.id.edDate);
        inputDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");

                }
            }

        });
    }
}
