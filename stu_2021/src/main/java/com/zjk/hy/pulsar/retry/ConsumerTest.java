package com.zjk.hy.pulsar.retry;

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
            Consumer<byte[]> consumer = pulsarClient.newConsumer(Schema.BYTES)
                    .topic("zjk-dead-retry-topic-byte")
                    .subscriptionName("my-subscription")
                    .subscriptionType(SubscriptionType.Shared)
                    .enableRetry(true) // 是否重试
                    .receiverQueueSize(10000)
                    //配置重试队列
                    .deadLetterPolicy(DeadLetterPolicy.builder()
                            .maxRedeliverCount(2) // 重试次数
                            // 设置重试队列
                            .retryLetterTopic("zjk-dead-retry-topic-byte-retry")
                            // 设置死信队列
                            .deadLetterTopic("zjk-dead-retry-topic-byte-DLQ")
                            .build())
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscribe();

            int index = 1;
            boolean flag = true;
            while (true) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                Message message = consumer.receive();
                if(message !=null) {
                    System.out.println("================index:"+index+",data:"+new String(message.getData()));
                   // consumer.acknowledge(message);
                    consumer.reconsumeLater(message,1,TimeUnit.MINUTES);
                    //consumer.acknowledge(message);
                    index++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
