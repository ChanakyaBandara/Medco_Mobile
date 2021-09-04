package com.sylabs.medco.recycleviews;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sylabs.medco.models.PreOder;
import com.sylabs.medco.PurchaseHistory;
import com.sylabs.medco.R;

import java.util.List;

public class Recycleview_config_history {
    private PurchaseHistory mContext;
    private RecAddaptor mRecAddaptor;

    public void setConfig(RecyclerView recyclerView, PurchaseHistory context, List<PreOder> recs, List<String> keys) {
        mContext = context;
        mRecAddaptor = new RecAddaptor(recs, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mRecAddaptor);
    }

    class RecItemView extends RecyclerView.ViewHolder {

        private TextView txtOrdPreID, txtOrdcDate, txtOrdCost, txtOrdPhamacy;
        private String key;

        public RecItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.item_purchase, parent, false));
            txtOrdPreID = (TextView) itemView.findViewById(R.id.txtOrdPreID);
            txtOrdcDate = (TextView) itemView.findViewById(R.id.txtOrdcDate);
            txtOrdCost = (TextView) itemView.findViewById(R.id.txtOrdCost);
            txtOrdPhamacy = (TextView) itemView.findViewById(R.id.txtOrdPhamacy);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ViewPrescription.class);
//                    PreOder preObj = mRecAddaptor.mPer.get(mRecAddaptor.mKey.indexOf(key));
//                    intent.putExtra("Extra_rec", (Serializable) preObj);
//                    mContext.startActivity(intent);
//                }
//            });
        }

        public void bind(PreOder preOder, String key) {
            //txtPresDate, txtPresDoc, txtPresName
            txtOrdPreID.setText(preOder.getOder_id());
            txtOrdcDate.setText(preOder.getOder_date());
            txtOrdCost.setText(preOder.getCost()+" LKR");
            txtOrdPhamacy.setText(preOder.getPh_name());
            this.key = key;
        }

    }


    class RecAddaptor extends RecyclerView.Adapter<RecItemView> {

        private List<PreOder> mPer;
        private List<String> mKey;

        public RecAddaptor(List<PreOder> mPer, List<String> mKey) {
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
