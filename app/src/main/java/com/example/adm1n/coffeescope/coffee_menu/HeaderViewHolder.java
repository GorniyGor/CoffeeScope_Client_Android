package com.example.adm1n.coffeescope.coffee_menu;

import android.view.View;
import android.widget.TextView;

import com.example.adm1n.coffeescope.R;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by adm1n on 25.07.2017.
 */

public class HeaderViewHolder extends GroupViewHolder {

    private TextView headerTitle;
    private OnGroupClickListener listener;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        super.setOnGroupClickListener(null);
        headerTitle = ((TextView) itemView.findViewById(R.id.tvTitle));
    }

    public void setHeaderName(String name) {
        headerTitle.setText(name);
    }

    public void disableListener(){
        super.expand();
        super.setOnGroupClickListener(null);
    }
}
