## Java  Comparable 和 Comparator整理 

## Comparable 

Comparable是一个排序接口，类实现该接口，意味着**该类支持排序** ，可通过Collections.sort或者Arrays.sort进行排序，该类的对象也可用于TreeMap、TreeSet等有序集合。

Person类实现Comparable接口：

```java 
@Override
    public int compareTo(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return -1;
        }
        Person person = (Person) o;
        return this.age - person.getAge();
    }
```

## Comparator

Compatator是比较器接口，Person继承Comparator：

```java
public class PersonComparator implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        return -(o1.getAge()-o2.getAge());
    }
}
```

测试代码：

```java
 public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("张三",18));
        list.add(new Person("王四",28));
        list.add(new Person("王五",20));
        list.add(new Person("王二",100));
        Collections.sort(list);
        list.stream().forEach(System.out::println);
        System.out.println("=============================");
        list.sort(new PersonComparator());
        list.stream().forEach(System.out::println);
        System.out.println("=============================");
        //通过java8 lambda实现
        list.sort((Person a,Person b)->a.getAge()-b.getAge());
        list.stream().forEach(System.out::println);
    }
```

打印结果：

```java
Person(name=张三, age=18)
Person(name=王五, age=20)
Person(name=王四, age=28)
Person(name=王二, age=100)
=============================
Person(name=王二, age=100)
Person(name=王四, age=28)
Person(name=王五, age=20)
Person(name=张三, age=18)
=============================
Person(name=张三, age=18)
Person(name=王五, age=20)
Person(name=王四, age=28)
Person(name=王二, age=100)
```

