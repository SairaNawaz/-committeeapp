package com.apponative.committee_app.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apponative.committee_app.R;
import com.apponative.committee_app.ui.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Muhammad Waqas on 8/18/2016.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    int lastCheckedPos;
    String[] menuItems;

    ArrayList<Integer> tagIds = new ArrayList<>(Arrays.asList(R.string.tag_home
            , R.string.tag_committee,
            R.string.tag_create,
            R.string.tag_contact,
            R.string.tag_calculator,
            R.string.tag_aboutus,
            R.string.tag_settings));

    public MenuAdapter(String[] menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder viewHolder, int i) {
        viewHolder.menu_item.setText(menuItems[i]);
        viewHolder.itemView.setSelected(i == lastCheckedPos);
    }

    @Override
    public int getItemCount() {
        return menuItems.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView menu_item;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            menu_item = (TextView) view.findViewById(R.id.menu_item);
        }

        @Override
        public void onClick(View view) {
            lastCheckedPos = getAdapterPosition();
            MainActivity.menuItemListener.OnMenuItemSelected(tagIds.get(lastCheckedPos), null);
            notifyDataSetChanged();
        }
    }

    public void setSelected(int tagId) {
        if (tagIds.contains(tagId)) {
            lastCheckedPos = tagIds.indexOf(tagId);
            notifyDataSetChanged();
        }
    }
}