package com.sylabs.medco.recycleviews;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sylabs.medco.Prescriptions;
import com.sylabs.medco.Queue;
import com.sylabs.medco.R;
import com.sylabs.medco.SelectPrescription;
import com.sylabs.medco.ViewPrescription;
import com.sylabs.medco.models.Prescription;

import java.io.Serializable;
import java.util.List;

public class Recycleview_config_select_prescriptions {
    private SelectPrescription mContext;
    private RecAddaptor mRecAddaptor;
    private String mPHID;

    public void setConfig(RecyclerView recyclerView, String PHID, SelectPrescription context, List<Prescription> recs, List<String> keys) {
        mContext = context;
        mPHID = PHID;
        mRecAddaptor = new RecAddaptor(recs, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mRecAddaptor);
    }

    class RecItemView extends RecyclerView.ViewHolder {

        private TextView txtPresDate, txtPresDoc, txtPresName;
        private String key;

        public RecItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.item_prescription, parent, false));
            txtPresDate = (TextView) itemView.findViewById(R.id.txtPresDate);
            txtPresDoc = (TextView) itemView.findViewById(R.id.txtPresDoc);
            txtPresName = (TextView) itemView.findViewById(R.id.txtPresName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Queue.class);
                    Prescription preObj = mRecAddaptor.mPer.get(mRecAddaptor.mKey.indexOf(key));
                    intent.putExtra("Extra_Pha_ID", (String) mPHID);
                    intent.putExtra("Extra_Pres_ID", (String) preObj.getPrID());
                    mContext.startActivity(intent);
                }
            });
        }

        public void bind(Prescription prescription, String key) {
            //txtPresDate, txtPresDoc, txtPresName
            txtPresDate.setText(prescription.getDate());
            txtPresDoc.setText(prescription.getDoc_Name());
            txtPresName.setText(prescription.getPrID());
            this.key = key;
        }

    }


    class RecAddaptor extends RecyclerView.Adapter<RecItemView> {

        private List<Prescription> mPer;
        private List<String> mKey;

        public RecAddaptor(List<Prescription> mPer, List<String> mKey) {
            this.mPer = mPer;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public RecItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecItemView holder, int position) {
            holder.bind(mPer.get(position), mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mPer.size();
        }
    }


}
