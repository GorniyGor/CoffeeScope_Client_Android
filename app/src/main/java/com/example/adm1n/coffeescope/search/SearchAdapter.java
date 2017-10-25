package com.example.adm1n.coffeescope.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.models.Place;

import java.util.List;

/**
 * Created by adm1n on 23.10.2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Place> places;
    private OnPlaceClickListener mListener;

    public SearchAdapter(List<Place> records, OnPlaceClickListener listener) {
        this.places = records;
        this.mListener = listener;
    }

    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_search_item, viewGroup, false);
        return new ViewHolder(v, mListener);
    }

    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Place place = places.get(i);
        viewHolder.name.setText(place.getName());
        viewHolder.address.setText(place.getAddress());
        viewHolder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    /**
     * Реализация класса ViewHolder, хранящего ссылки на виджеты.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;
        private TextView distance;
        private LinearLayout llMainFrame;
        private OnPlaceClickListener mListener;
        private int mPosition;

        public ViewHolder(View itemView, OnPlaceClickListener listener) {
            super(itemView);
            mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_search_coffee_name);
            address = (TextView) itemView.findViewById(R.id.tv_search_coffee_address);
            distance = (TextView) itemView.findViewById(R.id.tv_search_coffee_distance);
            llMainFrame = (LinearLayout) itemView.findViewById(R.id.llMainFrame);
            llMainFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPlaceClick(mPosition);
                }
            });
        }

        public void setPosition(int mPosition) {
            this.mPosition = mPosition;
        }
    }

    interface OnPlaceClickListener {
        void onPlaceClick(int position);
    }
}