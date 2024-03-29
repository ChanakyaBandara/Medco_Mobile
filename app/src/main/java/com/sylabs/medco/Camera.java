package com.sylabs.medco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import com.sylabs.medco.services.Backgroundworker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class Camera extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
    private Dialog myDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        myDialog = new Dialog(this);

        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Camera.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        String resultstr = result.toString();
        String prifix = resultstr.substring(0,3);
        if(prifix.toLowerCase(Locale.ROOT).equals("doc")){
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "load_doctor");
            param.put("Doc_QR", resultstr);
            Backgroundworker backgroundworker = new Backgroundworker(Camera.this);
            backgroundworker.execute(param);
        }else if(prifix.toLowerCase(Locale.ROOT).equals("pha")) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("type", "load_pharmacy");
            param.put("Pha_QR", resultstr);
            Backgroundworker backgroundworker = new Backgroundworker(Camera.this);
            backgroundworker.execute(param);
        }else {
            Toast.makeText(getApplicationContext(), "Error "+result.toString(), Toast.LENGTH_LONG).show();
        }
    }



    public void goBack(View view) {
        Intent intent = new Intent(Camera.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void displayNamePha(String result) {
        Log.i("Error_Check",result.toString());
        myDialog.setContentView(R.layout.custom_popup_pha);
        Button btnPopupPhaAddPres = (Button) myDialog.findViewById(R.id.btnPopupPhaAddPres);
        TextView txtPopupPhaName = (TextView) myDialog.findViewById(R.id.txtPopupPhaName);
        TextView txtPopupPhaReg = (TextView) myDialog.findViewById(R.id.txtPopupPhaReg);
        TextView txtPopupPhaPhone = (TextView) myDialog.findViewById(R.id.txtPopupPhaPhone);
        int PHID = 0;
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObj = (JSONObject) jsonArray.get(0);
            String Name = jsonObj.getString("Ph_name");
            String Reg = jsonObj.getString("Ph_reg");
            String Phone = jsonObj.getString("phone");
            PHID = jsonObj.getInt("ph_ID");
            txtPopupPhaName.setText(Name);
            txtPopupPhaReg.setText(Reg);
            txtPopupPhaPhone.setText(Phone);

        } catch (JSONException e) {
            Log.i("Error_Check",e.toString());
            e.printStackTrace();
        }

        int finalPHID = PHID;
        btnPopupPhaAddPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Camera.this,SelectPrescription.class);
                intent.putExtra("PHID",String.valueOf(finalPHID));
                startActivity(intent);
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void displayNameDoc(String result) {
        Log.i("Error_Check",result.toString());
        myDialog.setContentView(R.layout.custom_popup_doc);
        Button btnPopupDocAddQueue = (Button) myDialog.findViewById(R.id.btnPopupDocAddQueue);
        TextView txtPopupDocName = (TextView) myDialog.findViewById(R.id.txtPopupDocName);
        TextView txtPopupDocReg = (TextView) myDialog.findViewById(R.id.txtPopupDocReg);
        TextView txtPopupDocPhone = (TextView) myDialog.findViewById(R.id.txtPopupDocPhone);
        int DID=0;
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObj = (JSONObject) jsonArray.get(0);
            String Name = jsonObj.getString("D_name");
            String Reg = jsonObj.getString("medicalRegID");
            String Phone = jsonObj.getString("phone");
            DID = jsonObj.getInt("DID");
            txtPopupDocName.setText(Name);
            txtPopupDocReg.setText(Reg);
            txtPopupDocPhone.setText(Phone);

        } catch (JSONException e) {
            Log.i("Error_Check",e.toString());
            e.printStackTrace();
        }

        int finalDID = DID;
        btnPopupDocAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Camera.this,Queue.class);
                intent.putExtra("DID",String.valueOf(finalDID));
                startActivity(intent);
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
