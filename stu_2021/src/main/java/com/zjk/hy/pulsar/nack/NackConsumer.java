package com.zjk.hy.pulsar.nack;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-08 11:06
 * @Description
 */
public class NackConsumer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Consumer<String> consumer = pulsarClient.newConsumer(Schema.STRING)
                    .topic("my-test")
                    .negativeAckRedeliveryDelay(20,TimeUnit.SECONDS)
                    .subscriptionName("my-subscription")
                    .subscriptionType(SubscriptionType.Shared)
                    .enableRetry(true)
                    .subscribe();
            int index = 1;
            while (true) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                Message<String> message = consumer.receive();
                System.out.println("================index:"+index+",data:"+message.getValue());
                consumer.negativeAcknowledge(message);
                index++;
            }
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
