package com.zjk.hy.pulsar.retry;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

import java.nio.charset.StandardCharsets;

/**
 * @Author zhangjukai
 * @Date 2021-10-09 14:40
 * @Description
 */
public class DeadProducer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Producer<byte[]> producer = pulsarClient.newProducer(Schema.BYTES).enableBatching(false)
                    .topic("keytop-dead-retry-topic-byte")
                    .create();
            producer.send("my-retry-letter-topic-flag-ack55".getBytes(StandardCharsets.UTF_8));
            producer.close();
            pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
