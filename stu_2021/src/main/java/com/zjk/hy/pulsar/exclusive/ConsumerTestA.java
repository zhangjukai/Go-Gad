package com.zjk.hy.pulsar.exclusive;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

/**
 * @Author zhangjukai
 * @Date 2021-10-08 11:06
 * @Description
 */
public class ConsumerTestA {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Consumer<String> consumer = pulsarClient.newConsumer(Schema.STRING).topic("my-topic-1000")
                    .subscriptionName("my-subscription1")
                    .subscriptionType(SubscriptionType.Shared)
                    .subscribe();
            while (true) {
                Message<String> message = consumer.receive();
                System.out.println(message.getValue());
                System.out.println("================");
                consumer.acknowledge(message);
            }
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
