package com.quickblox.q_municate_user_service.cache;

import com.quickblox.q_municate_base_cache.QMBaseCache;
import com.quickblox.q_municate_user_service.model.QMUser;

import java.util.Collection;
import java.util.List;

public interface QMUserCache extends QMBaseCache<QMUser, Long> {

    void deleteUserByExternalId(String externalId);

    List<QMUser> getUsersByIDs(Collection<Integer> idsList);

    QMUser getUserByColumn(String column, String value);

    List<QMUser> getUsersByFilter(Collection<?> filterValue, String filter);

}
