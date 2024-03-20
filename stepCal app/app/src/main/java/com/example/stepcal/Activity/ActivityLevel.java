package com.example.stepcal.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stepcal.DTO.User;
import com.example.stepcal.R;


public class ActivityLevel extends AppCompatActivity {

    private RadioGroup activityLevelRadioGroup;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        Button lowButton = findViewById(R.id.low);
        Button moderateButton = findViewById(R.id.moderate);
        Button highButton = findViewById(R.id.high);
        Button extremeButton = findViewById(R.id.extreme);



        lowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevIntent=getIntent();
                User user=(User) prevIntent.getSerializableExtra("user");
                assert user != null;
                user.setActivity_level("low");
                Intent intent = new Intent(ActivityLevel.this, Goal.class);
                intent.putExtra("user", user);
                startActivity(intent);

            }
        });


        moderateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevIntent=getIntent();
                User user=(User) prevIntent.getSerializableExtra("user");
                assert user != null;
                user.setActivity_level("moderate");
                Intent intent = new Intent(ActivityLevel.this, Goal.class);
                intent.putExtra("user", user);
                startActivity(intent);


            }
        });


        highButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevIntent=getIntent();
                User user=(User) prevIntent.getSerializableExtra("user");
                assert user != null;
                user.setActivity_level("high");
                Intent intent = new Intent(ActivityLevel.this, Goal.class);
                intent.putExtra("user", user);
                startActivity(intent);


            }
        });


        extremeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevIntent=getIntent();
                User user=(User) prevIntent.getSerializableExtra("user");
                assert user != null;
                user.setActivity_level("extreme");
                Intent intent = new Intent(ActivityLevel.this, Goal.class);
                intent.putExtra("user", user);
                startActivity(intent);


            }
        });



    }




   
}