# AbstractList源码分析

​	AbstractList是对List接口的一个主要实现，如果想实现是一个不可修改的List，就需要继承这个抽象类，实现get和size两个方法，要实现可修改的list，必须额外实现set方法，如果集合的大小也可以改变，还需要实现add和remove方法。

AbstractList继承关系如下：

![AbstractList](D:\hycloud\notes\collection\AbstractList.png)

## add(E e)

将元素e添加到集合末尾，

```java
public boolean add(E e) {
    add(size(), e);
    return true;
}
```

## indexOf(Object o)

对象o第一次在集合中出现的位置

```java
public int indexOf(Object o) {
    //获取 ListIterator，此时游标位置为 0
    ListIterator<E> it = listIterator();
    if (o==null) { //如果对象o为null，
        while (it.hasNext())
            if (it.next()==null)
                //返回游标的前面元素索引
                return it.previousIndex();
    } else {
        while (it.hasNext())
            if (o.equals(it.next()))
                //返回游标的前面元素索引
                return it.previousIndex();
    }
    return -1;
}
```

## lastIndexOf(Object o) 

对象o最后一次出现的位置

```java
public int lastIndexOf(Object o) {
     //获取 ListIterator，此时游标位置为 size() ,即最后一位
    ListIterator<E> it = listIterator(size());
    if (o==null) {
        // 向前遍历
        while (it.hasPrevious())
            if (it.previous()==null)
                // 返回游标的后面一个元素的索引（因为是从后往前遍历的）
                return it.nextIndex();
    } else {
        while (it.hasPrevious())
            if (o.equals(it.previous()))
                return it.nextIndex();
    }
    return -1;
}
```

## removeRange(int fromIndex, int toIndex)

删除部分元素

```java
protected void removeRange(int fromIndex, int toIndex) {
    // 从fromIndex位置获取ListIterator
    ListIterator<E> it = listIterator(fromIndex);
    // 遍历toIndex-fromIndex次，删除之间的数据
    for (int i=0, n=toIndex-fromIndex; i<n; i++) {
        it.next();
        it.remove();
    }
}
```

## clear()

删除全部元素

```java
public void clear() {
    // 通过调用子类实现的size()方法，获取集合中元素数量
    // 然后调用removeRange删除从0-size的数据，即全部删除
    removeRange(0, size());
}
```

## addAll(int index, Collection<? extends E> c)

将集合c添加到集合的指定位置

```java
public boolean addAll(int index, Collection<? extends E> c) {
    // 验证index是否下标越界
    rangeCheckForAdd(index);
    boolean modified = false;
    for (E e : c) { //循环遍历，逐个元素进行添加
        // 调用子类的方法实现添加
        add(index++, e);
        modified = true;
    }
    return modified;
}
```

## equals

重写后的equals方法

```java
   public boolean equals(Object o) {
        if (o == this)   //如果两个对象相等，返回true
            return true;
        if (!(o instanceof List))  // 对象o不是List，返回false
            return false;

        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) { // 同时遍历两个集合
            E o1 = e1.next();
            Object o2 = e2.next();
            // 一个一个元素的进行比较
            if (!(o1==null ? o2==null : o1.equals(o2)))
                // 有个集合中存在一个元素不相等就返回false
                return false;
        }
       // 两个集合都遍历完则返回true，否则返回false
        return !(e1.hasNext() || e2.hasNext());
    }
```

# 两种内部迭代器

AbstractList 内部已经提供了 Iterator, ListIterator 迭代器的实现类，分别为 Itr, ListItr 。

## Itr迭代器

简单实现了 Iterator 的 next, remove 方法

```java
private class Itr implements Iterator<E> {
    /**
     * 游标
     */
    int cursor = 0;

    /**
     * 上一次迭代到的位置，每次迭代完就会被重置为-1
     */
    int lastRet = -1;

    /**
     * 用来判断是否发生并发操作的标识，如果这两个值不一样就会报错
     */
    int expectedModCount = modCount;

    // 判断是否还有元素
    public boolean hasNext() {
        return cursor != size();
    }

    public E next() {
        // 检查是否发生并发修改操作
        checkForComodification();
        try {
            int i = cursor;
            // 调用子类实现的get方法获取元素
            E next = get(i);
            // 记录迭代的位置
            lastRet = i;
            // 游标更新为下次迭代位置
            cursor = i + 1;
            return next;
        } catch (IndexOutOfBoundsException e) {
            checkForComodification();
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        if (lastRet < 0)
            throw new IllegalStateException();
        // 检查是否发生并发修改操作
        checkForComodification();

        try {
            // 调用子类实现的remove方法进行删除
            AbstractList.this.remove(lastRet);
            if (lastRet < cursor) // 这两行代码有点理解不了
                cursor--;
            // 删除成功后重置lastRet，
            lastRet = -1;
            expectedModCount = modCount;
        } catch (IndexOutOfBoundsException e) {
            throw new ConcurrentModificationException();
        }
    }

    // 检查是否发生并发操作
    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}
```

## ListItr 

ListItr 是 Itr 的增强版，ListItr 在 Itr 基础上多了 向前 和 set 操作。 

