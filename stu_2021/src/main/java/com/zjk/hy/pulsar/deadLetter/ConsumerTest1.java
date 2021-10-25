package com.zjk.hy.pulsar.deadLetter;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-08 11:06
 * @Description
 */
public class ConsumerTest1 {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {

            Consumer<String> consumer = pulsarClient.newConsumer(Schema.STRING)
                    .topic("zjk-dead-retry-topic-str-Retry")
                    .ackTimeout(5,TimeUnit.SECONDS)
                    .subscriptionName("my-subscription1")
                    .subscriptionType(SubscriptionType.Shared)
                    .enableRetry(true)
                    .receiverQueueSize(100000)
                    //配置重试队列
               /*     .deadLetterPolicy(DeadLetterPolicy.builder()
                            .maxRedeliverCount(3)
                            .retryLetterTopic("my-retry-topic-str-Retry")
                           // .deadLetterTopic("my-retry-topic-str-topic-DLQ")
                            .build())
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)*/
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscribe();

            int index = 1;
            boolean flag = true;
            while (true) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                Message message = consumer.receive();
                System.out.println("================index:"+index+",data:"+message.getValue());
                consumer.acknowledge(message);
                //consumer.reconsumeLater(message,5,TimeUnit.SECONDS);
                index++;
            }
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
