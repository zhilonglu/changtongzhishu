package changtongzhishu;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 计算出行规律指标体系中的16个指标，主要包括：(去掉出行速度大于120km/h,以及速度低于20km/h的)
 * 交通量
 * 出行距离
 * 出行时间
 * 延迟情况
 * 可靠性系数
 * 出行时间指数
 *
 */
public class newRuleCalNormal {
	BufferedReader br;
	BufferedWriter bw;
	File rootFile;//存放原始数据文件的文件夹
	File[] files1;//存放原始数据文件列表
	File[] files2;//存放文件目录下的所有文件
	//存储OD间的小客车标准行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer> standardTime_car=new HashMap<String,Integer>();
	//存储OD间的大中型客车标准行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer> standardTime_bus=new HashMap<String,Integer>();
	//存储OD间的1型货车标准行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer> standardTime_oneTruck=new HashMap<String,Integer>();
	//存储OD间的2型货车标准行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer> standardTime_twoTruck=new HashMap<String,Integer>();
	//存储OD间的3型货车标准行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer> standardTime_threeTruck=new HashMap<String,Integer>();
	//存储OD间的4型货车标准行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer> standardTime_fourTruck=new HashMap<String,Integer>();
	//存储OD间的5型货车标准行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer> standardTime_fiveTruck=new HashMap<String,Integer>();

	//存储OD间的小客车实际行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer[]> realTime_car=new HashMap<String,Integer[]>();
	//存储OD间的大中型客车实际行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer[]> realTime_bus=new HashMap<String,Integer[]>();
	//存储OD间的1型货车实际行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer[]> realTime_oneTruck=new HashMap<String,Integer[]>();
	//存储OD间的2型货车实际行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer[]> realTime_twoTruck=new HashMap<String,Integer[]>();
	//存储OD间的3型货车实际行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer[]> realTime_threeTruck=new HashMap<String,Integer[]>();
	//存储OD间的4型货车实际行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer[]> realTime_fourTruck=new HashMap<String,Integer[]>();
	//存储OD间的5型货车实际行程时间，key：OD；value：时间和   统计车辆数
	HashMap<String,Integer[]> realTime_fiveTruck=new HashMap<String,Integer[]>();

	//存储所有OD之间的总延误时长，Integer[]表示不同车型
	HashMap<String,Double[]> delayMap_sum=new HashMap<String,Double[]>();
	//存储所有OD之间的平均延误时长，Integer[]表示不同车型
	HashMap<String,Double[]> delayMap_ave=new HashMap<String,Double[]>();

	//存储所有OD之间的出行系数，Integer[]表示不同车型
	HashMap<String,Double[]> travelCoeMap=new HashMap<String,Double[]>();
	//存储所有OD之间的延迟系数，Integer[]表示不同车型
	HashMap<String,Double[]> delayCoeMap=new HashMap<String,Double[]>();

	//存储所有OD之间的可靠性系数，Integer[]表示不同车型
	HashMap<String,Double[]> rlbCoeMap=new HashMap<String,Double[]>();

