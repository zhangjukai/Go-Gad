1.首先确定github网站的IP:
打开：https://github.com.ipaddress.com
可以查看到github.com对应的IP为：140.82.114.3

2.确定域名的IP:
打开：https://fastly.net.ipaddress.com/github.global.ssl.fastly.net
可以查看到github.global.ssl.fastly.net对应的IP为：199.232.5.194

3.确定静态资源的IP:
打开：https://github.com.ipaddress.com/assets-cdn.github.com
可以查看到

4.修改host

C:\Windows\System32\drivers\etc

140.82.114.3 github.com
199.232.5.194 github.global.ssl.fastly.net
185.199.108.153 github.github.io
185.199.109.153 github.github.io
185.199.110.153 github.github.io
185.199.111.153 github.github.io