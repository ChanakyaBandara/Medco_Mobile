package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sylabs.medco.models.Prescription;
import com.sylabs.medco.recycleviews.Recycleview_config_select_prescriptions;
import com.sylabs.medco.services.Backgroundworker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Queue extends AppCompatActivity {

    private String Pha_ID,Pres_ID,Doc_ID;
    private TextView txtQueueLabelName,txtQueueLabelDate,txtQueueLabelPhaDoc,txtQueueValueName,txtQueueValueDate,txtQueueValuePhaDoc,txtQueueNumber,txtQueueLabelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        txtQueueLabelName = (TextView) findViewById(R.id.txtQueueLabelName);
        txtQueueLabelDate = (TextView) findViewById(R.id.txtQueueLabelDate);
        txtQueueLabelPhaDoc = (TextView) findViewById(R.id.txtQueueLabelPhaDoc);
        txtQueueValueName = (TextView) findViewById(R.id.txtQueueValueName);
        txtQueueValueDate = (TextView) findViewById(R.id.txtQueueValueDate);
        txtQueueValuePhaDoc = (TextView) findViewById(R.id.txtQueueValuePhaDoc);
        txtQueueNumber = (TextView) findViewById(R.id.txtQueueNumber);
        txtQueueLabelType = (TextView) findViewById(R.id.txtQueueLabelType);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Pha_ID= null;
            Pres_ID= null;
            Doc_ID= null;
        } else {
            if(extras.containsKey("DID")) {
                Doc_ID= extras.getString("DID");
                loadDocQueue();
            }else if(extras.containsKey("Extra_Pha_ID") && extras.containsKey("Extra_Pres_ID")){
                Pha_ID= extras.getString("Extra_Pha_ID");
                Pres_ID= extras.getString("Extra_Pres_ID");
                loadPharmacyQueue();
            }else {
                //do Nothing
            }
        }
    }

    private void loadDocQueue() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_doc_queue");
        param.put("MID", MainActivity.MID);
        param.put("DID", Doc_ID);

        Backgroundworker backgroundworker = new Backgroundworker(Queue.this);
        backgroundworker.execute(param);
    }

    private void loadPharmacyQueue() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_pharmacy_queue");
        param.put("MID", MainActivity.MID);
        param.put("Pha_ID", Pha_ID);
        param.put("Pres_ID", Pres_ID);

        Backgroundworker backgroundworker = new Backgroundworker(Queue.this);
        backgroundworker.execute(param);
    }

    public void displayDoc(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result.trim());
            if(jsonArray != null){
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                txtQueueLabelName.setText("Doctor :");
                txtQueueLabelDate.setText("DateTime");
                txtQueueLabelPhaDoc.setText("Patient");
                txtQueueLabelType.setText("Doctor Queue");
                txtQueueValueName.setText(jsonObject.getString("D_name"));
                txtQueueValueDate.setText(jsonObject.getString("timestamp"));
                txtQueueValuePhaDoc.setText(jsonObject.getString("name"));
                txtQueueNumber.setText(jsonObject.getString("DQID"));
            } else {
                Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }

    public void displayPharmacy(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result.trim());
            if(jsonArray != null){
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                txtQueueLabelName.setText("Pharmacy :");
                txtQueueLabelDate.setText("DateTime");
                txtQueueLabelPhaDoc.setText("");
                txtQueueLabelType.setText("Pharmacy Queue");
                txtQueueValueName.setText(jsonObject.getString("Ph_name"));
                txtQueueValueDate.setText(jsonObject.getString("timestamp"));
                txtQueueValuePhaDoc.setText("");
                txtQueueNumber.setText(jsonObject.getString("PQID"));
            } else {
                Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(Queue.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}