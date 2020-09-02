package HWFormularModelPlus;

/**
 * 水位变化 产生的基本信息类
 */
public class RiverLevelChangeItem {
    private Double startRiverLevel;//第i小时初水位
    private Double endRiverLevel;//第i小时末水位
    private Double Q1 = ModelConstant.Q1;//一泵站流量
    private Double Q2;//二泵站流量
    private Double elecFeePerUnit;//电价
    private Double energyTransRatio;//能量转化系数
    private Double waterUsage;//网管用水量
    private Double HF38;//自由压力HF38
    private Double H;//扬程
    private Double W1;//电费
    private Double H2;//高位水池水位

    public Double getStartRiverLevel() {
        return startRiverLevel;
    }

    public void setStartRiverLevel(Double startRiverLevel) {
        this.startRiverLevel = startRiverLevel;
    }

    public Double getEndRiverLevel() {
        return endRiverLevel;
    }

    public void setEndRiverLevel(Double endRiverLevel) {
        this.endRiverLevel = endRiverLevel;
    }

    public Double getQ1() {
        return Q1;
    }

    public void setQ1(Double q1) {
        Q1 = q1;
    }

    public Double getQ2() {
        return Q2;
    }

    public void setQ2(Double q2) {
        Q2 = q2;
    }

    public Double getElecFeePerUnit() {
        return elecFeePerUnit;
    }

    public void setElecFeePerUnit(Double elecFeePerUnit) {
        this.elecFeePerUnit = elecFeePerUnit;
    }

    public Double getEnergyTransRatio() {
        return energyTransRatio;
    }

    public void setEnergyTransRatio(Double energyTransRatio) {
        this.energyTransRatio = energyTransRatio;
    }

    public Double getWaterUsage() {
        return waterUsage;
    }

    public void setWaterUsage(Double waterUsage) {
        this.waterUsage = waterUsage;
    }

    public Double getHF38() {
        return HF38;
    }

    public void setHF38(Double HF38) {
        this.HF38 = HF38;
    }

    public Double getH() {
        return H;
    }

    public void setH(Double h) {
        H = h;
    }

    public Double getW1() {
        return W1;
    }

    public void setW1(Double w1) {
        W1 = w1;
    }

    public Double getH2() {
        return H2;
    }

    public void setH2(Double h2) {
        H2 = h2;
    }
}
