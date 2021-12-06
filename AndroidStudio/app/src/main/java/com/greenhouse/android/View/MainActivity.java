package com.greenhouse.android.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel mainViewModel;
    TextView showData;
    Button getData;
    EditText id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainViewModel.getRequestedData().observe(this, data -> showData.setText(data.getData()));

        getData = findViewById(R.id.get_button);
        showData = findViewById(R.id.textViewShow);
        id = findViewById(R.id.id_text);

        getData.setOnClickListener(v -> mainViewModel.requestData(Integer.parseInt( id.getText().toString())));
    }
}