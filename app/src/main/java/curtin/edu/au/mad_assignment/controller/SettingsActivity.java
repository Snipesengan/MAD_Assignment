package curtin.edu.au.mad_assignment.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.Settings;

public class SettingsActivity extends AppCompatActivity {

    private static final String SETTINGS = "curtin.edu.au.mad_assignment.controller.settings";

    private ImageButton mapWidthIncButton, mapWidthDecButton;
    private ImageButton mapHeightIncButton, mapHeightDecButton;
    private ImageButton initialMoneyDecButton, initialMoneyIncButton;
    private Button confirmButton, backButton;
    private TextView mapWidthValue, mapHeightValue, initialMoneyValue;

    private Settings settings = null;

    public static Intent getIntent(Context c, Settings settings){
        Intent intent = new Intent(c, SettingsActivity.class);
        intent.putExtra(SETTINGS,settings);
        return intent;
    }

    /**
     * Called whenever startActivityForResult() is called.
     * @param intent
     * @return User Adjusted Settings
     */
    public static Settings getSettings(Intent intent)
    {
        return (Settings) intent.getSerializableExtra(SETTINGS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getIntent() != null) {
            settings = (Settings) getIntent().getSerializableExtra(SETTINGS);
        }

        // Get References to Views/buttons
        mapWidthIncButton = findViewById(R.id.mapWidthIncButton);
        mapWidthDecButton = findViewById(R.id.mapWidthDecButton);
        mapHeightIncButton = findViewById(R.id.mapHeightIncButton);
        mapHeightDecButton = findViewById(R.id.mapHeightDecButton);
        mapWidthValue = findViewById(R.id.mapWidthValue);
        mapHeightValue = findViewById(R.id.mapHeightValue);
        initialMoneyDecButton = findViewById(R.id.initialMoneyDecButton);
        initialMoneyIncButton = findViewById(R.id.initialMoneyIncButton);
        initialMoneyValue = findViewById(R.id.initialMoneyValue);
        confirmButton = findViewById(R.id.confirmSettingsButton);
        backButton = findViewById(R.id.cancelSettingsButton);

        // Set value with initial values
        mapWidthValue.setText(Integer.toString(settings.getMapWidth()));
        mapHeightValue.setText(Integer.toString(settings.getMapHeight()));
        initialMoneyValue.setText(Integer.toString(settings.getInitialMoney()));

        //TODO: Create Events for setting activity


        mapWidthIncButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                try
                {
                    settings.setMapWidth(settings.getMapWidth() + 1);
                    mapWidthValue.setText(Integer.toString(settings.getMapWidth()));
                }catch(IllegalArgumentException e){
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mapWidthDecButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                try
                {
                    settings.setMapWidth(settings.getMapWidth() - 1);
                    mapWidthValue.setText(Integer.toString(settings.getMapWidth()));
                }catch(IllegalArgumentException e){
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mapHeightIncButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                try
                {
                    settings.setMapHeight(settings.getMapHeight() + 1);
                    mapHeightValue.setText(Integer.toString(settings.getMapHeight()));
                }catch(IllegalArgumentException e){
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mapHeightDecButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                try
                {
                    settings.setMapHeight(settings.getMapHeight() - 1);
                    mapHeightValue.setText(Integer.toString(settings.getMapHeight()));
                }catch(IllegalArgumentException e){
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        initialMoneyIncButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                try
                {
                    settings.setInitialMoney(settings.getInitialMoney() + 1);
                    initialMoneyValue.setText(Integer.toString(settings.getInitialMoney()));
                }catch(IllegalArgumentException e){
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        initialMoneyDecButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                try
                {
                    settings.setInitialMoney(settings.getInitialMoney() - 1);
                    initialMoneyValue.setText(Integer.toString(settings.getInitialMoney()));
                }catch(IllegalArgumentException e){
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                Intent returnData = new Intent();
                returnData.putExtra(SETTINGS,settings);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                Intent returnData = new Intent();
                returnData.putExtra(SETTINGS,settings);
                setResult(RESULT_CANCELED, returnData);
                finish();
            }
        });
    }

}
