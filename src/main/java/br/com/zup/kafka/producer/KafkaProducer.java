package br.com.zup.kafka.consumer.producer;

import java.util.Map;
import java.util.concurrent.Future;

import br.com.zup.kafka.consumer.KafkaMessage;
import br.com.zup.kafka.consumer.config.props.ProducerProperties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducer<K, V> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private Producer<K, KafkaMessage<V>> producer;

    public KafkaProducer(ProducerProperties props) {
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props.properties());
    }

    public Future<RecordMetadata> send(String topic, V message) {
        return producer.send(new ProducerRecord<>(topic, KafkaMessage.of(message)));
    }

    public Future<RecordMetadata> send(String topic, V message, Map<String, String> headers) {
        return producer.send(new ProducerRecord<>(topic, KafkaMessage.of(message, headers)));
    }

    public Future<RecordMetadata> send(String topic, KafkaMessage<V> message) {
        return producer.send(new ProducerRecord<>(topic, message));
    }
    
    public void close() {
        LOGGER.info("Destroying Kafka producer");
        if(producer != null) {
            producer.close();
        }
    }
}