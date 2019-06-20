package com.vvitguntur.baker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vvitguntur.baker.Model.BakingApp;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsHolder> {
    Context context;
    List<BakingApp> bakingAppList;

    public RecyclerAdapter(Context context, List<BakingApp> bakingAppList) {
        this.context = context;
        this.bakingAppList = bakingAppList;
    }
    @NonNull
    @Override
    public ItemsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_adapt,viewGroup,false);
        return new ItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsHolder itemsHolder, int i) {
        itemsHolder.textView.append(bakingAppList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return bakingAppList.size();
    }

    public class ItemsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        public ItemsHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.Cakeitem_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            Intent intent=new Intent(context,CakeListActivity.class);
            intent.putExtra("Recipe",bakingAppList.get(position));
            context.startActivity(intent);
        }
    }
}
