package com.fandf.mongo.cache;

import com.fandf.mongo.core.utils.StringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fandongfeng
 * @date 2022/11/19 13:58
 */
public class BaseCache {
    public final static String ZK_PREFIX = "/base:cache:";

    private String zkConnectString;
    private CuratorFramework zkClient;

    private final Map<String, List> data = new ConcurrentHashMap<>();

    private BaseCache(){

    }

    private static class Holder {
        final static BaseCache instance = new BaseCache();
    }

    public static BaseCache getInstance(){
        return Holder.instance;
    }

    public void init(){
        if(StringUtil.isBlank(zkConnectString)){
            return;
        }
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkClient = CuratorFrameworkFactory.newClient(zkConnectString, retryPolicy);
        zkClient.start();
        try {
            zkClient.blockUntilConnected();
        } catch (InterruptedException ex) {
            throw new BaseCacheException(ex.getMessage());
        }
    }

    public void destroy(){
        if(StringUtil.isBlank(zkConnectString)){
            return;
        }
        if(zkClient != null){
            CloseableUtils.closeQuietly(zkClient);
        }
    }

    public List getValue(String key) {
        return data.get(key);
    }

    public void setValue(String key, List value) {
        data.put(key, value);
    }

    public void setZkConnectString(String zkConnectString) {
        this.zkConnectString = zkConnectString;
    }

    public CuratorFramework getZkClient() {
        return zkClient;
    }
}
