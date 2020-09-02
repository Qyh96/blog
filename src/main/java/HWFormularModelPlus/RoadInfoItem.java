package HWFormularModelPlus;

import java.util.ArrayList;
import java.util.List;

public class RoadInfoItem {
    public List<RiverLevelChangeItem> items = new ArrayList<>();
    public String road;

    public Double totalElectricity;

    public List<RiverLevelChangeItem> getItems() {
        return items;
    }

    public void setItems(List<RiverLevelChangeItem> items) {
        this.items = items;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public Double getTotalElectricity() {
        return totalElectricity;
    }

    public void setTotalElectricity(Double totalElectricity) {
        this.totalElectricity = totalElectricity;
    }
}
