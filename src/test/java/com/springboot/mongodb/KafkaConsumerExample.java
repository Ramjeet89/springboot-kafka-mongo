package com.springboot.mongodb;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    public static void main(String[] args) {

        //US Region broker setup, it has own credential
        Properties properties1 = new Properties();
        properties1.put("bootstrap.servers", "us-east-1.aws.gbl.confluent.cloud:9092");
        properties1.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties1.put("value.deserializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties1.put("group.id", "region1-consumer-group");
        properties1.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"username1\" password=\"password1\";");

        //EU Region broker setup, it has also own credential
        Properties properties2 = new Properties();
        properties2.put("bootstrap.servers", "eu-central-1.aws.confluent.cloud:9092");
        properties2.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties2.put("value.deserializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties2.put("group.id", "region2-consumer-group");
        properties2.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"username2\" password=\"password2\";");

        Consumer<String, String> consumerRegion1 = new KafkaConsumer<String, String>(properties1);
        Consumer<String, String> consumerRegion2 = new KafkaConsumer<String, String>(properties2);

        consumerRegion1.subscribe(Collections.singletonList("kafka-mab-topic-us"));
        consumerRegion1.subscribe(Collections.singletonList("kafka-mab-topic-eu"));

        try (consumerRegion1; consumerRegion2) {
            while (true) {
                ConsumerRecords<String, String> recordRegion1 = consumerRegion1.poll(Duration.ofMillis(100));
                recordRegion1.forEach(record -> {
                    System.out.printf("Consumed record from Region 1: Key= %s, value=%s%n", record.key(), record.value());
                });
                ConsumerRecords<String, String> recordRegion2 = consumerRegion1.poll(Duration.ofMillis(100));
                recordRegion2.forEach(record -> {
                    System.out.printf("Consumed record from Region 1: Key= %s, value=%s%n", record.key(), record.value());
                });
                consumerRegion1.commitAsync();
                consumerRegion2.commitAsync();
            }
        }
    }
}

