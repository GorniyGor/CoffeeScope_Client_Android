package com.example.adm1n.coffeescope.coffee_ingredients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.models.Ingredients;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adm1n on 02.08.2017.
 */

public class CoffeeIngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ingredients> ingredients = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CoffeeIngredientsAdapter(List<Ingredients> list) {
        if (ingredients != null) {
            ingredients.clear();
            Ingredients fakeIngredients = new Ingredients();
            ingredients.add(fakeIngredients);
            ingredients.addAll(list);
        }
    }

    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coffee_adapter_header, viewGroup, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coffee_adapter_item, viewGroup, false);
            return new ItemViewHolder(v);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Ingredients ingredient = ingredients.get(i);
        if (viewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) viewHolder).headerTitle.setText(ingredient.getName());
        } else if (viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder) viewHolder).name.setText(ingredient.getName());
            ((ItemViewHolder) viewHolder).cost.setText(String.valueOf(ingredient.getPrice()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    /**
     * Реализация класса ViewHolder, хранящего ссылки на виджеты.
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView cost;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_napitok_name);
            cost = (TextView) itemView.findViewById(R.id.tv_napitok_cost);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}