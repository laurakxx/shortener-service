package com.laura.analytics_service.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;


import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.Serializer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.LinkedHashMap;

@Configuration
public class KafkaConfig {
  @Bean
  public ProducerFactory<String, Object> dlqProducerFactory(
      @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
  ) {
    Map<String, Object> props = new HashMap<>();

    props.put(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapServers
    );

    Map<Class<?>, Serializer<?>> serializers = new LinkedHashMap<>();

    serializers.put(
        byte[].class,
        new ByteArraySerializer()
    );

    serializers.put(
        Object.class,
        new JacksonJsonSerializer<>()
    );

    DelegatingByTypeSerializer valueSerializer =
        new DelegatingByTypeSerializer(serializers, true);

    return new DefaultKafkaProducerFactory<>(
        props,
        new StringSerializer(),
        valueSerializer
    );
  }

  @Bean
  public KafkaTemplate<String, Object> dlqKafkaTemplate(
      @Qualifier("dlqProducerFactory")
      ProducerFactory<String, Object> producerFactory
  ) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public DefaultErrorHandler errorHandler(@Qualifier("dlqKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
        (record, exception)-> new TopicPartition("link-clicks-dead-letter",
            record.partition()));
    FixedBackOff backOff = new FixedBackOff(1000L, 2L);
    return new DefaultErrorHandler(recoverer, backOff);
  }

  @Bean
  public ConsumerFactory<String, byte[]> dlqConsumerFactory(
      @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
    Map<String, Object> props = new HashMap<>();

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "analytics-dlq");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, byte[]> dlqKafkaListenerContainerFactory(
      ConsumerFactory<String, byte[]> dlqConsumerFactory) {

    ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(dlqConsumerFactory);

    return factory;
  }
}
