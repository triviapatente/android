package com.ted_developers.triviapatente.app.views.expandable_list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.listElements.TPHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TPExpandableList<T> extends Fragment {
    @BindView(R.id.listTitle) TextView listTitle;
    @BindView(R.id.listCounter) TextView listCounter;
    @BindView(R.id.listView) RecyclerView listView;

    public TPExpandableList() {}

    public static TPExpandableList newInstance() {
        TPExpandableList fragment = new TPExpandableList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tpexpandable_list, container, false);
        ButterKnife.bind(this, v);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    public void setGames(List<T> list, int layout, Class<? extends TPHolder<T>> holderClass) {
        TPExpandableListAdapter<T> adapter = new TPExpandableListAdapter<T>(getContext(), list, layout, holderClass);
        listView.setAdapter(adapter);
    }
}
