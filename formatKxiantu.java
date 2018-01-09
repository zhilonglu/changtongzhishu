package changtongzhishu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * ������ת����echarts��ʽ����k��ͼ����ʽ��ʾ����
 */
import java.text.DecimalFormat;

public class formatKxiantu {
	File file;
	File[] files1;
	File[] files2;
	File[] files3;
	DecimalFormat df=new DecimalFormat("##0.00");
	//�ҵ���Ч��Сʱ���ݽ����ÿ��ֻ����5-20ʱ������
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
	//��������ʽ�����
	public void readChuxingliang(String path,String time,String cTime){
		String output2="";
		String output3="";
		BufferedReader br = null;
		file=new File(path);
		files1= file.listFiles();
		File f = new File("H:\\���쳩ָͨ���ļ���\\"+time+"\\����ǰ\\��Сʱ����\\");
		if(!f.exists()){
			f.mkdirs();
		}
		for(int i=0;i<files1.length;i++)
		{
			files2 = files1[i].listFiles();
			for(int j=0;j<files2.length;j++)
			{
				files3 = files2[j].listFiles();
				for(int k=0;k<files3.length;k++){
					if(FindValidHour(files3[k].getName()))//�ҵ�ϵ���ļ�
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
							double sumCar=0;
							while((record = br.readLine())!=null){
								content = record.split(",");
								if(content[1].equals("NaN"))content[1]="0";
								double tempvalue = Double.valueOf(content[1]);
								sumCar += tempvalue;
							}
							output2 += files3[k].getName().substring(0,10)+","+sumCar+"\n";
							output3 += sumCar+",";
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
		}
		try {
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("H:\\���쳩ָͨ���ļ���\\"+time+"\\����ǰ\\��Сʱ����\\"+cTime+"С�ͳ�������ͳ��������_��Сʱ����.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File("H:\\���쳩ָͨ���ļ���\\"+time+"\\����ǰ\\��Сʱ����\\"+cTime+"С�ͳ�������ͳ��������_��Сʱ����(Echarts).txt")));
			bw3.write(output3);
			bw3.flush();
			bw3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readData(String path,String time,String cTime,String comstr){
		String output2="";
		String output3="";
		String outputPath = "H:\\���쳩ָͨ���ļ���\\"+time+"\\����ǰ\\��Сʱ����\\";//����ļ���·��λ��
		File f = new File(outputPath);
		if(!f.exists()){
			f.mkdirs();
		}
		BufferedReader br = null;
		file=new File(path);
		files1= file.listFiles();
		for(int i=0;i<files1.length;i++)
		{
			files2 = files1[i].listFiles();
			for(int j=0;j<files2.length;j++)
			{
				if(files2[j].getName().substring(0,4).equals(time.substring(0, 4)))//�ҵ�ϵ���ļ�
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
							output2 += content[0]+","+tempvalue+"\n";
							output3 += tempvalue+",";
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
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(outputPath+cTime+"С�ͳ�"+comstr+"ͳ��������.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File(outputPath+cTime+"С�ͳ�"+comstr+"ͳ��������(Echarts).txt")));
			bw3.write(output3);
			bw3.flush();
			bw3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		formatKxiantu obj = new formatKxiantu();
		obj.readChuxingliang("H:\\���쳩ָͨ���ļ���\\201712\\2017��12�·�С�ͳ���������ȥ���쳣��\\","201712","2017��12��");
		obj.readData("H:\\���쳩ָͨ���ļ���\\201712\\2017��12�·�С�ͳ�ƽ������ʱ��\\","201712","2017��12��","����ϵ��");
	}
}
