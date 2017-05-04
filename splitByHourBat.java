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
 * 第一步将一天的数据转换成按小时的数据
 * @author NLSDE
 *
 */
public class splitByHourBat{
	Map<String,FileWriter> map;
	PrintWriter pw;
	public splitByHourBat(String rootpath){//主要负责生成目录文件
		File rootfile;
		File[] files1;
		File[] files2;
		try{
			map= new HashMap<String,FileWriter>();
			rootfile = new File(rootpath);
			files1=rootfile.listFiles();
			for(int i=0;i<files1.length;i++){//遍历整个文件夹，批量操作整个文件夹
				files2 = files1[i].listFiles();
				String ym=files1[i].getName();
				for(int k=0;k<files2.length;k++){
					String ymd = files2[k].getName().substring(0,8);
					File f=new File("H:\\高速出入口数据\\2016年小时数据\\"+ym+"\\"+ymd);
					if(!f.exists())
						f.mkdirs();
					for(int j=0;j<=9;j++){
						String filepath="H:\\高速出入口数据\\2016年小时数据\\"+ym+"\\"+ymd+"\\"+ymd+"0"+j+".csv";
						File file=new File(filepath);
						if(!file.exists())
							file.createNewFile();
						map.put(filepath, new FileWriter(filepath));
					}
					for(int j=10;j<24;j++){
						String filepath="H:\\高速出入口数据\\2016年小时数据\\"+ym+"\\"+ymd+"\\"+ymd+j+".csv";
						File file=new File(filepath);
						if(!file.exists())
							file.createNewFile();
						map.put(filepath, new FileWriter(filepath));
					}
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
	public void splitByHour(String rootpath){
		File rootfile;
		File[] files1;
		File[] files2;
		rootfile = new File(rootpath);
		files1 = rootfile.listFiles();
		for(int i=0;i<files1.length;i++){
			String outputpath="R:\\高速出入口数据\\2016年小时数据\\";
			String path="";
			files2 = files1[i].listFiles();
			for(int j=0;j<files2.length;j++){
				String filename=files2[j].getName();
				BufferedReader br=null;
				try {
					br = new BufferedReader(new FileReader(new File(rootpath+"\\"+files1[i].getName()+"\\"+filename)));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String record;
				String[] content;
				try {
					while((record=br.readLine())!=null){
						content = record.split(",");
						if(content[10].contains("-") || content[10].contains("/")){
							String[] ymdHMS = content[10].split(" ");
							if(ymdHMS.length==1)
								continue;
							String ymd = ymdHMS[0];
							String HMS = ymdHMS[1];
							String y="",m="",d="",Hour="";
							if(ymd.contains("-")){//包含-的时间格式
								y = ymd.split("-")[0];
								m = ymd.split("-")[1];
								m = Format(m);
								d = ymd.split("-")[2];
								d = Format(d);
								Hour = HMS.split(":")[0];
								Hour = Format(Hour);
							}
							else if(ymd.contains("/")){//包含-的时间格式
								y = ymd.split("/")[0];
								m = ymd.split("/")[1];
								m = Format(m);
								d = ymd.split("/")[2];
								d = Format(d);
								Hour = HMS.split(":")[0];
								Hour = Format(Hour);
							}
							path = outputpath+y+m+"\\"+y+m+d+"\\"+y+m+d+Hour+".csv";
						}
						else{
							path = outputpath+content[10].substring(0,6)+"\\"+content[10].substring(0,8)+"\\"+content[10].substring(0,10)+".csv";
						}
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
		}
	}
	public static void main(String[] args){
		try{
			splitByHourBat obj = new splitByHourBat("R:\\高速出入口数据\\2016年数据");
			obj.splitByHour("R:\\高速出入口数据\\2016年数据");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
