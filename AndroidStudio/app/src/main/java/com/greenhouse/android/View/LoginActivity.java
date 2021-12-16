package com.greenhouse.android.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.UserViewModel;
import com.greenhouse.android.Wrappers.User;
import com.greenhouse.android.databinding.ActivityLoginBinding;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView textView;
    EditText email;
    EditText password;
    Button loginButton;

    boolean logged;


    ActivityLoginBinding binding;


    private UserViewModel userViewModel;

    public static Context contextOfApplication;

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextOfApplication = getApplicationContext();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(binding.getRoot());

        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.mainText);
        email = findViewById(R.id.text_login_email);
        password = findViewById(R.id.text_login_password);
        loginButton = findViewById(R.id.loginButton);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getToken().observe(this, jwt -> {
            if(jwt.getToken().equals("empty")||jwt.getToken().equals("loading")||jwt.getToken().equals("default")){
                password.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                loginButton.setText("LogIn");
                logged = false;
            }else{
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logged) {
                    userViewModel.logout();
                    return;
                }

                if(email.getText().toString().equals("")||email.getText().toString().equals("")) return;
                User toLogin = new User(email.getText().toString(),password.getText().toString());
                userViewModel.login(toLogin);
            }
        });
    }

    public void signUp(View v){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}