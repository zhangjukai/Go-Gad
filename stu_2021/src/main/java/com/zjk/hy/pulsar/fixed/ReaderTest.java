package com.zjk.hy.pulsar.fixed;

import com.zjk.hy.pulsar.util.PulsarFactory;
import org.apache.pulsar.client.api.*;

/**
 * @Author zhangjukai
 * @Date 2021-10-15 17:56
 * @Description
 */
public class ReaderTest {
    public static void main(String[] args) {
        try {
            PulsarClient pulsarClient = PulsarFactory.getClient();
            Reader<byte[]> reader = pulsarClient.newReader()
                    .topic("A")
                    .startMessageId(MessageId.earliest)
                    .create();
            while (true) {
                reader.hasMessageAvailable();
                Message message = reader.readNext();
                System.out.println(new String(message.getData()));
            }
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
