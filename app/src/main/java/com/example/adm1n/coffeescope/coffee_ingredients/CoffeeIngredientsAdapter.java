package com.example.adm1n.coffeescope.coffee_ingredients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.basket.Basket;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by adm1n on 02.08.2017.
 */

public class CoffeeIngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RealmList<Ingredients> ingredients = new RealmList<>();
    private RealmList<Ingredients> ingredientsInBasket = new RealmList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private OnIngredientsClick onClickListener;

    public CoffeeIngredientsAdapter(RealmList<Ingredients> list, OnIngredientsClick onClickListener) {
        this.onClickListener = onClickListener;
        if (ingredients != null) {
            ingredients.clear();
            Ingredients fakeIngredients = new Ingredients();
            ingredients.add(fakeIngredients);
            ingredients.addAll(list);
        }
    }

    public CoffeeIngredientsAdapter(RealmList<Ingredients> list, OnIngredientsClick onClickListener, RealmList<Ingredients> basketIngredientsList) {
        this.onClickListener = onClickListener;
        this.ingredientsInBasket.addAll(basketIngredientsList);
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
            return new ItemViewHolder(v, onClickListener);
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
            ((ItemViewHolder) viewHolder).cost.setText("+ " + String.valueOf(ingredient.getPrice() + " P"));
            if (!ingredientsInBasket.isEmpty()) {
                ((ItemViewHolder) viewHolder).ivAddProduct.setImageResource(checkIcon(i) ? R.drawable.done_icon : R.drawable.add_icon);
            } else {
                ((ItemViewHolder) viewHolder).ivAddProduct.setImageResource(R.drawable.add_icon);
            }
            ((ItemViewHolder) viewHolder).setPosition(i);

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
    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView cost;
        private RelativeLayout rlCoffeeAdapterFrame;
        private OnIngredientsClick onIngredientsClick;
        private Integer mPosition;
        private ImageView ivAddProduct;

        public ItemViewHolder(View itemView, OnIngredientsClick onClick) {
            super(itemView);
            this.onIngredientsClick = onClick;
            name = (TextView) itemView.findViewById(R.id.tv_napitok_name);
            cost = (TextView) itemView.findViewById(R.id.tv_napitok_cost);
            ivAddProduct = (ImageView) itemView.findViewById(R.id.iv_napitok_add);
            rlCoffeeAdapterFrame = (RelativeLayout) itemView.findViewById(R.id.rlCoffeeAdapterFrame);
            rlCoffeeAdapterFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIngredientsClick.onIngredientsClick(v, mPosition);
                }
            });
        }

        void setPosition(int position) {
            this.mPosition = position;
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }

    public interface OnIngredientsClick {
        void onIngredientsClick(View v, int position);
    }

    private boolean checkIcon(int position) {
        Integer id = ingredients.get(position).getId();
        for (Ingredients ingredient : ingredientsInBasket) {
            if (ingredient.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}