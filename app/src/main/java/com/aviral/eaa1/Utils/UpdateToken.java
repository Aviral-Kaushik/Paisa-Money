package com.aviral.eaa1.Utils;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class UpdateToken {
    private final Context context;

    public UpdateToken(Context context) {
        this.context = context;
    }

    public void updateToken(String email, String token){
        AndroidNetworking.post(Links.UPDATE_USER_TOKEN)
                .addBodyParameter("email", email)
                .addBodyParameter("token", token)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
