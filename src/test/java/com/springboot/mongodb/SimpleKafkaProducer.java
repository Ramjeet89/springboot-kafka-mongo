package com.springboot.mongodb;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.UUID;

public class SimpleKafkaProducer {
    public static void main(String[] args) {
        sendMessage();
    }

    private static void sendMessage() {
        // Configure the Kafka producer properties
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (Producer<String, String> producer = new KafkaProducer<>(properties)) {
            // Create a unique key for each message
            String key = UUID.randomUUID().toString();

            // Specify the topic to which you want to send messages
            String topic = "kafka-mab-topic-us";

            // Create a sample message in JSON format
            String message = "{\"applicationName\":\"recommendations\",\"experimentName\":\"bottom-recirc\",\"url\":\"https://allure.com\",\"strategy\":\"cral2_1\",\"event\":\"click\",\"count\":50}";

            // Create a ProducerRecord with the specified topic, key, and message
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

            // Send the message and wait for acknowledgement
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("Message sent successfully! Topic: " + metadata.topic() +
                                ", Partition: " + metadata.partition() +
                                ", Offset: " + metadata.offset());
                    } else {
                        exception.printStackTrace();
                    }
                }
            }).get(); // Blocking call to ensure the message is sent before moving on
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
