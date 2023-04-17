package com.aviral.eaa1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.Utils.TokenPreference;
import com.aviral.eaa1.Utils.UpdateToken;
import com.aviral.eaa1.databinding.LoginActivityBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    LoadingDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivityBinding binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new LoadingDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.OAuthClientId))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.signIn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginUser(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginUser(GoogleSignInAccount account) {
        String ce = account.getEmail();
        StringRequest check = new StringRequest(Request.Method.POST, Links.CHECK_EMAIL, response -> {
            try {

                JSONObject jsonObjectC = new JSONObject(response);
                String mes = jsonObjectC.getString("message");

                if (mes.equals("Registered")){

                    String token = TokenPreference.getInstance(getApplicationContext()).getDeviceToken();
                    new UpdateToken(getApplicationContext()).updateToken(account.getEmail(), token);
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("Check", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("email", account.getEmail());
                    editor.apply();
                    getUserDetails();

                }
                else if (mes.equals("Email Not Registered")){

                    dialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, ReferCodeActivity.class)
                            .putExtra("name", account.getDisplayName())
                            .putExtra("email", account.getEmail())
                            .putExtra("password", "")
                            .putExtra("fromGoogle", true));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(LoginActivity.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", ce);
                return map;
            }
        };
        RequestQueue cq = Volley.newRequestQueue(LoginActivity.this);
        cq.add(check);
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

                startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

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
