package com.zjk.hy.pulsar.txn;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangjukai
 * @Date 2021-10-21 16:36
 * @Description
 */
public class DebugTest {
    public static void main(String[] args) {
        String sinkTopic = "public/default/sink-topic1";
        PulsarClient txnClient = PulsarFactory.getTxnClient();
        Transaction transaction = null;
        try {
            transaction = txnClient.newTransaction().withTransactionTimeout(5, TimeUnit.MINUTES).build().get();
            Producer<String> producer1 = txnClient.newProducer(Schema.STRING).topic(sinkTopic).sendTimeout(0, TimeUnit.SECONDS).create();
            MessageId m = producer1.newMessage(transaction).value("MSG content 1").send();
            System.out.println(m.toString());
            transaction.commit();
           // producer1.close();
           // txnClient.close();
        } catch (Exception e) {
            if(transaction!=null) {
                transaction.abort();
            }
            e.printStackTrace();
        }
    }
}
