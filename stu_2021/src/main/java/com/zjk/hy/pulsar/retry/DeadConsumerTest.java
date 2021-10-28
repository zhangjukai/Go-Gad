package com.zjk.hy.pulsar.retry;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

/**
 * @Author zhangjukai
 * @Date 2021-10-08 11:06
 * @Description
 */
public class DeadConsumerTest {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Consumer<byte[]> consumer = pulsarClient.newConsumer(Schema.BYTES).topic("keytop-dead-retry-topic-byte-DLQ")
                    .subscriptionName("my-subscription1")
                    .subscriptionType(SubscriptionType.Shared)
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscribe();
            while (true) {
                Message<byte[]> message = consumer.receive();
                System.out.println(">>>>>>>>>>>>>> "+new String(message.getData()));
                consumer.acknowledge(message);
            }
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
