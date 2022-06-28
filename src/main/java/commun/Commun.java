package commun;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Commun {
    public static final String NAME_HASH_FEEDBACK = "asin";
    public static final String NAME_FIELD_FEEDBACK = "personneId";

    private static JedisPool jedisPool;

    public Commun(String host, String ip) {
        RedisClient(host, ip);
    }

    public Commun() {
        new Commun("127.0.0.13", "6379");
    }

    private void RedisClient(String ip, String port) {
        try {
            if (jedisPool == null) {
                jedisPool = new JedisPool(new URI("http://" + ip + ":" + port));
            }
        } catch (URISyntaxException e) {
            System.err.println("Malformed server address : " + e);
        }
    }

    // ??
    public List lrange(final String key, final long start, final long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> results = jedis.lrange(key, start, stop);
            jedis.close();
            return results;
        } catch (Exception ex) {
            System.err.println("Exception caught in lrange : " + ex);
        }
        return new ArrayList<String>();
    }

    public String getString(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            jedis.close();
            return value;
        } catch (Exception ex) {
            System.err.println("Exception caught in get : "+ ex);
        }
        return null;
    }

    public Map<String,String> getMap(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String,String> value = jedis.hgetAll(key);
            jedis.close();
            return value;
        } catch (Exception ex) {
            System.err.println("Exception caught in get : "+ ex);
        }
        return null;
    }

    public void shutdown() {
        jedisPool.close();
    }

    public void printMap(Map<String,String> map) {
        for (Map.Entry<String,String> entry: map.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public Map<String,String> getAllFeedbackFromPersonId(String personId) {
        Map<String,String> map = new HashMap<>();
        ArrayList<String> listOfFeedback = getAllLabelFeedback();

        for (String asian:listOfFeedback) {
            Map<String, String> currentMap = getMap(asian);
            for (Map.Entry<String,String> values:currentMap.entrySet()) {
                if(values.getKey().equals( NAME_FIELD_FEEDBACK + ":" + personId)) {
                    map.put(asian,values.getValue() + "");
                }
            }
        }

        return map;
    }

    public ArrayList<String> getAllLabelFeedback() {
        Jedis jedis = jedisPool.getResource();
        Set<String> sets = jedis.keys(NAME_HASH_FEEDBACK + ":" + "*");
        jedis.close();
        ArrayList<String> listtOfAsian = new ArrayList<>();
        if (!sets.isEmpty()) {
            for (String value : sets) {
                listtOfAsian.add(value);
            }
        }
        return listtOfAsian;
    }

    public long setAsin(final String asin, Map<String, String> feedbacks) {
        Jedis jedis = jedisPool.getResource();
        long result = jedis.hset(
                NAME_HASH_FEEDBACK+":"+asin,
                feedbacks.entrySet().stream().collect(Collectors.toMap(e -> NAME_FIELD_FEEDBACK + ":" + e.getKey(), Map.Entry::getValue))
        );
        jedis.close();
        return result;
    }

    public long setAsin(final String asin, String field, String value){
        Jedis jedis = jedisPool.getResource();
        long result = jedis.hset(
                NAME_HASH_FEEDBACK +":"+asin,
                NAME_FIELD_FEEDBACK+":"+field,value
        );


        jedis.close();
        return result;
    }


    public long deleteField(final String asin, String field){
        Jedis jedis = jedisPool.getResource();
        long result = jedis.hdel(NAME_HASH_FEEDBACK +":"+asin,NAME_FIELD_FEEDBACK+":"+field);

        jedis.close();
        return result;
    }

    public long deleteField(final String asin, String field[]){
        Jedis jedis = jedisPool.getResource();
        long result = jedis.hdel(NAME_HASH_FEEDBACK +":"+asin,field);

        jedis.close();
        return result;
    }

    public long deleteAsin(final String asin){
        Jedis jedis = jedisPool.getResource();
        long result = jedis.del(NAME_HASH_FEEDBACK +":"+asin);

        jedis.close();
        return result;
    }

    public long deleteAsin(final String asin[]){
        Jedis jedis = jedisPool.getResource();
        long result = jedis.del(asin);

        jedis.close();
        return result;
    }


}
