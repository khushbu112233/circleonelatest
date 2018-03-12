package com.circle8.circleOne.Common.listeners;

public interface FriendOperationListener {

    void onAcceptUserClicked(int position, int userId);

    void onRejectUserClicked(int position, int userId);
}