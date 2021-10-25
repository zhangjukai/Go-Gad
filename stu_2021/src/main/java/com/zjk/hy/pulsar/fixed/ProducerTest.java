package com.zjk.hy.pulsar.fixed;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

/**
 * @Author zhangjukai
 * @Date 2021-10-15 16:42
 * @Description
 */
public class ProducerTest {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        try {
            Producer<String> producer = pulsarClient.newProducer(Schema.STRING).enableBatching(false)
                    .topic("my-test")
                    .create();
            for (int i = 0; i < 10; i++) {
                producer.send("hello_"+i);
            }
           // producer.close();
            //pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
