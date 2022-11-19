package com.fandf.mongo.cache;

import com.fandf.mongo.core.BaseDao;
import com.fandf.mongo.core.BaseQuery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author fandongfeng
 * @date 2022/11/19 14:00
 */
@SuppressWarnings("unchecked")
public class CacheableDao  <T> extends BaseDao<T> {

    private BaseQuery<T> cacheQuery;

    private long reloadDelay;

    public CacheableDao(Class<T> clazz){
        super(clazz);
        super.addEntityListener(new DataChangeListener(this));
        BaseCache cache = BaseCache.getInstance();
        CuratorFramework zkClient = cache.getZkClient();
        if(zkClient != null){
            String path = BaseCache.ZK_PREFIX + clazz.getName();
            //create node if not exists
            try {
                if(zkClient.checkExists().forPath(path) == null){
                    zkClient.create().withMode(CreateMode.PERSISTENT).forPath(path);
                }
            } catch (Exception ex) {
                throw new BaseCacheException(ex.getMessage());
            }
            //create and start NodeCache
            NodeCache nodeCache = new NodeCache(zkClient, path);
            try {
                nodeCache.start();
            } catch (Exception ex) {
                throw new BaseCacheException(ex.getMessage());
            }
            //add NodeCache listener
            nodeCache.getListenable().addListener(new NodeCacheListener(){
                @Override
                public void nodeChanged() throws Exception {
                    reloadCacheData();
                }
            });
        }
    }

    protected void setCacheQuery(BaseQuery<T> cacheQuery) {
        this.cacheQuery = cacheQuery;
    }

    protected void setReloadDelay(long reloadDelay) {
        this.reloadDelay = reloadDelay;
    }

    /**
     * Get data from cache. If not exists, will query from database.
     * @return
     */
    public List<T> getCacheData(){
        BaseCache cache = BaseCache.getInstance();
        String key = clazz.getName();
        List<T> value = (List<T>)cache.getValue(key);
        if(value == null){
            if(cacheQuery == null){
                value = this.findAll();
            }else{
                value = cacheQuery.results();
            }
            cache.setValue(key, value);
        }
        return value;
    }

    /**
     * used in DataChangeListener, call this to refresh cache data.
     */
    public void dataChanged(){
        BaseCache cache = BaseCache.getInstance();
        CuratorFramework zkClient = cache.getZkClient();
        //process single JVM
        if(zkClient == null){
            reloadCacheData();
        }
        //process cluster JVM, by ZooKeeper/curator
        else{
            //set new value to ZK node, in order to trigger nodeChanged event
            String path = BaseCache.ZK_PREFIX + clazz.getName();
            String data = String.valueOf(System.currentTimeMillis());
            try {
                zkClient.setData().inBackground().forPath(path, data.getBytes());
            } catch (Exception ex) {
                throw new BaseCacheException(ex.getMessage());
            }
        }
    }

    private void reloadCacheData(){
        if(reloadDelay > 0){
            final Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    doReload();
                    timer.cancel();
                }
            };
            timer.schedule(task, reloadDelay);
        }
        else{
            doReload();
        }
    }

    private void doReload() {
        BaseCache cache = BaseCache.getInstance();
        String key = clazz.getName();
        List<T> value = null;
        if(cacheQuery == null){
            value = this.findAll();
        }else{
            value = cacheQuery.results();
        }
        cache.setValue(key, value);
    }

}
