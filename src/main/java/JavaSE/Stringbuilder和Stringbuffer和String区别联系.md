#### StringBuilder和StringBuffer是一种长度可变的字符串存储对象
StringBuilder和StringBuffer都是继承自AbstractStringBuilder类，而StringBuilder和StringBuffer存储字符串的方法都是用基类AbstractStringBuilder字符数组实现的
,所以StringBuilder和StringBuffer都是一种长度可变的字符串存储对象，而String没有继承其他基类，他存储字符串的方法是用自身定义的final修饰的字符数组来实现，所以String是一种不可变的字符串存储对象。

#### StringBuffer相较于StringBuilder是线程安全的
StringBuffer和StringBuilder方法都是继承或重写自基类AbstractStringBuilder，StringBuffer和StringBuilder方法基本一致，而StringBuffer实现线程安全是通过在方法名前加synchronized关键字实现的。

#### StringBuffer或StringBuilder和String相互转换的方法
```
StringBuffer/StringBuilder -> String: stringBuffer.toString()  
String -> StringBuffer/StringBuilder: new StringBuffer(string);
```


#### synchronized简单用法
- 修饰实例方法: 作用于当前对象实例加锁，进入同步代码前要获得当前对象实例的锁;
- 修饰静态方法: 也就是给当前类加锁，会作用于类的所有对象实例，因为静态成员不属于任何一个实例对象，是类成员（ static 表明这是该类的一个静态资源，不管new了多少个对象，只有
  一份）。所以如果一个线程A调用一个实例对象的非静态 synchronized方法，而线程B需要调用
  这个实例对象所属类的静态 synchronized方法是允许的，不会发⽣互斥现象，因为访问静态
  synchronized用法占用的锁是当前类的锁，而访问非静态 synchronized 方法占用的锁是当前
  实例对象锁；
- 修饰代码块: 指定加锁对象(类对象/实例对象)，对给定对象加锁，进入同步代码库前要获得给定对象的锁。

#### 单例模式中的synchronized用法
```java
public class SingleTon {
    private SingleTon() {
    }

    ;//私有化构造方法
    private static volatile SingleTon singleTon = null;

    public static SingleTon getSingleTon() {
        //第一次校验
        if (singleTon == null) {
            synchronized (SingleTon.class) {
                //第二次校验
                if (singleTon == null) {
                    singleTon = new SingleTon();
                }
            }
        }
        return singleTon;
    }
}
``` 
第一次校验：避免每次调用getSingleTon方法的时候都是执行同步代码块，同步代码块只在一开始创建实例的时候才会执行。  
第二次校验：创建SingleTon实例的时候，可能有多个线程同时执行到同步代码块的地方，第一个执行通过同步代码快的线程已经创建了一个实例，为避免之后的线程再重新创建实例，故这里也需要对实例进行二次校验。  
<u>**注意**</u>：这里的实例变量用关键字volatile修饰，volatile能确保变量从主内存中进行读写，确保数据变化在线程之间可见。
