package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sylabs.medco.models.PreOder;
import com.sylabs.medco.recycleviews.Recycleview_config_history;
import com.sylabs.medco.services.Backgroundworker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseHistory extends AppCompatActivity {

    private List<PreOder> dataList;
    private List<String> dataListID;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("type", "load_history");
        param.put("MID", MainActivity.MID);

        dataList = new ArrayList<PreOder>();
        dataListID = new ArrayList<String>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOder);

        Backgroundworker backgroundworker = new Backgroundworker(PurchaseHistory.this);
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
                PreOder preOder = new PreOder();
                String PrOID = jsonObject.getString("oder_id");
                preOder.setOder_id(PrOID);//`oder_date`, `reference`, `Cost`, `PHID`, `PID`, `Ph_name`
                preOder.setOder_date(jsonObject.getString("oder_date"));
                preOder.setReference(jsonObject.getString("reference"));
                preOder.setCost(jsonObject.getString("Cost"));
                preOder.setPHID(jsonObject.getString("PHID"));
                preOder.setPID(jsonObject.getString("PID"));
                preOder.setPh_name(jsonObject.getString("Ph_name"));

                dataListID.add(PrOID);
                dataList.add(preOder);
            }

            if (dataListID.isEmpty()) {
                Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
            } else {
                new Recycleview_config_history().setConfig(recyclerView, PurchaseHistory.this, dataList, dataListID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }
}