package com.springboot.mongodb.controller;

import com.springboot.mongodb.constant.CounterConstant;
import com.springboot.mongodb.entity.Counter;
import com.springboot.mongodb.repo.Impl.MongoClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CounterController {

    static final String SAVE = "/save";
    static final String UPDATE = "/update";
    static final String UPDATE_ALL = "/updateall";


    @Autowired
    private MongoClientImpl mongoClient;

    @PostMapping(SAVE)
    public ResponseEntity<Counter> save(@RequestBody Counter counter) {
        mongoClient.insert(counter);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(UPDATE)
    public ResponseEntity<String> updateCounter(@RequestBody Counter counter) {
        mongoClient.save(counter);
        return new ResponseEntity<>(CounterConstant.COUNTER_SAVED, HttpStatus.CREATED);
    }

    @PutMapping(UPDATE_ALL)
    public ResponseEntity<String> updateCounterAll(@RequestBody List<Counter> counters) {
        mongoClient.save(counters);
        return new ResponseEntity<>(CounterConstant.COUNTER_SAVED, HttpStatus.CREATED);
    }
}
