package com.example.adm1n.coffeescope;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.adm1n.coffeescope.models.Ingredients;

import java.util.ArrayList;

class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder> {

    private Context mContext;
    private ArrayList<Ingredients> dataList = new ArrayList<>();

    CoffeeAdapter(Context context, ArrayList<Ingredients> list) {
        mContext = context;
        this.dataList = list;
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

        private ImageButton ib_napitok_add;
        private Context mContext;

        CoffeeViewHolder(Context context) {
            this(LayoutInflater.from(context).inflate(R.layout.coffee_adapter_item_with_cardview, null));
            mContext = context;
        }

        private CoffeeViewHolder(View itemView) {
            super(itemView);
            ib_napitok_add = (ImageButton) itemView.findViewById(R.id.iv_napitok_add);

            ib_napitok_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Добавлено", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, ViborNapitka.class);
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}