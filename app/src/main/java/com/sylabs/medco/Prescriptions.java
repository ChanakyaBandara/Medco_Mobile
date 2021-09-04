package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sylabs.medco.models.Prescription;
import com.sylabs.medco.recycleviews.Recycleview_config_prescriptions;
import com.sylabs.medco.services.Backgroundworker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Prescriptions extends AppCompatActivity {

    private List<Prescription> dataList;
    private List<String> dataListID;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_prescription");
        param.put("MID", MainActivity.MID);

        dataList = new ArrayList<Prescription>();
        dataListID = new ArrayList<String>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Backgroundworker backgroundworker = new Backgroundworker(Prescriptions.this);
        backgroundworker.execute(param);
    }

    public void displayName(String result) {
        Log.i("Error_test1", result);
        dataList.clear();
        dataListID.clear();

        try {
            JSONArray jsonArray = new JSONArray(result.trim());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Prescription prescription = new Prescription();
                String PresID = jsonObject.getString("Pre_ID");
                prescription.setPrID(PresID);
                prescription.setDate(jsonObject.getString("Pre_Date"));
                prescription.setDoc_Name(jsonObject.getString("D_Name"));
                prescription.setQRcode(jsonObject.getString("QR_ID"));

                dataListID.add(PresID);
                dataList.add(prescription);
            }

            if (dataListID.isEmpty()) {
                Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
            } else {
                new Recycleview_config_prescriptions().setConfig(recyclerView, Prescriptions.this, dataList, dataListID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }
}