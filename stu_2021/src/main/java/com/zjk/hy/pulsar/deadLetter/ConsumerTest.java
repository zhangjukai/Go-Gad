package com.zjk.hy.pulsar.deadLetter;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-08 11:06
 * @Description
 */
public class ConsumerTest {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {

            Consumer<String> consumer = pulsarClient.newConsumer(Schema.STRING)
                    .topic("zjk-dead-retry-topic")
                    .ackTimeout(3,TimeUnit.SECONDS)
                    .subscriptionName("my-subscription")
                    .subscriptionType(SubscriptionType.Shared)
                    .enableRetry(true)
                    .receiverQueueSize(10000)
                    //配置重试队列
                    .deadLetterPolicy(DeadLetterPolicy.builder()
                            .maxRedeliverCount(3)
                            .retryLetterTopic("zjk-dead-retry-topic")
                            .deadLetterTopic("zjk-dead-retry-topic-str-topic-DLQ")
                            .build())
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscribe();

            int index = 1;
            boolean flag = true;
            while (true) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                Message message = consumer.receive(10 , TimeUnit.SECONDS);
                if(message !=null) {
                    System.out.println("================index:"+index+",data:"+message.getValue());
                   // consumer.acknowledge(message);
                    consumer.reconsumeLater(message,1,TimeUnit.SECONDS);
                    index++;
                }

            }
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
