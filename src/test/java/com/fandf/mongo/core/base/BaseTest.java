package com.fandf.mongo.core.base;

import com.fandf.mongo.core.BaseConnection;
import com.fandf.mongo.core.BaseFramework;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fandongfeng
 * @date 2022/11/19 17:40
 */
public abstract class BaseTest {

    protected void connectDB() {
        BaseConnection conn = BaseFramework.getInstance().createConnection();
        conn.setHost("127.0.0.1");
        conn.setPort(27017);
        conn.setSource("admin");
        conn.setUsername("root");
        conn.setPassword("123456");
        conn.setDatabase("test");
        conn.connect();
    }

    protected void disconnectDB() {
        BaseFramework.getInstance().destroy();
    }

    protected void connectDBWithOptions(MongoClientOptions options) {
        BaseConnection conn = BaseFramework.getInstance().createConnection();
        conn.setHost("127.0.0.1");
        conn.setPort(27017);
        conn.setUsername("root");
        conn.setPassword("123456");
        conn.setDatabase("test");
        conn.setOptions(options);
        conn.connect();
    }

}
