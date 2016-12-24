package com.ted_developers.triviapatente.socket.modules.game;

import com.ted_developers.triviapatente.app.utils.custom_classes.callbacks.SocketCallback;
import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.responses.Success;
import com.ted_developers.triviapatente.models.responses.SuccessCategories;
import com.ted_developers.triviapatente.models.responses.SuccessCategory;
import com.ted_developers.triviapatente.models.responses.SuccessInitRound;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Antonio on 08/12/16.
 */
public class GameSocketManager extends BaseSocketManager {
    public void init_round(Long gameID, SocketCallback<SuccessInitRound> initRoundSocketCallback) {
        try {
            JSONObject data = new JSONObject();
            data.put("game", gameID);
            emit("init_round", data, SuccessInitRound.class, initRoundSocketCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void get_proposed_categories(Long gameID, Long roundID, SocketCallback<SuccessCategories> proposedCategoriesCallback) {
        try {
            JSONObject data = new JSONObject();
            data.put("round_id", roundID);
            data.put("game", gameID);
            emit("get_categories", data, SuccessCategories.class, proposedCategoriesCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void choose_category(Long gameID, Long roundID, Long categoryID, SocketCallback<SuccessCategory> chooseCategoryCallback) {
        try {
            JSONObject data = new JSONObject();
            data.put("round_id", roundID);
            data.put("game", gameID);
            data.put("category", categoryID);
            emit("choose_category", data, SuccessCategory.class, chooseCategoryCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
