package com.apple.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apple.bank.databinding.ActivitySignUpBinding;
import com.apple.bank.databinding.ActivityTransferBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity  {
    private ActivitySignUpBinding binding;
    private String mail;
    private String pass;
    private String name;
    private String id;
    RequestQueue request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        request = Volley.newRequestQueue(getBaseContext());
        dataA();

    }

    private void dataA() {
        binding.btnSign.setOnClickListener(view -> {
            mail = binding.editTextTextEmailAddress.getText().toString();
            pass = binding.editTextTextPassword.getText().toString();
            name = binding.editname.getText().toString();
            id = binding.editId.getText().toString();
            Pattern pattern=Pattern
                    .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    +"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher =pattern.matcher(mail);
            if (name.equals("") || mail.equals("") || pass.equals("")){
                Toast.makeText(this, "Por favor ingresar datos", Toast.LENGTH_SHORT).show();
                binding.editname.requestFocus();
            }
            else if(pass.length()<8){
                Toast.makeText(this, "Por favor ingrese una contraseña mayor a 10 caracteres", Toast.LENGTH_SHORT).show();
                binding.editTextTextPassword.requestFocus();
            }
            else if (!matcher.find()) {
                Toast.makeText(this, "Ingrese un correo valido, vuelva a intentar", Toast.LENGTH_SHORT).show();
                binding.editTextTextEmailAddress.requestFocus();
            }
            else {
                authService();

            }
        });
    }

    private void authService() {
        HashMap<String, String> paramsAuth= new HashMap<>();
        paramsAuth.put("user_name", name);
        paramsAuth.put("user_identification",id);
        paramsAuth.put("user_email",mail);
        paramsAuth.put("user_password",pass);
        paramsAuth.put("user_estate","ACTIVO");
        try {
            request = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.51.1.40:8080/user/register",
                    new JSONObject(paramsAuth),
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            Log.e("Response", "onResponse: "+response.toString());
                            if (response.optBoolean("status")){
                                Log.e("Response", "if: "+response.toString());
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
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
                    }) {
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