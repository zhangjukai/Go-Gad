package com.zjk.hy.pulsar.txn;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-19 14:39
 * @Description
 */
public class TransactionTest {
    public static void main(String[] args) {
        Transaction txn = null;
        try {
            PulsarClient pulsarClient = PulsarClient.builder()
                    .serviceUrl("pulsar://192.168.40.128:6650,192.168.40.129:6650,192.168.40.130:6650")
                   // .statsInterval(0, TimeUnit.SECONDS)
                    .enableTransaction(true)
                    .build();

            String sourceTopic = "public/default/source-topic";
            String sinkTopic = "public/default/sink-topic1";

            Producer<String> sourceProducer = pulsarClient
                    .newProducer(Schema.STRING)
                    .topic(sourceTopic)
                    .create();
            sourceProducer.newMessage().value("hello pulsar transaction").sendAsync();

            Consumer<String> sourceConsumer = pulsarClient
                    .newConsumer(Schema.STRING)
                    .topic(sourceTopic)
                    .subscriptionName("test")
                    .subscriptionType(SubscriptionType.Shared)
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscribe();

            Producer<String> sinkProducer = pulsarClient
                    .newProducer(Schema.STRING)
                    .topic(sinkTopic)
                    .sendTimeout(0, TimeUnit.MILLISECONDS)
                    .create();

            txn = pulsarClient
                    .newTransaction()
                    .withTransactionTimeout(1, TimeUnit.MINUTES)
                    .build()
                    .get();

            // source message acknowledgement and sink message produce belong to one transaction,
            // they are combined into an atomic operation.
            Message<String> message = sourceConsumer.receive();

            // 支付  newTransaction
            System.out.println(message.getValue());
            sourceConsumer.acknowledgeAsync(message.getMessageId(), txn);
            sinkProducer.newMessage(txn).value("kaifp").send();
           // sinkProducer.newMessage(txn).value("sink data1").send();
            System.out.println("message："+message.getValue());
            System.out.println("执行成功");
            txn.commit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
