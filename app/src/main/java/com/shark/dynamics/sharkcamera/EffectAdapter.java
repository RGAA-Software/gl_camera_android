package com.shark.dynamics.sharkcamera;

import android.content.Context;

import com.shark.dynamics.basic.rv.RViewAdapter;
import com.shark.dynamics.basic.rv.RViewHolder;

import java.util.List;

public class EffectAdapter extends RViewAdapter<EffectItem> {
    public EffectAdapter(Context context, List<EffectItem> data) {
        super(context, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_effect;
    }

    @Override
    public void bindDataToView(RViewHolder holder, int position) {
        EffectItem item = mData.get(position);
        holder.setText(R.id.id_effect_name, item.name);
    }
}
