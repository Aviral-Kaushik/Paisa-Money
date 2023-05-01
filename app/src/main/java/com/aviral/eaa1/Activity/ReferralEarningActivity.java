package com.aviral.eaa1.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviral.eaa1.Adapter.ReferralEarningAdapter;
import com.aviral.eaa1.Models.Friend;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiConstants;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.ActivityReferralEarningBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReferralEarningActivity extends AppCompatActivity {

    private static final String TAG = "AviralAPI";

    private ActivityReferralEarningBinding binding;

    private LoadingDialog loadingDialog;

    private Context context;

    private ArrayList<Friend> friendArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferralEarningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ReferralEarningActivity.this;

        friendArrayList = new ArrayList<>();

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();


        binding.totalEarning.setText(String.format("₹%s", getSharedPreferences("user", Context.MODE_PRIVATE).getFloat("refer_earning", 0)));
        binding.userName.setText(getSharedPreferences("user", Context.MODE_PRIVATE).getString("name", ""));
        binding.tvUserName.setText(getSharedPreferences("user", Context.MODE_PRIVATE).getString("name", ""));

        getReferredFriendList(getSharedPreferences("user", Context.MODE_PRIVATE).getString("referral_code", ""));

    }

    private void getReferredFriendList(String referralCode) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d(TAG, "getReferredFriendList: Referral code in api backend " + referralCode);
        StringRequest request = new StringRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.LIST_REFER_FRIENDS,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("message").equals("Found")) {

                            Log.d(TAG, "getReferredFriendList: into if statement");

                            JSONArray friendJSONArray = jsonObject.getJSONArray("Friends");

                            for(int i = 0; i < friendJSONArray.length(); i++) {

                                Log.d(TAG, "getReferredFriendList: into for loop");

                                Gson gson = new Gson();

                                friendArrayList.add(
                                        gson.fromJson(friendJSONArray.get(i).toString()
                                                , Friend.class)
                                );

                                Log.d(TAG, "getReferredFriendList: Adding friends in array list");

                            }
                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                    }

                    Log.d(TAG, response);

                    loadingDialog.dismiss();

                    if (friendArrayList.size() > 0) {

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL, false);

                        binding.referralEarningRecyclerView.setLayoutManager(linearLayoutManager);

                        ReferralEarningAdapter referralEarningAdapter =
                                new ReferralEarningAdapter(friendArrayList);

                        binding.referralEarningRecyclerView.setAdapter(referralEarningAdapter);

                        loadingDialog.dismiss();

                        binding.friendsJoined.setText(String.valueOf(friendArrayList.size()));

                    }

                }, error -> Log.d(TAG, error.toString())) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
            }

            @Override
            protected String getParamsEncoding() {
                return "UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> postData = new HashMap<>();
                postData.put("referral_code", referralCode);
                return postData;
            }
        };

        queue.add(request);
    }

}