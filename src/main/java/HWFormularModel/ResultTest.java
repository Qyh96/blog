package HWFormularModel;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ResultTest {
    public static void main(String[] args) {
        Gson gson = new Gson();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        int[] hours = {1, 1, 1, 1};//单位hour
//        double[] q2 = {100, 500, 800, 1000};//单位m^3/h
//        for (int i = 0; i < hours.length; i++) {
//            double hf38 = HWFormularModel.getHF38(hours[i], q2[i]);
//            System.out.println("时间第:" + hours[i] + "小时，Q2:" + q2[i] + "(L/s)，" + "hf38: " + hf38);
//        }
        TestDemo req = new TestDemo();
        Map<String, String> params = new HashMap<>();
        req.setReq_from("gims");
        req.setSys_id("query_cal_eng_info");
        req.setReq_id("");
        req.setOpr_id("Query");
        req.setIs_continue(0);
        req.setPageSize(20);
        req.setPageNum(1);
        req.setCountSize(100);


        params.put("sql_type","DIST_AUM_EN");
        params.put("sub_type","100");
        params.put("qmark_10","2020-08-20");
        params.put("qmark_20","2020-08-20");
        params.put("qmark_30","2020-08-01");
        params.put("qmark_31","2020-08-20");
        params.put("qmark_40","2020-08-01");
        params.put("qmark_41","2020-08-20");
        req.setParams(params);
        System.out.println("gson.toJson(req) = " + gson.toJson(req));
    }
}
