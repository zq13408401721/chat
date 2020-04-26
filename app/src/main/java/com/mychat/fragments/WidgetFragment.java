package com.mychat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mychat.R;

public class WidgetFragment extends Fragment {
    RecyclerView recyWidget;

    /**
     * 初始化fragment
     *
     * @return
     */
    public static WidgetFragment getInstance(int pos) {
        WidgetFragment widgetFragment = new WidgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        //bundle.putInt("facetype",faceType);
        widgetFragment.setArguments(bundle);
        return widgetFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_face, container, false);
        recyWidget = view.findViewById(R.id.recyclerview);
        return view;
    }

}
