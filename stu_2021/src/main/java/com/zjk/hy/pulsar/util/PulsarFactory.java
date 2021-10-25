package com.zjk.hy.pulsar.util;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.transaction.Transaction;

import java.util.concurrent.ExecutionException;

/**
 * @Author zhangjukai
 * @Date 2021-10-08 10:52
 * @Description
 */
public class PulsarFactory {
    public static Transaction txn;
    public static PulsarClient getClient() {
        try {
            return PulsarClient.builder().serviceUrl("pulsar://192.168.40.128:6650,192.168.40.129:6650,192.168.40.130:6650")
                    //.enableTransaction(true)
                    .build();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static PulsarClient getTxnClient() {
        try {
            return PulsarClient.builder().serviceUrl("pulsar://192.168.40.128:6650,192.168.40.129:6650,192.168.40.130:6650")
                    .enableTransaction(true)
                    .build();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
        return null;
    }

   public static Transaction  getTxn(){
        if(txn == null) {
            try {
                txn = getTxnClient().newTransaction()
                        .build()
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }
        return txn;

   }
}
