package com.apponative.committee_app.ui.custom;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.apponative.committee_app.R;

/**
 * Created by Muhammad Waqas on 5/31/2017.
 */

public class ItemAnimator extends DefaultItemAnimator {
    Context context;
    Animation leftIn ,leftOut;
    public ItemAnimator(Context context){
        super();
        this.context = context;
        intializeAnimation();
    }

    void intializeAnimation(){
        leftOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
        leftIn = AnimationUtils.loadAnimation(context,R.anim.slide_in_left);
    }
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return true;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        return true;
    }


}
