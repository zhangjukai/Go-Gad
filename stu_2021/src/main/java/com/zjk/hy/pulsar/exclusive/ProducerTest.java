package com.zjk.hy.pulsar.exclusive;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.BatcherBuilder;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;


/**
 * @Author zhangjukai
 * @Date 2021-10-08 11:02
 * @Description
 */
public class ProducerTest {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Producer<String> producer = pulsarClient.newProducer(Schema.STRING)
                    .topic("my-topic-1000")
                    .create();
            for (int i = 0; i < 2000; i++) {
                producer.send("hello_"+i);
                Thread.sleep(10);
            }
            producer.close();
            pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
