package JavaSE;
import lombok.Data;

import java.util.List;
@Data
public class BaseObject {
    private int anInt;
    private long l;
    private Integer integer;
    private Long aLong;
    private String string;
    private List<Integer> list;

    public BaseObject(long l, Long aLong, String string) {
        this.l = l;
        this.aLong = aLong;
        this.string = string;
    }
//
//    public BaseObject() {
//    }
}
