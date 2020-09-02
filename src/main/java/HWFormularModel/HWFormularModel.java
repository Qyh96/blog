package HWFormularModel;


import com.google.gson.Gson;

import java.util.*;

public class HWFormularModel {
    /**
     * 对象转json变量
     */
    public final static Gson gson = new Gson();

    /**
     * 模型常量定义区域
     */
    public final static int A1 = 3675;  //计算二泵站流量的系数
    public final static int A2 = 1575;//高位水池底面积
    public final static double H2 = 0.5;//高位水池起始水位
    public final static int HOURS = 24;//24小时
    public final static double Q1 = 4166.67;//一级系站流量(m3/h)
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


    public final static HashMap<String, Double> HF38Cache = new HashMap<>();//HF38缓存

    /**
     * 模型输入值定义区域：最低水位、最高水位、划分水位数、水分划分数组
     */
    public static double minRiverLevel;//最低水位
    public static double maxRiverLevel;//最高水位
    public static int partition;//划分水位数
    public static double startRiverLevel;//起始水位


    /**
     * 程序入口
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("==============程序开始===============");
        try {
            String[] paraStr = new String[]{"最低水位", "最高水位", "划分水位数", "起始水位"};
            String[] paraData = new String[paraStr.length];
            Scanner scanner = new Scanner(System.in);
            //从终端读取最低水位、最高水位、划分水位数
            for (int i = 0; i < paraStr.length; i++) {
                System.out.println("请输入:" + paraStr[i] + " 以换行符结束");
                if (scanner.hasNext()) {
                    paraData[i] = scanner.next().trim();
                }
            }


            //最低水位、最高水位、划分水位数 水位分层 赋值
            minRiverLevel = Double.valueOf(paraData[0]);
            maxRiverLevel = Double.valueOf(paraData[1]);
            partition = Integer.valueOf(paraData[2]);
            startRiverLevel = Double.valueOf(paraData[3]);
            double[] H = new double[partition];//水位划分,根据输入的最低水位和最高水位和划分水位数得来
            for (int i = 0; i < partition; i++) {
                H[i] = minRiverLevel + (maxRiverLevel - minRiverLevel) / (partition - 1) * i;
            }

            //终端输出模型所有变量信息
            printModelVarValue(minRiverLevel, maxRiverLevel, partition, H);

            //遍历24小时
            HashMap<String, ElectricityItem> response = new HashMap<>();
            response.put("$", new ElectricityItem());
            for (int i = 0; i < HOURS; i++) {
                //深拷贝
                HashMap<String, ElectricityItem> responseCopy = new HashMap<>();
                responseCopy.putAll(response);
                response = new HashMap<String, ElectricityItem>();
                if (i < HOURS - 1) {
                    for (Map.Entry<String, ElectricityItem> item : responseCopy.entrySet()) {
                        String road = item.getKey();
                        ElectricityItem electricityItem = item.getValue();
                        //每一条的路线，终端和H水位层进行全连接
                        int starHour = i;//第i小时初
                        int endHour = i + 1;//第i+1小时初
                        //如果这条线路的电费已经超Integet.MaxVALUE，那这条路线就没有必要再进行下去，进入下一个循环
                        if (electricityItem.getSumElectricity() >= ELECTRICITY_LIMIT) {
                            continue;
                        } else {
                            //终端和H水位层进行全连接
                            for (int j = 0; j < H.length; j++) {
                                String newRoad = road + (char) ((int) 'A' + j);
                                ElectricityItem newElectricityItem = gson.fromJson(gson.toJson(electricityItem), ElectricityItem.class);//list深拷贝
                                double q2 = getQ2(newRoad, H, i, i + 1);
                                //如果Q2有效且i小时末高水位H2有效
                                Boolean checkQ2 = isValid(q2, i);
                                Boolean checkH2 = checkHighRiverLevel(i, H[j]);
                                double h2 = getHighRiverLevel(i, H[j]);
                                if (checkQ2 && checkH2) {
                                    double hf38;
                                    if (HF38Cache.get(i + "," + q2) != null) {
                                        //从缓存里读取hf38的值
                                        hf38 = HF38Cache.get(i + "," + q2);
                                    } else {
                                        hf38 = getHF38(i, q2);
                                        HF38Cache.put(i + "," + q2, hf38);
                                    }
                                    double h = getH(newRoad, H, i, q2, hf38);//扬程h
                                    double electricity = getElectricity(i, q2, h);//电费
                                    newElectricityItem.getH().add(h);//扬程塞值
                                    newElectricityItem.getQ2().add(q2);//Q2塞值
                                    newElectricityItem.getElectricityRoad().add(electricity);//电费塞值
                                    newElectricityItem.getH2().add(h2);//高位水位塞值
                                    double electrictySum = newElectricityItem.getElectricityRoad().stream().mapToDouble(Double::doubleValue).sum();
                                    newElectricityItem.setSumElectricity(electrictySum);//总电费塞值
                                    newElectricityItem.getHF38().add(hf38);
                                    response.put(newRoad, newElectricityItem);
                                } else {
                                    newElectricityItem.getQ2().add(q2);//Q2塞值
                                    newElectricityItem.getElectricityRoad().add(ELECTRICITY_LIMIT);//电费塞值
                                    newElectricityItem.getH2().add(h2);//高位水位塞值
                                    double electrictySum = newElectricityItem.getElectricityRoad().stream().mapToDouble(Double::doubleValue).sum();
                                    newElectricityItem.setSumElectricity(electrictySum);
                                    response.put(newRoad, newElectricityItem);
                                }
                            }
                        }
                    }
                } else {
                    //第24小时回归原位
                    for (Map.Entry<String, ElectricityItem> item : responseCopy.entrySet()) {
                        String road = item.getKey();
                        ElectricityItem electricityItem = item.getValue();
                        //每一条的路线，终端和原始水位全连接
                        int starHour = i;//第23小时初
                        int endHour = i + 1;//第24小时初
                        //如果这条线路的电费已经超1000000000，那这条路线就没有必要再进行下去，进入下一个循环
                        if (electricityItem.getSumElectricity() >= ELECTRICITY_LIMIT) {
                            continue;
                        } else {
                            //终端和原始水位进行全连接
                            String newRoad = road + "$";
                            ElectricityItem newElectricityItem = gson.fromJson(gson.toJson(electricityItem), ElectricityItem.class);//list深拷贝
                            double q2 = getQ2(newRoad, H, i, i + 1);
                            //如果Q2有效且23小时末高水位H2有效
                            Boolean checkQ2 = isValid(q2, i);
                            Boolean checkH2 = checkHighRiverLevel(i, startRiverLevel);
                            double h2 = getHighRiverLevel(i, startRiverLevel);
                            if (checkQ2 && checkH2) {
                                double hf38;
                                if (HF38Cache.get(i + "," + q2) != null) {
                                    //从缓存里读取hf38的值
                                    hf38 = HF38Cache.get(i + "," + q2);
                                } else {
                                    hf38 = getHF38(i, q2);
                                    HF38Cache.put(i + "," + q2, hf38);
                                }
                                double h = getH(newRoad, H, i, q2, hf38);//扬程h
                                double electricity = getElectricity(i, q2, h);//电费
                                newElectricityItem.getH().add(h);//扬程塞值
                                newElectricityItem.getQ2().add(q2);//Q2塞值
                                newElectricityItem.getElectricityRoad().add(electricity);//电费塞值
                                newElectricityItem.getH2().add(h2);//高位水位塞值
                                double electrictySum = newElectricityItem.getElectricityRoad().stream().mapToDouble(Double::doubleValue).sum();
                                newElectricityItem.setSumElectricity(electrictySum);
                                newElectricityItem.getHF38().add(hf38);
                                response.put(newRoad, newElectricityItem);
                            } else {
                                newElectricityItem.getQ2().add(q2);//Q2塞值
                                newElectricityItem.getElectricityRoad().add(ELECTRICITY_LIMIT);//电费塞值
                                newElectricityItem.getH2().add(h2);//高位水位塞值
                                double electrictySum = newElectricityItem.getElectricityRoad().stream().mapToDouble(Double::doubleValue).sum();
                                newElectricityItem.setSumElectricity(electrictySum);
                                response.put(newRoad, newElectricityItem);
                            }
                        }
                    }


                }
//                System.out.println("第" + i + "小时末电费路线结果为 = " + gson.toJson(response));
                System.out.println("第" + i + "小时末");
            }

            String responseJson = gson.toJson(response);
//            System.out.println("responseJson = " + responseJson);

            //找到最小电费的路线
            double bestFee = Double.valueOf("100000000");
            String bestRoad = "";
            ElectricityItem bestRoadInfo = new ElectricityItem();
            for (Map.Entry<String, ElectricityItem> item : response.entrySet()) {
                if (item.getValue().getSumElectricity() < bestFee) {
                    bestFee = item.getValue().sumElectricity;
                    bestRoad = item.getKey();
                    bestRoadInfo = item.getValue();
                }
            }

            System.out.println("最佳路径bestRoad = " + bestRoad);
            System.out.println("最佳电费bestFee = " + bestFee);
            System.out.println("最佳路线所有信息bestRoadInfo = " + bestRoadInfo);

            System.out.println("程序结束");
        } catch (Exception e) {
            System.out.println("程序报错，请检查!" + e.getMessage());
        }
        System.out.println("=============程序结束===============");
    }

    /**
     * 终端打印出模型的所有变量信息
     *
     * @param minRiverLevel:最低水位
     * @param maxRiverLevel:最高水位
     * @param partition:划分水位数
     * @param H:水位划分,根据输入的最低水位和最高水位和划分水位数得来
     */
    private static void printModelVarValue(double minRiverLevel, double maxRiverLevel, int partition, double[] H) {
        System.out.println("============模型所有变量信息如下:==============");
        System.out.println("二泵站流量的系数A1 = " + A1);
        System.out.println("小时数HOURS = " + HOURS);
        System.out.println("一级系站流量(m3/h)Q1 = " + Q1);
        System.out.println("电费的效率EFFICIENCY = " + EFFICIENCY);
        System.out.println("扬程的标准水位线BASIC_RIVER_LEVEL = " + BASIC_RIVER_LEVEL);
        System.out.println("计算电费的标准系数BASIC_FACTOR = " + BASIC_FACTOR);
        System.out.println("最低水位minRiverLevel = " + minRiverLevel);
        System.out.println("最高水位maxRiverLevel = " + maxRiverLevel);
        System.out.println("划分水位数partition = " + partition);
        System.out.println("水位层数组(H) = " + gson.toJson(H));
        System.out.println("起始水位startRiverLevel = " + startRiverLevel);
        System.out.println("电费上限ELECTRICITY_LIMIT = " + ELECTRICITY_LIMIT);
        System.out.println("=========================================");
    }

