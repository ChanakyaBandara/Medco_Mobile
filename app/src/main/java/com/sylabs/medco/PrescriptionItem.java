package com.sylabs.medco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PrescriptionItem extends AppCompatActivity {

    private TextView txtDname,txtDmanf,txtDdesc,txtCap;
    private ImageView imgDview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_item);
        String DRid = getIntent().getStringExtra("Extra_Drg");
        txtDname = (TextView) findViewById(R.id.txtPIdrg);
        txtDmanf = (TextView) findViewById(R.id.txtPImanf);
        txtDdesc = (TextView) findViewById(R.id.txtPIdes);
        txtCap = (TextView) findViewById(R.id.txtPIcap);
        imgDview = (ImageView) findViewById(R.id.imgview);

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("type", "load_drug");
        param.put("DRid", DRid);


        Backgroundworker backgroundworker = new Backgroundworker(PrescriptionItem.this);
        backgroundworker.execute(param);
    }

    public void displayName(String result) {
        Log.i("Error_test3", result);

        try {
            JSONArray jsonArray = new JSONArray(result.trim());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                txtDname.setText(jsonObject.getString("drg_name"));
                txtDmanf.setText(jsonObject.getString("manf_comp"));
                txtCap.setText(jsonObject.getString("drg_strength"));
                txtDdesc.setText(jsonObject.getString("drg_Desc"));
                
                String photoURL = jsonObject.getString("drg_Img");

                Glide.with(this)
                        .load("http://192.168.8.102/Medco/Images/"+photoURL)
                        .into(imgDview);


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("testingerror", e.toString());
        }
    }
}