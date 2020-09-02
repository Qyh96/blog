package JavaSE;

public class SingleTon {
    private SingleTon() {
    }

    ;//私有化构造方法
    private static volatile SingleTon singleTon = null;

    public static SingleTon getSingleTon() {
        //第一次校验
        if (singleTon == null) {
            synchronized (SingleTon.class) {
                if (singleTon == null) {
                    singleTon = new SingleTon();
                }
            }
        }
        return singleTon;
    }
}
