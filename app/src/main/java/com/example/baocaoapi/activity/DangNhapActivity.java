package com.example.baocaoapi.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.baocaoapi.MainActivity;
import com.example.baocaoapi.R;
import com.example.baocaoapi.fragments.LogInFragment;
import com.example.baocaoapi.fragments.SignUpFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class DangNhapActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppCompatButton btnGoogle;
    private GoogleSignInClient client;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
    }
    private void initView() {
        tabLayout = findViewById(R.id.tab_layout);
        btnGoogle = findViewById(R.id.btnGoogle);
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();
        client = GoogleSignIn.getClient(this, options);

        tabLayout.addTab(tabLayout.newTab().setText("Đăng nhập"));
        tabLayout.addTab(tabLayout.newTab().setText("Đăng kí"));
        replaceFragment(new LogInFragment());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText() == "Đăng nhập") {
                    replaceFragment(new LogInFragment());
                } else {
                    replaceFragment(new SignUpFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i, 123);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123) {
            if(resultCode == RESULT_OK)
            {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//                auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        if(task.isSuccessful()) {
//                          //  startActivity(new Intent(DangNhapActivity.this, SanPhamActivity.class));
//                            Toast.makeText(DangNhapActivity.this, "successfully", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            Toast.makeText(DangNhapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                task.getResult(ApiException.class);
                finish();
                startActivity(new Intent(DangNhapActivity.this, SanPhamActivity.class));
            } catch(ApiException e) {
                e.printStackTrace();
            }
        }
        }
    }
}