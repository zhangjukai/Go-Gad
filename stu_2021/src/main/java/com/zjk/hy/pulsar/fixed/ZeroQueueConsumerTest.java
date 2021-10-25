package com.zjk.hy.pulsar.fixed;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

/**
 * @Author zhangjukai
 * @Date 2021-10-15 16:44
 * @Description
 */
public class ZeroQueueConsumerTest {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Consumer<String> consumer = pulsarClient.newConsumer(Schema.STRING)
                    .subscriptionName("my-subscription")
                    .topic("my-test")
                    .messageListener(new MessageListener<String>() {
                        @Override
                        public void received(Consumer<String> consumer, Message<String> message) {
                            System.out.println(message.getValue());
                            System.out.println("================");
                        }
                    })
                    .receiverQueueSize(0)
                    .subscribe();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
