package com.example.ahmed.reze1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tifaa on 15/04/18.
 */


public class contactAddapter extends ArrayAdapter<String> {
    Context context;
    int resource;
    ArrayList<String> vals;
    JSONObject jsonObject;
    RequestQueue requestQueue;
    String userId;
    String btnId;
    TextView id;
    View rowView;
    Button button;
    View entireBtn;
    Button buttonC;
    public contactAddapter(@NonNull Context context, int resource,ArrayList<String> vals) {
        super(context, resource, vals);
        this.context = context;
        this.resource = resource;
        this.vals = vals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //convertView = LayoutInflater.from(context).inflate(R.layout.item_friend,parent,false);
        //TextView textView = (TextView)convertView.findViewById(R.id.sugName);
       /* try {
            jsonObject = new JSONObject(vals.get(position));
            //textView.setText("50");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
       // return super.getView(position, convertView, parent);
        userId = context.getSharedPreferences(AppConfig.SHARED_PREFERECE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "0");
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        rowView = inflater.inflate(R.layout.item_friend, null, true);
        TextView textView = (TextView)rowView.findViewById(R.id.sugName);
        button = (Button)rowView.findViewById(R.id.sendAdd);

        requestQueue = Volley.newRequestQueue(context);
        try {
            jsonObject = new JSONObject(vals.get(position));
            textView.setText(jsonObject.get("name").toString());
            button.setId(Integer.parseInt(jsonObject.get("id").toString()));
            btnId = jsonObject.get("id").toString();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   entireBtn = v;
                   buttonC = (Button)v.findViewById(entireBtn.getId());
                   if (buttonC.getText().equals("Add")){
                    StringRequest request = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/addfriend.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
                            buttonC.setText("Remove");
                            buttonC.setBackground(context.getResources().getDrawable(R.drawable.roundone_dark));
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);

                                if(jsonObject.getString("msg").equals("added")){

                                }
                                else {
                                    //Toast.makeText(getBaseContext(),response.toString(),Toast.LENGTH_LONG).show();
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
                            parameters.put("add","add");
                            parameters.put("from",userId);
                            parameters.put("to",entireBtn.getId()+"");

                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                   }
                   else if(buttonC.getText().equals("Remove")){
                       buttonC.setText("Add");
                       buttonC.setBackground(context.getResources().getDrawable(R.drawable.roundone_green));

                       Toast.makeText(getContext(),"remove freind",Toast.LENGTH_LONG).show();
                   }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rowView;
    }
}
