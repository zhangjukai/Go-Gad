# Redis分布式锁

## setnx命令

**格式： setnx key value  **

将key的值设置为value，当且仅当key不存在时，设置成功返回1 ，

如果给定的key已经存在，则不做任何操作，返回0

## 将 SETNX 用于加锁(locking)  

