# AbstractCollection源码分析

AbstractCollection实现了Collection，实现了其中部分方法，是一个抽象类。

**isEmpty**判断集合是否为空

```java
 public boolean isEmpty() {
     // 通过size()方法获取集合中对象的数量
     // 通过size == 0来判断是否为null
     return size() == 0;
 }
```

**contains**集合是否包含某个元素

```java
public boolean contains(Object o) {
    Iterator<E> it = iterator();
    if (o==null) { //传入的对象为null，则判断集合中是否有null
        while (it.hasNext())
            if (it.next()==null)
                return true;
    } else {
        while (it.hasNext())
            // 传入对象不为null，通过equals方法判断两个对象的值是否相等
            if (o.equals(it.next()))
                return true;
    }
    return false;
}
```

**toArray**将集合转化为数组

```java
public Object[] toArray() {
    // 根据size定义一个数组
    Object[] r = new Object[size()];
    Iterator<E> it = iterator();
    for (int i = 0; i < r.length; i++) {
        if (! it.hasNext()) // 如果it中已没有元素
            //通过Arrays.copyof()方法进行null的填充
            return Arrays.copyOf(r, i);
        // 通过下标设置数组的值
        r[i] = it.next();
    }
    // 如果it.hasNext返回True，表示还有值，则用finishToArray进行转换，finishToArray会进行扩容
    return it.hasNext() ? finishToArray(r, it) : r;
}
```

**toArray(T[] a)**将结合拷贝到指定的集合中

```java
public <T> T[] toArray(T[] a) {
    // Estimate size of array; be prepared to see more or fewer elements
    int size = size();
    // 如果目标数组的长度大于等于集合数量，则直接使用目标数组a，
    // 如果小于，则通过反射根据a的类型和集合数量创建新的数组
    T[] r = a.length >= size ? a :
              (T[])java.lang.reflect.Array
              .newInstance(a.getClass().getComponentType(), size);
    Iterator<E> it = iterator();

    for (int i = 0; i < r.length; i++) {
        if (! it.hasNext()) { // it中已经没有元素
            if (a == r) { // 如何r就是传入的目标数组，即a.length>=sie,
                // 将i位置的元素设置为null，i++后面的值不变
                r[i] = null; // null-terminate
            } else if (a.length < i) { // 传入的数组长度小于集合大小size
                // 将数据拷贝到新数组中
                return Arrays.copyOf(r, i);
            } else { 
                // (a!=r && a.length>=i && !it.hasNext())等同于(a.length<size && a.leng>=i && !it.hasNext())
                // 就以上条件，感觉只有在多线程并发的情况下才会出现
                System.arraycopy(r, 0, a, 0, i);
                if (a.length > i) {
                    a[i] = null;
                }
            }
            // 此处直接返回，说明i以后的数据是不会变化的
            return a;
        }
        r[i] = (T)it.next();
    }
    // more elements than expected
    // 还有元素，调用finishToArray方法 同样感觉只有在多线程并发的情况下才会出现
    return it.hasNext() ? finishToArray(r, it) : r;
}
```
**finishToArray**数组扩容

```java
// 数组允许的最大容量
// -8是以为有部分空间用来存放头信息，用来存数据的大小
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

//数组扩容
private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
    int i = r.length;
    while (it.hasNext()) {
        int cap = r.length;
        // 注意：这里的迭代器是从上层的方法（toArray）传过来的，
        // 并且这个迭代器已执行了一部分，而不是从头开始迭代的
        // 理论上第一次执行i == cap 是返回true的
        if (i == cap) {
            // 计算新的数组大小
            int newCap = cap + (cap >> 1) + 1;
            // overflow-conscious code
            if (newCap - MAX_ARRAY_SIZE > 0)
                // 超过MAX_ARRAY_SIZE，创建大小为MAX_ARRAY_SIZE的数组
                newCap = hugeCapacity(cap + 1);
            // 数组拷贝
            r = Arrays.copyOf(r, newCap);
        }
        r[i++] = (T)it.next();
    }
    // trim if overallocated
    return (i == r.length) ? r : Arrays.copyOf(r, i);
}
```

其他的add、remove方法逻辑都很简单，就不详细介绍了