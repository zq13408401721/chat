package com.mychat.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mychat.R;
import com.mychat.base.BaseAdapter;

import java.util.List;

public class TrendsImgAdapter extends BaseAdapter {

    public TrendsImgAdapter(List<String> list, Context context){
        super(list,context);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_trends_image_item;
    }

    @Override
    public void bindData(BaseViewHolder holder, Object o) {
        String imgUrl = (String) o;
        ImageView img = (ImageView) holder.getView(R.id.img);
        if(!TextUtils.isEmpty(imgUrl)){
            Glide.with(mContext).load(imgUrl).into(img);
        }
    }
}
