package com.circle8.circleOne.ui.fragments.search;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.circle8.circleOne.Common.KeyboardUtils;
import com.circle8.circleOne.Common.listeners.SearchListener;
import com.circle8.circleOne.Common.listeners.UserOperationListener;
import com.circle8.circleOne.Common.listeners.simple.SimpleOnRecycleItemClickListener;
import com.circle8.circleOne.Fragments.SortFragment;
import com.circle8.circleOne.Helper.LoginSession;
import com.circle8.circleOne.Model.FriendConnection;
import com.circle8.circleOne.R;
import com.circle8.circleOne.Utils.Utility;
import com.circle8.circleOne.ui.activities.profile.UserProfileActivity;
import com.circle8.circleOne.ui.adapters.search.GlobalSearchAdapter;
import com.circle8.circleOne.ui.fragments.base.BaseFragment;
import com.circle8.circleOne.ui.fragments.dialogs.base.OneButtonDialogFragment;
import com.circle8.circleOne.ui.views.recyclerview.SimpleDividerItemDecoration;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.q_municate_core.core.command.Command;
import com.quickblox.q_municate_core.models.AppSession;
import com.quickblox.q_municate_core.qb.commands.friend.QBAddFriendCommand;
import com.quickblox.q_municate_core.service.QBService;
import com.quickblox.q_municate_core.service.QBServiceConsts;
import com.quickblox.q_municate_core.utils.ConstsCore;
import com.quickblox.q_municate_db.managers.DataManager;
import com.quickblox.q_municate_db.managers.base.BaseManager;
import com.quickblox.q_municate_user_service.QMUserService;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.model.QBUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnTouch;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.circle8.circleOne.Utils.Utility.CustomProgressDialog;
import static com.circle8.circleOne.Utils.Utility.convertInputStreamToString;
import static com.circle8.circleOne.Utils.Utility.dismissProgress;

public class GlobalSearchFragment extends BaseFragment implements SearchListener, SwipyRefreshLayout.OnRefreshListener {

    private static final String TAG = GlobalSearchFragment.class.getSimpleName();
    private static final int SEARCH_DELAY = 1000;
    private static final int MIN_VALUE_FOR_SEARCH = 3;

    @BindView(R.id.contacts_swipyrefreshlayout)
    SwipyRefreshLayout swipyRefreshLayout;

    @BindView(R.id.contacts_recyclerview)
    RecyclerView contactsRecyclerView;

    private Timer searchTimer;
    private int page = 1;
    private int totalEntries;
    private UserOperationAction userOperationAction;
    private DataManager dataManager;
    private Observer commonObserver;
    private GlobalSearchAdapter globalSearchAdapter;
    private List<QBUser> usersList;
    private String searchQuery;
    private boolean excludedMe;
    private String progressStatus = "FIRST";
    public static ArrayList<FriendConnection> allTaggs;
    LoginSession session;
    static String UserId = "";

    public static GlobalSearchFragment newInstance() {
        return new GlobalSearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_global_search, container, false);

        activateButterKnife(view);

