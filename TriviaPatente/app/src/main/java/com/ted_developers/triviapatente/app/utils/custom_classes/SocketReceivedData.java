package com.ted_developers.triviapatente.app.utils.custom_classes;

import com.ted_developers.triviapatente.models.auth.Hints;
import com.ted_developers.triviapatente.models.game.Invite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 11/12/16.
 */
public class SocketReceivedData {
    public static List<Invite> pendingInvites = new ArrayList<>();
    public static Integer global_rank_position, friend_rank_position;
    public static List<Hints> statsHints = new ArrayList<>();

    
}
