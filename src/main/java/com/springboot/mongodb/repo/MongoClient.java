package com.springboot.mongodb.repo;


import com.springboot.mongodb.entity.Counter;

import java.util.List;

public interface MongoClient {

    void save(List<Counter> counters);

    void save(Counter counter);

    void insert(Counter counter);
}
