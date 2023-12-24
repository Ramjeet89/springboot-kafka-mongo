package com.springboot.mongodb.repo.Impl;

import com.springboot.mongodb.constant.CounterConstant;
import com.springboot.mongodb.entity.Counter;
import com.springboot.mongodb.repo.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
public class MongoClientImpl implements MongoClient {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoClientImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public void save(List<Counter> counters) {
        counters.forEach(updateCounter());

    }

    private Consumer<? super Counter> updateCounter() {
        return counter -> {
            Query query = new Query(
                    Criteria.where(CounterConstant.APPLICATION_NAME).is(counter.getApplicationName())
                            .and(CounterConstant.EXPERIMENT_NAME).is(counter.getExperimentName())
                            .and(CounterConstant.URL).is(counter.getUrl())
                            .and(CounterConstant.STRATEGY).is(counter.getStrategy())
                            .and(CounterConstant.EVENT).is(counter.getEvent())
            );

            Update update = new Update()
                    .inc(CounterConstant.VALUE, counter.getValue())
                    .set(CounterConstant.UPDATED_DATE, LocalDateTime.now()); // Assuming 'updatedOn' is the date field
            // Use the 'upsert' method to insert or update based on the query
            mongoTemplate.upsert(query, update, Counter.class);
        };
    }

    @Override
    public void save(Counter counter) {
        Query query = new Query(
                Criteria.where(CounterConstant.APPLICATION_NAME).is(counter.getApplicationName())
                        .and(CounterConstant.EXPERIMENT_NAME).is(counter.getExperimentName())
                        .and(CounterConstant.URL).is(counter.getUrl())
                        .and(CounterConstant.STRATEGY).is(counter.getStrategy())
                        .and(CounterConstant.EVENT).is(counter.getEvent())
        );
        Update update = new Update()
                .inc(CounterConstant.VALUE, counter.getValue())
                .set(CounterConstant.UPDATED_DATE, LocalDateTime.now()); // Assuming 'updatedOn' is the date field

        // Use the 'upsert' method to insert or update based on the query
        mongoTemplate.upsert(query, update, Counter.class);
    }


    @Override
    public void insert(Counter counter) {
        mongoTemplate.insert(counter);
    }
}
