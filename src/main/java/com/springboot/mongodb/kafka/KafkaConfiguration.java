package com.springboot.mongodb.kafka;

import com.springboot.mongodb.entity.Counter;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Optional;

@Configuration
@EnableKafkaStreams
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public Serde entrySerde() {
        return Serdes.serdeFrom(new CounterSerializer(), new CounterDeserializer());
    }


    @Bean
    @Autowired
    public KStream<String, Counter> KStream(
            final StreamsBuilder streamsBuilder,
            final Serde<Counter> entrySerde,
            DataAggregator dataAggregater,
            @Value("${kafka.mab-topic1}") final String kafkaTopicOne) {
        KStream<String, Counter> entryStream = streamsBuilder.stream(kafkaTopicOne, Consumed.with(Serdes.String(), entrySerde));
        entryStream.peek((key, entry) -> dataAggregater.serialiseData(Optional.of(entry)));
        return entryStream;
    }

    /*
      I need to implement a scenario in my application where two topics are involved.
      However, each topic is associated with distinct broker server credentials and regions.
      how can I achieve this and how should I pass the details(can I keep both region credential in .property file)
      so that both topic will read his own given credential and broker server url

     */

    @Bean
    @Autowired
    public KStream<String, Counter> KStream(
            final StreamsBuilder streamsBuilder,
            final Serde<Counter> entrySerde,
            DataAggregator dataAggregater,
            @Value("${kafka.mab-topic1}") final String kafkaTopicUS,
            @Value("${kafka.mab-topic2}") final String kafkaTopicEU) {
        KStream<String, Counter> usStream = streamsBuilder.stream(kafkaTopicUS, Consumed.with(Serdes.String(), entrySerde));
        KStream<String, Counter> euStream = streamsBuilder.stream(kafkaTopicEU, Consumed.with(Serdes.String(), entrySerde));

        KStream<String, Counter> mergedStream = usStream.merge(euStream);

        //peek is used to perform the specified action for each record without affecting the origional stream mergedStream
        mergedStream.peek((key, counter) -> dataAggregater.serialiseData(Optional.of(counter)));
        return mergedStream;
    }
}
