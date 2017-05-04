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
	public splitByhourTxt(){//主要负责生成目录文件
//		ReadAllCar();
		File f;
		String rootpath="";
		String ymd="";
		try{
			map= new HashMap<String,FileWriter>();
			for(int i=1;i<=31;i++){
				if(i<10){
					rootpath = "R:\\高速出入口数据\\2017年小时数据\\201703\\"+"2017030"+i;
					ymd = "2017030"+i;
				}
				else{
					rootpath = "R:\\高速出入口数据\\2017年小时数据\\201703\\"+"201703"+i;
					ymd = "201703"+i;
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
	public void splitByHour(String rootpath){
		String outputpath="R:\\高速出入口数据\\2017年小时数据\\201703\\";
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
				String time = FormatTime2(content[10]);
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
			splitByhourTxt obj = new splitByhourTxt();
			obj.splitByHour("G:\\2017年3月高速出入口\\3月出口.txt");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}