/**
 * Copyright (c) 2015 Shumei Inc. All Rights Reserved.
 * Authors: Liang Kun <liangkun@ishumei.com>
 */
package com.shumei.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Simple Kafka Producer that read lines from stdin. */
public class ConsoleProducer {
  public final static String USAGE = "USAGE: ConsoleProducer broker-list topic";
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      throw new IllegalArgumentException(USAGE);
    }

    String brokers = args[0];
    String topic = args[1];

    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String line = reader.readLine();
    while (line != null) {
      try {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, line);
        Future<RecordMetadata> result = producer.send(record);
        System.err.println("data sent at offset: " + result.get(1000, TimeUnit.MILLISECONDS).offset());
        line = reader.readLine();
      } catch (TimeoutException e) {
        System.err.println("Failed to send data: " + e.getMessage());
      }
    }
  }
}
