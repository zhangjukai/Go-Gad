# Java equals、hashCode整理

## equals 与 ==

 equals()的作用是**用来判断两个对象是否相等**，定义在Object.java类中

```java 
public boolean equals(Object obj) {
        return (this == obj);
}
```

根据equals方法中的源码可以看出，对象在不重写equals()方法的情况下，使用equals()方法判读两个对象是否相等，equals()等价于==；重写equals方法的情况看如下实例：

```java
public static void main(String[] args) {
        String str = new String("abc");
        String str1 = "abc";
        System.out.println(str==str1);
        System.out.println(str.equals(str1));
}
```

打印结果为：false   true

str==str1返回false，是因为str存储的是new String("abc")的对象地址，因而str于str2是不相等的

str.equals(str1)返回true，是因为String重写了equals方法，比较的为两个字符串的具体内容。String equals方法源码：

```java
  public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
```

（该equals方法是通过逐个字符进行对比的）

## hashCode

hashCode()的作用是获取哈希码（散列码），实际返回一个int整数，这个哈希码的作用是确定对象在哈希表中的位置。需要强调的是: **hashCode() 在散列表中才有用，在其它情况下没用** 。后面对HashSet, Hashtable, HashMap等的源码进行研究的时候再补充hashCode()方法在其中怎样用的。

```java
public static void main(String[] args) {
        Set<Person> set = new HashSet<Person>();
        Person person = new Person("aaa",10);
        Person person1 = new Person("aaa",10);
        set.add(person);
        set.add(person1);
        //重写equals方法后，返回true
        System.out.println(person.equals(person1));
        //不重写hashCode方法，会打印两个对象
        set.stream().forEach(System.out::println);
}
```

Person中只重写了equals方法：

```java
public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        //如果是同一个对象返回true，反之返回false
        if (this == obj) {
            return true;
        }
        Person person = (Person) obj;
        return name.equals(person.name) && age == person.age;
    }
```



最后打印出如下结果：

```java
true
Person(name=aaa, address=null, phone=0, age=10)
Person(name=aaa, address=null, phone=0, age=10)
```

当重写hashCode()方法后：

```java
  @Override
    public int hashCode() {
        return this.name.hashCode() ^ age;
    }
```

打印结果如下：

```java
true
Person(name=aaa, address=null, phone=0, age=10)
```