	//存储所有OD之间的距离
	HashMap<String,Double> distanceMap=new HashMap<String,Double>();
	HashMap<String,String> str_car=new HashMap<String,String>(),//存储OD间的小客车实际行程时间，每一条数据记录投影OD、行程时间
			str_bus=new HashMap<String,String>(),//存储OD间的大中型客车实际行程时间，每一条数据记录投影OD、行程时间
			str_one=new HashMap<String,String>(),//存储OD间的1型货车实际行程时间，每一条数据记录投影OD、行程时间
			str_two=new HashMap<String,String>(),//存储OD间的2型货车实际行程时间，每一条数据记录投影OD、行程时间
			str_three=new HashMap<String,String>(),//存储OD间的3型货车实际行程时间，每一条数据记录投影OD、行程时间
			str_four=new HashMap<String,String>(),//存储OD间的4型货车实际行程时间，每一条数据记录投影OD、行程时间
			str_five=new HashMap<String,String>();//存储OD间的5型货车实际行程时间，每一条数据记录投影OD、行程时间
	//OD间平均行程时间，key：OD；value：平均行程时间
	HashMap<String,Integer> average=new HashMap<String,Integer>();
	DateFormat 
	sdf_1501=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),//2015年专用
	sdf_1502=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),//2015年专用
	sdf_1503=new SimpleDateFormat("yyyyMMddHHmmss");//2015年专用
	/**
	 * 读入标准行程时间
	 * @param standardFile
	 */
	public newRuleCalNormal(String standardFile){
		File file=new File(standardFile);
		String record;
		String[] st;
		try {
			br=new BufferedReader(new FileReader(file));
			while((record=br.readLine())!=null){
				st=record.split(",");
				standardTime_car.put(st[0], Integer.valueOf(st[1]));
				standardTime_bus.put(st[0], Integer.valueOf(st[2]));
				standardTime_oneTruck.put(st[0], Integer.valueOf(st[3]));
				standardTime_twoTruck.put(st[0], Integer.valueOf(st[4]));
				standardTime_threeTruck.put(st[0], Integer.valueOf(st[5]));
				standardTime_fourTruck.put(st[0], Integer.valueOf(st[6]));
				standardTime_fiveTruck.put(st[0], Integer.valueOf(st[7]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化函数
	 * @param rootFilePath 原始数据文件夹地址
	 */
	public void init(){
		realTime_car.clear();realTime_bus.clear();realTime_oneTruck.clear();
		realTime_twoTruck.clear();realTime_threeTruck.clear();realTime_fourTruck.clear();
		realTime_fiveTruck.clear();
		delayMap_sum.clear();delayMap_ave.clear();
		travelCoeMap.clear();delayCoeMap.clear();rlbCoeMap.clear();
		str_car.clear();str_bus.clear();str_one.clear();str_two.clear();str_three.clear();
		str_four.clear();str_five.clear();
	}
	/**
	 * 读取数据
	 */
	public void readData(String rootFilePath,String yandm){
		rootFile=new File(rootFilePath);
		files1=rootFile.listFiles();
		String record;
		String[] st;
		String outputPath="";
		//读取OD距离csv文件，初始化distancemap
		readODCsv_new();
		for(int i=0;i<files1.length;i++){
			files2 = files1[i].listFiles();
			outputPath = files1[i].getName();
			for(int j=0;j<files2.length;j++){
				try {
					init();
					br=new BufferedReader(new FileReader(files2[j]));
					String date=files2[j].getName().split("\\.")[0];
					System.out.println(date);
					while((record=br.readLine())!=null){
						st=record.split(";");//txt文件时的分隔符是分号，而不是逗号
						String enstation=st[9],exstation=st[0];
						if(enstation.equals("0")||exstation.equals("0"))continue;
						if(!(st[10].substring(0,4).equals("2017")) || !(st[2].substring(0,4).equals("2017")))continue;
						if(st[10].length()<10||st[2].length()<10)continue;
						long entime=this.changeDateToSeconds(st[10]),
								extime=this.changeDateToSeconds(st[2]);
						if(extime<entime||(extime-entime>3600*24))continue;
						if(!distanceMap.containsKey(enstation+"-"+exstation)){
							continue;
						}
						//此处加入条件，剔除速度大于120以及低于20的车辆，不计入统计
						double speed=0;
						speed = 3600*distanceMap.get(enstation+"-"+exstation)/(extime-entime);
						if(speed<=20 || speed>=120)continue;
						//txt文件中有多余的空格，去掉空格
						st[28] = st[28].replaceAll(" ", "");
						st[11] = st[11].replaceAll(" ", "");
						int flag=this.getVehicleType(st[28], st[11]);
						getRealRunTime(enstation,exstation,entime,extime,flag);
						storeInfo(enstation,exstation,extime-entime,flag);
					}
					getDelayTime();
					getCoe();
					Output(outputPath,date,yandm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 读取csv文件并写入map
	 */
	public void readODCsv_new(){
		try {
			BufferedReader newbr = new BufferedReader(new FileReader(new File("R:\\高速出入口数据\\重庆静态基础数据\\收费站数据\\收费站最短距离20160623104640.csv")));
			String newRecord;
			String[] newst;
			try {
				while((newRecord=newbr.readLine())!=null){
					newst = newRecord.split(",");
					if(newst[2].equals("0"))continue;
					distanceMap.put(newst[0]+"-"+newst[1], Double.valueOf(newst[2]));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				newbr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 输出函数，调用output1(),output2(),output3()
	 */
	public void Output(String path,String date,String yandm){
		try{
			String newFilename = "";
			newFilename = "Q:\\重庆畅通指数的计算\\201703\\2017年3月份的系数结果（去掉异常）\\"+yandm+"\\"+path+"\\"+date;
			File rootFile_write=new File(newFilename);
			if(!rootFile_write.exists())rootFile_write.mkdirs();
			outPut1(realTime_car,realTime_bus,realTime_oneTruck,realTime_twoTruck,realTime_threeTruck,
					realTime_fourTruck,realTime_fiveTruck,
					new BufferedWriter(new FileWriter(new File(newFilename+"\\实际行程时间.csv"))));
			//			System.out.println(date+"实际行程时间输出");
			outPut2(delayMap_sum,new BufferedWriter(new FileWriter(new File(newFilename+"\\总延误时间.csv"))));
			//			System.out.println(date+"总延误时间输出");
			outPut2(delayMap_ave,new BufferedWriter(new FileWriter(new File(newFilename+"\\平均延误时间.csv"))));
			//			System.out.println(date+"平均延误时间输出");
			outPut2(travelCoeMap,new BufferedWriter(new FileWriter(new File(newFilename+"\\出行系数.csv"))));
			//			System.out.println(date+"出行系数输出");
			outPut2(delayCoeMap,new BufferedWriter(new FileWriter(new File(newFilename+"\\延误系数.csv"))));
			//			System.out.println(date+"延误系数输出");
			outPut3(rlbCoeMap,new BufferedWriter(new FileWriter(new File(newFilename+"\\可靠性系数.csv"))));
			//			System.out.println(date+"可靠性系数输出");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 保存车辆进出高速的基本信息
	 * @param enstation O
	 * @param exstation D
	 * @param time  行程时间
	 */
	public void storeInfo(String enstation,String exstation,long time,int flag){
		if(flag==0){
			if(str_car.containsKey(enstation+"-"+exstation))
				str_car.put(enstation+"-"+exstation, str_car.get(enstation+"-"+exstation)+","+time);
			else{
				str_car.put(enstation+"-"+exstation,""+time);
			}
		}
		else if(flag==1){
			if(str_bus.containsKey(enstation+"-"+exstation))
				str_bus.put(enstation+"-"+exstation, str_bus.get(enstation+"-"+exstation)+","+time);
			else{
				str_bus.put(enstation+"-"+exstation,""+time);
			}
		}
		else if(flag==2){
			if(str_one.containsKey(enstation+"-"+exstation))
				str_one.put(enstation+"-"+exstation, str_one.get(enstation+"-"+exstation)+","+time);
			else{
				str_one.put(enstation+"-"+exstation,""+time);
			}
		}
		else if(flag==3){
			if(str_two.containsKey(enstation+"-"+exstation))
				str_two.put(enstation+"-"+exstation, str_two.get(enstation+"-"+exstation)+","+time);
			else{
				str_two.put(enstation+"-"+exstation,""+time);
			}
		}
		else if(flag==4){
			if(str_three.containsKey(enstation+"-"+exstation))
				str_three.put(enstation+"-"+exstation, str_three.get(enstation+"-"+exstation)+","+time);
			else{
				str_three.put(enstation+"-"+exstation,""+time);
			}
		}
		else if(flag==5){
			if(str_four.containsKey(enstation+"-"+exstation))
				str_four.put(enstation+"-"+exstation, str_four.get(enstation+"-"+exstation)+","+time);
			else{
				str_four.put(enstation+"-"+exstation,""+time);
			}
		}
		else{
			if(str_five.containsKey(enstation+"-"+exstation))
				str_five.put(enstation+"-"+exstation, str_five.get(enstation+"-"+exstation)+","+time);
			else{
				str_five.put(enstation+"-"+exstation,""+time);
			}
		}
	}

	/**
	 * 返回车辆的车型 
	 * @param khFlag 客货标志位
	 * @param vehClass 车型代码
	 * @return 0（小客车） 1（大中型客车）    2（1型货车）   3（2型货车） 4（3型货车） 5（4型货车） 6（5型货车）  
	 */
	public int getVehicleType(String khFlag,String vehClass){
		if(khFlag.equals("0")&&Integer.valueOf(vehClass)<=1)return 0;
		if(khFlag.equals("0")&&Integer.valueOf(vehClass)>1)return 1;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)<=1)return 2;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==2)return 3;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==3)return 4;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==4)return 5;
		if(!khFlag.equals("0")&&Integer.valueOf(vehClass)==5)return 6;
		return -1;
	}

	/**
	 * 将日期格式转化为时间，以秒为单位
	 * @param date 日期
	 * @return 秒
	 */
	public long changeDateToSeconds(String date){
		try {
			return sdf_1501.parse(date).getTime()/1000;
		} catch (ParseException e) {
			try {
				return sdf_1502.parse(date).getTime()/1000;
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				try {
					return sdf_1503.parse(date).getTime()/1000;
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					return -1;
				}
			}
		}
	}

	/**
	 * 将不同车型的标准行程时间填写到对应的map中
	 * @param map
	 * @param entime
	 * @param extime
	 * @param enstation
	 * @param exstation
	 */
	public void addIntoMap(HashMap<String,Integer[]> map,long entime,long extime,String enstation,String exstation){
		if(map.containsKey(enstation+"-"+exstation)){
			map.get(enstation+"-"+exstation)[0]=
					(int) (map.get(enstation+"-"+exstation)[0]+(extime-entime));
			map.get(enstation+"-"+exstation)[1]++;
		}
		else{
			Integer[] values={(int) (extime-entime),1};
			map.put(enstation+"-"+exstation,values);
		}
	}



	/**
	 * 获取所有OD间不同车型的实际行程时间
	 * 不同车型：小客车、大中型客车、1-5型货车
	 */
	public void getRealRunTime(String enstation,String exstation,long entime,long extime,
			int flag){
		if(flag==0){     //小客车
			addIntoMap(realTime_car,entime,extime,enstation,exstation);
		}
		else if(flag==1){//大中型客车
			addIntoMap(realTime_bus,entime,extime,enstation,exstation);
		}
		else if(flag==2){//1型货车
			addIntoMap(realTime_oneTruck,entime,extime,enstation,exstation);
		}
		else if(flag==3){//2型货车
			addIntoMap(realTime_twoTruck,entime,extime,enstation,exstation);
		}
		else if(flag==4){//3型货车
			addIntoMap(realTime_threeTruck,entime,extime,enstation,exstation);
		}
		else if(flag==5){//4型货车
			addIntoMap(realTime_fourTruck,entime,extime,enstation,exstation);
		}
		else{            //5型货车
			addIntoMap(realTime_fiveTruck,entime,extime,enstation,exstation);
		}
	}

	/**
	 * 获取指定OD的不同车型的总延误时间
	 * @param key OD
	 * @param realMap 时间行程时间map
	 * @param standardMap 标准行程时间map
	 * @return
	 */
	public double getDelay(String key,HashMap<String,Integer[]> realMap,HashMap<String,Integer> standardMap){
		if(standardMap.containsKey(key)&&standardMap.get(key)!=-1){
			Integer[] values_real=realMap.get(key);
			return values_real[0]-values_real[1]*(standardMap.get(key));
		}
		else{
			return -1;
		}
	}

	/**
	 * 获取所有OD对之间的总延误时长和平均延误时长
	 */
	public void getDelayTime(){
		Iterator it=realTime_car.keySet().iterator();
		int[] carNum={0,0,0,0,0,0,0};
		while(it.hasNext()){
			String key=String.valueOf(it.next());
			Double[] values_sum={0.0,0.0,0.0,0.0,0.0,0.0,0.0},
					values_ave={0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			values_sum[0]=getDelay(key,realTime_car,standardTime_car);
			if(values_sum[0]!=-1)
			{
				values_ave[0]=values_sum[0]/realTime_car.get(key)[1];
				if(values_sum[0]>0)carNum[0]+=realTime_car.get(key)[1];
			}
			else values_ave[0]=-1.0;


			if(realTime_bus.containsKey(key)){
				values_sum[1]=getDelay(key,realTime_bus,standardTime_bus);
				if(values_sum[1]!=-1)
				{
					values_ave[1]=values_sum[1]/realTime_bus.get(key)[1];
					if(values_sum[1]>0)carNum[1]+=realTime_bus.get(key)[1];
				}
				else values_ave[1]=-1.0;
			}

			if(realTime_oneTruck.containsKey(key)){
				values_sum[2]=getDelay(key,realTime_oneTruck,standardTime_oneTruck);
				if(values_sum[2]!=-1)
				{
					values_ave[2]=values_sum[2]/realTime_oneTruck.get(key)[1];
					if(values_sum[2]>0)carNum[2]+=realTime_oneTruck.get(key)[1];
				}
				else values_ave[2]=-1.0;
			}

			if(realTime_twoTruck.containsKey(key)){
				values_sum[3]=getDelay(key,realTime_twoTruck,standardTime_twoTruck);
				if(values_sum[3]!=-1)
				{
					values_ave[3]=values_sum[3]/realTime_twoTruck.get(key)[1];
					if(values_sum[3]>0)carNum[3]+=realTime_twoTruck.get(key)[1];
				}
				else values_ave[3]=-1.0;
			}

			if(realTime_threeTruck.containsKey(key)){
				values_sum[4]=getDelay(key,realTime_threeTruck,standardTime_threeTruck);
				if(values_sum[4]!=-1)
				{
					values_ave[4]=values_sum[4]/realTime_threeTruck.get(key)[1];
					if(values_sum[4]>0)carNum[4]+=realTime_threeTruck.get(key)[1];
				}
				else values_ave[4]=-1.0;
			}

			if(realTime_fourTruck.containsKey(key)){
				values_sum[5]=getDelay(key,realTime_fourTruck,standardTime_fourTruck);
				if(values_sum[5]!=-1)
				{
					values_ave[5]=values_sum[5]/realTime_fourTruck.get(key)[1];
					if(values_sum[5]>0)carNum[5]+=realTime_fourTruck.get(key)[1];
				}
				else values_ave[5]=-1.0;
			}

			if(realTime_fiveTruck.containsKey(key)){
				values_sum[6]=getDelay(key,realTime_fiveTruck,standardTime_fiveTruck);
				if(values_sum[6]!=-1)
				{
					values_ave[6]=values_sum[6]/realTime_fiveTruck.get(key)[1];
					if(values_sum[6]>0)carNum[6]+=realTime_fiveTruck.get(key)[1];
				}
				else values_ave[6]=-1.0;
			}

			delayMap_sum.put(key,values_sum);
			delayMap_ave.put(key,values_ave);

		}
	}

	/**
	 * 获取指定OD指定车型的可靠性系数
	 * @param map 车型
	 * @param ave 平均行程时间
	 * @param key OD
	 * @return
	 */
	public double getPercentage(HashMap<String,String> map,int ave,String key){
		double lowTen=0,sum=0,limit=ave*1.1;
		String[] values=map.get(key).split(",");
		sum=values.length;
		for(int i=0;i<sum;i++){
			if(Integer.valueOf(values[i])<=limit){
				lowTen++;
			}
		}
		return lowTen/sum;
	}

	/**
	 * 获取车辆的出行系数、延迟系数和可靠性系数
	 */
	public void getCoe(){
		Iterator it=realTime_car.keySet().iterator();
		while(it.hasNext()){
			String key=String.valueOf(it.next());
			if(!distanceMap.containsKey(key))continue;
			Double[] values_travel={0.0,0.0,0.0,0.0,0.0,0.0,0.0},
					values_delay={0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			Double[]  values_rlb={0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			values_travel[0]=(realTime_car.get(key)[0]/distanceMap.get(key)/realTime_car.get(key)[1]);
			if(standardTime_car.containsKey(key)){
				values_delay[0]=(values_travel[0]-standardTime_car.get(key)/distanceMap.get(key));
				values_rlb[0]=getPercentage(str_car,standardTime_car.get(key),key);
			}
			else values_delay[0]=-1.0;
			if(realTime_bus.containsKey(key)){
				values_travel[1]=(realTime_bus.get(key)[0]/distanceMap.get(key)/realTime_bus.get(key)[1]);
				if(standardTime_bus.containsKey(key)){
					values_rlb[1]=getPercentage(str_bus,standardTime_bus.get(key),key);
					values_delay[1]=(values_travel[1]-standardTime_bus.get(key)/distanceMap.get(key));
				}
				else values_delay[1]=-1.0;
			}
			if(realTime_oneTruck.containsKey(key)){
				values_travel[2]=(realTime_oneTruck.get(key)[0]/distanceMap.get(key)/realTime_oneTruck.get(key)[1]);
				if(standardTime_oneTruck.containsKey(key)){
					values_delay[2]=(values_travel[2]-standardTime_oneTruck.get(key)/distanceMap.get(key));
					values_rlb[2]=getPercentage(str_one,standardTime_oneTruck.get(key),key);
				}
				else values_delay[2]=-1.0;
			}
			if(realTime_twoTruck.containsKey(key)){
				values_travel[3]=(realTime_twoTruck.get(key)[0]/distanceMap.get(key)/realTime_twoTruck.get(key)[1]);
				if(standardTime_twoTruck.containsKey(key)){
					values_delay[3]=(values_travel[3]-standardTime_twoTruck.get(key)/distanceMap.get(key));
					values_rlb[3]=getPercentage(str_two,standardTime_twoTruck.get(key),key);
				}
				else values_delay[3]=-1.0;
			}
			if(realTime_threeTruck.containsKey(key)){
				values_travel[4]=(realTime_threeTruck.get(key)[0]/distanceMap.get(key)/realTime_threeTruck.get(key)[1]);
				if(standardTime_threeTruck.containsKey(key)){
					values_delay[4]= (values_travel[4]-standardTime_threeTruck.get(key)/distanceMap.get(key));
					values_rlb[4]=getPercentage(str_three,standardTime_threeTruck.get(key),key);
				}
				else values_delay[4]=-1.0;
			}
			if(realTime_fourTruck.containsKey(key)){
				values_travel[5]=(realTime_fourTruck.get(key)[0]/distanceMap.get(key)/realTime_fourTruck.get(key)[1]);
				if(standardTime_fourTruck.containsKey(key)){
					values_delay[5]=(values_travel[5]-standardTime_fourTruck.get(key)/distanceMap.get(key));
					values_rlb[5]=getPercentage(str_four,standardTime_fourTruck.get(key),key);
				}
				else values_delay[5]=-1.0;
			}
			if(realTime_fiveTruck.containsKey(key)){
				values_travel[6]=(realTime_fiveTruck.get(key)[0]/distanceMap.get(key)/realTime_fiveTruck.get(key)[1]);
				if(standardTime_fiveTruck.containsKey(key)){
					values_delay[6]= (values_travel[6]-standardTime_fiveTruck.get(key)/distanceMap.get(key));
					values_rlb[6]=getPercentage(str_five,standardTime_fiveTruck.get(key),key);
				}
				else values_delay[6]=-1.0;
			}
			travelCoeMap.put(key,values_travel);
			delayCoeMap.put(key,values_delay);
			rlbCoeMap.put(key, values_rlb);
		}
	}

	/**
	 * 输出标准和实际的行程时间
	 */
	public void outPut1(HashMap<String,Integer[]> map1,HashMap<String,Integer[]> map2,HashMap<String,Integer[]> map3,
			HashMap<String,Integer[]> map4,HashMap<String,Integer[]> map5,HashMap<String,Integer[]> map6,
			HashMap<String,Integer[]> map7,BufferedWriter bw){
		Iterator it=map1.keySet().iterator();
		while(it.hasNext()){
			String key=String.valueOf(it.next());
			String[] OD=key.split("-");
			if(OD[0].equals(OD[1]))continue;
			//				String values=map1.get(key)[0]/map1.get(key)[1]+",";
			String values=map1.get(key)[0]+","+map1.get(key)[1]+",";
			if(map2.containsKey(key)){
				//					values+=map2.get(key)[0]/map2.get(key)[1]+",";
				values+=map2.get(key)[0]+","+map2.get(key)[1]+",";
			}
			else{
				values+="-1,-1,";
			}
			if(map3.containsKey(key)){
				//					values+=map3.get(key)[0]/map3.get(key)[1]+",";
				values+=map3.get(key)[0]+","+map3.get(key)[1]+",";
			}
			else{
				values+="-1,-1,";
			}
			if(map4.containsKey(key)){
				//					values+=map4.get(key)[0]/map4.get(key)[1]+",";
				values+=map4.get(key)[0]+","+map4.get(key)[1]+",";
			}
			else{
				values+="-1,-1,";
			}
			if(map5.containsKey(key)){
				//					values+=map5.get(key)[0]/map5.get(key)[1]+",";
				values+=map5.get(key)[0]+","+map5.get(key)[1]+",";
			}
			else{
				values+="-1,-1,";
			}
			if(map6.containsKey(key)){
				//					values+=map6.get(key)[0]/map6.get(key)[1]+",";
				values+=map6.get(key)[0]+","+map6.get(key)[1]+",";
			}
			else{
				values+="-1,-1,";
			}
			if(map7.containsKey(key)){
				//					values+=map7.get(key)[0]/map7.get(key)[1];
				values+=map7.get(key)[0]+","+map7.get(key)[1];
			}
			else{
				values+="-1,-1";
			}try {
				bw.write(key+","+distanceMap.get(key)+","+values+"\n");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 输出系数
	 */
	public void outPut2(HashMap<String,Double[]> map,BufferedWriter bw){
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
			String key=String.valueOf(it.next());
			String[] OD=key.split("-");
			if(OD[0].equals(OD[1]))continue;
			Double[] values=map.get(key);
			String rec="";
			for(int i=0;i<values.length;i++){
				rec+=values[i]+",";
			}
			try {
				bw.write(key+","+distanceMap.get(key)+","+rec+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出系数
	 */
	public void outPut3(HashMap<String,Double[]> map,BufferedWriter bw){
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
			String key=String.valueOf(it.next());
			String[] OD=key.split("-");
			if(OD[0].equals(OD[1]))continue;
			Double[] values=map.get(key);
			String rec="";
			for(int i=0;i<values.length;i++){
				rec+=values[i]+",";
			}
			try {
				bw.write(key+","+distanceMap.get(key)+","+rec+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {

			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		newRuleCalNormal rc=new newRuleCalNormal("R:\\高速出入口数据\\重庆地图\\标准行程时间（90per_h）.csv");
		rc.readData("R:\\高速出入口数据\\2017年小时数据\\201703","201703");
	}
}
