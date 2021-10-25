package com.zjk.hy.pulsar.txn;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-19 10:55
 * @Description
 */
public class ConsumerTest {
    public static void main(String[] args) {
        PulsarClient txnClient = PulsarFactory.getTxnClient();
        try {
            Consumer<String> consumer1 = txnClient.newConsumer(Schema.STRING).subscriptionType(SubscriptionType.Shared)
                    .enableBatchIndexAcknowledgment(true).topic("input-topic-10001")
                    .subscriptionName("my-sub-1").subscribe();
            Consumer<String> consumer2 = txnClient.newConsumer(Schema.STRING).subscriptionType(SubscriptionType.Shared)
                    .enableBatchIndexAcknowledgment(true).topic("input-topic-20001")
                    .subscriptionName("my-sub-1").subscribe();
            Message<String> message1 = consumer1.receive();
            Message<String> message2 = consumer2.receive();
            System.out.println(message1.getValue());
            System.out.println(message2.getValue());
            Transaction transaction = txnClient.newTransaction().withTransactionTimeout(1, TimeUnit.MINUTES).build().get();
            consumer1.acknowledgeAsync(message1.getMessageId(), transaction);
            consumer2.acknowledgeAsync(message2.getMessageId(), transaction);
            transaction.commit();
            System.out.println("Consumer end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
