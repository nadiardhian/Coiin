package com.apps.bandung.bandungcoin.Adapter;

import android.content.Context;
import android.content.Intent;
import com.apps.bandung.bandungcoin.Main2Activity;
import com.apps.bandung.bandungcoin.Model.Main;
import com.apps.bandung.bandungcoin.R;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    //Deklarasi Context dan ArrayList pada Post
    Context context;
    ArrayList<Main> mainList;


    public MainAdapter(Context context, ArrayList<Main> mainlist) {
        this.context = context;
        this.mainList = mainlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Main main = mainList.get(position);


        holder.mNama.setText(main.getNama());
        holder.mAlamat.setText(main.getAlamat());
        Glide.with(context).load(main.getLogo()).into(holder.mLogo);

    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Deklarasi Variable
        TextView mNama;
        TextView mAlamat;
        ImageView mLogo;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNama = itemView.findViewById(R.id.main_nama);
            mAlamat = itemView.findViewById(R.id.main_alamat);
            mLogo = itemView.findViewById(R.id.main_image);
            cardView = itemView.findViewById(R.id.cvPost);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
            String nama = mainList.get(mPosition).getNama();
            Intent i = new Intent(view.getContext(), Main2Activity.class);
            i.putExtra("nama",nama);
            view.getContext().startActivity(i);
        }
    }


}
