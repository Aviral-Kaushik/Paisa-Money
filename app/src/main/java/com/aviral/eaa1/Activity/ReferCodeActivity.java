package com.aviral.eaa1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.Utils.RandomAlphaNumeric;
import com.aviral.eaa1.Utils.TokenPreference;
import com.aviral.eaa1.Utils.UpdateToken;
import com.aviral.eaa1.databinding.ReferCodeDesignBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReferCodeActivity extends AppCompatActivity {
    private ReferCodeDesignBinding binding;
    private LoadingDialog dialog;
    boolean fromGoogle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReferCodeDesignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new LoadingDialog(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        fromGoogle = intent.getBooleanExtra("fromGoogle", false);

        binding.register.setOnClickListener(v -> {
            String code = binding.referrerReferCode.getText().toString();
            if (code.equals("")){
                dialog.show();
                registerWithoutCode(name, email, password);
            }
            else{
                dialog.show();
                AndroidNetworking.post(Links.CHECK_CODE)
                        .addBodyParameter("referral_code", code)
                        .build().getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String message = response.getString("message");
                                    if (message.equals("incorrect")){
                                        dialog.dismiss();
                                        Toast.makeText(ReferCodeActivity.this, "No Referral Code Found", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        dialog.show();
                                        registerWithCode(name, email, password, code);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        });
    }

    private void registerWithCode(String name, String email, String password, String code){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.REGISTER, response16 -> {
            try {
                JSONObject jsonObject = new JSONObject(response16);
                String status = jsonObject.getString("message");
                switch (status) {
                    case "failed, try again":
                        dialog.dismiss();
                        Toast.makeText(ReferCodeActivity.this, "Some error occurred please try again", Toast.LENGTH_SHORT).show();
                        break;
                    case "user registered successfully":
                        String token = TokenPreference.getInstance(getApplicationContext()).getDeviceToken();
                        new UpdateToken(getApplicationContext()).updateToken(email, token);
                        dialog.dismiss();
                        SharedPreferences sharedPreferences = ReferCodeActivity.this.getSharedPreferences("Check", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", email);
                        editor.apply();
                        getUserDetails();
                        break;
                    case "Email already exists":
                        dialog.dismiss();
                        token = TokenPreference.getInstance(getApplicationContext()).getDeviceToken();
                        new UpdateToken(getApplicationContext()).updateToken(email, token);
                        SharedPreferences sharedPreferences2 = ReferCodeActivity.this.getSharedPreferences("Check", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putBoolean("isLoggedIn", true);
                        editor2.putString("email", email);
                        editor2.apply();
                        getUserDetails();
                        break;
                    default:
                        Toast.makeText(ReferCodeActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                }

            } catch (JSONException e) {
                dialog.dismiss();
                e.printStackTrace();
            }
        }, error -> Toast.makeText(ReferCodeActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String date = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss", Locale.US);
                String time = currentTime.format(calendar.getTime());
                String uid = new RandomAlphaNumeric().generateAlphaNumeric(21);
                String coin = String.valueOf(0);
                String zero = String.valueOf(0);
                String model = Build.MODEL;
                String brand = Build.BRAND;
                String token = TokenPreference.getInstance(ReferCodeActivity.this).getDeviceToken();
                String referralCode = new RandomAlphaNumeric().generateAlphaNumeric(6).toUpperCase();
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("coins", coin);
                map.put("uid", uid);
                map.put("lifeTimeEarning", coin);
                map.put("loginCount", zero);
                map.put("disabled", zero);
                map.put("referred", zero);
                map.put("date", date);
                map.put("time",time);
                map.put("referred_by", code);
                map.put("token", token);
                if (fromGoogle){
                    map.put("password", name+email);
                }
                else{
                    map.put("password", password);
                }
                map.put("referral_code", referralCode);
                map.put("model", model);
                map.put("brand", brand);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ReferCodeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void registerWithoutCode(String name, String email, String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.REGISTER, response16 -> {
            try {
                JSONObject jsonObject = new JSONObject(response16);
                String status = jsonObject.getString("message");
                switch (status) {
                    case "failed, try again":
                        dialog.dismiss();
                        Toast.makeText(ReferCodeActivity.this, "Some error occurred please try again", Toast.LENGTH_SHORT).show();
                        break;
                    case "registered":
                        String token = TokenPreference.getInstance(getApplicationContext()).getDeviceToken();
                        new UpdateToken(getApplicationContext()).updateToken(email, token);
                        dialog.dismiss();
                        SharedPreferences sharedPreferences = ReferCodeActivity.this.getSharedPreferences("Check", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", email);
                        editor.apply();
                        getUserDetails();
                        break;
                    case "Email already exists":
                        dialog.dismiss();
                        token = TokenPreference.getInstance(getApplicationContext()).getDeviceToken();
                        new UpdateToken(getApplicationContext()).updateToken(email, token);
                        SharedPreferences sharedPreferences2 = ReferCodeActivity.this.getSharedPreferences("Check", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putBoolean("isLoggedIn", true);
                        editor2.putString("email", email);
                        editor2.apply();
                        getUserDetails();
                        break;
                    default:
                        Toast.makeText(ReferCodeActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                }

            } catch (JSONException e) {
                dialog.dismiss();
                e.printStackTrace();
            }
        }, error -> Toast.makeText(ReferCodeActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String date = currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss", Locale.US);
                String time = currentTime.format(calendar.getTime());
                String uid = new RandomAlphaNumeric().generateAlphaNumeric(21);
                String coin = String.valueOf(0);
                String zero = String.valueOf(0);
                String model = Build.MODEL;
                String brand = Build.BRAND;
                String token = TokenPreference.getInstance(ReferCodeActivity.this).getDeviceToken();
                String referralCode = new RandomAlphaNumeric().generateAlphaNumeric(6).toUpperCase();
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("coins", coin);
                map.put("uid", uid);
                map.put("lifeTimeEarning", coin);
                map.put("loginCount", zero);
                map.put("disabled", zero);
                map.put("referred", zero);
                map.put("date", date);
                map.put("time",time);
                map.put("referred_by", "-");
                map.put("token", token);
                if (fromGoogle){
                    map.put("password", name+email);
                }
                else{
                    map.put("password", password);
                }
                map.put("referral_code", referralCode);
                map.put("model", model);
                map.put("brand", brand);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ReferCodeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private void getUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("Check", Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("email", null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.FETCH_DATA, response -> {
            try {
                JSONObject model = new JSONObject(response);
                String name = model.getString("name");
                String email = model.getString("email");
                String uid = model.getString("uid");
                int disabled = model.getInt("disabled");
                int referred = model.getInt("referred");
                String date = model.getString("date");
                String time = model.getString("time");
                String referredBy = model.getString("referred_by");
                String referralCode = model.getString("referral_code");
                float refer_earning = (float) model.getDouble("refer_earning");
                float lifetime = (float) model.getDouble("lifetime");
                String is_rewarded = model.getString("is_rewarded");
                SharedPreferences sharedPreferences1 = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("uid", uid);
                editor.putInt("disabled", disabled);
                editor.putInt("referred", referred);
                editor.putString("date", date);
                editor.putString("time", time);
                editor.putString("referred_by", referredBy);
                editor.putString("token", "-");
                editor.putString("referral_code", referralCode);
                editor.putFloat("refer_earning", (float) refer_earning);
                editor.putFloat("lifetime", (float) lifetime);
                editor.putString("is_rewarded", is_rewarded);
                editor.apply();
                startActivity(new Intent(ReferCodeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", currentUserId);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }
}
