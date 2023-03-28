package com.example.baocaoapi.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baocaoapi.R;
import com.example.baocaoapi.activity.SanPhamActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {
    private View mView;
    private FirebaseAuth auth;
    private TextInputEditText edtUsername, edtPassword, edtRePassword;
    private AppCompatButton btnSignUp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initView();
        return mView;
    }
    private void initView() {
        auth = FirebaseAuth.getInstance();
        edtUsername = mView.findViewById(R.id.edtUsername);
        edtPassword = mView.findViewById(R.id.edtPassword);
        edtRePassword = mView.findViewById(R.id.edtRePassword);
        btnSignUp = mView.findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String repassword = edtRePassword.getText().toString().trim();
                if(user.isEmpty()){
                    edtUsername.setError("Email can't be empty");
                    return;
                }
                if(password.isEmpty() || password.length() < 6) {
                    edtPassword.setError("Password can't be empty");
                    return;
                }
                if(repassword.isEmpty() || repassword.length() < 6) {
                    edtRePassword.setError("Re-enter your password");
                    return;
                }
                if(!password.contentEquals(repassword)) {
                    edtRePassword.setError("Re-enter your password");
                } else {
                    auth.createUserWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Signup successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), SanPhamActivity.class));
                            } else {
                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}