    /**
     * 计算电费w
     *
     * @param hour:第i小时
     * @param Q2:二系站流量
     * @param h:扬程
     * @return w:电费
     */
    public static double getElectricity(Integer hour, double Q2, double h) {
        double w;
        w = BASIC_FACTOR * E[hour] * N[hour] * Q2 * h / EFFICIENCY / 3.6;
        return w;
    }


    /**
     * 得到扬程
     *
     * @param road:水位随时间变化的路线
     * @param H:水位数组
     * @param hour:第i个小时
     * @param Q2:二级系站流量
     * @return h:扬程
     */
    public static double getH(String road, double[] H, int hour, double Q2, double hf38) {
        if (hour < road.length()) {
            int index = (int) road.charAt(hour) - (int) 'A';
            double riverLevel = (road.charAt(hour) == '$') ? startRiverLevel : H[index];
            double h = hf38 + BASIC_RIVER_LEVEL - riverLevel + 2;
            return h;
        } else {
            throw new RuntimeException("计算geth报错，水位路线road没有第" + hour + "小时的信息，请检查");
        }
    }


    /**
     * 计算 Q2 二级系站流量
     *
     * @param road：水位随时间变化的路线
     * @param H:水位数组
     * @param startHour:第i小时初
     * @param endHour:第i+1小时初
     * @return
     */
    public static double getQ2(String road, double[] H, int startHour, int endHour) {
        if (startHour < road.length() || endHour < road.length()) {
            int startIndex = (int) road.charAt(startHour) - (int) 'A';
            int endIndex = (int) road.charAt(endHour) - (int) 'A';
            double startRiverLevel = (road.charAt(startHour) == '$') ? HWFormularModel.startRiverLevel : H[startIndex];//第i小时初水位
            double endRiverLevel = (road.charAt(endHour) == '$') ? HWFormularModel.startRiverLevel : H[endIndex];//第i+1小时初水位
            double Q2 = Q1 - (endRiverLevel - startRiverLevel) * A1;
            return Q2;
        } else {
            throw new RuntimeException("计算getQ2报错，水位路线road没有" + "第" + startHour + "小时初:" + "到第" + endHour + "小时初的水位值，请检查");
        }
    }

