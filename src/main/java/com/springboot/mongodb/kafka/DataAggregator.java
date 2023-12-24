package com.springboot.mongodb.kafka;

import com.springboot.mongodb.entity.Counter;
import com.springboot.mongodb.repo.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataAggregator {

    private final MongoClient mongoClient;

    @Autowired
    public DataAggregator(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void serialiseData(Optional<Counter> optionalCounter) {
        optionalCounter.ifPresent(
                mongoClient::save
        );
    }
}
