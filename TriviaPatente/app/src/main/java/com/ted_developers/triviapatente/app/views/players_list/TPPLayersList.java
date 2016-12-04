package com.ted_developers.triviapatente.app.views.players_list;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.adapters.TPListAdapter;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.footer.TPFooter;
import com.ted_developers.triviapatente.app.views.expandable_list.DividerItemDecoration;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TPPLayersList<T> extends Fragment {
    @BindColor(R.color.mainColor) @ColorInt int mainColor;
    // players list
    @BindView(R.id.listView) RecyclerView listView;
    TPListAdapter<T> adapter;

    public TPPLayersList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tpplayers_list, container, false);
        ButterKnife.bind(this, v);
        listView.addItemDecoration(new DividerItemDecoration(mainColor, v.getWidth()));
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    public void setItems(List<T> list,
                         @LayoutRes int holderLayout, Class<? extends TPHolder<T>> holderClass,
                         @LayoutRes int footerLayout, Class<? extends TPFooter> footerClass,
                         int elementHeight) {
        adapter = new TPListAdapter<>(getContext(), list, holderLayout, holderClass, footerLayout, footerClass, elementHeight, listView);
        listView.setAdapter(adapter);
    }
}
