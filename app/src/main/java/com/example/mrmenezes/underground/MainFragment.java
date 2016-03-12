package com.example.mrmenezes.underground;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by Eric on 12/03/2016.
 */
public class MainFragment extends Fragment {
    private TextView userText;
    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {



        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("Mr", "onSuccess");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile !=null){
                Log.d("Mr", "Logado " + profile.getName());
                userText.setText(profile.getName());

            }else{
                verifyProfile();

            }

        }

        @Override
        public void onCancel() {
            Log.d("Mr", "onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("Mr", "onError" + error);
        }
    };

    private void verifyProfile() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                mProfileTracker.startTracking();
                // profile2 is the new profile
                Log.d("Mr", "Logado " + profile2.getName());
                userText.setText(profile2.getName());
                mProfileTracker.stopTracking();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        userText  = (TextView) view.findViewById(R.id.textUser);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, mCallBack);
        verifyProfile();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
