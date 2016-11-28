package com.ted_developers.triviapatente.app.views.players_list;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.views.expandable_list.DividerItemDecoration;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TPPLayersList<T> extends Fragment {
    // all or friends
    @BindView(R.id.all_button)
    Button allButton;
    @BindView(R.id.friends_button) Button friendsButton;
    @BindDrawable(R.drawable.all_button_not_selected)
    Drawable allButtonNotSelected;
    @BindDrawable(R.drawable.all_button_selected) Drawable allButtonSelected;
    @BindDrawable(R.drawable.friends_button_not_selected) Drawable friendsButtonNotSelected;
    @BindDrawable(R.drawable.friends_button_selected) Drawable friendsButtonSelected;
    @BindColor(R.color.mainColor) @ColorInt
    int mainColor;
    @BindColor(android.R.color.white) @ColorInt int whiteColor;
    // search
    @BindView(R.id.search_bar)
    LinearLayout searchBar;
    // players list
    @BindView(R.id.listView) RecyclerView listView;
    TPPlayersListAdapter<T> adapter;

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

    public void setItems() {
        adapter = new TPPlayersListAdapter<T>();
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.all_button)
    public void allButtonClick() {
        allButton.setBackground(allButtonSelected);
        searchBar.setVisibility(View.VISIBLE);
        friendsButton.setBackground(friendsButtonNotSelected);
        allButton.setTextColor(whiteColor);
        friendsButton.setTextColor(mainColor);
        // todo do stuff
    }

    @OnClick(R.id.friends_button)
    public void friendsButtonClick() {
        friendsButton.setBackground(friendsButtonSelected);
        allButton.setBackground(allButtonNotSelected);
        searchBar.setVisibility(View.GONE);
        allButton.setTextColor(mainColor);
        friendsButton.setTextColor(whiteColor);
        // todo do stuff
    }
}
