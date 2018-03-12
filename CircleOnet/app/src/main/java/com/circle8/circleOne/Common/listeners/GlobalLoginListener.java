package com.circle8.circleOne.Common.listeners;

public interface GlobalLoginListener {

    void onCompleteQbLogin();

    void onCompleteQbChatLogin();

    void onCompleteWithError(String error);
}