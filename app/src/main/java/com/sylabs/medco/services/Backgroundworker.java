package com.sylabs.medco.services;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

//import com.github.ybq.android.spinkit.sprite.Sprite;
//import com.github.ybq.android.spinkit.style.Wave;

import com.sylabs.medco.Login;
import com.sylabs.medco.MainActivity;
import com.sylabs.medco.PrescriptionItem;
import com.sylabs.medco.Prescriptions;
import com.sylabs.medco.Profile;
import com.sylabs.medco.PurchaseHistory;
import com.sylabs.medco.Registration;
import com.sylabs.medco.ViewPrescription;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class Backgroundworker extends AsyncTask<HashMap<String, String>, Void, String> {

    private MainActivity ParentM;
    private Registration ParentR;
    private Login ParentL;
    private Profile ParentP;
    private Prescriptions ParentPre;
    private ViewPrescription ParentView;
    private PurchaseHistory ParentPHis;
    private PrescriptionItem ParentDrg;
    private String type = "";
    private Dialog myDialog;


    public Backgroundworker(MainActivity parent) {
        ParentM = parent;
        myDialog = new Dialog(ParentM);
    }

    public Backgroundworker(Registration parent) {
        ParentR = parent;
        myDialog = new Dialog(ParentR);
    }

    public  Backgroundworker(Login parent) {
        ParentL = parent;
        myDialog = new Dialog(ParentL);
    }

    public Backgroundworker(Profile parent) {
        ParentP = parent;
        myDialog = new Dialog(ParentP);
    }

    public Backgroundworker(Prescriptions parent) {
        ParentPre = parent;
        myDialog = new Dialog(ParentPre);
    }

    public Backgroundworker(ViewPrescription parent) {
        ParentView = parent;
        myDialog = new Dialog(ParentView);
    }

    public Backgroundworker(PurchaseHistory parent) {
        ParentPHis = parent;
        myDialog = new Dialog(ParentPHis);
    }

    public Backgroundworker(PrescriptionItem parent) {
        ParentDrg = parent;
        myDialog = new Dialog(ParentDrg);
    }




    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        HashMap<String, String> param = params[0];
        type = param.get("type");
        String login_url = "http://192.168.1.4/Medco/PHP/mobile.php";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = "";
            for (String name : param.keySet()) {
                String key = name;
                String value = param.get(name).toString();
                post_data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8") + "&";
            }
            if (post_data.length() > 0) {
                post_data = post_data.substring(0, post_data.length() - 1);
            }

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("Error_test1", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Error_test2", e.toString());
        } catch (Exception e) {
            Log.i("Error_test3", e.toString());
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        /*myDialog.setContentView(R.layout.activity_custome_pop_up);
        ProgressBar progressBar = (ProgressBar) myDialog.findViewById(R.id.spin_kit);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();*/
    }

    @Override
    protected void onPostExecute(String result) {
        myDialog.dismiss();
        if (type.equals("login")) {
            ParentL.displayName(result);
        }

        if (type.equals("addMember")) {
            ParentR.displayName(result);
        }

        if (type.equals("load_member_data")) {
            ParentP.displayName(result);
        }

        if (type.equals("load_member_name")) {
            ParentM.displayName(result);
        }

        if (type.equals("load_prescription")) {
            ParentPre.displayName(result);
        }

        if (type.equals("load_drugs")) {
            ParentView.displayName(result);
        }

        if (type.equals("load_history")) {
            ParentPHis.displayName(result);
        }

        if (type.equals("load_drug")) {
            ParentDrg.displayName(result);
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
