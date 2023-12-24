package com.springboot.mongodb.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.mongodb.entity.Counter;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CounterSerializer implements Serializer<Counter> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Counter counter) {
        if (counter == null) {
            return null;
        }

        try {
            String jsonString = counterToJson(counter);

            return jsonString.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing counter", e);
        }
    }

    private String counterToJson(Counter counter) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(counter);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing counter to Json", e);
        }
    }

    @Override
    public void close() {

    }
}
