package com.zjk.hy.pulsar.chunk;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

import java.nio.charset.StandardCharsets;

/**
 * @Author zhangjukai
 * @Date 2021-10-09 14:40
 * @Description
 */
public class ChunkProducer {
    public static void main(String[] args) {
        PulsarClient pulsarClient = PulsarFactory.getClient();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 10000000; i++) {
            str.append("hello_java_spring_ES_sparcanal_redis");
        }
        try {
            Producer producer = pulsarClient.newProducer()
                    .topic("persistent://public/default/test-chunk")
                    .enableBatching(false)
                    .enableChunking(true)
                    .create();
            System.out.println(str.toString().getBytes(StandardCharsets.UTF_8).length);
            producer.send(str.toString().getBytes(StandardCharsets.UTF_8));

            //producer.close();
            //pulsarClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
