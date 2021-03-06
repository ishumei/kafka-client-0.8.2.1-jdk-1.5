/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.kafka.common.config;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;
import org.junit.Test;

public class AbstractConfigTest {

  @Test
  public void testConfiguredInstances() {
    testValidInputs("");
    testValidInputs("org.apache.kafka.common.config.AbstractConfigTest$TestMetricsReporter");
    testValidInputs("org.apache.kafka.common.config.AbstractConfigTest$TestMetricsReporter,org.apache.kafka.common.config.AbstractConfigTest$TestMetricsReporter");
    testInvalidInputs(",");
    testInvalidInputs("org.apache.kafka.clients.producer.unknown-metrics-reporter");
    testInvalidInputs("test1,test2");
    testInvalidInputs("org.apache.kafka.common.config.AbstractConfigTest$TestMetricsReporter,");
  }

  private void testValidInputs(String configValue) {
    Properties props = new Properties();
    props.put(TestConfig.METRIC_REPORTER_CLASSES_CONFIG, configValue);
    TestConfig config = new TestConfig(props);
    try {
      config.getConfiguredInstances(TestConfig.METRIC_REPORTER_CLASSES_CONFIG,
          MetricsReporter.class);
    } catch (ConfigException e) {
       fail("No exceptions are expected here, valid props are :" + props);
    }
  }
  
  private void testInvalidInputs(String configValue) {
    Properties props = new Properties();
    props.put(TestConfig.METRIC_REPORTER_CLASSES_CONFIG, configValue);
    TestConfig config = new TestConfig(props);
    try {
      config.getConfiguredInstances(TestConfig.METRIC_REPORTER_CLASSES_CONFIG,
          MetricsReporter.class);
      fail("Expected a config exception due to invalid props :" + props);
    } catch (ConfigException e) {
      // this is good
    }
  }

  private static class TestConfig extends AbstractConfig {

    private static final ConfigDef config;

    public static final String METRIC_REPORTER_CLASSES_CONFIG = "metric.reporters";
    private static final String METRIC_REPORTER_CLASSES_DOC = "A list of classes to use as metrics reporters.";

    static {
      config = new ConfigDef().define(METRIC_REPORTER_CLASSES_CONFIG,
          Type.LIST, "", Importance.LOW, METRIC_REPORTER_CLASSES_DOC);
    }

    public TestConfig(Map<? extends Object, ? extends Object> props) {
      super(config, props);
    }
  }
  
  public static class TestMetricsReporter implements MetricsReporter {

    
    public void configure(Map<String, ?> configs) {
    }

    
    public void init(List<KafkaMetric> metrics) {
}

    
    public void metricChange(KafkaMetric metric) {
    }

    
    public void close() {
    }
  }
}
