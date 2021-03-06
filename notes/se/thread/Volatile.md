## volatile

volatile关键字的作用：保障可见性、保障有序性、保障long/double型变量读写的原子性

### 底层原理

+ 字节码层面

  ACC_VOLATILE

+ JVM层面

  volatile内存区的读写都加了内存屏障

  > StoreStoreBarrier
  >
  > volatile写操作
  >
  > StoreLoadBarrier

  > LoadLoadBarrier
  >
  > volatile读操作
  >
  > LoadStoreBarrier

+ OS和硬件层面

  Windows下是通过Lock指令实现的

### 指令级Lock

通过Idea的hsdis插件查看VolatileTest的汇编指令,源代码：

```java
public class VolatileTest {
    public volatile static boolean flag = false;
    public static void main(String[] args) {
        flag = true;
    }
}
```

汇编指令：

```java
CompilerOracle: compileonly *VolatileTest.*
Loaded disassembler from D:\Program Files\Java\jdk8\jre\bin\server\hsdis-amd64.dll
Decoding compiled method 0x0000000002a04950:
Code:
[Disassembling for mach='i386:x86-64']
[Entry Point]
[Verified Entry Point]
[Constants]
  # {method} {0x0000000054bd2ad8} '<clinit>' '()V' in 'com/zjk/test/VolatileTest'
  #           [sp+0x40]  (sp of caller)
  0x0000000002a04aa0: mov    %eax,-0x6000(%rsp)
  0x0000000002a04aa7: push   %rbp
  0x0000000002a04aa8: sub    $0x30,%rsp
  0x0000000002a04aac: movabs $0x54bd2b98,%rsi   ;   {metadata(method data for {method} {0x0000000054bd2ad8} '<clinit>' '()V' in 'com/zjk/test/VolatileTest')}
  0x0000000002a04ab6: mov    0xdc(%rsi),%edi
  0x0000000002a04abc: add    $0x8,%edi
  0x0000000002a04abf: mov    %edi,0xdc(%rsi)
  0x0000000002a04ac5: movabs $0x54bd2ad0,%rsi   ;   {metadata({method} {0x0000000054bd2ad8} '<clinit>' '()V' in 'com/zjk/test/VolatileTest')}
  0x0000000002a04acf: and    $0x0,%edi
  0x0000000002a04ad2: cmp    $0x0,%edi
  0x0000000002a04ad5: je     0x0000000002a04b04  ;*iconst_0
                                                ; - com.zjk.test.VolatileTest::<clinit>@0 (line 4)

  0x0000000002a04adb: nopl   0x0(%rax,%rax,1)
  0x0000000002a04ae0: jmpq   0x0000000002a04b6a  ;   {no_reloc}
  0x0000000002a04ae5: add    %al,(%rax)
  0x0000000002a04ae7: add    %al,(%rax)
  0x0000000002a04ae9: add    %bh,0x0(%rdi)
  0x0000000002a04aef: mov    %dil,0x68(%rsi)
  0x0000000002a04af3: lock addl $0x0,(%rsp)     ;*putstatic flag
                                                ; - com.zjk.test.VolatileTest::<clinit>@1 (line 4)
```

在以上指令中我们发现**0x0000000002a04af3: lock addl $0x0,(%rsp)     ;*putstatic flag**，这条指令对应的就是给flag赋值，在这条指令上加上了**lock**前缀。

**Lock指令会锁总线，其他CPU的内存操作将会被阻塞**

+ 嗅探

  每个处理器通过嗅探在总线上传播的数据来检查自己缓存的值是不是过期了，当处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置成无效状态，当处理器对这个数据进行修改操作的时候，会重新从系统内存中把数据读到处理器缓存里。

+ 总线风暴

  由于Volatile的MESI缓存一致性协议，需要不断的从主内存嗅探和cas不断循环，无效交互会导致总线带宽达到峰值。

  所以不要大量使用Volatile，至于什么时候去使用Volatile什么时候使用锁，根据场景区分。

#### volatile注意点

1. 最好用于修饰基本类型，不要用来修饰引用类型，引用类型变量存放的是对象地址，修改对象中的内容时，内存地址不会发生变化
2. 不是完全确定的情况不要使用volatile

对于第一点存在的问题，示例如下：

```java
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Demo01 {
    private static volatile ArrayList<Integer> list = new ArrayList<>();
    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                list.add(i);
                System.out.println("i:"+i);
            }
        }).start();

        new Thread(()->{
            while (true) {
                if (list.size()==5) {
                    System.out.println("到达退出点");
                    break;
                }
            }
        }).start();
    }
}
```

上述代码的运行结果是：永远不会打印"到达退出点"，原因是因为list的值是ArrayList对象的内存地址，一直没有发生过改变。