package com.aviral.eaa1.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class ApiBackendProvider {

    private static String TAG = "AviralAPI";

    private Context context;

    public ApiBackendProvider(Context context) {
        this.context = context;
    }


    public boolean checkForEmailRegistration(String email) {

        AtomicBoolean isEmailRegistered = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.CHECK_EMAIL,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        isEmailRegistered.set(jsonObject.getString("message").equals("Registered"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

                    isEmailRegistered.set(false);
                    Log.d(TAG, error.toString());
            });

        queue.add(request);

        return isEmailRegistered.get();
    }

    public boolean registerUser(RegisterUser user) {

        AtomicBoolean isRegisterUserRequestSuccessful = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", user.getName());
            postData.put("email", user.getEmail());
            postData.put("uid", user.getUid());
            postData.put("token", user.getToken());
            postData.put("password", user.getPassword());
            postData.put("referral_code", user.getReferralCode());
            postData.put("referred_by", user.getReferredBy());
            postData.put("model", user.getModel());
            postData.put("brand", user.getBrand());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.REGISTER_USER,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        isRegisterUserRequestSuccessful.set(jsonObject.getString("message")
                                .equals("“user registered successfully"));

                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

                    isRegisterUserRequestSuccessful.set(false);
                    Log.d(TAG, error.toString());
        });

        queue.add(request);

        return isRegisterUserRequestSuccessful.get();

    }

    public boolean checkReferralCodeValidation(String referralCode) {

        AtomicBoolean isReferralCodeValid = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("referral_code", referralCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.CHECK_REFERRAL_CODE,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        isReferralCodeValid.set(jsonObject.getString("message").equals("correct"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

                    isReferralCodeValid.set(false);
                    Log.d(TAG, error.toString());
        });

        queue.add(request);

        return isReferralCodeValid.get();

    }

    public boolean userLogin(String email, String password, String token) {

        AtomicBoolean isUserLoginRequestSuccessFul = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("Password", password);
            postData.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.LOGIN,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        isUserLoginRequestSuccessFul.set(jsonObject.getString("message").equals("success"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

                    isUserLoginRequestSuccessFul.set(false);
                    Log.d(TAG, error.toString());
            });

            queue.add(request);

            return isUserLoginRequestSuccessFul.get();

    }

    public UserData fetchUserData(String email) {
        UserData userData = new UserData();

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.FETCH_USER_DATA,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        userData.setName(jsonObject.getString("name"));
                        userData.setEmail(jsonObject.getString("email"));
                        userData.setUid(jsonObject.getString("uid"));
                        userData.setDisabled(jsonObject.getString("disabled"));
                        userData.setReferred(jsonObject.getString("referred"));
                        userData.setDate(jsonObject.getString("date"));
                        userData.setTime(jsonObject.getString("time"));
                        userData.setReferredBy(jsonObject.getString("referred_by"));
                        userData.setReferralCode(jsonObject.getString("referral_code"));
                        userData.setBalance(jsonObject.getString("balance"));
                        userData.setReferralEarning(jsonObject.getString("refer_earning"));
                        userData.setLifetime(jsonObject.getString("lifetime"));
                        userData.setIsRewarded(jsonObject.getString("is_rewarded"));


                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
        });

        queue.add(request);

        return userData;
    }

    private boolean resetPassword(String email, String password) {

        AtomicBoolean isResetPasswordRequestSuccessful = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.RESET_PASSWORD,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        isResetPasswordRequestSuccessful.set(jsonObject.getString("message").equals("success"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

                    isResetPasswordRequestSuccessful.set(false);
                    Log.d(TAG, error.toString());
        });

        queue.add(request);

        return isResetPasswordRequestSuccessful.get();

    }

    private boolean updateUserBalance(String userId, String updateBalance) {

        AtomicBoolean isUpdateUserBalanceRequestSuccessful = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("user_id", userId);
            postData.put("value", updateBalance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.UPDATE_USER_BALANCE,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        isUpdateUserBalanceRequestSuccessful.set(jsonObject.getString("status").equals("updated"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

                    isUpdateUserBalanceRequestSuccessful.set(false);
                    Log.d(TAG, error.toString());
        });

        queue.add(request);

        return isUpdateUserBalanceRequestSuccessful.get();

    }

    public boolean makeWithdrawRequest(WithdrawRequest withdrawRequest) {
        AtomicBoolean isWithdrawRequestSuccessful = new AtomicBoolean(false);

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("user_id", withdrawRequest.getUserId());
            postData.put("name", withdrawRequest.getName());
            postData.put("number", withdrawRequest.getNumber());
            postData.put("id", withdrawRequest.getId());
            postData.put("amount", withdrawRequest.getAmount());
            postData.put("bank_name", withdrawRequest.getBankName());
            postData.put("method", withdrawRequest.getMethod());
            postData.put("unique_id", withdrawRequest.getUniqueID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.WITHDRAW_REQUEST,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        isWithdrawRequestSuccessful.set(jsonObject.getString("message").equals("added"));
                    } catch (JSONException e) {
                        Log.d(TAG, "checkForEmailRegistration: Exception Occurred while api call " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

            isWithdrawRequestSuccessful.set(false);
            Log.d(TAG, error.toString());
        });

        queue.add(request);

        return isWithdrawRequestSuccessful.get();
    }

    private ArrayList<Friend> getReferredFriendList(String referralCode) {

        ArrayList<Friend> referredFriendArrayList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("referral_code", referralCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                ApiConstants.BASE_URL + ApiConstants.LIST_REFER_FRIENDS,
                postData,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

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
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> {

            Log.d(TAG, error.toString());
        });

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
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, response.toString());
                }, error -> Log.d(TAG, error.toString()));

        queue.add(request);

        return links;

    }

}
