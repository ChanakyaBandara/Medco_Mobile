package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sylabs.medco.services.Backgroundworker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    private TextView txtName,txtPhone,txtGender,txtNIC,txtEmail,txtAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        txtName = (TextView) findViewById(R.id.txtname);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtNIC = (TextView) findViewById(R.id.txtNIC);
        txtEmail = (TextView) findViewById(R.id.txtemail);
        txtAge = (TextView) findViewById(R.id.txtAge);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_member_data");
        param.put("LID", MainActivity.LID);
        Backgroundworker backgroundworker = new Backgroundworker(Profile.this);
        backgroundworker.execute(param);
    }

    public void goBack(View view) {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(Login.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(Profile.this, Login.class);
        MainActivity.MID = null;
        MainActivity.LID = null;
        startActivity(intent);
        finish();
    }

    public void changePass(View view) {
    }

    public void displayName(String result) {
        Log.i("Error_Check",result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            String Name = jsonObj.getString("Name");
            String MobileNo = jsonObj.getString("Phone");
            String Age = jsonObj.getString("Age");
            String NIC = jsonObj.getString("NIC");
            String Email = jsonObj.getString("Email");
            String Gender = jsonObj.getString("Gender");

            txtPhone.setText(MobileNo);
            txtAge.setText(Age);
            txtNIC.setText(NIC);
            txtEmail.setText(Email);
            txtGender.setText(Gender);
            txtName.setText(Name);

        } catch (JSONException e) {
            Log.i("Error_Check",e.toString());
            e.printStackTrace();
        }
    }
}