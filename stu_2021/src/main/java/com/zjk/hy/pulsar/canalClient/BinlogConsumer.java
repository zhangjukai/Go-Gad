package com.zjk.hy.pulsar.canalClient;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-25 11:35
 * @Description
 */
public class BinlogConsumer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();

        Consumer<byte[]> consumer = null;
        try {
            consumer = pulsarClient.newConsumer(Schema.BYTES)
                    .topic("persistent://public/default/example")
                    .subscriptionType(SubscriptionType.Exclusive).maxPendingChunkedMessage(Integer.MAX_VALUE)
                    //.enableRetry(true)
                    .ackTimeout(5000, TimeUnit.MILLISECONDS)
                    .subscriptionName("my-subsc")
                    //.subscriptionType(SubscriptionType.Failover)
                    //.subscriptionType(SubscriptionType.Shared)
                    .subscribe();
            while (true) {
                Message<byte[]> message = consumer.receive();
                System.out.println("msg:"+message.getValue());
            }

        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
