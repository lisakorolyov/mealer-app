package com.example.meallogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class WelcomeClientScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_client_screen);
        Client client = (Client) getIntent().getSerializableExtra("Client");
        MaterialButton logout = (MaterialButton) findViewById(R.id.logout);
        TextView welcome = (TextView)findViewById(R.id.WelcomeMessage);
        welcome.setText("Welcome "+client.getUsername());
        logout.setOnClickListener(v->
        {
            openMainActivity();
        });
        MaterialButton home = (MaterialButton) findViewById(R.id.Home);
        home.setOnClickListener(v -> {
            openWelcomeClient();
        });
        MaterialButton search = (MaterialButton) findViewById(R.id.search);
        search.setOnClickListener(v -> {
            openSearchMeals();
        });
        MaterialButton settings = (MaterialButton) findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
            openClientSettingsFrag();
        });
    }
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openWelcomeClient() {
        Intent intent = new Intent(this, WelcomeClientScreen.class);
        startActivity(intent);
    }
    public void openClientSettingsFrag() {
        Intent intent = new Intent(this, ClientSettingsFrag.class);
        startActivity(intent);
    }
    public void openSearchMeals() {
        Intent intent = new Intent(this, SearchMeals.class);
        startActivity(intent);
    }
}