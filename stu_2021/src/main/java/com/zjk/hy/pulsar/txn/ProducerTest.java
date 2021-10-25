package com.zjk.hy.pulsar.txn;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-19 11:02
 * @Description
 */
public class ProducerTest {
    public static void main(String[] args) {
        PulsarClient txnClient = PulsarFactory.getTxnClient();
        Transaction transaction = null;
        try {
            transaction = txnClient.newTransaction().withTransactionTimeout(1, TimeUnit.MINUTES).build().get();
            System.out.println("Producer");
            Producer<String> producer1 = txnClient.newProducer(Schema.STRING).topic("input-topic-10001").sendTimeout(0, TimeUnit.SECONDS).create();
            Producer<String> producer2 = txnClient.newProducer(Schema.STRING).topic("input-topic-20001").sendTimeout(0, TimeUnit.SECONDS).create();
            MessageId m = producer1.newMessage(transaction).value("MSG content 1").send();
            producer2.newMessage(transaction).value("MSG 2").send();
            System.out.println("Producer end:"+m.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
