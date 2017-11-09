package com.kyt.framework.dbconn;

import com.kyt.framework.config.Config;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

public class ClientManager
        implements ManagerIF {

    private ClientPoolByHost commentClientPoolByHost;
    private static final Lock createLock_ = new ReentrantLock();
    private static Map<String, ClientManager> INSTANCES = new HashMap();
    private static Logger logger_ = Logger.getLogger(ClientManager.class);

    public static ManagerIF getInstance(String instanceName) {
        if (!INSTANCES.containsKey(instanceName)) {
            createLock_.lock();
            try {
                if (!INSTANCES.containsKey(instanceName)) {
                    INSTANCES.put(instanceName, new ClientManager(instanceName));
                }
            } finally {
                createLock_.unlock();
            }
        }
        return (ManagerIF) INSTANCES.get(instanceName);
    }

    public ClientManager(String instanceName) {
        String driver = Config.getParam(instanceName, "driver");
        String url = Config.getParam(instanceName, "url");
        String user = Config.getParam(instanceName, "user");
        String password = Config.getParam(instanceName, "password");
        this.commentClientPoolByHost = new ClientPoolByHost(driver, url, user, password);
    }

    public Connection borrowClient() {
        Connection client = this.commentClientPoolByHost.borrowClient();
        return client;
    }

    public void returnClient(Connection client) {
        this.commentClientPoolByHost.returnObject(client);
    }

    public void invalidClient(Connection client) {
        this.commentClientPoolByHost.invalidClient(client);
    }
}
