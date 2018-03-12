package com.circle8.circleOne.Common.listeners;

public interface SearchListener {

    void prepareSearch();

    void search(String searchQuery);

    void cancelSearch();
}