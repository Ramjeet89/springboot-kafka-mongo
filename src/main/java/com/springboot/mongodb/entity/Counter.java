package com.springboot.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("counter")
public class Counter {
    private String applicationName;
    private String experimentName;
    private String url;
    private String strategy;
    private String event;
    private int value;
}
