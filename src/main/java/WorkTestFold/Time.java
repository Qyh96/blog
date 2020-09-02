package WorkTestFold;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Time {
    private final static SimpleDateFormat sYmdDf = new SimpleDateFormat("yyyy.MM.dd");

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(10000.0/24);
        System.out.println("bigDecimal = " + bigDecimal);
    }
}
