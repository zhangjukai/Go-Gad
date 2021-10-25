package com.zjk.hy.pulsar.chunk;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-09 14:47
 * @Description
 */
public class ChunkConsumer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();

        Consumer<String> consumer = null;
        try {
            consumer = pulsarClient.newConsumer(Schema.STRING)
                    .topic("persistent://public/default/test-chunk")
                    .subscriptionType(SubscriptionType.Exclusive).maxPendingChunkedMessage(Integer.MAX_VALUE)
                    //.enableRetry(true)
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
            Message<String> message = null;
            try {
                Thread.sleep(1000);
                message = consumer.receive();
                if(message!=null) {
                    System.out.println("get messages");
                    // do something
                    //System.out.println("batch：" + index + ",data:" + message.getValue());
                    System.out.println(message.getValue().getBytes(StandardCharsets.UTF_8).length);
                    consumer.acknowledge(message);
                    index++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