```java
private class ListItr extends Itr implements ListIterator<E> {
    // 添加了指定游标的构造方法
    ListItr(int index) {
        cursor = index;
    }

    // 判断是否有前一个元素
    public boolean hasPrevious() {
        return cursor != 0;
    }

    //获取前一个元素
    public E previous() {
        checkForComodification();
        try {
            int i = cursor - 1;
            E previous = get(i);
            // 设置前一次迭代的位置和游标
            lastRet = cursor = i;
            return previous;
        } catch (IndexOutOfBoundsException e) {
            checkForComodification();
            throw new NoSuchElementException();
        }
    }

    // 返回向后迭代位置，即游标
    public int nextIndex() {
        return cursor;
    }
	// 返回向前迭代位置，即游标-1
    public int previousIndex() {
        return cursor-1;
    }

    // 更新上次迭代位置的元素为 e
    public void set(E e) {
        if (lastRet < 0) // 如果lastRes=-1，直接抛出异常
            throw new IllegalStateException();
        //检查是否发生并发操作
        checkForComodification();
        try {
            // 调用子类实现的set方法
            AbstractList.this.set(lastRet, e);
            expectedModCount = modCount;
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }

    // 添加元素
    public void add(E e) {
        //检查是否发生并发操作
        checkForComodification();
        try {
            int i = cursor;
            // 调用子类的方法在游标位置添加元素
            AbstractList.this.add(i, e);
            // 重置迭代位置
            lastRet = -1;
            // 游标+1
            cursor = i + 1;
            expectedModCount = modCount;
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }
}
```
# 两种内部类

## SubList

```java
// 根据fromIndex和toIndex切割集合，当fromIndex和toIndex相等时返回空集合
public List<E> subList(int fromIndex, int toIndex) {
    return (this instanceof RandomAccess ?
            new RandomAccessSubList<>(this, fromIndex, toIndex) :
            new SubList<>(this, fromIndex, toIndex));
}
```

从上面源码可以发现，该方法根据条件会返回RandomAccessSubList或者SubList对象。RandomAccessSubList和SubList都是AbstractList的内部类。

通过subList(int fromIndex, int toIndex)方法生成的子集合是原集合的视图，在子集合上做结构化的修改时是会影响到原集合的，如下：

```java
public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");
    list.add("d");
    list.add("e");
    list.add("f");

    List<String> list1 = list.subList(1,4);
    list1.forEach(System.out::print);
    System.out.println();
    System.out.println("==========");
    list1.add("zjk");
    list1.forEach(System.out::print);
    System.out.println();
    System.out.println("==========");
    list.forEach(System.out::print);
    System.out.println();
}
打印的结果如下：
bcd
==========
bcdzjk
==========
abcdzjkef
```

由此可见修改子集合确实也修改了原集合，反之如果修改原集合会是什么样的呢？

将实例中的list1.add("zjk");改为list.add("zjk");然后运行，打印结果如下:

```java
bcd
==========
Exception in thread "main" java.util.ConcurrentModificationException
	at java.util.ArrayList$SubList.checkForComodification(ArrayList.java:1231)
	at java.util.ArrayList$SubList.listIterator(ArrayList.java:1091)
	at java.util.AbstractList.listIterator(AbstractList.java:299)
	at java.util.ArrayList$SubList.iterator(ArrayList.java:1087)
	at java.lang.Iterable.forEach(Iterable.java:74)
	at com.zjk.hy.java.Conllection.CopyOfTest.main(CopyOfTest.java:22)

```

由此可见在对原集合做结构修改后再使用子集合，会抛出java.util.ConcurrentModificationException异常，这是因为在操作原集合是改变了modCount，而子集合的modCount并没有改变，导致产生了fail-fast问题。而在修改子集合时同时会处理原集合的modCount，所以不存在这个问题。

由此可见，在石油SubList时需要特别注意，另外可以了解到SubList的另外一个用法：删除一个区间的元素，

```java
list.subList(1,4).clear();
```

SubList在ArrayList中定义为一个私有的内部类，不过功能上是相同的

## Java8关于SubList的替代方案

使用skip或者limit，

limit: 对一个Stream进行截断操作，获取其前N个元素，如果原Stream中包含的元素个数小于N，那就获取其所有的元素；

![limit](D:\hycloud\notes\collection\limit.jpg)

skip: 返回一个丢弃原Stream的前N个元素后剩下元素组成的新Stream，如果原Stream中包含的元素个数小于N，那么返回空Stream；

![skip](D:\hycloud\notes\collection\skip.jpg)

## RandomAccessSubList

```java 
class RandomAccessSubList<E> extends SubList<E> implements RandomAccess {
    RandomAccessSubList(AbstractList<E> list, int fromIndex, int toIndex) {
        super(list, fromIndex, toIndex);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return new RandomAccessSubList<>(this, fromIndex, toIndex);
    }
}
```

RandomAccessSubList继承自SubList以及实现了RandomAccess接口，并没有添加其他实际的功能，其中RandomAccess 是一个空的接口，它用来标识某个类是否支持 **随机访问**（随机访问，相对比“按顺序访问”）。一个支持随机访问的类明显可以使用更加高效的算法。 

比如遍历，实现了 RandomAccess 的集合使用 get():

```java
for (int i=0, n=list.size(); i &lt; n; i++)
	list.get(i);
```

比用迭代器更快：

```java
for (Iterator i=list.iterator(); i.hasNext(); )
    i.next();
```