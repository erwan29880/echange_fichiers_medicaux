package fr.erwan.med.med.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * gestion email/password dans redis
 */
public class PasswordRedisManager {

    private static final Logger LOGGER = Logger.getLogger(PasswordRedisManager.class.getPackage().getName() );

    /**
     * instance du singleton de connection
     */
    private RedisConnect redisConnectClass = RedisConnect.getInstance();

    /**
     * connection redis
     */
    private JedisPool pool = redisConnectClass.getRedisConnection();
    
    /**
     * vérification que la clé existe dans redis
     * @param key la clé
     * @return vrai si la clé existe
     */
    public boolean hasKey(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key) == null ? false : true;
        }
    }

    /**
     * obtient la value de la clé dans redis
     * @param key la clé
     * @return la value ou null
     */
    public boolean compare(String key, String password) {
        if (!hasKey(key)) return false;
        try (Jedis jedis = pool.getResource()) {
            if (jedis.get(key).equals(password)) return true;
            return false;
        }
    }

    /**
     * entrer une association key/value dans redis, avec temporisation de 5 minutes
     * @param key la clé
     * @param value la value
     * @return vrai si pas d'erreur et si la clé n'existe pas déjà
     */
    public boolean setByKey(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            // ovveride key/value if already exists
            jedis.set(key, value);
            jedis.expire(key, 5*60); // 5 minutes
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "set key in redis " + e.getMessage());
            return false;
        }
    }
}
