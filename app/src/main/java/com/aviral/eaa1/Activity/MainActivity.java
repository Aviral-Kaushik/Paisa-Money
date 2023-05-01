package com.aviral.eaa1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.aviral.eaa1.Fragments.CardFragment;
import com.aviral.eaa1.Fragments.EarnMoneyFragment;
import com.aviral.eaa1.Fragments.ProfileFragment;
import com.aviral.eaa1.Fragments.ScratchCardFragment;
import com.aviral.eaa1.Fragments.SpinFragment;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.ActivityMainBinding;
import com.ayetstudios.publishersdk.AyetSdk;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {



    private final EarnMoneyFragment earnMoneyFragment = new EarnMoneyFragment(this);
    public ProfileFragment profileFragment = new ProfileFragment(this);
    private final SpinFragment spinFragment = new SpinFragment(this);
    private final CardFragment cardFragment = new CardFragment(this);
    public final FragmentManager fm = getSupportFragmentManager();
    public Fragment active_fragment = earnMoneyFragment;
    public int offer_complete;


    String spin;
    String scratch;

    public String getAyet() {
        return ayet;
    }

    public void setAyet(String ayet) {
        this.ayet = ayet;
    }

    String ayet;

    double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getSpin() {
        return spin;
    }

    public void setSpin(String spin) {
        this.spin = spin;
    }

    public String getScratch() {
        return scratch;
    }

    public void setScratch(String scratch) {
        this.scratch = scratch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("Check", Context.MODE_PRIVATE);
        boolean isL = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isL) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            AyetSdk.init(getApplication());
            getApiKeys();
            fm.beginTransaction().add(R.id.frame_container, profileFragment, "profileFragment").hide(profileFragment).commit();
            fm.beginTransaction().add(R.id.frame_container, cardFragment, "cardFragment").hide(cardFragment).commit();
            fm.beginTransaction().add(R.id.frame_container, spinFragment, "spinFragment").hide(spinFragment).commit();
            fm.beginTransaction().add(R.id.frame_container, earnMoneyFragment, "earnMoneyFragment").show(earnMoneyFragment).commit();
            binding.bottomNav.setBackground(null);
            binding.bottomNav.setItemIconTintList(null);
            binding.bottomNav.setOnItemSelectedListener(this);
            AndroidNetworking.get(Links.GET_HIDES)
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                setSpin(response.getString("spin"));
                                setScratch(response.getString("scratch"));
                                setAyet(response.getString("ayet"));
                                getUserDetails();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu1){
            fm.beginTransaction().hide(active_fragment).show(earnMoneyFragment).commit();
            active_fragment = earnMoneyFragment;
        }
        else if (item.getItemId() == R.id.menu2){
            if (getSpin().equals("no")){
                fm.beginTransaction().hide(active_fragment).show(spinFragment).commit();
                active_fragment = spinFragment;
            }
            else{
                Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
            }
        }
        else if (item.getItemId() == R.id.menu3){
            if (getSpin().equals("no")){
                fm.beginTransaction().hide(active_fragment).show(cardFragment).commit();
                active_fragment = cardFragment;
            }
            else{
                Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
            }
        }
        else if (item.getItemId() == R.id.menu4){
            if (getAyet().equals("no")){
                Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
            }
            else{
                if (AyetSdk.isInitialized()){
                    AyetSdk.showOfferwall(getApplication(), "PaisaMoney");
                }
                else{
                    Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (item.getItemId() == R.id.menu5){
            fm.beginTransaction().hide(active_fragment).show(profileFragment).commit();
            active_fragment = profileFragment;
        }
        return true;
    }



    private void getUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("Check", Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("email", null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Links.FETCH_DATA, response -> {
            try {
                Log.d("response", response.toString());
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
                get_user_balance();
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

    public void get_user_balance(){
        String uid = getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", "");
        AndroidNetworking.post(Links.GET_USER_COINS)
                .addBodyParameter("user_id", uid)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float balance = (float) response.getDouble("balance");
                            setBalance(balance);
                            String balance_text = "₹"+String.format(Locale.US, "%.2f", balance);
                            earnMoneyFragment.binding.walletBalance.setText(balance_text);
                            earnMoneyFragment.binding.btnBalance.setText(balance_text);
                            profileFragment.binding.walletBalance.setText(balance_text);
                            profileFragment.binding.btnBalance.setText(balance_text);
                            spinFragment.binding.balance.setText(balance_text);
                            cardFragment.binding.balance.setText(balance_text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    private void getApiKeys(){
        AndroidNetworking.post(Links.GET_API_KEYS)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SharedPreferences.Editor editor = getSharedPreferences("api_keys", Context.MODE_PRIVATE).edit();
                            String ayet = response.getString("ayet");
                            editor.putString("ayet", ayet);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}