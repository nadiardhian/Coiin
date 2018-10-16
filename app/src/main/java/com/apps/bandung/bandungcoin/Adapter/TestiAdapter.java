package com.apps.bandung.bandungcoin.Adapter;

import android.content.Context;

import com.apps.bandung.bandungcoin.Model.TModel;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class TestiAdapter extends RecyclerView.Adapter<TestiAdapter.ViewHolder>{
    Context context;
    ArrayList<TModel> testimoniList;

    public TestiAdapter(Context context, ArrayList<TModel> testilist) {
        this.context = context;
        this.testimoniList = testilist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.apps.bandung.bandungcoin.R.layout.testi_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TModel tmodel = testimoniList.get(position);

        holder.tNama.setText(tmodel.getTnama());
        holder.tDeskripsi.setText(tmodel.getTdeskripsi());
        holder.tTime.setText(String.valueOf(tmodel.getTimestamp()));
        Glide.with(context).load(tmodel.getTfoto()).into(holder.tFoto);

    }

    @Override
    public int getItemCount() {
        return testimoniList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //Deklarasi Variable
        TextView tNama;
        TextView tDeskripsi;
        TextView tTime;
        ImageView tFoto;
        CardView tCardView;

        public ViewHolder(View itemView) {
            super(itemView);

            tNama = itemView.findViewById(com.apps.bandung.bandungcoin.R.id.testi_nama);
            tDeskripsi = itemView.findViewById(com.apps.bandung.bandungcoin.R.id.testi_desc);
            tTime = itemView.findViewById(com.apps.bandung.bandungcoin.R.id.testi_time);
            tFoto = itemView.findViewById(com.apps.bandung.bandungcoin.R.id.testi_foto);
            tCardView = itemView.findViewById(com.apps.bandung.bandungcoin.R.id.cvTesti);

        }

    }


}
