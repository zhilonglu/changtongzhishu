package changtongzhishu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

//计算OD上的出行量
public class chuxingliang {
	//存储所有OD之间的距离
	HashMap<String,Double> distanceMap=new HashMap<String,Double>();
	DateFormat 
	sdf_1501=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),//2015年专用
	sdf_1502=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),//2015年专用
	sdf_1503=new SimpleDateFormat("yyyyMMddHHmmss");//2015年专用
	/**
	 * 获取车辆的类型，分为七个车型，
	 * 0：小客车    1：小型车    2:1型货车  3：2型货车  4:3型货车  5:4型货车  6:5型货车
	 * @param Flag
	 * @param vType
	 * @return
	 */
	public int getVehicleType(String Flag,String vType){
		if(Flag.equals("0")){//表明是客车，然后具体区分小客和大中客
			if(Integer.valueOf(vType)<=1){
				return 0;
			}
			else{
				return 1;
			}
		}
		if(Flag.equals("1")){//表明是货车，然后具体分为5类车型
			if(vType.equals("1") || vType.equals("0")){
				return 2;
			}
			else if(vType.equals("2")){
				return 3;
			}
			else if(vType.equals("3")){
				return 4;
			}
			else if(vType.equals("4")){
				return 5;
			}
			else if(vType.equals("5")){
				return 6;
			}
		}
		return -1;
	}
	/**
	 * 读取csv文件并写入map
	 */
	public void readODCsv_new(){
		try {
			BufferedReader newbr = new BufferedReader(new FileReader(new File("J:\\高速出入口数据\\重庆静态基础数据\\收费站数据\\收费站最短距离20160623104640.csv")));
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
	public void readData(String rootFilePath,String date,String cTime){
		readODCsv_new();
		File rootFile=new File(rootFilePath);
		File[] files1=rootFile.listFiles();
		File[] files2;
		String record;
		String[] st;
		BufferedReader br = null;
		BufferedWriter bw = null;
		for(int i=0;i<files1.length;i++){
			files2 = files1[i].listFiles();
			String Filepath = "H:\\重庆畅通指数的计算\\"+date+"\\"+cTime+"份小型车出行量（去掉异常）\\"+date+"小时出行量\\"+files1[i].getName()+"\\";
			File f = new File(Filepath);
			if(!f.exists()){
				f.mkdirs();
			}
			for(int j=0;j<files2.length;j++){
				HashMap<String,Integer> ODcarnum = new HashMap<String,Integer>();
				int type =-1;
				String str="";
				try {
					br=new BufferedReader(new FileReader(files2[j]));
					String Filename = Filepath+files2[j].getName().split("\\.")[0]+"出行量.csv";
					try {
						bw = new BufferedWriter(new FileWriter(new File(Filename)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while((record=br.readLine())!=null){
						st=record.split(";");//txt文件时的分隔符是分号，而不是逗号
						String enstation=st[9],exstation=st[0];
						if(enstation.equals("0")||exstation.equals("0"))continue;
						if(!(st[10].substring(0,4).equals(date.substring(0,4))) || !(st[2].substring(0,4).equals(date.substring(0,4))))continue;
						if(st[10].length()<10||st[2].length()<10)continue;
						long entime=this.changeDateToSeconds(st[10]),
								extime=this.changeDateToSeconds(st[2]);
						if(extime<entime||(extime-entime>3600*24))continue;
						String ODname = st[9]+"-"+st[0];
						if(!distanceMap.containsKey(ODname)){
							continue;
						}
						//此处加入条件，剔除速度大于120以及低于20的车辆，不计入统计
						double speed=0;
						speed = 3600*distanceMap.get(ODname)/(extime-entime);
						if(speed<=20 || speed>=120)continue;
						//txt文件中有多余的空格，去掉空格
						st[28] = st[28].replaceAll(" ", "");
						st[11] = st[11].replaceAll(" ", "");
						type = getVehicleType(st[28],st[11]);
						if(type!=0)//此处改变，可以得到关于不同车型的出行量计算情况
							continue;
						if(!ODcarnum.containsKey(ODname)){
							ODcarnum.put(ODname, 1);
							continue;
						}
						for(String ODName:ODcarnum.keySet()){
							if(ODName.equals(ODname)){
								int tempNUm=ODcarnum.get(ODname);
								tempNUm = tempNUm + 1 ;//计算出来出行系数
								ODcarnum.put(ODname, tempNUm);
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Iterator itIndex=ODcarnum.entrySet().iterator();
				while(itIndex.hasNext()){
					Map.Entry entryIndex=(Entry)itIndex.next();
					str +=entryIndex.getKey()+","+entryIndex.getValue()+"\n";								
				}
				try {
					bw.write(str);
					bw.flush();
					bw.close();
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	} 
	public static void main(String[] args){
		chuxingliang obj = new chuxingliang();
		obj.readData("J:\\高速出入口数据\\2017年小时数据\\201712","201712","2017年12月");
	}
}
