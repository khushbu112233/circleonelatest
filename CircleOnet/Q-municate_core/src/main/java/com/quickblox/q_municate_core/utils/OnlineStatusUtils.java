package com.quickblox.q_municate_core.utils;

import android.content.Context;

public class OnlineStatusUtils {

    public static int getOnlineStatus(boolean online) {
        if (online) {
            return com.quickblox.q_municate_core.R.string.frl_online;
        } else {
            return com.quickblox.q_municate_core.R.string.frl_offline;
        }
    }

    public static String getOnlineStatus(Context context, boolean online, String offlineStatus) {
        if (online) {
            return context.getString(com.quickblox.q_municate_core.R.string.frl_online);
        } else {
            return offlineStatus;
        }
    }
}