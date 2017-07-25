package com.example.adm1n.coffeescope.coffee_menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.model.Products;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by adm1n on 25.07.2017.
 */

public class MenuAdapter extends ExpandableRecyclerViewAdapter<HeaderViewHolder, BodyViewHolder> {

    private OnProductClick onClickListener;

    public MenuAdapter(List<? extends ExpandableGroup> groups, OnProductClick listener) {
        super(groups);
        this.onClickListener = listener;
    }

    @Override
    public HeaderViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_adapter_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public BodyViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_adapter_item_with_cardview, parent, false);
        return new BodyViewHolder(view, onClickListener);
    }

    @Override
    public void onBindChildViewHolder(BodyViewHolder holder, int flatPosition, ExpandableGroup group,
                                      int childIndex) {
        Products products = (Products) group.getItems().get(childIndex);
        holder.onBind(products);
    }

    @Override
    public void onBindGroupViewHolder(HeaderViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setHeaderName(group.getTitle());
        holder.disableListener();
    }

    public interface OnProductClick {
        void onClick(View v);
    }
}
