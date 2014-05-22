package com.hortonworks.streaming.impl.collectors;

import java.util.Properties;



import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.hortonworks.streaming.impl.domain.AbstractEventCollector;
import com.hortonworks.streaming.impl.domain.transport.MobileEyeEvent;

public class KafkaEventCollector extends AbstractEventCollector {

	private static final String TOPIC = "truck_events";
	
	private Producer<String, String> kafkaProducer;

	public KafkaEventCollector() {
        Properties props = new Properties();
        //props.put("metadata.broker.list", "gvetticaden-kafka-cluster.secloud.hortonworks.com:9092,gvetticaden-kafka-cluster2.secloud.hortonworks.com:9092");
        props.put("metadata.broker.list", "hadoopsummit-kafka.secloud.hortonworks.com:9092,hadoopsummit-kafka2.secloud.hortonworks.com:9092");

        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
 
        try {
        	ProducerConfig producerConfig = new ProducerConfig(props);		
            kafkaProducer = new Producer<String, String>(producerConfig);        	
        } catch (Exception e) {
        	logger.error("Error creating producer" , e);
        }
        
      
	}
	
	@Override
	public void onReceive(Object event) throws Exception {
		MobileEyeEvent mee = (MobileEyeEvent) event;
		String eventToPass = mee.toString();
		String driverId = String.valueOf(mee.getTruck().getDriver().getDriverId());
		
		logger.debug("Creating event["+eventToPass+"] for driver["+driverId + "] in truck [" + mee.getTruck() + "]");
		
		try {
			KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, driverId, eventToPass);
			kafkaProducer.send(data);			
		} catch (Exception e) {
			logger.error("Error sending event[" + eventToPass + "] to Kafka queue", e);
		}		

	}

}
