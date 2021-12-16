package com.greenhouse.android.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.UserViewModel;
import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.User;
import com.greenhouse.android.databinding.ActivityRegisterBinding;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private TextView textView;
    EditText name;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button registerButton;



    boolean logged;

    private ActivityRegisterBinding binding;


    private UserViewModel userViewModel;

    public static Context contextOfApplication;

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextOfApplication = getApplicationContext();

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);

        // Hiding title bar and making it fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(binding.getRoot());

        setContentView(R.layout.activity_register);

        textView = findViewById(R.id.mainText);
        name = findViewById(R.id.text_register_name);
        email = findViewById(R.id.text_register_email);
        password = findViewById(R.id.text_register_password);
        confirmPassword = findViewById(R.id.text_register_confirm_password);
        registerButton = findViewById(R.id.registerButton);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getToken().observe(this, new Observer<LoggedUser>() {
            @Override
            public void onChanged(LoggedUser loggedUser) {
                if(loggedUser.getToken().equals("empty")|| loggedUser.getToken().equals("loading")){
                    password.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    registerButton.setVisibility(View.VISIBLE);
                    registerButton.setText("SUBMIT");
                    logged = false;
                }else{

                    Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logged) {
                    textView.setText("Logging out");
                    userViewModel.logout();
                    return;
                }

                if (email.getText().toString().equals("") || name.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User toReg = new User(email.getText().toString(),password.getText().toString(),name.getText().toString());

                Toast.makeText(getApplicationContext(), "Trying to register "+toReg.getName(), Toast.LENGTH_SHORT).show();
                userViewModel.register(toReg);
            }
        });
    }


    public void goLogin(View v){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}