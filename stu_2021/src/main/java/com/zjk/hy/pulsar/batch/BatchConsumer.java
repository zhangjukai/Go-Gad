package com.zjk.hy.pulsar.batch;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-09 14:47
 * @Description
 */
public class BatchConsumer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();

        Consumer<String> consumer = null;
        try {
            consumer = pulsarClient.newConsumer(Schema.STRING)
                    .topic("my-test-batch")
                    .subscriptionType(SubscriptionType.Exclusive)
                    .enableRetry(true)
                    .receiverQueueSize(10)
                    .ackTimeout(1000,TimeUnit.MILLISECONDS)

                    .subscriptionName("my-subsc")
                    //.subscriptionType(SubscriptionType.Failover)
                    //.subscriptionType(SubscriptionType.Shared)
                    .subscribe();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }

        int index = 1;
        boolean flag = true;
        while (true) {
            Messages<String> messages = null;
            try {
                Thread.sleep(1000);
                messages = consumer.batchReceive();
                if(messages!=null && messages.size()>0) {
                    System.out.println("get messages");
                    for (Message<String> message : messages) {
                        // do something
                        System.out.println("batch：" + index + ",data:" + message.getValue());
                    }
                    /*if(index==10 && flag) {
                        System.out.println("===============================================================");
                        flag = false;
                        consumer.negativeAcknowledge(messages);
                    } else {
                        consumer.acknowledge(messages);
                    }*/
                   consumer.acknowledge(messages);
                    index++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
