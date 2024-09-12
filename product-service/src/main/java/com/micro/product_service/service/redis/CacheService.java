package com.micro.product_service.service.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.micro.product_service.dto.ProductDTO;

@Service
public class CacheService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteValue(String key){
        redisTemplate.delete(key);
    }

    public void setTimout(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public void setValueWithTimeout(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, timeout, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkExistsKey(String key) {
        boolean check = false;
        try {
            check = redisTemplate.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }

    public void lPushAll(String key, List<ProductDTO> value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void lPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sPush(String key, Object value){
        try {
            redisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeValueFromSet(String key, Object value) {
        try {
            redisTemplate.opsForSet().remove(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<Object> getRandomMembers(String key, int count) {
        try {
            return redisTemplate.opsForSet().distinctRandomMembers(key, count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }    

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }
}
