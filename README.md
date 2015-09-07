# Kafka Java Client

Build for jdk 1.5.

## Usage:

- mvn install

This will install following artifact into local maven cache.

  <dependency>
      <groupId>org.apache.kafka</groupId>
      <artifactId>kafka-clients-jdk15</artifactId>
      <version>0.8.2.1</version>
  </dependency>

- cd example; mvn package

This will build a simple console producer.

## 已知问题

- 不支持SNAPPY和LZ4压缩方式
- 不支持jdk1.6之后添加的TimeUnit.MINUETS, TimeUnit.HOURS, TimeUnit.DAYS.