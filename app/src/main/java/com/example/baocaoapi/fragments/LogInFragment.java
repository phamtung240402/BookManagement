package com.example.baocaoapi.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baocaoapi.R;
import com.example.baocaoapi.activity.SanPhamActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInFragment extends Fragment {
    private View mView;
    private FirebaseAuth auth;
    private TextInputEditText edtUsername, edtPassword;
    private AppCompatButton btnSignIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_log_in, container, false);
        initView();
        return mView;
    }
    private void initView() {
        auth = FirebaseAuth.getInstance();
        edtUsername = mView.findViewById(R.id.edtUsername);
        edtPassword = mView.findViewById(R.id.edtPassword);
        btnSignIn = mView.findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if(!user.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                    if(!password.isEmpty() && password.length() > 6) {
                        auth.signInWithEmailAndPassword(user, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(getActivity(), "Login successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), SanPhamActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        edtPassword.setError("Your password is unvalid");
                    }
                } else if(user.isEmpty()) {
                    edtUsername.setError("Your username is unvalid");
                } else {
                    edtUsername.setError("Re enter your username");
                }
            }
        });
    }
}