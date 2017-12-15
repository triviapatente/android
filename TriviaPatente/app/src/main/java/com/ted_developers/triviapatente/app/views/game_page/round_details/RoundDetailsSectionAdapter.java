package com.ted_developers.triviapatente.app.views.game_page.round_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.RoundDetailsSectionCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.RoundHolder;
import com.ted_developers.triviapatente.models.game.Question;
import com.ted_developers.triviapatente.models.game.Quiz;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by donadev on 07/12/17.
 */

class RoundDetailsSectionAdapter extends RecyclerView.Adapter<RoundHolder> {

    private String winnerEmojii;

    public void setWinnerEmojii(String value) {
        this.winnerEmojii = value;
    }

    private Map<String, List<Quiz>> answerMap;
    private SuccessRoundDetails response;
    private Context context;
    private RoundDetailsSectionCallback sectionListener;

    public void notifyDataSetChanged(SuccessRoundDetails response, Map<String, List<Quiz>> answerMap) {
        this.answerMap = answerMap;
        this.response = response;
        this.notifyDataSetChanged();
    }

    public RoundDetailsSectionAdapter(Context context, RoundDetailsSectionCallback listener) {
        this.context = context;
        this.sectionListener = listener;
    }

    private Comparator<String> mComparator = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            Long left = Long.parseLong(lhs), right = Long.parseLong(rhs);
            if(left > right) return 1;
            else if(right > left) return -1;
            return 0;
        }
    };

    private List<String> getKeys() {
        List<String> output = new ArrayList<>(answerMap.keySet());
        Collections.sort(output, mComparator);
        return output;
    }

    @Override
    public RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RoundHolder.newHolder(context, this);
    }

    @Override
    public void onBindViewHolder(RoundHolder holder, int position) {
        List<String> keys = getKeys();
        String item = keys.size() == position ? winnerEmojii : keys.get(position);
        holder.bind(item, sectionListener);
    }

    @Override
    public int getItemCount() {
        int increment = 0;
        if(response != null && response.game != null && response.game.ended) {
            increment += 1;
        }
        return getKeys().size() + increment;
    }
}

