package com.example.baocaoapi.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.baocaoapi.R;
import com.example.baocaoapi.activity.DangNhapActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingFragment extends Fragment {
    private View mView;
    private EditText edtUsername;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private AppCompatButton btnLogOut;
    private GoogleSignInClient gsc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        initView();
        return mView;
    }
    private void initView() {
        auth = FirebaseAuth.getInstance();
        edtUsername = mView.findViewById(R.id.edtUsername);
        btnLogOut = mView.findViewById(R.id.btnLogOut);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(getContext(), options);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account != null) {
            edtUsername.setText(account.getEmail());
        }
        user = auth.getCurrentUser();
        if(user != null) {
            edtUsername.setText(user.getEmail());
        }


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account != null) {
                    gsc.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), DangNhapActivity.class));
                        }
                    });
                }
                if(user != null) {
                    auth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
            }
        });
    }
}