        initFields();
        initContactsList(usersList);
        initCustomListeners();
        allTaggs = new ArrayList<>();
        addActions();
        addObservers();
        session = new LoginSession(getContext());
        HashMap<String, String> user = session.getUserDetails();
        UserId = user.get(LoginSession.KEY_USERID);
        new HttpAsyncTask().execute(Utility.BASE_URL+"GetFriendConnection");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        globalSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeActions();
        deleteObservers();
    }

    @OnTouch(R.id.contacts_recyclerview)
    boolean touchContactsList(View view, MotionEvent event) {
        KeyboardUtils.hideKeyboard(baseActivity);
        return false;
    }

    @Override
    public void prepareSearch() {
        clearOldData();
    }

    @Override
    public void search(String searchQuery) {
        this.searchQuery = searchQuery;
        clearOldData();

        if (!baseActivity.checkNetworkAvailableWithError()) {
            return;
        }

        startSearch();
        if (globalSearchAdapter != null && !globalSearchAdapter.getAllItems().isEmpty()) {
            globalSearchAdapter.setFilter(searchQuery);
        }
    }

    @Override
    public void cancelSearch() {
        searchQuery = null;
        searchTimer.cancel();
        clearOldData();

        if (globalSearchAdapter != null) {
            updateList();
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
        if (!usersList.isEmpty() && usersList.size() < totalEntries) {
            page++;
            searchUsers();
        } else {
            swipyRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onConnectedToService(QBService service) {
        super.onConnectedToService(service);
        if (friendListHelper != null && globalSearchAdapter != null) {
            globalSearchAdapter.setFriendListHelper(friendListHelper);
        }
    }

    @Override
    public void onChangedUserStatus(int userId, boolean online) {
        super.onChangedUserStatus(userId, online);
        globalSearchAdapter.notifyDataSetChanged();
    }

    private void initFields() {
        dataManager = DataManager.getInstance();
        searchTimer = new Timer();
        usersList = new ArrayList<>();
        userOperationAction = new UserOperationAction();
        commonObserver = new CommonObserver();
        swipyRefreshLayout.setEnabled(false);
    }

    private void initContactsList(List<QBUser> usersList) {
        globalSearchAdapter = new GlobalSearchAdapter(baseActivity, usersList);
        globalSearchAdapter.setFriendListHelper(friendListHelper);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));;
        contactsRecyclerView.setAdapter(globalSearchAdapter);
        globalSearchAdapter.setUserOperationListener(userOperationAction);
    }

    private void initCustomListeners() {
        globalSearchAdapter.setOnRecycleItemClickListener(new SimpleOnRecycleItemClickListener<QBUser>() {

            @Override
            public void onItemClicked(View view, QBUser user, int position) {
                boolean isFriend = dataManager.getFriendDataManager().existsByUserId(user.getId());
                boolean outgoingUser = dataManager.getUserRequestDataManager()
                        .existsByUserId(user.getId());
                if (isFriend || outgoingUser) {
                    UserProfileActivity.start(baseActivity, user.getId());
                }
            }
        });

        swipyRefreshLayout.setOnRefreshListener(this);
    }

    private void updateList() {
        globalSearchAdapter.setList(usersList);
    }

    private void updateContactsList(List<QBUser> usersList) {
        this.usersList = usersList;
        globalSearchAdapter.setList(usersList);
        globalSearchAdapter.setFilter(searchQuery);
    }

    private void removeActions() {
        baseActivity.removeAction(QBServiceConsts.FIND_USERS_SUCCESS_ACTION);
        baseActivity.removeAction(QBServiceConsts.FIND_USERS_FAIL_ACTION);

        baseActivity.removeAction(QBServiceConsts.ADD_FRIEND_SUCCESS_ACTION);
        baseActivity.removeAction(QBServiceConsts.ADD_FRIEND_FAIL_ACTION);

        baseActivity.updateBroadcastActionList();
    }

    private void addActions() {
        baseActivity.addAction(QBServiceConsts.FIND_USERS_SUCCESS_ACTION, new FindUserSuccessAction());
        baseActivity.addAction(QBServiceConsts.FIND_USERS_FAIL_ACTION, new FindUserFailAction());

        baseActivity.addAction(QBServiceConsts.ADD_FRIEND_SUCCESS_ACTION, new AddFriendSuccessAction());
        baseActivity.addAction(QBServiceConsts.ADD_FRIEND_FAIL_ACTION, failAction);

        baseActivity.updateBroadcastActionList();
    }

    private void addObservers() {
        dataManager.getUserRequestDataManager().addObserver(commonObserver);
        dataManager.getFriendDataManager().addObserver(commonObserver);
    }

    private void deleteObservers() {
        if(dataManager != null) {
            dataManager.getUserRequestDataManager().deleteObserver(commonObserver);
            dataManager.getFriendDataManager().deleteObserver(commonObserver);
        }
    }

    private void clearOldData() {
        usersList.clear();
        page = 1;
        excludedMe = false;
    }

    private void startSearch() {
        searchTimer.cancel();
        searchTimer = new Timer();
        searchTimer.schedule(new SearchTimerTask(), SEARCH_DELAY);
    }

    private void searchUsers() {
        if (!TextUtils.isEmpty(searchQuery) && checkSearchDataWithError(searchQuery)) {

            QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
            requestBuilder.setPage(page);
            requestBuilder.setPerPage(ConstsCore.FL_FRIENDS_PER_PAGE);

            QMUserService.getInstance().getUsersByFullName(searchQuery, requestBuilder, true).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new rx.Observer<List<QMUser>>() {

                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError" + e.getMessage());
                            swipyRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onNext(List<QMUser> qbUsers) {

                            if (qbUsers != null && !qbUsers.isEmpty()) {
                                checkForExcludeMe(qbUsers);

                                for (int i = 0; i< allTaggs.size(); i++){
                                    if (allTaggs.get(i).getQ_ID().toString().equalsIgnoreCase(qbUsers.get(i).getId().toString())){
                                        usersList.add(qbUsers.get(i));
                                    }
                                }


                                updateContactsList(usersList);
                            }

                            swipyRefreshLayout.setRefreshing(false);
                            checkForEnablingRefreshLayout();
                        }
                    });
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            if (progressStatus.equalsIgnoreCase("FIRST"))
            {
                String loading = "Fetching Friends" ;
                CustomProgressDialog(loading, getActivity());
                progressStatus = "SECOND";
            }


        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            dismissProgress();

            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray jsonArray;
                    jsonArray = jsonObject.getJSONArray("connection");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        FriendConnection nfcModelTag = new FriendConnection();
                        nfcModelTag.setName(object.getString("FirstName") + " " + object.getString("LastName"));
                        nfcModelTag.setCompany(object.getString("CompanyName"));
                        nfcModelTag.setEmail(object.getString("UserName"));
                        nfcModelTag.setWebsite(object.getString("Website"));
                        nfcModelTag.setPh_no(object.getString("Phone"));
                        nfcModelTag.setDesignation(object.getString("Designation"));
                        nfcModelTag.setCard_front(object.getString("Card_Front"));
                        nfcModelTag.setCard_back(object.getString("Card_Back"));
                        nfcModelTag.setUser_image(object.getString("UserPhoto"));
                        nfcModelTag.setProfile_id(object.getString("ProfileId"));
                        nfcModelTag.setDateInitiated(object.getString("DateInitiated"));
                        nfcModelTag.setAddress(object.getString("Address1") + " " + object.getString("Address2")
                                + " " + object.getString("Address3") + " " + object.getString("Address4"));
//                        Toast.makeText(getActivity(),"Profile_id"+object.getString("ProfileId"),Toast.LENGTH_SHORT).show();
                        nfcModelTag.setNfc_tag("en000000001");
                        nfcModelTag.setLatitude(object.getString("Latitude"));
                        nfcModelTag.setLongitude(object.getString("Longitude"));
                        nfcModelTag.setQ_ID(object.getString("Q_ID"));
                        allTaggs.add(nfcModelTag);

                    }

                } else {
                    Toast.makeText(getContext(), "Not able to load Friends..", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("Type", SortFragment.SortType);
            jsonObject.accumulate("numofrecords", "1000");
            jsonObject.accumulate("pageno", "1");
            jsonObject.accumulate("userid", UserId);


            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }

    private boolean checkSearchDataWithError(String searchQuery) {
        boolean correct = searchQuery != null && searchQuery.length() >= MIN_VALUE_FOR_SEARCH;
        if (correct) {
            return true;
        } else {
            OneButtonDialogFragment.show(getChildFragmentManager(), R.string.search_at_last_items, true);
            return false;
        }
    }

    private void addToFriendList(final int userId) {
        if (!baseActivity.checkNetworkAvailableWithError()) {
            return;
        }

        baseActivity.showProgress();
        QBAddFriendCommand.start(baseActivity, userId);
        KeyboardUtils.hideKeyboard(baseActivity);
    }

    private void checkForEnablingRefreshLayout() {
        swipyRefreshLayout.setEnabled(usersList.size() != totalEntries);
    }

    private void parseResult(Bundle bundle) {
    }

    private void checkForExcludeMe(Collection<QMUser> usersCollection) {
        QBUser qbUser = AppSession.getSession().getUser();
        QMUser me = QMUser.convert(qbUser);
        if (usersCollection.contains(me)) {
            usersCollection.remove(me);
            excludedMe = true;
            totalEntries--;
        }
    }

    private class SearchTimerTask extends TimerTask {

        @Override
        public void run() {
            searchUsers();
        }
    }

    private class FindUserSuccessAction implements Command {

        @Override
        public void execute(Bundle bundle) {
            parseResult(bundle);

            swipyRefreshLayout.setRefreshing(false);
            checkForEnablingRefreshLayout();
        }
    }

    private class FindUserFailAction implements Command {

        @Override
        public void execute(Bundle bundle) {
            OneButtonDialogFragment.show(getChildFragmentManager(), R.string.search_users_not_found, true);
            usersList.clear();

            swipyRefreshLayout.setRefreshing(false);
            checkForEnablingRefreshLayout();
        }
    }

    private class UserOperationAction implements UserOperationListener {

        @Override
        public void onAddUserClicked(int userId) {
            addToFriendList(userId);
        }
    }

    private class AddFriendSuccessAction implements Command {

        @Override
        public void execute(Bundle bundle) {
            int userId = bundle.getInt(QBServiceConsts.EXTRA_FRIEND_ID);

            QMUser addedUser = QMUserService.getInstance().getUserCache().get((long)userId);
            globalSearchAdapter.notifyDataSetChanged();

            baseActivity.hideProgress();
        }
    }

    private class CommonObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if (data != null) {
                String observerKey = ((Bundle) data).getString(BaseManager.EXTRA_OBSERVE_KEY);
                if (observerKey.equals(dataManager.getUserRequestDataManager().getObserverKey()) || observerKey.equals(dataManager.getFriendDataManager().getObserverKey())) {
                    updateList();
                }
            }
        }
    }
}