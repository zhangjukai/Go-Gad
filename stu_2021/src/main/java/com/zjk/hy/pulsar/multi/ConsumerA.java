package com.zjk.hy.pulsar.multi;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.util.regex.Pattern;

/**
 * @Author zhangjukai
 * @Date 2021-10-08 11:45
 * @Description
 */
public class ConsumerA {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        Pattern pattern = Pattern.compile("public/default/.*");
        try {
            Consumer<String> consumer = pulsarClient.newConsumer(Schema.STRING)
                    .subscriptionName("my-subscription")
                    .topicsPattern(pattern)
                    .subscriptionTopicsMode(RegexSubscriptionMode.AllTopics)
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
