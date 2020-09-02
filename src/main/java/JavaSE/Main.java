package JavaSE;

public class Main {
    public static void main(String[] args) {
        Integer integer = new Integer("1");

        new Thread(
                () -> {
                    synchronized (integer) {
                        System.out.println("thread1: "+Thread.currentThread()+"is running");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("thread1: "+Thread.currentThread()+"end run");
                    }
                }
                , "线程1").start();

    }

    public void method(){
        synchronized (this){
            System.out.println("synchronize代码块");
        }
    }

}