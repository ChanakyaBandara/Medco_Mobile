package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sylabs.medco.models.Drug;
import com.sylabs.medco.models.Prescription;
import com.sylabs.medco.services.Backgroundworker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewPrescription extends AppCompatActivity implements View.OnClickListener {
    private Prescription prescription;
    private TextView txtID,txtPName,txtDName,txtDate;
    private List<Drug> dataList;
    private List<String> dataListID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);
        prescription = (Prescription) getIntent().getSerializableExtra("Extra_rec");
        txtID = (TextView) findViewById(R.id.txtPresViewID);
        txtPName = (TextView) findViewById(R.id.txtPresViewPName);
        txtDName = (TextView) findViewById(R.id.txtPresViewDName);
        txtDate = (TextView) findViewById(R.id.txtPresViewDate);

        txtID.setText(prescription.getPrID());
        txtPName.setText(MainActivity.Name);
        txtDName.setText(prescription.getDoc_Name());
        txtDate.setText(prescription.getDate());

        dataList = new ArrayList<Drug>();
        dataListID = new ArrayList<String>();

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("type", "load_drugs");
        param.put("PreID", prescription.getPrID());


        Backgroundworker backgroundworker = new Backgroundworker(ViewPrescription.this);
        backgroundworker.execute(param);
    }

    public void gotoDetail(View view) {
    }

    public void displayName(String result) {
        Log.i("Error_test3", result);
        dataList.clear();
        dataListID.clear();

        try {
            JSONArray jsonArray = new JSONArray(result.trim());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Drug drug = new Drug();
                String DrgID = jsonObject.getString("drg_ID");
                drug.setDrID(DrgID);
                drug.setDrName(jsonObject.getString("drg_name"));
                drug.setDrMan(jsonObject.getString("manf_comp"));
                drug.setDrStr(jsonObject.getString("drg_strength"));
                drug.setDrDes(jsonObject.getString("drg_Desc"));
                drug.setDrImg(jsonObject.getString("drg_Img"));
                drug.setDose(jsonObject.getString("dose"));

                dataListID.add(DrgID);
                dataList.add(drug);
            }

            if (dataListID.isEmpty()) {
                Toast.makeText(this, "No Records Available !", Toast.LENGTH_SHORT).show();
            } else {
                manageTable();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }

    public void manageTable(){
        TableLayout tbl1 =(TableLayout) findViewById(R.id.tblDrugs);
        for (Drug drg : dataList){
            TableRow tblrw = new TableRow(this);
            tblrw.setPadding(10,10,10,10);
            tblrw.setTag(drg.getDrID());
            tblrw.setClickable(true);
            tblrw.setOnClickListener(this);

            TextView tv1 = new TextView(this);
            tv1.setText(drg.getDrID());
            tv1.setTextColor(Color.BLACK);
            tv1.setLayoutParams(new TableRow.LayoutParams(1));
            TextView tv2 = new TextView(this);
            tv2.setText(drg.getDrName());
            tv2.setTextColor(Color.BLACK);
            tv2.setLayoutParams(new TableRow.LayoutParams(2));
            TextView tv3 = new TextView(this);
            tv3.setText(drg.getDose());
            tv3.setTextColor(Color.BLACK);
            tv3.setLayoutParams(new TableRow.LayoutParams(3));
            tblrw.addView(tv1);
            tblrw.addView(tv2);
            tblrw.addView(tv3);
            tbl1.addView(tblrw);
        }
    }

    public void goToQR(View view) {
        Intent intent = new Intent(ViewPrescription.this, QrView.class);
        intent.putExtra("Extra_Qr", (String) prescription.getQRcode());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        String drgID = (String) view.getTag();
        //Toast.makeText(this, "DrID - "+drgID, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ViewPrescription.this, PrescriptionItem.class);
        intent.putExtra("Extra_Drg", (String) drgID);
        startActivity(intent);
    }
}
