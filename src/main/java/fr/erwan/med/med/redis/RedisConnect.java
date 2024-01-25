package fr.erwan.med.med.redis;

import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;
import java.util.logging.Level;

public class RedisConnect {
    private static final Logger LOGGER = Logger.getLogger(RedisConnect.class.getPackage().getName() );
    private static RedisConnect instance = new RedisConnect();
    private JedisPool connection;

    private RedisConnect() {
        try {
            connection = new JedisPool("redis_med", 6379);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "connection à redis impossible " + e.getMessage());
        }
    }

    public static RedisConnect getInstance() {
        return instance;
    }

    public JedisPool getRedisConnection() {
        return connection;
    }
}
