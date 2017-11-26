package com.ted_developers.triviapatente.app.views.game_page.round_details;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.baseActivityClasses.TPGameActivity;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.RoundDetailsSectionCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SimpleItemCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.TPCallback;
import com.ted_developers.triviapatente.models.auth.User;
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

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

public class RoundDetailsActivity extends TPGameActivity {
    @BindView(R.id.sectionList) RecyclerView sectionList;
    @BindView(R.id.answerList) RecyclerView answerList;
    @BindView(R.id.gameEndedView) View gameEndedView;
    private FragmentGameDetailsScore detailsScore;

    private SuccessRoundDetails response;
    private Boolean fromGameOptions;

    private Map<String, List<Quiz>> answerMap = new HashMap<>();

    private LinearLayoutManager answerLayout = new LinearLayoutManager(this);
    private LinearLayoutManager sectionLayout = new LinearLayoutManager(this);

    private final int NUMBER_OF_ROUNDS = 4;

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
                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext()) {
                        @Override protected int getVerticalSnapPreference() {
                            return LinearSmoothScroller.SNAP_TO_START;
                        }

                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return new PointF(0, answerList.getHeight() / 4 * targetPosition);
                        }
                    };
                    smoothScroller.setTargetPosition(section * NUMBER_OF_ROUNDS);
                    answerLayout.startSmoothScroll(smoothScroller);
                }
            }
        }
    };
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(dy == 0) return;
            int position = answerLayout.findFirstCompletelyVisibleItemPosition();
            int roundPosition = position / NUMBER_OF_ROUNDS;
            RoundHolder holder = (RoundHolder) sectionList.findViewHolderForAdapterPosition(roundPosition);
            holder.select(false);
        }
    };

    private RecyclerView.Adapter<RoundHolder> sectionAdapter = new RecyclerView.Adapter<RoundHolder>() {
        @Override
        public RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return RoundHolder.newHolder(RoundDetailsActivity.this, sectionAdapter);
        }

        public List<String> getKeys() {
            List<String> output = new ArrayList<>(answerMap.keySet());
            Collections.sort(output, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    Long left = Long.parseLong(lhs),
                            right = Long.parseLong(rhs);
                    if(left > right) {
                        return 1;
                    } else if(right > left) {
                        return -1;
                    }
                    return 0;
                }
            });
            return output;
        }

        @Override
        public void onBindViewHolder(RoundHolder holder, int position) {
            List<String> keys = getKeys();
            String item = keys.size() == position ? "🏆" : keys.get(position);
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
    };
    private RecyclerView.Adapter<RecyclerView.ViewHolder> questionAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private final int TYPE_QUESTION = 1;
        private final int TYPE_GAME_ENDED = 2;
        @Override
        public int getItemViewType(int position) {
            if(position == response.quizzes.size()) return TYPE_GAME_ENDED;
            return TYPE_QUESTION;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_QUESTION)
                return QuestionHolder.newHolder(RoundDetailsActivity.this, answerList.getHeight() / 4);
            else
                return GameEndedHolder.newHolder(RoundDetailsActivity.this, answerList.getHeight());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof QuestionHolder) {
                Quiz quiz = response.quizzes.get(position);
                ((QuestionHolder)holder).bind(quiz, opponent);
            } else if(holder instanceof GameEndedHolder) {
                ((GameEndedHolder)holder).bind(response, opponent);
            }
        }

        @Override
        public int getItemCount() {
            if(response != null && response.quizzes != null) {
                int increment = 0;
                if(response.game != null && response.game.ended) {
                    increment += 1;
                }
                return response.quizzes.size() + increment;
            }
            return 0;
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
            if(r.id == quiz.round_id) {
                return r;
            }
        }
        return null;
    }
    private Round roundFor(int number) {
        for(Round r : response.rounds) {
            if(r.number == number) {
                return r;
            }
        }
        return null;
    }
    private Category categoryFor(Round round) {
        if(round == null) return null;
        for(Category c : response.categories) {
            if(c.id == round.cat_id) {
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

    private GameEndedHolder gameEndedHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_details);
        sectionList.setLayoutManager(sectionLayout);
        sectionList.setAdapter(sectionAdapter);
        answerList.setLayoutManager(answerLayout);
        answerList.setAdapter(questionAdapter);
        answerList.addOnScrollListener(scrollListener);
        gameEndedHolder = new GameEndedHolder(gameEndedView, this);
        gameEndedHolder.setUser(opponent, 2);
        fromGameOptions = getIntent().getBooleanExtra(getString(R.string.extra_string_from_game_options), false);
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
        return getString(R.string.title_activity_round_details);
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
    public void listen() {
        gameSocketManager.listenRoundStarted(new SocketCallback<RoundStartedEvent>() {
            @Override
            public void response(final RoundStartedEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        response.categories.add(event.category);
                        detailsScore.add(event.answers);
                        response.answers.addAll(event.answers);
                        answerMap = computeMap();
                        sectionAdapter.notifyDataSetChanged();
                        questionAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        gameSocketManager.listenUserAnswered(new SocketCallback<QuestionAnsweredEvent>() {
            @Override
            public void response(final QuestionAnsweredEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        detailsScore.add(event.answer);
                        response.answers.add(event.answer);
                        answerMap = computeMap();
                        questionAdapter.notifyDataSetChanged();
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
                        questionAdapter.notifyDataSetChanged();
                        sectionAdapter.notifyDataSetChanged();
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
                        questionAdapter.notifyDataSetChanged();
                        sectionAdapter.notifyDataSetChanged();
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
                            detailsScore.set(opponent, response.answers);
                            sectionList.setVisibility(View.VISIBLE);
                            detailsScore.getView().setVisibility(View.VISIBLE);
                            answerList.setVisibility(View.VISIBLE);
                            gameEndedView.setVisibility(View.GONE);
                            sectionAdapter.notifyDataSetChanged();
                            questionAdapter.notifyDataSetChanged();
                            sectionListener.onSelected(0    ,true);
                        }
                    });
                }
            }
        });
    }
}
