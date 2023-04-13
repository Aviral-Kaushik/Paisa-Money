package com.aviral.eaa1.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aviral.eaa1.Models.Friend;
import com.aviral.eaa1.Models.RegisterUser;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.Models.WithdrawRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiBackendProvider {

    private static final String TAG = "AviralAPI";

    private final Context context;

    public ApiBackendProvider(Context context) {
        this.context = context;
    }

    public UserData fetchUserData(String email) {
        UserData userData = new UserData();

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.FETCH_USER_DATA,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        userData.setName(jsonObject.getString("name"));
                        userData.setEmail(jsonObject.getString("email"));
                        userData.setUid(jsonObject.getString("uid"));
                        userData.setDisabled(Integer.parseInt(jsonObject.getString("disabled")));
                        userData.setReferred(Integer.parseInt(jsonObject.getString("referred")));
                        userData.setDate(jsonObject.getString("date"));
                        userData.setTime(jsonObject.getString("time"));
                        userData.setReferredBy(jsonObject.getString("referred_by"));
                        userData.setReferralCode(jsonObject.getString("referral_code"));
                        userData.setBalance(jsonObject.getString("balance"));
                        userData.setReferralEarning(Float.parseFloat(jsonObject.getString("refer_earning")));
                        userData.setLifetime(Float.parseFloat(jsonObject.getString("lifetime")));
                        userData.setIsRewarded(jsonObject.getString("is_rewarded"));


                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                    }

                    Log.d(TAG, response);
                }, error -> {

            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            Log.d(TAG, error.toString());
        }) {

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
                postData.put("email", email);
                return postData;
            }
        };

        queue.add(request);

        return userData;
    }

    public boolean updateUserBalance(String userId, String updateBalance) {

        AtomicBoolean isUpdateUserBalanceRequestSuccessful = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.UPDATE_USER_BALANCE,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        isUpdateUserBalanceRequestSuccessful.set(jsonObject.getString("status").equals("updated"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                    }

                    Log.d(TAG, response);
                }, error -> {

            isUpdateUserBalanceRequestSuccessful.set(false);
            Log.d(TAG, error.toString());
        }) {

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
                postData.put("user_id", userId);
                postData.put("value", updateBalance);
                return postData;
            }
        };

        queue.add(request);

        return isUpdateUserBalanceRequestSuccessful.get();

    }

    public boolean makeWithdrawRequest(WithdrawRequest withdrawRequest) {
        AtomicBoolean isWithdrawRequestSuccessful = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.WITHDRAW_REQUEST,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        isWithdrawRequestSuccessful.set(jsonObject.getString("message").equals("added"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                    }

                    Log.d(TAG, response);
                }, error -> {

            isWithdrawRequestSuccessful.set(false);
            Log.d(TAG, error.toString());
        }) {

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
                postData.put("user_id", withdrawRequest.getUserId());
                postData.put("name", withdrawRequest.getName());
                postData.put("number", withdrawRequest.getNumber());
                postData.put("id", withdrawRequest.getId());
                postData.put("amount", String.valueOf(withdrawRequest.getAmount()));
                postData.put("bank_name", withdrawRequest.getBankName());
                postData.put("method", withdrawRequest.getMethod());
                postData.put("unique_id", withdrawRequest.getUniqueID());
                return postData;
            }
        };

        queue.add(request);

        return isWithdrawRequestSuccessful.get();
    }

    private ArrayList<Friend> getReferredFriendList(String referralCode) {

        ArrayList<Friend> referredFriendArrayList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.LIST_REFER_FRIENDS,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("message").equals("Found")) {

                            JSONArray friendJSONArray = jsonObject.getJSONArray("Friends");

                            for(int i = 0; i < friendJSONArray.length(); i++) {

                                Gson gson = new Gson();

                                referredFriendArrayList.add(
                                        gson.fromJson(friendJSONArray.get(i).toString()
                                                , Friend.class)
                                );

                            }

                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                    }

                    Log.d(TAG, response);
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

        return referredFriendArrayList;
    }

    public ArrayList<String> getAllLinks() {

        ArrayList<String> links = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                ApiConstants.BASE_URL + ApiConstants.GET_LINKS,
                null,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        for (int i = 1; i <= 16; i++) {
                            links.add(jsonObject.getString(String.valueOf(i)));
                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                    }

                    Log.d(TAG, response.toString());
                }, error -> Log.d(TAG, error.toString()));

        queue.add(request);

        return links;

    }

}
