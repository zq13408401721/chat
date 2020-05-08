package com.mychat.activitys.trends;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.R;
import com.mychat.adapters.TrendsPublishAdapter;
import com.mychat.base.BaseActivity;
import com.mychat.interfaces.IBasePersenter;
import com.mychat.module.vo.TrendsVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendsActivity extends BaseActivity {
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.layout_title)
    ConstraintLayout layoutTitle;
    @BindView(R.id.edit_word)
    EditText editWord;
    @BindView(R.id.recy_imgs)
    RecyclerView recyImgs;

    TrendsPublishAdapter trendsPublishAdapter;
    List<TrendsVo> list;

    @Override
    protected int getLayout() {
        return R.layout.activity_trends_publish;
    }

    @Override
    protected void initView() {

    }

    /**
     * 初始化界面数据
     */
    @Override
    protected void initData() {
        list = new ArrayList<>();
        //判断上一次是否有保存的草稿信息  从sp中去获取 json
        //添加加号按钮
        list.add(getNormalTrendsItem());
        trendsPublishAdapter = new TrendsPublishAdapter(this,list);
        recyImgs.setLayoutManager(new GridLayoutManager(this,3));
        recyImgs.setAdapter(trendsPublishAdapter);
    }

    @Override
    protected IBasePersenter createPersenter() {
        return null;
    }


    /**
     * 加号按钮
     * @return
     */
    private TrendsVo getNormalTrendsItem(){
        TrendsVo trendsVo = new TrendsVo();
        trendsVo.setType(0);
        return trendsVo;
    }
}
