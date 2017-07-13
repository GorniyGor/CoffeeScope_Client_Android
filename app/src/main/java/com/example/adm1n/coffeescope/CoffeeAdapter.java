package com.example.adm1n.coffeescope;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder> {

    private Context mContext;

    CoffeeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CoffeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoffeeViewHolder(parent.getContext());
    }

    @Override
    public void onBindViewHolder(CoffeeViewHolder holder, int position) {
//        holder.appName.setText(applicationInfo.loadLabel(mPackageManager));
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class CoffeeViewHolder extends RecyclerView.ViewHolder {

        TextView appName;

        CoffeeViewHolder(Context context) {
            this(LayoutInflater.from(context).inflate(R.layout.coffee_adapter_item, null));
        }

        private CoffeeViewHolder(View itemView) {
            super(itemView);
//            appName = (TextView) itemView.findViewById(R.id.app_name);
        }
    }
}