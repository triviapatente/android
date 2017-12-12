package com.ted_developers.triviapatente.app.views.game_page.round_details;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.TPUtils;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.RoundDetailsSectionCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.GameEndedHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.QuestionHolder;
import com.ted_developers.triviapatente.app.utils.custom_classes.listViews.listElements.normal.RoundHolder;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Question;
import com.ted_developers.triviapatente.models.game.Quiz;
import com.ted_developers.triviapatente.models.game.Round;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessRoundDetails;
import com.ted_developers.triviapatente.socket.modules.events.GameEndedEvent;
import com.ted_developers.triviapatente.socket.modules.events.GameLeftEvent;
import com.ted_developers.triviapatente.socket.modules.events.QuestionAnsweredEvent;
import com.ted_developers.triviapatente.socket.modules.events.RoundStartedEvent;
import com.ted_developers.triviapatente.socket.modules.game.GameSocketManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;

public class RoundDetailsActivity extends TPGameActivity {
    @BindView(R.id.sectionList) RecyclerView sectionList;
    @BindView(R.id.answerList) RecyclerView answerList;
    @BindString(R.string.activity_round_details_emojii_winner) String winnerEmojii;
    @BindString(R.string.activity_round_details_user_has_left) String opponentHasLeftMessage;
    @BindString(R.string.activity_round_details_user_annulled) String opponentAnnulledMessage;
    @BindString(R.string.title_activity_round_details) String activityTitle;
    @BindString(R.string.extra_string_from_game_options) String extraKeyFromGame;
    @BindString(R.string.extra_string_opponent_has_left) String extraKeyOpponentHasLeft;
    @BindString(R.string.extra_string_opponent_annulled) String extraKeyOpponentAnnulled;

    @BindInt(R.integer.number_of_questions_per_round) int NUMBER_OF_QUESTIONS_PER_ROUND;

    @BindString(R.string.socket_event_game_ended) String gameEndedEvent;
    @BindString(R.string.socket_event_user_left_game) String userLeftGameEvent;
    @BindString(R.string.socket_event_user_answered) String userAnsweredGameEvent;

    private FragmentGameDetailsScore detailsScore;

    private SuccessRoundDetails response;
    private Boolean fromGameOptions;

    private Map<String, List<Quiz>> answerMap = new HashMap<>();

    private void decideToShowUserLeftMessage() {
        Boolean opponentHasLeft = getIntent().getBooleanExtra(extraKeyOpponentHasLeft, false);
        Boolean opponentAnnulled = getIntent().getBooleanExtra(extraKeyOpponentAnnulled, false);

        if(opponentHasLeft) {
            Toast.makeText(this, opponentHasLeftMessage, Toast.LENGTH_LONG).show();
        } else if(opponentAnnulled) {
            Toast.makeText(this, opponentAnnulledMessage, Toast.LENGTH_LONG).show();
        }
    }

