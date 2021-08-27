package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Registration extends AppCompatActivity {

    private EditText txtName, txtNIC, txtEmail, txtPhone, txtPassword, txtAge;
    private String Gender;
    private AlertDialog alertDialog;    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        txtName = (EditText) findViewById(R.id.txtname);
        txtEmail = (EditText) findViewById(R.id.txtemail);
        txtNIC = (EditText) findViewById(R.id.txtNIC);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAge = (EditText) findViewById(R.id.txtAge);
        txtPassword = (EditText) findViewById(R.id.txtpassword);
        Gender = "null";
    }

    public void ForgetPass(View view) {
    }

    public void displayName(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            String status = jsonObj.getString("Status");
            if (status.equals("1")) {
                String LID = jsonObj.getString("LID");
                Intent intent = new Intent(this, MainActivity.class);
                String Extra_text1 = LID;
                intent.putExtra("Extra_text", Extra_text1);
                this.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Registration Status");
            String reg = "SQLSTATE[23000]";
            if (result.contains(reg)) {
                alertDialog.setMessage("NIC number exists !");
            } else {
                alertDialog.setMessage("Error");
            }
            alertDialog.show();
        }
    }

    public void loadSpinnerData(String result) {
    }

    public void registration(View view) {
        String Name = txtName.getText().toString();
        String Email = txtEmail.getText().toString();
        String NIC = txtNIC.getText().toString();
        String Phone = txtPhone.getText().toString();
        String Age = txtAge.getText().toString();
        String Password = txtPassword.getText().toString();

        if (!(TextUtils.isEmpty(Name) && TextUtils.isEmpty(NIC) && TextUtils.isEmpty(Age) && TextUtils.isEmpty(Email) && TextUtils.isEmpty(Phone) && TextUtils.isEmpty(Password)  && TextUtils.isEmpty(NIC))) {
            if (validateNIC(NIC)) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("type", "addMember");
                param.put("name", Name);
                param.put("email", Email);
                param.put("nic", NIC.toLowerCase().trim());
                param.put("age", Age);
                param.put("phone", Phone.trim());
                param.put("gender", Gender);
                param.put("Password", Password);
                //name email nic age phone gender Password
                Backgroundworker backgroundworker = new Backgroundworker(Registration.this);
                backgroundworker.execute(param);
            } else {
                Toast.makeText(Registration.this, "Invalide NIC ", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(Registration.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateNIC(String id) {

        int type = NICtype(id);
        if (type == 0) {
            return false;
        }
        String person = "";
        String birthYear = "";
        String dates = "";
        switch (type) {
            case 1:
                person = "Male";
                birthYear = "19" + id.substring(0, 2);
                dates = id.substring(2, 5);
                break;
            case 2:
                person = "Male";
                birthYear = id.substring(0, 4);
                dates = id.substring(4, 7);
                break;
            default:
                Toast.makeText(Registration.this, "Invalide NIC ", Toast.LENGTH_SHORT).show();
        }
        if (type == 1 || type == 2) {
            int year = Integer.parseInt(birthYear);
            int no = Integer.parseInt(dates);
            if (no > 500) {
                no -= 500;
                person = "Female";
            }

            --no;
            if (year % 100 == 0) {
                if (year % 400 != 0) {
                    --no;
                }
            } else if (year % 4 != 0) {
                --no;
            }

            if (no < 60) {
                ++no;
            }
            Gender = person;

        }
        return true;
    }

    private int NICtype(String nicno) {
        nicno = nicno.toLowerCase().trim();
        int type;
        if ((nicno.length() == 10) && ((nicno.substring(nicno.length() - 1).equals("v")) || (nicno.substring(nicno.length() - 1).equals("x"))) && (nicno.substring(0, nicno.length() - 1).matches("\\d+"))) {
            type = 1;
        } else if ((nicno.length() == 12) && (nicno.matches("\\d+"))) {
            type = 2;
        } else {
            type = 0;
        }
        return type;
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        this.startActivity(intent);
    }
}