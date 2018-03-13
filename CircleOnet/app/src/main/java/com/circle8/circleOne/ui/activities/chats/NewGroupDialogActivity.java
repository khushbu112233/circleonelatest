package com.circle8.circleOne.ui.activities.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.circle8.circleOne.Common.ToastUtils;
import com.circle8.circleOne.Common.listeners.SelectUsersListener;
import com.circle8.circleOne.Common.listeners.simple.SimpleOnRecycleItemClickListener;
import com.circle8.circleOne.R;
import com.circle8.circleOne.ui.activities.others.BaseFriendsListActivity;
import com.circle8.circleOne.ui.adapters.friends.FriendsAdapter;
import com.circle8.circleOne.ui.adapters.friends.SelectableFriendsAdapter;
import com.circle8.circleOne.ui.views.recyclerview.SimpleDividerItemDecoration;
import com.circle8.circleOne.Common.ToastUtils;
import com.circle8.circleOne.Common.listeners.SelectUsersListener;
import com.circle8.circleOne.Common.listeners.simple.SimpleOnRecycleItemClickListener;
import com.quickblox.q_municate_user_service.model.QMUser;

import java.util.List;

import butterknife.BindView;

public class NewGroupDialogActivity extends BaseFriendsListActivity implements SelectUsersListener {

    @BindView(R.id.members_edittext)
    EditText membersEditText;

    public static void start(Context context) {
        Intent intent = new Intent(context, NewGroupDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        context.startActivity(intent);
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_new_group;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFields();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpActionBarWithUpButton();
        initCustomListeners();
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        ((SelectableFriendsAdapter) friendsAdapter).setSelectUsersListener(this);
        friendsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));;
    }

    @Override
    protected FriendsAdapter getFriendsAdapter() {
        return new SelectableFriendsAdapter(this, getFriendsList(), true);
    }

    @Override
    protected void performDone() {
        List<QMUser> selectedFriendsList = ((SelectableFriendsAdapter) friendsAdapter).getSelectedFriendsList();
        if (!selectedFriendsList.isEmpty()) {
            CreateGroupDialogActivity.start(this, selectedFriendsList);
        } else {
            ToastUtils.longToast(R.string.new_group_no_friends_for_creating_group);
        }
    }

    @Override
    public void onSelectedUsersChanged(int count, String fullNames) {
        membersEditText.setText(fullNames);
    }

    private void initFields() {
        title = getString(R.string.new_group_title);
    }

    private void initCustomListeners() {
        friendsAdapter.setOnRecycleItemClickListener(new SimpleOnRecycleItemClickListener<QMUser>() {

            @Override
            public void onItemClicked(View view, QMUser entity, int position) {
                ((SelectableFriendsAdapter) friendsAdapter).selectFriend(position);
            }
        });
    }
}