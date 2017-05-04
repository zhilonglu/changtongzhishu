package changtongzhishu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class xishuFabu {
	String[] fileNames={"����ϵ��.csv","�ɿ���ϵ��.csv","ƽ������ʱ��.csv"};
	File[] files1;
	File[] files2;
	File[] files3;
	BufferedReader brXishu = null;
	BufferedReader brDelayTime = null;
	BufferedReader brChuxingliang = null;
	BufferedWriter bw_xishu1 = null;
	BufferedWriter bw_xishu2 = null;
	BufferedWriter bw_Time1 = null;
	BufferedWriter bw_Time2 = null;
	//��¼ĳ��Сʱ��������ռ�����
	HashMap<String,Double> chuxingliangRate=new HashMap<String,Double>();
	public int ReadChuxingliang(String day,String hour){
		chuxingliangRate.clear();
		File[] file1;
		File[] file2;
		File rootfile = new File("Q:\\���쳩ָͨ���ļ���\\201703\\2017��3�·�С�ͳ���������ȥ���쳣��\\201703Сʱ������\\");
		file1= rootfile.listFiles();
		int sumVehicle=0;
		for(int i=0;i<file1.length;i++){
			file2 = file1[i].listFiles();
			if(!file1[i].getName().equals(day))
				continue;
			for(int j=0;j<file2.length;j++){
				if(!file2[j].getName().substring(0, 10).equals(hour))
					continue;
				try {
					brChuxingliang = new BufferedReader(new FileReader(file2[j]));
					String record;
					String[] content;
					try {
						while((record=brChuxingliang.readLine())!=null){
							content = record.split(",");
							chuxingliangRate.put(content[0], Double.valueOf(content[1]));
							sumVehicle += Double.valueOf(content[1]);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Iterator itIndex=chuxingliangRate.entrySet().iterator();
		while(itIndex.hasNext()){
			Map.Entry entryIndex=(Entry)itIndex.next();
			String key = (String) entryIndex.getKey();
			double tempIndex = (double) entryIndex.getValue();
			tempIndex = tempIndex/sumVehicle;
			chuxingliangRate.put(key, tempIndex);
		}
		return sumVehicle;
	}
	//�ҵ���Ч��Сʱ���ݽ��
	public boolean FindValidHour(String inputHour){
		if(inputHour.substring(8).equals("00") || inputHour.substring(8).equals("01")|| inputHour.substring(8).equals("02")
				|| inputHour.substring(8).equals("03") || inputHour.substring(8).equals("04")){
			return false;
		}
		else if(inputHour.substring(8).equals("21") || inputHour.substring(8).equals("22")|| inputHour.substring(8).equals("23")){
			return false;
		}
		return true;
	}
	public void ReadData(String rootpathXishu){
		File rootfile = new File(rootpathXishu);
		files1= rootfile.listFiles();
		String pathXishu="Q:\\���쳩ָͨ���ļ���\\201703\\2017��3�·�С�ͳ�����ϵ��\\201703\\";
		File f= new File(pathXishu);
		if(!f.exists()){
			f.mkdirs();
		}
		String pathDelayTime = "Q:\\���쳩ָͨ���ļ���\\201703\\2017��3�·�С�ͳ�ƽ������ʱ��\\201703\\";
		File f2= new File(pathDelayTime);
		if(!f2.exists()){
			f2.mkdirs();
		}
		String secondpathXishu = "Q:\\���쳩ָͨ���ļ���\\201703\\2017��3�·�С�ͳ�����ϵ��\\201703\\�����µĳ���ϵ��.csv";
		try {
			bw_xishu2 = new BufferedWriter(new FileWriter(new File(secondpathXishu)));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String secondpathTime = "Q:\\���쳩ָͨ���ļ���\\201703\\2017��3�·�С�ͳ�ƽ������ʱ��\\201703\\�����µ�ƽ������ʱ��.csv";
		try {
			bw_Time2 = new BufferedWriter(new FileWriter(new File(secondpathTime)));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String allput_xishu="";
		String allput_time = "";
		for(int i=0;i<files1.length;i++){
			String firstpathXishu = "";
			files2 = files1[i].listFiles();
			firstpathXishu = pathXishu + files1[i].getName()+".csv";
			String firstpathTime = "";
			firstpathTime = pathDelayTime + files1[i].getName()+".csv";
			String output_xishu="";
			String output_time ="";
			for(int j=0;j<files2.length;j++){
				double xishuHour = 0;
				double DelayTimeHour = 0;
				files3= files2[j].listFiles();
				String day = files1[i].getName();
				String hour = files2[j].getName();
				if(!FindValidHour(hour))//���ʱ�䷶Χ��������5�㵽����8��֮��
					continue;
				output_xishu += hour +",";
				allput_xishu += hour +",";
				output_time += hour+",";
				allput_time += hour +",";
				int sumCar = ReadChuxingliang(day,hour);
				for(int k=0;k<files3.length;k++){
					if(files3[k].getName().equals("����ϵ��.csv")){
						try {
							bw_xishu1 = new BufferedWriter(new FileWriter(new File(firstpathXishu)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							brXishu = new BufferedReader(new FileReader(files3[k]));
							String record;
							String[] content;
							try {
								while((record=brXishu.readLine())!=null){
									content = record.split(",");
									if(content[2].equals("Infinity") || content[2].equals("NaN"))
										content[2]="0";
									xishuHour += Double.valueOf(content[2])*chuxingliangRate.get(content[0]);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(files3[k].getName().equals("ƽ������ʱ��.csv")){
						try {
							bw_Time1 = new BufferedWriter(new FileWriter(new File(firstpathTime)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							brDelayTime = new BufferedReader(new FileReader(files3[k]));
							String record;
							String[] content;
							try {
								while((record=brDelayTime.readLine())!=null){
									content = record.split(",");
									if(content[2].equals("Infinity") || content[2].equals("NaN"))
										content[2]="0";
									double tempTime = Double.valueOf(content[2]);
									if(tempTime<0)
										tempTime=0;
									if(content[1].equals("null")|| content[1].equals("0"))continue;
									tempTime = tempTime/Double.valueOf(content[1]);//����������ƽ����������ʱ��
									DelayTimeHour += tempTime*chuxingliangRate.get(content[0]);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				output_time += DelayTimeHour+"\n";
				allput_time += DelayTimeHour+","+sumCar+"\n";
				output_xishu += xishuHour+"\n";
				allput_xishu += xishuHour+","+sumCar+"\n";
			}

			try {
				bw_xishu1.write(output_xishu);
				bw_xishu1.flush();
				bw_xishu1.close();
				bw_Time1.write(output_time);
				bw_Time1.flush();
				bw_Time1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bw_xishu2.write(allput_xishu);
			bw_xishu2.flush();
			bw_xishu2.close();
			bw_Time2.write(allput_time);
			bw_Time2.flush();
			bw_Time2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		xishuFabu obj = new xishuFabu();
		obj.ReadData("Q:\\���쳩ָͨ���ļ���\\201703\\2017��3�·ݵ�ϵ�������ȥ���쳣��\\201703");
	}
}
