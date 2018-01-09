package changtongzhishu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * 将按天计算的系数转换成按天计算，直接将有效的小时求和平均
 */
import java.text.DecimalFormat;

public class formatKxiantuByday {
	File file;
	File[] files1;
	File[] files2;
	File[] files3;
	DecimalFormat df=new DecimalFormat("##0.00");
	//找到有效的小时数据结果
	public boolean FindValidHour(String inputHour){
		if(inputHour.substring(8,10).equals("00") || inputHour.substring(8,10).equals("01")|| inputHour.substring(8,10).equals("02")
				|| inputHour.substring(8,10).equals("03") || inputHour.substring(8,10).equals("04")){
			return false;
		}
		else if(inputHour.substring(8,10).equals("21") || inputHour.substring(8,10).equals("22")|| inputHour.substring(8,10).equals("23")){
			return false;
		}
		return true;
	}
	//出行量格式化输出
	public void readChuxingliang(String path,String time,String cTime){
		String output2="";
		String output3="";
		BufferedReader br = null;
		file=new File(path);
		files1= file.listFiles();
		File f = new File("H:\\重庆畅通指数的计算\\"+time+"\\折算前\\按天计算\\");
		if(!f.exists()){
			f.mkdirs();
		}
		for(int i=0;i<files1.length;i++)
		{
			files2 = files1[i].listFiles();
			for(int j=0;j<files2.length;j++)
			{
				files3 = files2[j].listFiles();
				double sumCar=0;
				for(int k=0;k<files3.length;k++){
					if(FindValidHour(files3[k].getName()))//找到系数文件
					{
						try {
							br = new BufferedReader(new FileReader(files3[k]));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String record;
						String[] content;
						try {
							while((record = br.readLine())!=null){
								content = record.split(",");
								if(content[1].equals("NaN"))content[1]="0";
								double tempvalue = Double.valueOf(content[1]);
								sumCar += tempvalue;
							}
							
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				output2 += files2[j].getName().substring(0,8)+","+sumCar+"\n";
				output3 += sumCar+",";
			}
		}
		try {
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("H:\\重庆畅通指数的计算\\"+time+"\\折算前\\按天计算\\"+cTime+"小型车出行量统计输出结果_按天计算.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File("H:\\重庆畅通指数的计算\\"+time+"\\折算前\\按天计算\\"+cTime+"小型车出行量统计输出结果_按天计算(Echarts).txt")));
			bw3.write(output3);
			bw3.flush();
			bw3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readData(String path,String time,String cTime,String comStr){
		String output2="";
		String output3="";
		BufferedReader br = null;
		file=new File(path);
		files1= file.listFiles();
		String outputPath = "H:\\重庆畅通指数的计算\\"+time+"\\折算前\\按天计算\\";//输出文件的路径位置
		File f = new File(outputPath);
		if(!f.exists()){
			f.mkdirs();
		}
		for(int i=0;i<files1.length;i++)
		{
			files2 = files1[i].listFiles();
			for(int j=0;j<files2.length;j++)
			{
				double tempvalue = 0;
				if(files2[j].getName().substring(0,4).equals(time.substring(0,4)))//找到系数文件
				{
					try {
						br = new BufferedReader(new FileReader(files2[j]));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String record;
					String[] content;
					try {
						while((record = br.readLine())!=null){
							content = record.split(",");
							if(content[1].equals("NaN"))content[1]="0";
							tempvalue += Double.valueOf(content[1]);
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				tempvalue /= 16;
				if(files2[j].getName().substring(0,4).equals("2017")){
					output2 += files2[j].getName().substring(0,8)+","+tempvalue+"\n";
					output3 += tempvalue+",";
				}
				
			}
		}
		try {
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(outputPath+cTime+"小型车"+comStr+"统计输出结果.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File(outputPath+cTime+"小型车"+comStr+"统计输出结果(Echarts).txt")));
			bw3.write(output3);
			bw3.flush();
			bw3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		formatKxiantuByday obj = new formatKxiantuByday();
		obj.readChuxingliang("H:\\重庆畅通指数的计算\\201712\\2017年12月份小型车出行量（去掉异常）\\","201712","2017年12月");
		obj.readData("H:\\重庆畅通指数的计算\\201712\\2017年12月份小型车平均延误时间\\","201712","2017年12月","平均延误时间");
	}
}
