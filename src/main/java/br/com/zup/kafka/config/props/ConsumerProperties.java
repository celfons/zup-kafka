package br.com.zup.kafka.config.props;

import br.com.zup.kafka.config.props.core.GenericBuilder;
import br.com.zup.kafka.consumer.config.KMessageConsumer;
import br.com.zup.kafka.consumer.deserializer.JsonDeserializer;
import br.com.zup.kafka.util.Assert;
import com.fasterxml.jackson.databind.JavaType;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.List;
import java.util.regex.Pattern;

public class ConsumerProperties<K, V> extends GenericBuilder {

    private List<String> topics;
    private Pattern topicPattern;
    private ConsumerRebalanceListener consumerRebalanceListener = new NoOpConsumerRebalanceListener();
    private KMessageConsumer<K, V> messageConsumer;
    private boolean commitAsync = false;
    private boolean commitSync = false;
    private boolean enableAutoCommit = true;

    ConsumerProperties(KMessageConsumer<K, V> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public ConsumerProperties<K, V> withServers(String... servers) {
        props.put("bootstrap.servers", toCommaSeparated(servers));
        return this;
    }

    public ConsumerProperties<K, V> withKeyDeserializer(Class<?> clazz) {
        props.put("key.deserializer", clazz.getName());
        return this;
    }

    public ConsumerProperties<K, V> withValueDeserializer(Class<?> clazz) {
        props.put("value.deserializer", clazz.getName());
        return this;
    }

    public ConsumerProperties<K, V> withGroupId(String groupId) {
        props.put("group.id", groupId);
        return this;
    }

    public ConsumerProperties<K, V> withDeserializerClass(Class<?> clazz) {
        props.put("deserializer.type", clazz);
        withValueDeserializer(JsonDeserializer.class);
        return this;
    }

    public ConsumerProperties<K, V> withDeserializerType(JavaType type) {
        props.put("deserializer.type", type);
        withValueDeserializer(JsonDeserializer.class);
        return this;
    }

    public ConsumerProperties<K, V> withAutoOffsetReset(OffsetReset offsetReset) {
        props.put("auto.offset.reset", offsetReset.name().toLowerCase());
        return this;
    }

    public ConsumerProperties<K, V> withSessionTimeoutInMillis(int ms) {
        props.put("session.timeout.ms", ms);
        return this;
    }
    public ConsumerProperties<K, V> withTopics(List<String> topics) {
        this.topics = topics;
        return this;
    }

    public ConsumerProperties<K, V> withTopicPattern(Pattern topicPattern) {
        this.topicPattern = topicPattern;
        return this;
    }

    public ConsumerProperties<K, V> withConsumerRebalanceListener(ConsumerRebalanceListener consumerRebalanceListener) {
        this.consumerRebalanceListener = consumerRebalanceListener;
        return this;
    }

    public ConsumerProperties<K, V> withEnableAutoCommit(boolean enableAutoCommit) {
        props.put("enable.auto.commit", enableAutoCommit);
        this.enableAutoCommit = enableAutoCommit;
        return this;
    }

    public ConsumerProperties<K, V> withCommitAsync() {
        this.commitSync = false;
        this.commitAsync = true;
        return this;
    }

    public ConsumerProperties<K, V> withCommitSync() {
        this.commitSync = true;
        this.commitAsync = false;
        return this;
    }

    public ConsumerProperties<K, V> withMaxPollRecords(int maxPollRecords) {
        props.put("max.poll.records", maxPollRecords);
        return this;
    }

    @Override
    public void addDefaults() {
        addIfNull("key.deserializer", StringDeserializer.class.getName());
        addIfNull("value.deserializer", StringDeserializer.class.getName());
    }

    public void validate() {
        Assert.assertFalse((topicPattern == null && (topics == null || topics.isEmpty())), "KMessageConsumer cannot be null");
        Assert.notNull(messageConsumer, "KMessageConsumer cannot be null");
        Assert.notNull(consumerRebalanceListener);
    }

    public Pattern getTopicPattern() {
        return topicPattern;
    }

    public List<String> getTopics() {
        return topics;
    }

    public KMessageConsumer<K, V> getMessageConsumer() {
        return messageConsumer;
    }

    public ConsumerRebalanceListener getConsumerRebalanceListener() {
        return consumerRebalanceListener;
    }

    public boolean isTopicByPattern() {
        return topicPattern != null;
    }

    public boolean isCommitAsync() {
        return commitAsync;
    }

    public boolean isCommitSync() {
        return commitSync;
    }

    public boolean isEnableAutoCommit() {
        return enableAutoCommit;
    }
}
