package com.apple.bank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apple.bank.databinding.ActivityTransferBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {
    ActivityTransferBinding binding;
    private String numberBillDes;
    private String amount;
    public String codeQr;
    private String numberBill;
    private String base64Img;
    private String token;
    RequestQueue request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTransferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        request = Volley.newRequestQueue(getBaseContext());
        viewQR();
        viewscann();
        data();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "reading canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                codeQr = String.valueOf(result.getContents());
                binding.editTextNumberBill.setText(codeQr);
                Log.d("prueba1", "captureInfo: " + codeQr);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void data(){
        binding.btnTransfer.setOnClickListener(view -> {
            numberBillDes=binding.editTextNumberBill.getText().toString();
            amount=binding.editTextAmount.getText().toString();
            numberBill=Users.billNumber;
            token=Users.token;
            operationTransfer();
        });
    }

    private void viewscann() {
        binding.btnCode.setOnClickListener(view -> {
            IntentIntegrator integrator= new IntentIntegrator(TransferActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Lector - QR");
            integrator.setCameraId(0);
            integrator.setOrientationLocked(false);
            integrator.setBeepEnabled(true);
            integrator.setCaptureActivity(CaptureActivityPortrait.class);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });
    }
    private void operationTransfer(){
        HashMap<String, String> paramsAuth= new HashMap<>();
        paramsAuth.put("numberBill",numberBill);
        paramsAuth.put("typeTransation","TRANSFERENCIA");
        paramsAuth.put("amount",amount);
        paramsAuth.put("numberBillDestiny",numberBillDes);
        Log.e("json",paramsAuth.toString());
        try {
            request = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    "http://10.51.1.40:8080/transaction/transfer",
                    new JSONObject(paramsAuth),
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            Log.e("Response", "onResponse: "+response.toString());
                            if (response.optBoolean("status")){
                                Log.e("Response", "if: "+response.toString());
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
                                //operationsNext();
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
                    params.put("Authorization",token);
                    Log.e("Authorization",params.toString());
                    return params;
                }
            };
            request.add(arrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void viewQR(){
        HashMap<String, String> paramsAuth= new HashMap<>();
        paramsAuth.put("numberBill",Users.billNumber);
        try {
            request = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.51.1.40:8080/QR//createCommonQRCode",
                    new JSONObject(paramsAuth),
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            Log.e("Response", "onResponse: "+response.toString());
                            if (response.optBoolean("status")){
                                Log.e("Response", "if: "+response.toString());
                                Toast.makeText(getApplicationContext(),response.optString("msg"),Toast.LENGTH_LONG).show();
                                //operationsNext();
                                base64Img=response.optString("img");
                                Log.e( "base64: ",""+base64Img );
                                byte[] bytes= Base64.decode(base64Img,Base64.DEFAULT);
                                Log.e("bytes", ": "+bytes.toString());
                                Bitmap byteQr= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                Log.e("bitmap", ": "+byteQr.toString());
                                binding.codQR.setImageBitmap(byteQr);
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