package com.example.adm1n.coffeescope.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.example.adm1n.coffeescope.models.Ingredients;
import com.example.adm1n.coffeescope.models.basket.BasketProducts;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by adm1n on 21.08.2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<BasketProducts> productsList = new ArrayList<>();
    private OnOrderClick onClickListener;

    public OrderAdapter(List<BasketProducts> records, OnOrderClick click) {
        this.onClickListener = click;
        this.productsList = records;
    }

    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coffee_adapter_item_swipe_del, viewGroup, false);
        return new ViewHolder(v, onClickListener);
    }

    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        BasketProducts basketProducts = productsList.get(i);
        viewHolder.name.setText(basketProducts.getName());
        viewHolder.cost.setText(String.valueOf(basketProducts.getCostSumm()));
        String ingredients = "";
        RealmList<Ingredients> ingredientses = basketProducts.getmIngredientsList();
        for (int j = 0; j < ingredientses.size(); j++) {
            ingredients += ingredientses.get(j).getName();
        }
        viewHolder.ingredients.setText(ingredients);
        viewHolder.size.setText(basketProducts.getSizeId());
        viewHolder.count.setText("x" + String.valueOf(basketProducts.getCount()));
        viewHolder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    /**
     * Реализация класса ViewHolder, хранящего ссылки на виджеты.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView ingredients;
        private TextView size;
        private TextView cost;
        private TextView count;
        private ImageView iv_order_confirm_del_position;
        private ImageView iv_order_cancel_del_position;
        private OnOrderClick onClickListener;
        private int mPosition;

        public ViewHolder(View itemView, OnOrderClick click) {
            super(itemView);
            this.onClickListener = click;
            name = (TextView) itemView.findViewById(R.id.tv_order_napitok_name);
            ingredients = (TextView) itemView.findViewById(R.id.tv_order_ingredients);
            size = (TextView) itemView.findViewById(R.id.tv_order_product_size);
            cost = (TextView) itemView.findViewById(R.id.tv_order_product_cost);
            iv_order_cancel_del_position = (ImageView) itemView.findViewById(R.id.iv_order_cancel_del_position);
            iv_order_cancel_del_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onDelete(v, mPosition);
                }
            });
            iv_order_confirm_del_position = (ImageView) itemView.findViewById(R.id.iv_order_confirm_del_position);
            iv_order_confirm_del_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onRefactor(v, mPosition);
                }
            });
            count = (TextView) itemView.findViewById(R.id.tv_order_product_count);
        }

        void setPosition(int position) {
            this.mPosition = position;
        }
    }

    public interface OnOrderClick {
        void onDelete(View v, int position);

        void onRefactor(View v, int position);
    }
}