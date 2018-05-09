package app.reze.ahmed.reze1.helper.vendor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import app.reze.ahmed.reze1.api.product.ApiResponse;
import app.reze.ahmed.reze1.api.product.ProductResponse;
import app.reze.ahmed.reze1.app.AppConfig;
import app.reze.ahmed.reze1.helper.VolleyCustomRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mona Abdallh on 4/28/2018.
 */

public class ProductFragment extends Fragment {

    private static final String VENDOR_ID_EXTRA = "vendor_id";

    RecyclerView recyclerView;

    RequestQueue requestQueue;
    String vendorId;
    ProductResponse[] products;
    RecyclerView.Adapter adapter;

    public static ProductFragment createFragment(String vendor_id){
        Bundle bundle = new Bundle();
        bundle.putString(VENDOR_ID_EXTRA, vendor_id);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(app.reze.ahmed.reze1.R.layout.fragment_product, container, false);
        recyclerView = v.findViewById(app.reze.ahmed.reze1.R.id.productRecView);

        requestQueue = Volley.newRequestQueue(getActivity());

        if (getArguments() != null){
            if (getArguments().getString(VENDOR_ID_EXTRA) != null) {
                vendorId = getArguments().getString(VENDOR_ID_EXTRA);
                Log.i("vendor_id_arg_string", "onCreate: " + vendorId);
            } else {
                vendorId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                        .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);
                Log.i("vendor_id_arg", "onCreate: " + vendorId);
            }
        } else {
            vendorId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                    .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);
            Log.i("vendor_id_no_arg", "onCreate: " + vendorId);
        }

        fetchProducts();
        return v;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView productImageView;
        TextView productTitleView;
        TextView productDetailView;
        TextView priceView;
        TextView avilView;
        ImageView productContextMenuView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(app.reze.ahmed.reze1.R.id.productImageView);
            productTitleView = itemView.findViewById(app.reze.ahmed.reze1.R.id.productTitleView);
            productDetailView = itemView.findViewById(app.reze.ahmed.reze1.R.id.productDetailView);
            priceView = itemView.findViewById(app.reze.ahmed.reze1.R.id.priceView);
            avilView = itemView.findViewById(app.reze.ahmed.reze1.R.id.avilView);
            productContextMenuView = itemView.findViewById(app.reze.ahmed.reze1.R.id.productContextMenuView);
        }

        public void bind(ProductResponse product){
            productTitleView.setText(product.getTitle());
            productDetailView.setText(product.getDescription());

            String point = getActivity().getResources().getString(app.reze.ahmed.reze1.R.string.pound);
            priceView.setText(String.valueOf(product.getPrice()) + " " + point);

            if (product.getAmount() > product.getSoldAmount()){
                avilView.setText(app.reze.ahmed.reze1.R.string.available);
            } else {
                avilView.setText(app.reze.ahmed.reze1.R.string.unavailable);
            }
        }
    }


    class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductViewHolder>{

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(app.reze.ahmed.reze1.R.layout.product_card, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            holder.bind(products[position]);
        }

        @Override
        public int getItemCount() {
            return products.length;
        }
    }


    private void fetchProducts(){
        //todo
        VolleyCustomRequest stringRequest = new VolleyCustomRequest(Request.Method.POST, "https://rezetopia.com/app/reze/vendor_operation.php",
                ApiResponse.class,
                new Response.Listener<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        Log.i("product_response", "onResponse: " + response.getProducts()[0].getTitle());
                        products = response.getProducts();
                        adapter = new ProductRecyclerAdapter();
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        //updateUi();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley error", "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("method", "get_products");
                map.put("vendor_id", vendorId);

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateUi(){
        if (adapter == null){
            adapter = new ProductRecyclerAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
