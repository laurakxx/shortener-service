package com.laura.analytics_service.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {
  @Bean
  public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
        (record, exception)-> new TopicPartition("link-clicks-dead-letter",
            record.partition()));
    FixedBackOff backOff = new FixedBackOff(1000L, 2L);
    return new DefaultErrorHandler(recoverer, backOff);
  }
}
