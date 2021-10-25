package com.zjk.hy.pulsar.nack;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

/**
 * @Author zhangjukai
 * @Date 2021-10-09 14:40
 * @Description
 */
public class NackProducer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Producer<String> producer = pulsarClient.newProducer(Schema.STRING)
                    .topic("my-test")
                    .create();
            for (int i = 0; i < 5; i++) {
                producer.send("hello_nack_"+i);
            }
            producer.close();
            pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