    /**
     * 根据时间和二泵站流量的系数计算HF38
     *
     * @param hour：小时
     * @param Q2：二泵站流量
     * @return HF38的值
     */
    public static double getHF38(int hour, double Q2) {
//        //将m^3/h -> L/s
//        Q2 = Q2 / 3.6;
//        hour = hour + 1;
//        System.out.println(" getHF38被调用，第" + hour + "d小时");
        String s, ss;
//总管段数NP,总节点数NN,压力平差节点数NHC,Iprt=输出指针
        int NP = 60, NN = 40, NHC = 39, Iprt = 1;
        int i, j, kk1, jb = 0, k0 = 0;
        double baa = 119.3, bpc = 2972, alfa = 1.504, Hend = 147.50, maxDQ = 100.0, ERDQ = 0.001;
        double ac, ad, bc0, bc1, tcc, psign = 0;
        double qx[] = new double[NP], hx[] = new double[NP], hf[] = new double[NP], sp[] = new double[NP], vpb[] = new double[NP];
        double HF[] = new double[NN], DH[] = new double[NN], DQ[] = new double[NN], sumc[] = new double[NHC];
//节点衔接矩阵
        int I0[] = {
                4, 0, 1, 5, 1, 2, 6, 2, 3, 7, 10, 5, 11, 6, 12, 7, 12, 3, 8, 22, 22, 13, 10, 10, 16, 15, 18, 18, 17, 11, 19, 12,
                20, 21, 26, 25, 25, 19, 24, 23, 28, 29, 24, 30, 25, 31, 26, 31, 29, 28, 34, 33, 27, 35, 32, 36, 37, 32, 38, 39
        };
        int J0[] = {
                0, 1, 5, 4, 2, 6, 5, 3, 7, 6, 4, 11, 10, 12, 11, 13, 13, 8, 14, 14, 21, 21, 9, 16, 15, 9, 10, 17, 16, 19, 18, 20, 19,
                26, 25, 20, 24, 24, 23, 18, 23, 28, 29, 29, 30, 30, 31, 32, 32, 34, 33, 27, 28, 34, 35, 35, 36, 37, 24, 3
        };
//节点用水量（m^3/s）
        double qjc[] = {
                0.03721, 0.059, 0.05287, 0.03639, 0.04878, 0.10971, 0.04738, 0.04747, 0.02217, 0.01117, 0.04539, 0.11087,
                0.10404, 0.04567, 0.02095, 0.0103, 0.0134, 0.01206, 0.05896, 0.06207, 0.07476, 0.04936, 0.01815, 0.04897,
                0.03729, 0.04813, 0.03366, 0.0223, 0.04504, 0.05421, 0.04401, 0.0374, 0.0647, 0.0106, 0.01988, 0.04253,
                0.06334, 0.06592, -1.21528, -0.52083
        };
//(节点用水量比例系数)
        double bl[] = {
                0.28, 0.256, 0.232, 0.232, 0.416, 0.704, 0.832, 0.896, 1, 0.928, 0.8, 0.824, 0.824, 0.824, 0.888, 0.896,
                0.904, 0.944, 0.904, 0.8, 0.512, 0.416, 0.408, 0.28
        };
        double Q[] = {1750.00, 1600.00, 1450.00, 1450.00, 2600.00, 4400.00, 5200.00, 5600.00, 6250.00, 5800.00,
                5000.00, 5150.00, 5150.00, 5150.00, 5550.00, 5600.00, 5650.00, 5900.00, 5650.00, 5000.00,
                3200.00, 2600.00, 2550.00, 1750.00};//(管网用水量m3/h)

        double qj[] = new double[NN];
        for (i = 0; i < NN - 2; i++) {
            qj[i] = qjc[i] * bl[hour];
        }
        qj[NN - 1] = (Q2 - Q[hour]) / 3600;
        qj[NN - 2] = -Q2 / 3600;

//节点地面标高（m）
        double Hj[] = {
                123.90, 129.30, 130.50, 145.00, 128.55, 129.40, 129.38, 135.10, 126.75, 119.50, 124.50, 125.40,
                124.98, 123.56, 115.50, 120.50, 125.50, 121.50, 120.30, 120.30, 119.15, 119.93, 121.21, 125.50,
                125.50, 121.66, 117.20, 121.50, 125.50, 125.50, 120.58, 121.83, 121.15, 119.80, 120.50, 119.10,
                120.40, 120.40, 125.50, 154.50
        };
//节点初始压力（m）
        double HH[] = {
                152.31, 181.09, 189.36, 197.88, 158.59, 161.2, 169.91, 182.94, 190.2, 147.5, 160.02, 165.19, 165.5, 167.56,
                178.32, 152.04, 158.94, 162.57, 170.87, 180.14, 171.15, 170.73, 173.7, 179.86, 188.23, 181.88, 175.38,
                167.05, 171.77, 180.27, 172.81, 170.85, 168.24, 164.25, 163.63, 160.06, 158.83, 164.98, 123.2, 199.14
        };
//管段长度（m）
        double lp[] = {
                1440.89, 1213.06, 1409.63, 1573.81, 971.52, 1509.42, 947.71, 1256.71, 1630.21, 936.31, 1338.17,
                1418.18, 1694.56, 1633.60, 940.99, 1788.45, 985.24, 1674.28, 1889.54, 1596.97, 1054.22, 952.47,
                1430.94, 1011.01, 1247.91, 1004.40, 1315.28, 1304.68, 1275.54, 1227.34, 1325.19, 1216.88, 896.25,
                1021.01, 1262.54, 1293.11, 798.96, 1103.56, 1282.53, 1421.42, 1094.55, 1003.63, 1004.65, 828.32,
                1008.64, 568.12, 1521.36, 1012.59, 802.56, 1810.72, 1023.08, 1169.40, 1474.78, 1205.44, 1731.37,
                674.89, 1506.03, 792.74, 320.54, 270.62
        };
//管段摩阻C值
        double HWC[] = {
                130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130,
                130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130,
                130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130, 130
        };
//管段标准管径（m）
        double Dpb[] = {
                0.10, 0.15, 0.15, 0.25, 0.30, 0.20, 0.25, 0.40, 0.35, 0.25, 0.25, 0.25, 0.15, 0.20, 0.30, 0.20,
                0.20, 0.30, 0.25, 0.25, 0.20, 0.20, 0.10, 0.15, 0.15, 0.10, 0.25, 0.20, 0.20, 0.30, 0.25, 0.30,
                0.20, 0.25, 0.30, 0.30, 0.45, 0.45, 0.40, 0.30, 0.25, 0.25, 0.45, 0.20, 0.25, 0.30, 0.10, 0.25,
                0.30, 0.25, 0.20, 0.20, 0.25, 0.25, 0.25, 0.30, 0.15, 0.30, 0.90, 0.60
        };
//-------------

//-------------
/*outfile<<endl;
outfile<<"节点压力平差程序：管道造价C= "<<baa<<"+"<<bpc<<"*pow(d,"<<alfa<<")"<<endl;
outfile<<endl;
//-------------
outfile<<"I0= "; for(i=0;i<NP;i++) outfile<<" "<<I0[i];outfile<<endl;
outfile<<"J0= "; for(i=0;i<NP;i++) outfile<<" "<<J0[i];outfile<<endl;
outfile<<"=======平差计算开始========"<<endl;
outfile<<"标准管径 Dpb[i]= "; for(i=0;i<NP;i++)outfile<<" "<<Dpb[i];
outfile<<endl;
//-----水力平差-----
outfile<<endl; outfile<<"sp[i]= ";*/
        for (i = 0; i < NP; i++) {
            sp[i] = 10.67 * lp[i] / (Math.pow(HWC[i], 1.852) * Math.pow(Dpb[i], 4.87));/*outfile<<" "<<sp[i];*/
        }
/*outfile<<endl;
outfile<<endl; outfile<<"初始压力 HH[i]= ";for(i=0;i<NN;i++) outfile<<" "<<HH[i];outfile<<endl;*/
        for (i = 0; i < NP; i++) {
            qx[i] = 0.0;
        }
        for (i = 0; i < NN; i++) {
            DQ[i] = qj[i];
            DH[i] = 0.0;
        }
//---start kk1 iteration---
        for (kk1 = 0; maxDQ > ERDQ; kk1++) {/*if(Iprt==1)
{outfile<<"kk1= "<<kk1<<endl;
outfile<<"qx[NP]= ";for(i=0;i<NP;i++){s.Format("%6.2lf",qx[i]);outfile<<s;}outfile<<endl;
outfile<<"DQ[NN]= ";for(i=0;i<NN;i++){s.Format("%6.2lf",DQ[i]);outfile<<s;}outfile<<endl;
outfile<<"DH[NN]= ";for(i=0;i<NN;i++){s.Format("%6.2lf",DH[i]);outfile<<s;}outfile<<endl;
outfile<<"HH[NN]= ";for(i=0;i<NN;i++){s.Format("%6.2lf",HH[i]);outfile<<s;}outfile<<endl;
outfile<<"maxDQ= ";outfile<<maxDQ<<endl;}*/
            maxDQ = 0.0;
            for (i = 0; i < NHC; i++) {
                DQ[i] = qj[i];
                sumc[i] = 0.0;  //---DQ赋初值（节点流量）---
//---计算节点压力平差参数---
                for (j = 0; j < NP; j++) {
                    if (I0[j] == i || J0[j] == i) {
                        if (HH[I0[j]] == HH[J0[j]]) {
                            HH[I0[j]] = HH[J0[j]] + 0.00001 * lp[j];
                        }
                        qx[j] = Math.pow(sp[j], -0.54) * Math.pow(Math.abs(HH[I0[j]] - HH[J0[j]]), -0.46) * (HH[I0[j]] - HH[J0[j]]);
                        hx[j] = HH[I0[j]] - HH[J0[j]];
                        hf[j] = sp[j] * Math.pow(Math.abs(qx[j]), 0.852) * qx[j];
                        bc0 = 1.852 * sp[j] * Math.pow(Math.abs(qx[j]), 0.852);
                        bc1 = Math.pow(bc0, -1);
                        sumc[i] = sumc[i] + bc1;
                        if (I0[j] == i) {
                            k0 = J0[j];
                            psign = 1.0;
                        }
                        if (J0[j] == i) {
                            k0 = I0[j];
                            psign = -1.0;
                        }
                        DQ[i] = DQ[i] + psign * qx[j] - 0.5 * bc1 * DH[k0];
                    }
                }
                DH[i] = -DQ[i] / sumc[i];
                if (maxDQ < Math.abs(DQ[i])) {
                    maxDQ = Math.abs(DQ[i]);
                }
            }
            for (i = 0; i < NHC; i++) {
                HH[i] = HH[i] + 0.5 * DH[i];
            }
        }
//--------计算管段流速--------
        for (i = 0; i < NP; i++) {
            ad = 0.7854 * Math.pow(Dpb[i], 2);
            vpb[i] = Math.abs(qx[i]) / ad;
        }
/*outfile<<"======管网平差计算完成：管段计算结果数据======"<<endl;
outfile<<"管号  上节点  下节点  长度  管径  流量  摩阻   流速  损失"<<endl;
outfile<<"  i       I0      J0    lp   Dpb   qx   HWC  vpb   hf"<<endl;
for(i=0;i<NP;i++)
{ss.Format("%3d%7d%7d%6.0lf%6.1lf%6.3lf%6.0lf%6.2lf%6.2lf\n",
		   i,I0[i],J0[i],lp[i],Dpb[i],qx[i],HWC[i],vpb[i],hf[i]);outfile<<ss;cout<<ss;}
outfile<<"==============节点计算结果数据==============="<<endl;
outfile<<"  节点号 节点流量 计算压力 节点标高 自由压力"<<endl;
outfile<<"     i        qj[i]        HH[i]        Hj[i]        HF[i]"<<endl;*/
        for (i = 0; i < NN; i++) {
            HF[i] = HH[i] - Hj[i];
        }

///////////////////////////////////////////改动/////////////////////////////////////
        double HFmin = HF[0];
        double Detail;
        for (i = 1; i < NN; i++) {
            if (i == 39) {
                break;
            } else if (HFmin > HF[i]) {
                HFmin = HF[i];
            }
        }
        Detail = 28 - HFmin;
        for (i = 0; i < NN; i++) {
            HH[i] += Detail;
            HF[i] += Detail;
        }
////////////////////////////////////////////改动///////////////////////////////////


/*for(i=0;i<NN;i++)
{ss.Format("%5d%10.3lf%10.3lf%10.3lf%10.3lf\n",i,qj[i],HH[i],Hj[i],HF[i]);
outfile<<ss;cout<<ss;}
//-----------管网造价----------
tcc=0.0; for(i=0;i<NP;i++) {ac=baa+bpc*pow(Dpb[i],alfa)*lp[i];tcc=tcc+ac;}
outfile<<"==管网标准管径平差结果：管道总造价= "<<tcc<<" 元。=="<<endl;
outfile.close();*/
        return HF[38];
    }


