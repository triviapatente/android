package it.triviapatente.android.app.views.game_page.round_details;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.TPUtils;
import it.triviapatente.android.app.utils.baseActivityClasses.TPGameActivity;
import it.triviapatente.android.app.utils.custom_classes.callbacks.QuizSheetCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.RoundDetailsSectionCallback;
import it.triviapatente.android.app.utils.custom_classes.callbacks.SocketCallback;
import it.triviapatente.android.app.utils.custom_classes.listViews.listElements.normal.RoundHolder;
import it.triviapatente.android.models.game.Category;
import it.triviapatente.android.models.game.Question;
import it.triviapatente.android.models.game.Quiz;
import it.triviapatente.android.models.game.Round;
import it.triviapatente.android.models.responses.Success;
import it.triviapatente.android.models.responses.SuccessRoundDetails;
import it.triviapatente.android.socket.modules.events.GameEndedEvent;
import it.triviapatente.android.socket.modules.events.GameLeftEvent;
import it.triviapatente.android.socket.modules.events.QuestionAnsweredEvent;
import it.triviapatente.android.socket.modules.game.GameSocketManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;

public class RoundDetailsActivity extends TPGameActivity {
    // ads
    private InterstitialAd mInterstitialAd;
    @BindString(R.string.admob_interstitial) String adMobInterstitialID;

    @BindView(R.id.sectionList) RecyclerView sectionList;
    @BindView(R.id.answerList) RecyclerView answerList;
    @BindView(R.id.roundDetailsSheet) View roundDetailsBottomSheet;

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
        int lastOfRound = (section + 1) * NUMBER_OF_QUESTIONS_PER_ROUND - 1;
        int visiblePosition = answerLayout.findFirstCompletelyVisibleItemPosition();
        if(firstOfRound <= visiblePosition) return firstOfRound;
        else return lastOfRound;
    }

    private RoundDetailsSectionCallback sectionListener = new RoundDetailsSectionCallback() {
        @Override
        public void onSelected(int section, Boolean needsScroll) {
            if(response != null) {
                if(needsScroll) {
                    answerList.stopScroll();
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
                    scrollListener.updateRoundPosition(target);
                    smoothScroller.setTargetPosition(target);
                    answerLayout.startSmoothScroll(smoothScroller);
                }
            }
            if(section < answerMap.size()) {
                Round round = roundFor(section + 1);
                Category category = categoryFor(round);
                gameHeader.setHeader(round, category);
            } else {
                gameHeader.endedGameHeader();
            }
        }
    };

    private LinearLayoutManager answerLayout = new LinearLayoutManager(this);
    private RoundDetailsQuestionAdapter answerAdapter = new RoundDetailsQuestionAdapter(this);
    private LinearLayoutManager sectionLayout = new LinearLayoutManager(this);
    private RoundDetailsSectionAdapter sectionAdapter = new RoundDetailsSectionAdapter(this, sectionListener);

    private RoundDetailsScrollListener scrollListener;



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
        answerAdapter.setOnItemSelectListener(new QuizSheetCallback(roundDetailsBottomSheet));
        answerList.setAdapter(answerAdapter);
        scrollListener = new RoundDetailsScrollListener(NUMBER_OF_QUESTIONS_PER_ROUND, answerList, sectionList);
        scrollListener.setRoundDetailsSectionCallback(sectionListener);
        answerList.addOnScrollListener(scrollListener);
        fromGameOptions = getIntent().getBooleanExtra(extraKeyFromGame, false);
        this.decideToShowUserLeftMessage();
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
        load();

        gameSocketManager.listenUserAnswered(new SocketCallback<QuestionAnsweredEvent>() {
            @Override
            public void response(final QuestionAnsweredEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.answers == null) response.answers = new ArrayList<>(); // can be null
                        response.answers.add(event.answer);
                        detailsScore.set(RoundDetailsActivity.this, response.answers);
                        answerMap = computeMap();
                        answerAdapter.notifyDataSetChanged(response, opponent);
                        scrollListener.refreshAdapter(answerAdapter);
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
                        detailsScore.setResponse(response, RoundDetailsActivity.this);
                        answerAdapter.notifyDataSetChanged(response, opponent);
                        scrollListener.refreshAdapter(answerAdapter);
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
                        detailsScore.setResponse(response, RoundDetailsActivity.this);
                        answerAdapter.notifyDataSetChanged(response, opponent);
                        scrollListener.refreshAdapter(answerAdapter);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.success) {
                                RoundDetailsActivity.this.response = response;
                                answerMap = computeMap();
                                setToolbarTitle(getToolbarTitle());
                                detailsScore.set(RoundDetailsActivity.this, opponent, response.answers);
                                detailsScore.setResponse(response, RoundDetailsActivity.this);
                                sectionList.setVisibility(View.VISIBLE);
                                detailsScore.getView().setVisibility(View.VISIBLE);
                                answerList.setVisibility(View.VISIBLE);
                                sectionAdapter.notifyDataSetChanged(response, answerMap);
                                answerAdapter.notifyDataSetChanged(response, opponent);
                                scrollListener.refreshAdapter(answerAdapter);
                                sectionList.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int section = answerMap.size() - (response.game.ended ? 0 : 1);
                                        RoundHolder holder = (RoundHolder) sectionList.findViewHolderForAdapterPosition(section);
                                        if (holder != null) holder.select(true, true);

                                    }
                                });
                                // display ads
                                if (System.currentTimeMillis() - response.game.getUpdatedAtMillis() < 60 * 1000 && response.game.ended) {
                                    displayInterstitial();
                                }
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), httpConnectionError, Snackbar.LENGTH_INDEFINITE)
                                        .setAction(httpConnectionErrorRetryButton, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                load();
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mInterstitialAd != null)
            mInterstitialAd.setAdListener(null);
    }

    @Override
    protected void customReinit() {
        load();
    }

    private void initAdView() {
        mInterstitialAd = new InterstitialAd(RoundDetailsActivity.this);
        mInterstitialAd.setAdUnitId(adMobInterstitialID);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
    }

    private void displayInterstitial() {
        if(mInterstitialAd == null) initAdView();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
}
