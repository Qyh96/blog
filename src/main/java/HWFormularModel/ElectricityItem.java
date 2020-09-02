package HWFormularModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一个水位路线对应的电费、流量、扬程详情
 */
public class ElectricityItem {
    List<Double> electricityRoad = new ArrayList<>();//电费
    List<Double> Q2 = new ArrayList<>();//流量Q2
    List<Double> h = new ArrayList<>();//扬程
    List<Double> HF38 = new ArrayList<>();//HF38
    List<Double> H2 = new ArrayList<>();//高位水池水位
    double sumElectricity;//总电费


    public List<Double> getH2() {
        return H2;
    }

    public void setH2(List<Double> h2) {
        H2 = h2;
    }

    public List<Double> getHF38() {
        return HF38;
    }

    public void setHF38(List<Double> HF38) {
        this.HF38 = HF38;
    }

    public List<Double> getElectricityRoad() {
        return electricityRoad;
    }

    public void setElectricityRoad(List<Double> electricityRoad) {
        this.electricityRoad = electricityRoad;
    }


    public List<Double> getQ2() {
        return Q2;
    }

    public void setQ2(List<Double> q2) {
        Q2 = q2;
    }

    public List<Double> getH() {
        return h;
    }

    public void setH(List<Double> h) {
        this.h = h;
    }

    public double getSumElectricity() {
        return sumElectricity;
    }

    public void setSumElectricity(double sumElectricity) {
        this.sumElectricity = sumElectricity;
    }
}
