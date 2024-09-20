package com.example.SpringAI.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServices {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> classEntity){
        try {
            Object object=redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper=new ObjectMapper();
            return objectMapper.readValue(object.toString(),classEntity);
        } catch (JsonProcessingException e) {
            log.error("Exception: ",e);
            return null;
        }
    }

    public void set(String key,Object object, Long time){
        try {
            ObjectMapper objectMapper=new ObjectMapper();

            String stringObject=objectMapper.writeValueAsString(object);

            redisTemplate.opsForValue().set(key,stringObject,time, TimeUnit.MINUTES);
        } catch (Exception e) {
          log.error("Exception: ",e);
        }

    }
}
