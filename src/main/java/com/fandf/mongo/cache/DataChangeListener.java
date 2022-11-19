package com.fandf.mongo.cache;

import com.fandf.mongo.core.BaseEntity;
import com.fandf.mongo.core.listener.EntityListener;

/**
 * @author fandongfeng
 * @date 2022/11/19 14:02
 */
public class DataChangeListener implements EntityListener {

    private CacheableDao dao;

    public DataChangeListener(CacheableDao dao) {
        this.dao = dao;
    }

    @Override
    public void entityInserted(BaseEntity entity) {
        dao.dataChanged();
    }

    @Override
    public void entityUpdated(BaseEntity entity) {
        dao.dataChanged();
    }

    @Override
    public void entityDeleted(BaseEntity entity) {
        dao.dataChanged();
    }
}
