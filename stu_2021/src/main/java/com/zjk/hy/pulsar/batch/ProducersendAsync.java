package com.zjk.hy.pulsar.batch;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

/**
 * @Author zhangjukai
 * @Date 2021-10-11 15:34
 * @Description
 */
public class ProducersendAsync {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Producer<String> producer = pulsarClient.newProducer(Schema.STRING)
                    .topic("my-test").enableBatching(true).batchingMaxMessages(50)
                    .create();
            for (int i = 0; i < 200; i++) {
                producer.sendAsync("hello_"+i).thenAccept(msgId -> {
                    System.out.println("Message with ID " + msgId + " successfully sent");
                });
            }
           // producer.close();
           // pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
