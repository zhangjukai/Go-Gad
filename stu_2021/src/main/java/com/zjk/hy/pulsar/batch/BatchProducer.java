package com.zjk.hy.pulsar.batch;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.CompletableFuture;

/**
 * @Author zhangjukai
 * @Date 2021-10-09 14:40
 * @Description
 */
public class BatchProducer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Producer<String> producer = pulsarClient.newProducer(Schema.STRING)
                    .topic("my-test-batch")
                    .create();
            for (int i = 0; i < 200; i++) {
                producer.send("hello_"+i);
            }
            producer.close();
            pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
