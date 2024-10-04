package com.example.SpringAI.Services.ServiceImpl;

import dev.langchain4j.data.embedding.Embedding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EmbeddingCacheServiceImpl {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Method to store embeddings in Redis
    public void cacheEmbeddings(String key, List<Embedding> embeddings) {
        redisTemplate.opsForValue().set(key, embeddings, 24, TimeUnit.HOURS); // Cache for 24 hours
    }
    //Method to store single embedding in Redis
    public void cache1Embedding(String key, Embedding embedding) {
        redisTemplate.opsForValue().set(key, embedding, 24, TimeUnit.HOURS); // Cache for 24 hours
    }


    // Method to retrieve embeddings from Redis
    public List<Embedding> getCachedEmbeddings(String key) {

        return (List<Embedding>) redisTemplate.opsForValue().get(key);
    }
    // Method to retrieve single embedding from Redis
    public Embedding get1CachedEmbedding(String key) {
        return (Embedding) redisTemplate.opsForValue().get(key);
    }



    // Method to check if a key exists in Redis
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}
