package com.example.firebasestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.firebasestore.MainActivity.Text_Green;
import static com.example.firebasestore.MainActivity.Text_Red;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    String item[];
    String fixednames[];
    Long gpsstatus;
    public Adapter(Context context, String[] item,String[] fixednames, Long gpsstatus){
        this.context = context;
        this.item = item;
        this.fixednames = fixednames;
        this.gpsstatus = gpsstatus;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_main,parent,false);
        Item item = new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Item)holder).textView.setText(fixednames[position]);
        ((Item)holder).FixedNames.setText(item[position]);

    }



    @Override
    public int getItemCount() {
        return 8;
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class Item extends RecyclerView.ViewHolder{
        TextView textView;
        TextView FixedNames;


        public Item(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text1);
            FixedNames = (TextView) itemView.findViewById(R.id.text2);
            if(gpsstatus==1){
                textView.setTextColor(itemView.getResources().getColor(Text_Green));
            }else{
                textView.setTextColor(itemView.getResources().getColor(Text_Red));
            }
        }
    }
}
