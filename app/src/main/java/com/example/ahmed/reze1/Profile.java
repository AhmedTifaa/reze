package com.example.ahmed.reze1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    private static final String USER_ID = "user_id";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ViewPager viewPager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //private User user;
    public TextView playerNameTv;
    public TextView playerCityTv;
    public TextView playerPositionTv;
    public TextView playerMatchesTv;
    public TextView playerPointsTv;
    public TextView playerLevelsTv;
    RequestQueue requestQueue;

    private OnFragmentInteractionListener mListener;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
         viewPager = (ViewPager)v.findViewById(R.id.pager);

        View profile_menu = v.findViewById(R.id.profile_menu);
        profile_menu.setOnClickListener(new optionProfile(getContext()));
        //getUser(userId);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String userName = null;
        String userCity=null;
        String userPosition=null;
        String userMatches=null;
        String userPoints=null;
        String userLevel=null;

        playerNameTv.setText(userName);
        playerCityTv.setText(userCity);
        playerPositionTv.setText(userPosition);
        playerMatchesTv.setText(userMatches);
        playerPointsTv.setText(userPoints);
        playerLevelsTv.setText(userLevel);


        return v;
        // Inflate the layout for this fragment
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
class optionProfile implements View.OnClickListener {
    private Profile profile;
    private Context mContext;

    public optionProfile(Context context) {
        mContext = context;
        profile = profile;
    }

    @Override
    public void onClick(View v) {
        // This is an android.support.v7.widget.PopupMenu;
        PopupMenu popupMenu = new PopupMenu(mContext, v) {
//            public boolean onMenuItemSelected(MenuItem item) {
//                switch (item.getItemId()) {
////                    case R.id.album_overflow_delete:
////                        deleteAlbum(mAlbum);
////                        return true;
////
////                    case R.id.album_overflow_rename:
////                        renameAlbum(mAlbum);
////                        return true;
////
////                    case R.id.album_overflow_lock:
////                        lockAlbum(mAlbum);
////                        return true;
////
////                    case R.id.album_overflow_unlock:
////                        unlockAlbum(mAlbum);
////                        return true;
////
////                    case R.id.album_overflow_set_cover:
////                        setAlbumCover(mAlbum);
////                        return true;
//
//                    default:
//                        //return true;
//                        return super.onMenuItemSelected(item);
//                }
//            }
        };

        popupMenu.inflate(R.menu.profile_menu);

//        if (mAlbum.isLocked()) {
//            popupMenu.getMenu().removeItem(R.id.album_overflow_lock);
//            popupMenu.getMenu().removeItem(R.id.album_overflow_rename);
//            popupMenu.getMenu().removeItem(R.id.album_overflow_delete);
//        } else {
//            popupMenu.getMenu().removeItem(R.id.album_overflow_unlock);
//        }

        popupMenu.show();
    }
    private void getUser(long userId) {
        // Load product info
        //String url = String.format(EndPoints.PRODUCTS_SINGLE_RELATED, SettingsMy.getActualNonNullShop(getActivity()).getId(), productId);
        //setContentVisible(CONST.VISIBLE.PROGRESS);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                "", new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray students = response.getJSONArray("students");
                   // for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(Integer.parseInt("users"));

                        String userName = student.getString("userName");
                        String userCity = student.getString("userCity");
                        String userPosition = student.getString("userPosition");
                        String userMatches = student.getString("userMatches");
                        String userPoints = student.getString("userPoints");
                        String userLevel = student.getString("userLevel");



                       // result.append(firstname + " " + lastname + " " + age + " \n");
                   // }
                    //result.append("===\n");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });
        //requestQueue.add(jsonObjectRequest);

    }


}
