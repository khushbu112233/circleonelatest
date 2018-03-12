package com.circle8.circleOne.Common.listeners;

public interface UserStatusChangingListener {

    void onChangedUserStatus(int userId, boolean online);
}