#### ==与equals
==: 比较两个对象的地址是否是一样；  
equals: equals()方法来自于Object对象，如果对象重写了equals()方法，那么比较的是两个对象的内容是否一致，否则和==一样比较两对象的内存地址是否一样。

#### hashCode和equal联系
hashCode也是Object的方法，一般来说，类如果重写了equals()方法，也需要重写hashCode()方法，hashCode()的作用是散列表中用来代替频繁的调用equals()方法，从而减少程序花费在equals()方法时间。

#### 简述线程和进程
进程是程序的一次执行过程，是系统运行程序的基本单位，一个进程在执行过程中可以产生多个线程，多线程共享同一块内存空间和一组系统资源。

#### final关键字
final修饰基本数据变量: 其数值初始化后不能更改;
final修饰非基本数据变量: 变量初始化后不能再指向另外一个对象.

#### Throwable和Error和Exception
Error和Exception都继承自类Throwable，Exception对象常用的getMessage()方法和printStackTrace()方法都是来自于Throwable  
Throwable类常用方法:
- public string getMessage():返回异常发生时的简要描述
- public string toString():返回异常发生时的详细信息
- public void printStackTrace():在控制台上打印 Throwable 对象封装的异常信息


#### try catch finally
无论是否捕获异常，finally块的语句都会执行，如果try catch里有return语句，也是等finally语句块执行完后再执行。  
<u>**注意**</u>: finally如果有return的话，会直接返回。

#### 用transient关键字修饰不想序列化的变量
transient 关键字的作用是：阻止实例中那些用此关键字修饰的的变量序列化；当对象被反序列化时，
被 transient 修饰的变量值不会被持久化和恢复。

### Collections和Arrays工具类常用方法
#### Collections常用方法
- 排序:
```
void reverse(List list)//反转
void sort(List list, Comparator c)//定制排序，由Comparator控制排序逻辑
void swap(List list, int i , int j)//交换两个索引位置的元素
```
- 查找:
```
int max(Collection coll, Comparator c)//根据定制排序，返回最大元素
int min(Collection coll, Comparator c)//根据定制排序，返回最小元素
```

#### Arrays类常用方法
```
void sort(T[] a, Comparator<? super T> c)//定制排序，由Comparator控制排序逻辑
binarySearch()//查找
asList()//转列表,返回由指定数组支持的固定大小的列表
copyOf()//复制
```

