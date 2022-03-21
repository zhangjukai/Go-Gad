persistent://public/default/

查看topic状态：

```
./bin/pulsar-admin topics stats persistent://public/default/canal-to-pulsar-student
./bin/pulsar-admin topics list public/default/ | grep canal-to-pulsar-student
```



删除分区topic

```
./bin/pulsar-admin topics delete-partitioned-topic canal-to-pulsar-student
```

syncBatchSize





./bin/pulsar-admin topics update-partitioned-topic persistent://public/default/rds_test_carcome_1 --partitions 30

./bin/pulsar-admin topics create-partitioned-topic persistent://public/default/rds_test_carcome_1 --partitions 20

