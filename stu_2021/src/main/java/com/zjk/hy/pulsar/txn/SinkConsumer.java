package com.zjk.hy.pulsar.txn;

import org.apache.pulsar.client.api.*;

/**
 * @Author zhangjukai
 * @Date 2021-10-19 14:49
 * @Description
 */
public class SinkConsumer {
    public static void main(String[] args) {
        try{
            PulsarClient pulsarClient = PulsarClient.builder()
                    .serviceUrl("pulsar://192.168.40.128:6650,192.168.40.129:6650,192.168.40.130:6650")
                    // .statsInterval(0, TimeUnit.SECONDS)
                    .enableTransaction(true)
                    .build();

            String sourceTopic = "public/default/source-topic";
            String sinkTopic = "public/default/sink-topic1";
            Consumer<String> sinkConsumer = pulsarClient
                    .newConsumer(Schema.STRING)
                    .topic(sinkTopic)
                    .subscriptionName("test")
                    .subscriptionType(SubscriptionType.Shared)
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscribe();
            while (true) {
                Message<String> message = sinkConsumer.receive();
                System.out.println("sink获取到的消息："+message.getValue());
                sinkConsumer.acknowledge(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
