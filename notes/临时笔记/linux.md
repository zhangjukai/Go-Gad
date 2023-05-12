文件挂载：

umount /boot

mount /dev/sda1 /boot

文件类型：

-：普通文件（可执行，图片，文本）

d：目录

l：链接

b：块设备

c：字符设备

s：socket

p：pipeline

[eventpoll]：



0：标准输入  1：标准输出  2：报错输出

网络：

lsof -p：lsof -p pid |wc -l：统计单个进程打开的句柄数



netstat -natp

tcpdump 抓取通讯包

tcpdump -nn -i eth0 port 9090



socket 四元组 cip+cport+sip+sport

strace -ff -o out cmd 跟踪系统调用，关注IO的实现

cat info.log | grep error | more



grep "error" info.log | more

grep -R "3e15c18e21b34a849e2e7182facd159e" ./

cat  catalina.out | grep 【MQ-HTTP-API】消息处理超时 | tail -n 10

grep -iR 9100 ./