    private int getScrollPositionFor(Integer section) {
        if(section == answerMap.keySet().size()) return section * NUMBER_OF_QUESTIONS_PER_ROUND;

        int firstOfRound = section * NUMBER_OF_QUESTIONS_PER_ROUND;
        int lastOfRound = (section + 1) * (NUMBER_OF_QUESTIONS_PER_ROUND) - 1;
        int visiblePosition = answerLayout.findFirstCompletelyVisibleItemPosition();
        if(firstOfRound <= visiblePosition) return firstOfRound;
        else return lastOfRound;
    }
    private RoundDetailsSectionCallback sectionListener = new RoundDetailsSectionCallback() {
        @Override
        public void onSelected(int section, Boolean needsScroll) {
            if(response != null) {
                if(section < answerMap.size()) {
                    Round round = roundFor(section + 1);
                    Category category = categoryFor(round);
                    gameHeader.setHeader(round, category);
                } else {
                    gameHeader.endedGameHeader();
                }
                if(needsScroll) {
                    int target = getScrollPositionFor(section);
                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext()) {
                        @Override protected int getVerticalSnapPreference() {
                            return LinearSmoothScroller.SNAP_TO_START;
                        }

                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return new PointF(0, 0);
                        }
                    };
                    smoothScroller.setTargetPosition(target);
                    answerLayout.startSmoothScroll(smoothScroller);
                }
            }
        }
    };

    private LinearLayoutManager answerLayout = new LinearLayoutManager(this);
    private RoundDetailsQuestionAdapter answerAdapter = new RoundDetailsQuestionAdapter(this);
    private LinearLayoutManager sectionLayout = new LinearLayoutManager(this);
    private RoundDetailsSectionAdapter sectionAdapter = new RoundDetailsSectionAdapter(this, sectionListener);

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(dy == 0) return;
            int position = answerLayout.findFirstCompletelyVisibleItemPosition();
            if(position != -1) {
                int roundPosition = position / NUMBER_OF_QUESTIONS_PER_ROUND;
                RoundHolder holder = (RoundHolder) sectionList.findViewHolderForAdapterPosition(roundPosition);
                if (holder != null) holder.select(false);
            }
        }
    };



    private List<Question> getAnswersFor(Quiz quiz) {
        List<Question> output = new ArrayList<>();
        for(Question answer : response.answers) {
            if(answer.quiz_id.equals(quiz.id)) {
                output.add(answer);
            }
        }
        return output;
    }
    private Round roundFor(Quiz quiz) {
        for(Round r : response.rounds) {
            if(r.id.equals(quiz.round_id)) {
                return r;
            }
        }
        return null;
    }
    private Round roundFor(int number) {
        for(Round r : response.rounds) {
            if(r.number.equals(number)) {
                return r;
            }
        }
        return null;
    }
    private Category categoryFor(Round round) {
        if(round == null) return null;
        for(Category c : response.categories) {
            if(c.id.equals(round.cat_id)) {
                return c;
            }
        }
        return null;
    }
    private Map<String, List<Quiz>> computeMap() {
        Map<String, List<Quiz>> map = new HashMap<>();
        for(Quiz quiz : response.quizzes) {
            Round round = roundFor(quiz);
            String key = Long.toString(round.number);
            if(!map.containsKey(key)) {
                map.put(key, new ArrayList<Quiz>());
            }
            quiz.answers = getAnswersFor(quiz);

            List<Quiz> quizzes = map.get(key);
            quizzes.add(quiz);
            map.put(key, quizzes);
        }
        return map;
    }

    private void updateEmojiAndSet() {
        winnerEmojii = TPUtils.translateEmoticons(winnerEmojii);
        sectionAdapter.setWinnerEmojii(winnerEmojii);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_details);
        // emojii update
        updateEmojiAndSet();
        sectionList.setLayoutManager(sectionLayout);
        sectionList.setAdapter(sectionAdapter);
        answerList.setLayoutManager(answerLayout);
        answerAdapter.setAnswerList(answerList);
        answerList.setAdapter(answerAdapter);
        answerList.addOnScrollListener(scrollListener);
        fromGameOptions = getIntent().getBooleanExtra(extraKeyFromGame, false);
        this.decideToShowUserLeftMessage();
        this.load();
        this.joinAndListen();
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        // init fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        detailsScore = (FragmentGameDetailsScore) fragmentManager.findFragmentById(R.id.gameScore);
        detailsScore.getView().setVisibility(View.GONE);
    }

    @Override
    protected String getToolbarTitle() {
        if(opponent != null) return opponent.username;
        return activityTitle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoundHolder.finalizeHolder();
    }

    @Override
    protected int getBackButtonVisibility(){
        return View.VISIBLE;
    }
    @Override
    protected int getHeartCounterVisibility() { return View.GONE; }

    @Override
    public void onBackPressed() {
        if(fromGameOptions)
            this.finish();
        else super.onBackPressed();
    }
    private GameSocketManager gameSocketManager = new GameSocketManager();

    public void joinAndListen() {
        gameSocketManager.join(gameID, new SocketCallback<Success>() {
            @Override
            public void response(Success response) {
                listen();
            }
        });
    }
    private void unlisten() {
        gameSocketManager.stopListen(userAnsweredGameEvent, userLeftGameEvent, gameEndedEvent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        this.unlisten();
    }

    public void listen() {
        gameSocketManager.listenUserAnswered(new SocketCallback<QuestionAnsweredEvent>() {
            @Override
            public void response(final QuestionAnsweredEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        response.answers.add(event.answer);
                        detailsScore.set(RoundDetailsActivity.this, response.answers);
                        answerMap = computeMap();
                        answerAdapter.notifyDataSetChanged(response, opponent);
                    }
                });
            }
        });
        gameSocketManager.listenGameEnded(new SocketCallback<GameEndedEvent>() {
            @Override
            public void response(final GameEndedEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        response.game.ended = true;
                        response.game.winner_id = event.winnerId;
                        response.partecipations = event.partecipations;
                        answerAdapter.notifyDataSetChanged(response, opponent);
                        sectionAdapter.notifyDataSetChanged(response, answerMap);
                    }
                });
            }
        });
        gameSocketManager.listenGameLeft(new SocketCallback<GameLeftEvent>() {
            @Override
            public void response(final GameLeftEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        response.game.ended = true;
                        response.game.winner_id = event.winnerId;
                        response.partecipations = event.partecipations;
                        answerAdapter.notifyDataSetChanged(response, opponent);
                        sectionAdapter.notifyDataSetChanged(response, answerMap);
                    }
                });
            }
        });
    }
    public void load() {
        gameSocketManager.round_details(gameID, new SocketCallback<SuccessRoundDetails>() {
            @Override
            public void response(final SuccessRoundDetails response) {
                if(response.success) {
                    RoundDetailsActivity.this.response = response;
                    answerMap = computeMap();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setToolbarTitle(getToolbarTitle());
                            detailsScore.set(RoundDetailsActivity.this, opponent, response.answers);
                            sectionList.setVisibility(View.VISIBLE);
                            detailsScore.getView().setVisibility(View.VISIBLE);
                            answerList.setVisibility(View.VISIBLE);
                            sectionAdapter.notifyDataSetChanged(response, answerMap);
                            answerAdapter.notifyDataSetChanged(response, opponent);
                            sectionListener.onSelected(0    ,true);
                        }
                    });
                }
            }
        });
    }
}
