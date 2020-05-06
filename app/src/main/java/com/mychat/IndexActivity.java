package com.mychat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mychat.fragments.home.HomeFragment;
import com.mychat.fragments.own.OwnFragment;
import com.mychat.fragments.trends.TrendsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexActivity extends AppCompatActivity {

    @BindView(R.id.fragment_box)
    FrameLayout fragmentBox;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    HomeFragment homeFragment;
    TrendsFragment trendsFragment;
    OwnFragment ownFragment;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        initView();


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        fragmentTransaction.replace(R.id.fragment_box,homeFragment).commit();
                        break;
                    case R.id.menu_trends:
                        fragmentTransaction.replace(R.id.fragment_box,trendsFragment).commit();
                        break;
                    case R.id.menu_own:
                        fragmentTransaction.replace(R.id.fragment_box,ownFragment).commit();
                        break;
                }
                return false;
            }
        });

        //默认初始化显示第一个页面
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_box,homeFragment).commit();
    }

    private void initView(){
        homeFragment = new HomeFragment();
        trendsFragment = new TrendsFragment();
        ownFragment = new OwnFragment();
    }
}
