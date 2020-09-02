package HWFormularModelPlus;

/**
 * 定义模型的基本常量
 */
public class ModelConstant {
    public final static int A1 = 3675;  //计算二泵站流量的系数
    public final static int A2 = 1575;//高位水池底面积
    public final static double H2 = 0.5;//高位水池起始水位
    public final static int HOURS = 24;//24小时
    public final static double Q1 = 100000.0/24;//一级系站流量(m3/h)
    public final static double EFFICIENCY = 0.7;//计算电费的效率
    public final static double BASIC_RIVER_LEVEL = 125.5;//计算扬程的标准水位线125.5
    public final static double BASIC_FACTOR = 3.58;//计算电费的标准系数3.58
    public final static double[] Q = {1750.00, 1600.00, 1450.00, 1450.00, 2600.00, 4400.00, 5200.00, 5600.00, 6250.00, 5800.00,
            5000.00, 5150.00, 5150.00, 5150.00, 5550.00, 5600.00, 5650.00, 5900.00, 5650.00, 5000.00,
            3200.00, 2600.00, 2550.00, 1750.00};//(管网用水量m3/h)
    public final static double ELECTRICITY_LIMIT = 1000000000;//电费上限10亿
    public final static double[] E = {0.333, 0.333, 0.333, 0.333, 0.333, 0.333, 0.685, 0.685, 1.113, 1.113, 1.113, 0.685, 0.685, 0.685,
            0.685, 0.685, 0.685, 0.685, 1.113, 1.113, 1.113, 0.685, 0.333, 0.333};//C电价数组
    public final static double[] N = {0.019, 0.018, 0.016, 0.016, 0.030, 0.057, 0.149, 0.166, 0.549, 0.285, 0.228, 0.147, 0.147,
            0.147, 0.164, 0.166, 0.168, 0.180, 0.274, 0.228, 0.127, 0.061, 0.029, 0.019};//能量变化系数
}
