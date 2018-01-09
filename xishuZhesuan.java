package changtongzhishu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * 将按小时计算的系数转换成按天计算，直接将有效的小时求和平均，然后折算成5个等级，
 * 小于40为1，
 * 40-45为2，
 * 45-50为3，
 * 50-60为4，
 * 60以上为5
 */
import java.text.DecimalFormat;

public class xishuZhesuan {
	File file;
	File[] files1;
	File[] files2;
	File[] files3;
	//将系数转换成相应的五个系数
	public int ConvertToIndex(double inputValue){
		if(inputValue<=40)
			return 1;
		else if(inputValue>40 && inputValue<=45)
			return 2;
		else if(inputValue>45 && inputValue<=50)
			return 3;
		else if(inputValue>50 && inputValue<=60)
			return 4;
		return 5;
	}
	public void readDataByDay(String path,String road){
		String output2="";
		String output3="";
		BufferedReader br = null;
		file=new File(path);
		files1= file.listFiles();
		for(int i=0;i<files1.length;i++)
		{
			files2 = files1[i].listFiles();
			for(int j=0;j<files2.length;j++)
			{
				double tempvalue = 0;
				if(files2[j].getName().substring(0,4).equals("2016"))//找到系数文件
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
				if(files2[j].getName().substring(0,4).equals("2016")){
					output2 += files2[j].getName().substring(0,8)+","+ConvertToIndex(tempvalue)+"\n";
					output3 += ConvertToIndex(tempvalue)+",";
				}

			}
		}
		try {
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("N:\\201601\\按全路网计算\\折算后\\按天计算\\2016年1月小型车出行系数统计输出结果.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File("N:\\201601\\按全路网计算\\折算后\\按天计算\\2016年1月小型车出行系数统计输出结果(Echarts).txt")));
			bw3.write(output3);
			bw3.flush();
			bw3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readDataByHour(String path,String road){
		String output2="";
		String output3="";
		BufferedReader br = null;
		file=new File(path);
		files1= file.listFiles();
		for(int i=0;i<files1.length;i++)
		{
			files2 = files1[i].listFiles();
			for(int j=0;j<files2.length;j++)
			{
				if(files2[j].getName().substring(0,4).equals("2016"))//找到系数文件
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
							double tempvalue = Double.valueOf(content[1]);
							output2 += content[0]+","+ConvertToIndex(tempvalue)+"\n";
							output3 += ConvertToIndex(tempvalue)+",";
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
		}
		try {
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("N:\\201601\\按全路网计算\\折算后\\按小时计算\\2016年1月小型车出行系数统计输出结果.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File("N:\\201601\\按全路网计算\\折算后\\按小时计算\\2016年1月小型车出行系数统计输出结果(Echarts).txt")));
			bw3.write(output3);
			bw3.flush();
			bw3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		xishuZhesuan obj = new xishuZhesuan();
		String[] roadName={"G50S","沪蓉","沪渝","南涪","绕城","三环","渝沪","渝昆","渝邻",
				"渝黔","渝蓉","渝遂","渝武","渝湘"};
//		for(int i=0;i<14;i++){
//			obj.readDataByDay("N:\\201601\\按路段计算\\2016年1月"+roadName[i]+"小型车出行系数（去掉异常）\\",roadName[i]);
//			obj.readDataByHour("N:\\201601\\按路段计算\\2016年1月"+roadName[i]+"小型车出行系数（去掉异常）\\",roadName[i]);
//		}
//		obj.readDataByDay("N:\\201601\\按路段计算\\2016年1月渝遂小型车出行系数（去掉异常）\\");
//		obj.readDataByHour("N:\\201601\\按路段计算\\2016年1月渝遂小型车出行系数（去掉异常）\\");
		obj.readDataByHour("N:\\201601\\按全路网计算\\2016年1月份小型车出行系数\\","2015");
		obj.readDataByDay("N:\\201601\\按全路网计算\\2016年1月份小型车出行系数\\","2015");
	}
}