    /**
     * 判断计算出来的Q2是否在限制范围内，是的话，返回true,否则返回false
     *
     * @param Q2:计算得来的Q2
     * @param hour:第i个小时
     * @return
     */
    public static boolean isValid(double Q2, int hour) {
        if (Q2 > 0 && -5512.5 < (Q2 - Q[hour]) && (Q2 - Q[hour]) < 5512.5) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 高位水池水位限制
     *
     * @param hour 第i小时
     * @param h    第i小时清水水位线cop
     * @return 第i小时末高位水池水池是否在[0.5, 4]
     */
    public static boolean checkHighRiverLevel(int hour, double h) {
        double sumQ = Arrays.stream(Arrays.copyOf(Q, hour + 1)).sum();
        double H = H2 + (Q1 * (hour + 1) - (h - startRiverLevel) * A1 - sumQ) / A2;
        if (0.5 <= H && H <= 4) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获得高位水池水位
     *
     * @param hour
     * @param h
     * @return
     */
    public static double getHighRiverLevel(int hour, double h) {
        double sumQ = Arrays.stream(Arrays.copyOf(Q, hour + 1)).sum();
        double H = H2 + (Q1 * (hour + 1) - (h - startRiverLevel) * A1 - sumQ) / A2;
        return H;
    }

}


