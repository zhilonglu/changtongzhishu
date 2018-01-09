package changtongzhishu;
/*
 * 将txt数据转换成日存储的csv文件
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 批量处理一个文件夹下的所有文件
 * 按照小时计算出行系数（时间以进入高速收费站为准）
 * 第一步将一天的数据转换成按小时的数据
 */
public class splitByhourTxt{
	Map<String,FileWriter> map;
	PrintWriter pw;
	String[] allCarNumber = new String[1000];
	DateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//根据输入的日期，输出该月份的天数
	public int returnMonthDay(String time){
		int day = 30;
		int d_year = Integer.valueOf(time.substring(0,4));
		int d_mon = Integer.valueOf(time.substring(4,6));
		switch(d_mon){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:day = 31;break;
		case 2:
			if((d_year % 4==0 && d_year % 100 !=0) || (d_year % 400==0))
				day = 29;
			else
				day = 28;
			break;
		case 4:
		case 6:
		case 9:
		case 11:day=30;break;
		}
		return day;
	}
	public splitByhourTxt(String time){//主要负责生成目录文件
		int day = returnMonthDay(time);
		File f;
		String rootpath="";
		String ymd="";
		try{
			map= new HashMap<String,FileWriter>();
			for(int i=1;i<=day;i++){
				if(i<10){
					rootpath = "J:\\高速出入口数据\\"+time.substring(0,4)+"年小时数据\\"+time+"\\"+time+"0"+i;
					ymd = "2017120"+i;
				}
				else{
					rootpath = "J:\\高速出入口数据\\"+time.substring(0,4)+"年小时数据\\"+time+"\\"+time+i;
					ymd = "201712"+i;
				}
				f=new File(rootpath);
				if(!f.exists())
					f.mkdirs();
				for(int j=0;j<=9;j++){
					String filepath=rootpath+"\\"+ymd+"0"+j+".csv";
					File file=new File(filepath);
					if(!file.exists())
						file.createNewFile();
					map.put(filepath, new FileWriter(filepath));
				}
				for(int j=10;j<24;j++){
					String filepath=rootpath+"\\"+ymd+j+".csv";
					File file=new File(filepath);
					if(!file.exists())
						file.createNewFile();
					map.put(filepath, new FileWriter(filepath));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//将输入时间转换为类似于20150608101030的时间格式 2015/11/01 00:00:00
	public String FormatTime1(String inputTime){
		String outTime="";
		outTime = inputTime.substring(0,4)+inputTime.substring(5,7)+inputTime.substring(8,10);
		outTime += inputTime.substring(11,13)+inputTime.substring(14,16)+inputTime.substring(17,19);
		return outTime;
	}
	//将输入时间格式为XXXX/X/X 00:00:00转换为20100101000000的形式
	public String FormatTime2(String inputTime){
		String outputTime="";
		String[] st = inputTime.split(" ");
		String[] st1 = st[0].split("/");
		String[] st2 = st[1].split(":");
		outputTime += st1[0];
		if(st1[1].length()==1){
			outputTime += "0"+st1[1];
		}
		else
		{
			outputTime += st1[1];
		}
		if(st1[2].length()==1){
			outputTime += "0"+st1[2];
		}
		else
		{
			outputTime += st1[2];
		}
		if(st2[0].length()==1){
			outputTime += "0"+st2[0];
		}
		else
		{
			outputTime += st2[0];
		}
		return outputTime;
	}
	public void splitByHour(String rootpath,String YMtime){
		String outputpath="J:\\高速出入口数据\\"+YMtime.substring(0,4)+"年小时数据\\"+YMtime+"\\";
		String path="";
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(new File(rootpath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String record;
		String[] content;
		try {
			record = br.readLine();//先去掉第一行数据
			while((record=br.readLine())!=null){
				content = record.split(";");
				if(content[10].length()<16)//时间格式不正确，直接剔除数据
					continue;
				String time = FormatTime2(content[10]);//入口时间
				path = outputpath+time.substring(0, 8)+"\\"+time.substring(0, 10)+".csv";
				if(map.get(path)!=null){
					pw = new PrintWriter(map.get(path));
					pw.println(record);
					pw.flush();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		try{
			splitByhourTxt obj = new splitByhourTxt("201712");
			obj.splitByHour("C:\\Users\\xibol\\Desktop\\2017年12月高速出口刷卡数据.txt","201712");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}