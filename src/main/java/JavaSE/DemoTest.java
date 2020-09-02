package JavaSE;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DemoTest {
    public static Gson gson = new Gson();
    public static void main(String[] args) {
        int i = 0;
        System.out.println("System.identityHashCode(i) = " + System.identityHashCode(i));
        int j = i;
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = list1;

        i = 1;
        list1.add(2);

        System.out.println("System.identityHashCode(i) = " + System.identityHashCode(i));
        System.out.println("System.identityHashCode(j) = " + System.identityHashCode(j));
        System.out.println("System.identityHashCode(list1) = " + System.identityHashCode(list1));
        System.out.println("System.identityHashCode(list2) = " + System.identityHashCode(list2));

        System.out.println("i = " + i);
        System.out.println("j = " + j);
        System.out.println("gson.toJson(list1) = " + gson.toJson(list1));
        System.out.println("gson.toJson(list2) = " + gson.toJson(list2));
    }
}