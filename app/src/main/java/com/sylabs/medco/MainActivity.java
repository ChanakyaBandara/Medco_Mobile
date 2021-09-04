package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sylabs.medco.services.Backgroundworker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static String LID;
    public static String MID,Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (TextUtils.isEmpty(LID)) {
            LID = getIntent().getStringExtra("Extra_text");
        }

        load_member_name(LID);
    }

    private void load_member_name(String user) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_member_name");
        param.put("LID", user);
        Backgroundworker backgroundworker = new Backgroundworker(MainActivity.this);
        backgroundworker.execute(param);
    }

    public void goToCamera(View view) {
        Intent intent = new Intent(MainActivity.this, Camera.class);
        startActivity(intent);
    }


    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }

    public void goToHistory(View view) {
        Intent intent = new Intent(MainActivity.this, PurchaseHistory.class);
        startActivity(intent);
    }

    public void goToPrescription(View view) {
        Intent intent = new Intent(MainActivity.this, Prescriptions.class);
        startActivity(intent);
    }

    public void displayName(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            String MIDtemp = jsonObj.getString("MID");
            String tName = jsonObj.getString("Name");
            MainActivity.MID = MIDtemp;
            MainActivity.Name = tName;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}