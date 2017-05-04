package changtongzhishu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 批量处理一个文件夹下的所有文件
 * 按照小时计算出行系数（时间以进入高速收费站为准）
 * 将数据划分成按天数据
 * @author NLSDE
 *
 */
public class splitByday{
	Map<String,FileWriter> map;
	PrintWriter pw;
	public splitByday(){//主要负责生成目录文件
		try{
			map= new HashMap<String,FileWriter>();
			for(int i=3;i<4;i++){
				File f=new File("R:\\高速出入口数据\\2017年按天数据\\20170"+i+"\\");
				if(!f.exists())
					f.mkdirs();
				for(int j=1;j<=9;j++){
					String filepath="R:\\高速出入口数据\\2017年按天数据\\20170"+i+"\\20170"+i+"0"+j+".csv";
					File file=new File(filepath);
					if(!file.exists())
						file.createNewFile();
					map.put(filepath, new FileWriter(filepath));
				}
				for(int j=10;j<=31;j++){
					String filepath="R:\\高速出入口数据\\2017年按天数据\\20170"+i+"\\20170"+i+j+".csv";
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
	//格式化日期，将长度固定为两位数
	public String Format(String mTime){
		String time="";
		if(mTime.length()==2)
			return mTime;
		time = "0"+mTime;
		return time;
	}
	//将输入时间转换为类似于20170308101030的时间格式 2017/11/01 00:00:00
	public String FormatTime(String inputTime){
		String outTime="";
		outTime = inputTime.substring(0,4)+inputTime.substring(5,7)+inputTime.substring(8,10);
		outTime += inputTime.substring(11,13)+inputTime.substring(14,16)+inputTime.substring(17,19);
		return outTime;
	}
	//将格式不标准的格式转化为201703101230的时间格式
	public String FormatYMDHMMTime(String inputTime){
		String output="";
		String y="",m="",d="",Hour="",Minute="",Second="";
		String[] ymdHMS = inputTime.split(" ");
		String ymd = ymdHMS[0];
		String HMS = ymdHMS[1];
		if(ymd.contains("-")){//包含-的时间格式
			y = ymd.split("-")[0];
			m = ymd.split("-")[1];
			m = Format(m);
			d = ymd.split("-")[2];
			d = Format(d);
			Hour = HMS.split(":")[0];
			Hour = Format(Hour);
			Minute = HMS.split(":")[1];
			Minute = Format(Minute);
			Second = HMS.split(":")[2];
			Second = Format(Second);
		}
		else if(ymd.contains("/")){//包含-的时间格式
			y = ymd.split("/")[0];
			m = ymd.split("/")[1];
			m = Format(m);
			d = ymd.split("/")[2];
			d = Format(d);
			Hour = HMS.split(":")[0];
			Hour = Format(Hour);
			Minute = HMS.split(":")[1];
			Minute = Format(Minute);
			Second = HMS.split(":")[2];
			Second = Format(Second);
		}
		output = y+m+d+Hour+Minute+Second;
		return output;
	}
	//排除掉类似于1970的情况
	public boolean IsLegal(String input){
		if(input.contains("-") || input.contains("/")){
			String[] ymdHMS = input.split(" ");
			if(ymdHMS.length==1)
				return false;
		}
		return true;
	}
	public void splitByHour(){
		String outputpath="R:\\高速出入口数据\\2017年按天数据\\201703\\";
		String path="";
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(new File("G:\\2017年3月高速出入口\\3月出口.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String record;
		String[] st;
		try {
			record=br.readLine();
			while((record=br.readLine())!=null){
				st = record.split(";");
				if(st[10].length()<10||st[2].length()<10)continue;
				//如果读取的文件是txt格式，由于时间格式不正确，所以需要将时间的格式转换为年月日时分秒的形式
				st[10] = FormatTime(st[10]);
				st[2] = FormatTime(st[2]);
				if(IsLegal(st[10])==false || IsLegal(st[2])==false){
					continue;
				}
				if(st[10].contains("-") || st[10].contains("/")){
					st[10] = FormatYMDHMMTime(st[10]);
				}
				if(st[2].contains("-") || st[2].contains("/")){
					st[2] = FormatYMDHMMTime(st[2]);
				}
				path = outputpath+st[10].substring(0,8)+".csv";
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
			splitByday obj = new splitByday();
			obj.splitByHour();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
