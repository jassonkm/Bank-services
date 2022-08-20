package com.apple.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apple.bank.databinding.ActivityLoginBinding;
import com.apple.bank.databinding.ActivityMainBinding;
import com.apple.bank.databinding.ActivityOperationsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private String mail;
    private String pass;
    RequestQueue request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        request = Volley.newRequestQueue(getBaseContext());
        operations();
    }

    private void operations() {
        binding.btnLogin.setOnClickListener(view -> {
            mail=binding.editTextTextEmailAddress2.getText().toString();
            pass=binding.editTextTextPassword2.getText().toString();
            if (mail.equals("") && pass.equals("")) {
                Toast.makeText(this, "Por favor llenar datos", Toast.LENGTH_SHORT).show();
                binding.editTextTextEmailAddress2.requestFocus();
            }else
            {
                authService();
            }

        });
    }

    private void authService(){
        HashMap<String, String> paramsAuth= new HashMap<>();
        paramsAuth.put("user_email",mail);
        paramsAuth.put("user_password",pass);
        try {
            request = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.51.1.40:8080/user/login",
                    new JSONObject(paramsAuth),
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            Log.e("Response", "onResponse: "+response.toString());
                            if (response.optBoolean("status")){
                                Log.e("Response", "if: "+response.toString());
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
                                Users.token=response.optString("token");
                                JSONObject data = response.optJSONObject("data");
                                Users.id=data.optString("user_id");
                                Users.name=data.optString("user_name");
                                Users.identification=data.optString("user_identification");
                                Users.email=(data.optString("user_email"));
                                JSONObject bill = data.optJSONObject("bill");
                                Users.billAmount=(bill.optInt("bill_amount"));
                                String pasar=String.valueOf(bill.optLong("bill_number"));
                                Users.billNumber=(pasar);
                                Log.e("onResponse: ","valores"+Users.name+Users.billNumber+Users.token);
                                operationsNext();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "onErrorResponse: "+error.getMessage());
                        }
                    }

                    ) {
            };
            request.add(arrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void operationsNext(){
        Intent intent=new Intent(this,OperationsActivity.class);
        startActivity(intent);
    }

}