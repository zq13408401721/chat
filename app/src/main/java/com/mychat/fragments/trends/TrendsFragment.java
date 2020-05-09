package com.mychat.fragments.trends;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.mychat.R;
import com.mychat.activitys.trends.TrendsActivity;
import com.mychat.base.BaseFragment;
import com.mychat.interfaces.IBasePersenter;

import butterknife.BindView;
import butterknife.OnClick;

public class TrendsFragment extends BaseFragment {
    @BindView(R.id.img_trends)
    ImageView imgTrends;

    @Override
    protected int getLayout() {
        return R.layout.fragment_trends;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected IBasePersenter createPersenter() {
        return null;
    }

    @OnClick(R.id.img_trends)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.img_trends:
                openPublish();
                break;
        }
    }

    private void openPublish(){
        Intent intent = new Intent(context, TrendsActivity.class);
        startActivity(intent);
    }
}
