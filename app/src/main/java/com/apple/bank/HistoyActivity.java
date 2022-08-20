package com.apple.bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apple.bank.databinding.ActivityHistoyBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoyActivity extends AppCompatActivity {
    ActivityHistoyBinding binding;
    private List<DataTransf>list;
    private RecyclerView.Adapter adapter;
    RequestQueue request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHistoyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        request = Volley.newRequestQueue(getBaseContext());
        list=new ArrayList<>();
        viewHistoy();
    }
    private void setAdapter(){
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this.getBaseContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter=new HistoryAdapter(list);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
    }
    private void viewHistoy(){
        HashMap<String, String> paramsAuth= new HashMap<>();
        paramsAuth.put("numberBill",Users.billNumber);
        try {
            request = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.51.1.40:8080/transaction/getTransfers",
                    new JSONObject(paramsAuth),
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            Log.e("Response", "onResponse: "+response.toString());
                            if (response.optBoolean("status")){
                                Log.e("Response", "if: "+response.toString());
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
                                JSONArray dataArray=response.optJSONArray("data");
                                Log.e("Response", "rry: "+dataArray.toString());
                                for (int i = 0; i <dataArray.length() ; i++) {
                                    try {
                                        DataTransf transf =new DataTransf();
                                        transf.setType(dataArray.getJSONObject(i).optString("transaction_type"));
                                        transf.setDescription(dataArray.getJSONObject(i).optString("transaction_description"));
                                        transf.setAmount(dataArray.getJSONObject(i).optString("transaction_amount"));
                                        transf.setDate(dataArray.getJSONObject(i).optString("transaction_date"));
                                        transf.setEstate(dataArray.getJSONObject(i).optBoolean("transaction_estate"));
                                        Log.e("for",dataArray.getJSONObject(i).optString("transaction_id"));
                                        list.add(transf);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                                setAdapter();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "onErrorResponse: "+error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String>params= new HashMap<>();
                    params.put("Authorization",Users.token);
                    Log.e("Authorization",params.toString());
                    return params;
                }
            };
            request.add(arrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}