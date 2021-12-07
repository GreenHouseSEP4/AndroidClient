package com.greenhouse.android.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.greenhouse.android.R;
import com.greenhouse.android.View.StartActivity;
import com.greenhouse.android.ViewModel.UserViewModel;
import com.greenhouse.android.Wrappers.APIResponse.JWT;
import com.greenhouse.android.Wrappers.User;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView textView;
    EditText email;
    EditText password;
    Button loginButton;

    boolean logged;





    private UserViewModel userViewModel;

    public static Context contextOfApplication;

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextOfApplication = getApplicationContext();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.mainText);
        email = findViewById(R.id.text_login_email);
        password = findViewById(R.id.text_login_password);
        loginButton = findViewById(R.id.loginButton);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getToken().observe(this, new Observer<JWT>() {
            @Override
            public void onChanged(JWT jwt) {
//                Toast.makeText(getApplicationContext(), userViewModel.getToken().getValue().getToken(), Toast.LENGTH_SHORT).show();
                if(jwt.getToken().equals("empty")||jwt.getToken().equals("loading")){
                    password.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    loginButton.setText("LOGIN");
                    logged = false;
                }else{
//                    password.setVisibility(View.INVISIBLE);
//                    email.setVisibility(View.INVISIBLE);
//                    loginButton.setText("LOGOUT");
//                    logged = true;
                    Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
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

                Toast.makeText(getApplicationContext(), userViewModel.getToken().getValue().getToken(), Toast.LENGTH_SHORT).show();
                userViewModel.login(toLogin);
                Toast.makeText(getApplicationContext(), userViewModel.getToken().getValue().getToken(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void signUp(View v){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}