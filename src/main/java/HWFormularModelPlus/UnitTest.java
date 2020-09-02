package HWFormularModelPlus;

import com.google.gson.Gson;

import javax.jws.WebParam;

public class UnitTest {
    public final static Gson gson = new Gson();

    public static void main(String[] args) {
        Model.minRiverLevel = 123.0;
        Model.maxRiverLevel = 125.5;
        Model.partition = 6;
        Model.startRiverLevel = 123.5;
        Model.H = new double[]{123.0, 123.5, 124.0, 124.5, 125.0, 125.5};
        RiverLevelChangeItem res = Model.getRiverLevelChangeItem(16, "$CDFFFFFFEEDDCBBAA");
        System.out.println("gson.toJson(res) = " + gson.toJson(res));
        System.out.println("gson.toJson(Model.HF38Cache) = " + gson.toJson(Model.HF38Cache));
        System.out.println("gson.toJson(Model.RiverLevelChangeInfoCache) = " + gson.toJson(Model.RiverLevelChangeInfoCache));
    }
}
