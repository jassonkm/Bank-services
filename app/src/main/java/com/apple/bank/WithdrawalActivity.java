package com.apple.bank;

import androidx.appcompat.app.AppCompatActivity;

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
import com.apple.bank.databinding.ActivityWithdrawalBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WithdrawalActivity extends AppCompatActivity {
    ActivityWithdrawalBinding binding;
    RequestQueue request;
    private String amount;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWithdrawalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        request = Volley.newRequestQueue(getBaseContext());
        codeRetirement();
        data();
    }
    private void data(){
        binding.btnRetire.setOnClickListener(view -> {
            amount=binding.editTextNumber.getText().toString();
            retirement();
        });
    }
    private void codeRetirement(){
        HashMap<String, String> paramsAuth= new HashMap<>();
        paramsAuth.put("numberBill",Users.billNumber);
        try {
            request = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.51.1.40:8080/transaction/codeRetirement",
                    new JSONObject(paramsAuth),
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            Log.e("Response", "onResponse: "+response.toString());
                            if (response.optBoolean("status")){
                                Log.e("Response", "if: "+response.toString());
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
                                JSONObject data=response.optJSONObject("data");
                                code=data.optString("code");
                                binding.txtCode.setText(code);
                                binding.textNcuenta.setText(Users.billNumber);
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
    private void retirement() {
        Log.e("code", "codigo:  "+code );
        HashMap<String, String> paramsAuth= new HashMap<>();
        paramsAuth.put("codeAut",code);
        paramsAuth.put("numberBill",Users.billNumber);
        paramsAuth.put("typeTransation","RETIRO");
        paramsAuth.put("amount",amount);
        try {
            request = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    "http://10.51.1.40:8080/transaction/retirement",
                    new JSONObject(paramsAuth),
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            Log.e("Response", "onResponse: "+response.toString());
                            if (response.optBoolean("status")){
                                Log.e("Response", "if: "+response.toString());
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
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