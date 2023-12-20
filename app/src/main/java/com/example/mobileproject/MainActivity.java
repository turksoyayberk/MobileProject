package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button newJourneyButton;
    private Button myJourneysButton;
    private Button deleteDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        newJourneyButton= findViewById(R.id.newJournalButton);
        myJourneysButton = findViewById(R.id.myJournalsButton);
        deleteDataButton=findViewById(R.id.deleteDataButton);

        newJourneyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Diğer sayfa açılcak. O sayfa da harita olcak.
                OpenMapActivity();
            }
        });

        myJourneysButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Daha önceden tuttuğu günlükler açılcak.
                //Aynı sayfa da açılcak, sadece UI değişcek.
            }
        });

        deleteDataButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Emin misiniz diye sorabilir.
                deleteDatabase();
            }
        });
    }
    public void deleteDatabase() {
        this.deleteDatabase("AppDatabase");
    }
    private void OpenMapActivity() {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }
}