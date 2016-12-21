package com.ted_developers.triviapatente.app.utils;

import android.util.Log;

import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.game.Category;
import com.ted_developers.triviapatente.models.game.Game;
import com.ted_developers.triviapatente.models.game.Invite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
public class ReceivedData {
    // invites
    public static List<Invite> pendingInvites = null;
    public static Integer numberOfInvites = new Integer(0);
    // rank
    public static Integer global_rank_position = new Integer(0), friends_rank_position = new Integer(0);
    // stats
    public static Category[] statsHints = null;
    // recent games
    public static List<Game> recentGames = null;


    // manage invites
    public static void removeInvite(Invite element) {
        if(pendingInvites == null) {
            pendingInvites = new ArrayList<>();
        }
        pendingInvites.remove(element);
        numberOfInvites = pendingInvites.size();
    }
    public static void addInvite(Invite element) {
        if(pendingInvites != null) {
            pendingInvites.add(0, element);
            numberOfInvites = pendingInvites.size();
        } else {
            numberOfInvites++;
        }
    }

}
