package com.ted_developers.triviapatente.socket.modules.game;

import android.util.Log;
import android.util.Pair;

import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessAnsweredCorrectly;
import com.ted_developers.triviapatente.models.responses.SuccessCategories;
import com.ted_developers.triviapatente.models.responses.SuccessCategory;
import com.ted_developers.triviapatente.models.responses.SuccessInitRound;
import com.ted_developers.triviapatente.models.responses.SuccessQuizzes;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Antonio on 08/12/16.
 */
public class GameSocketManager extends BaseSocketManager {
    public void init_round(Long gameID, SocketCallback<SuccessInitRound> initRoundSocketCallback) {
        emit("init_round",
                buildJSONObject(
                        new Pair<>("game", (Object) gameID)),
                SuccessInitRound.class, initRoundSocketCallback);
    }

    public void get_proposed_categories(Long gameID, Long roundID, SocketCallback<SuccessCategories> proposedCategoriesCallback) {
        emit("get_categories",
                buildJSONObject(
                        new Pair<>("game", (Object) gameID),
                        new Pair<>("round_id", (Object) roundID)),
                SuccessCategories.class, proposedCategoriesCallback);
    }

    public void choose_category(Long gameID, Long roundID, Long categoryID, SocketCallback<SuccessCategory> chooseCategoryCallback) {
        emit("choose_category",
                buildJSONObject(
                        new Pair<>("game", (Object) gameID),
                        new Pair<>("round_id", (Object) roundID),
                        new Pair<>("category", (Object) categoryID)),
                SuccessCategory.class, chooseCategoryCallback);
    }

    public void get_questions(Long gameID, Long roundID, SocketCallback<SuccessQuizzes> getQuestionsCallback) {
        emit("get_questions",
                buildJSONObject(
                        new Pair<>("game", (Object) gameID),
                        new Pair<>("round_id", (Object) roundID)),
                SuccessQuizzes.class, getQuestionsCallback);
    }

    public void answer(Long gameID, Long roundID, Long quizID, Boolean answer, SocketCallback<SuccessAnsweredCorrectly> answerCallback) {
        emit("answer",
                buildJSONObject(
                        new Pair<>("game", (Object) gameID),
                        new Pair<>("round_id", (Object) roundID),
                        new Pair<>("quiz_id", (Object) quizID),
                        new Pair<>("answer", (Object) answer)),
                SuccessAnsweredCorrectly.class, answerCallback);
    }
}
