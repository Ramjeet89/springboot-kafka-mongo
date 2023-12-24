package com.springboot.mongodb.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.mongodb.entity.Counter;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CounterDeserializer implements Deserializer<Counter> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Counter deserialize(String topic, byte[] counter) {
        if (counter == null) {
            return null;
        }

        try {

            String jsonString = new String(counter, StandardCharsets.UTF_8);
            return jsonToCounter(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing counter", e);
        }
    }

    private Counter jsonToCounter(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, Counter.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing counter to Json", e);
        }
    }

    @Override
    public void close() {

    }
}