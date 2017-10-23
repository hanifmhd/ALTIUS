package com.mis.altius.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mis.altius.MyData;
import com.mis.altius.ProfileActivity;
import com.mis.altius.R;

import java.util.ArrayList;


/**
 * Created by Hanifmhd on 9/7/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private Context context;
    private ArrayList<MyData> my_data;

    public CustomAdapter(Context context, ArrayList<MyData> my_data){
        this.context = context;
        this.my_data = my_data;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(itemView, context, my_data);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, final int position) {
        YoYo.with(Techniques.Tada).playOn(holder.cardView);
        holder.nama.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.angkatan.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.kota_kantor.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.perusahaan.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.jabatan.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

        holder.nama.setText(my_data.get(position).getNama());
        holder.angkatan.setText(my_data.get(position).getAngkatan());
        holder.kota_kantor.setText(my_data.get(position).getKota_kantor());
        holder.perusahaan.setText(my_data.get(position).getPerusahaan());
        holder.jabatan.setText(my_data.get(position).getJabatan());

        if (my_data.get(position).getJenis_kelamin().equals("Perempuan")){
            holder.foto.setImageResource(R.drawable.woman);
        }
        else holder.foto.setImageResource(R.drawable.man);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(),""+my_data.get(position).getNama(), Toast.LENGTH_SHORT).show();
                Context context = view.getContext();
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("id", my_data.get(position).getId());
                intent.putExtra("nama", my_data.get(position).getNama());
                intent.putExtra("angkatan", my_data.get(position).getAngkatan());
                intent.putExtra("kota_kantor", my_data.get(position).getKota_kantor());
                intent.putExtra("perusahaan", my_data.get(position).getPerusahaan());
                intent.putExtra("jabatan", my_data.get(position).getJabatan());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama;
        TextView angkatan;
        TextView kota_kantor;
        TextView perusahaan;
        TextView jabatan;
        ImageView foto;
        CardView cardView;

        ArrayList<MyData> my_data = new ArrayList<MyData>();
        Context context;
        public ViewHolder(View itemView, Context context, ArrayList<MyData> my_data){
            super(itemView);
            this.my_data = my_data;
            this.context = context;
            nama = itemView.findViewById(R.id.nama);
            angkatan = itemView.findViewById(R.id.tahun);
            kota_kantor = itemView.findViewById(R.id.kota_kantor);
            perusahaan = itemView.findViewById(R.id.kantor);
            jabatan = itemView.findViewById(R.id.jabatan);
            foto = itemView.findViewById(R.id.foto);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }

}
