package changtongzhishu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/*
 * ����Сʱ�����ϵ��ת���ɰ�����㣬ֱ�ӽ���Ч��Сʱ���ƽ����Ȼ�������5���ȼ���
 * С��40Ϊ1��
 * 40-45Ϊ2��
 * 45-50Ϊ3��
 * 50-60Ϊ4��
 * 60����Ϊ5
 */
import java.text.DecimalFormat;

public class xishuZhesuan {
	File file;
	File[] files1;
	File[] files2;
	File[] files3;
	//��ϵ��ת������Ӧ�����ϵ��
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
				if(files2[j].getName().substring(0,4).equals("2016"))//�ҵ�ϵ���ļ�
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
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("N:\\201601\\��ȫ·������\\�����\\�������\\2016��1��С�ͳ�����ϵ��ͳ��������.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File("N:\\201601\\��ȫ·������\\�����\\�������\\2016��1��С�ͳ�����ϵ��ͳ��������(Echarts).txt")));
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
				if(files2[j].getName().substring(0,4).equals("2016"))//�ҵ�ϵ���ļ�
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
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("N:\\201601\\��ȫ·������\\�����\\��Сʱ����\\2016��1��С�ͳ�����ϵ��ͳ��������.csv")));
			bw2.write(output2);
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File("N:\\201601\\��ȫ·������\\�����\\��Сʱ����\\2016��1��С�ͳ�����ϵ��ͳ��������(Echarts).txt")));
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
		String[] roadName={"G50S","����","����","�ϸ�","�Ƴ�","����","�廦","����","����",
				"��ǭ","����","����","����","����"};
//		for(int i=0;i<14;i++){
//			obj.readDataByDay("N:\\201601\\��·�μ���\\2016��1��"+roadName[i]+"С�ͳ�����ϵ����ȥ���쳣��\\",roadName[i]);
//			obj.readDataByHour("N:\\201601\\��·�μ���\\2016��1��"+roadName[i]+"С�ͳ�����ϵ����ȥ���쳣��\\",roadName[i]);
//		}
//		obj.readDataByDay("N:\\201601\\��·�μ���\\2016��1������С�ͳ�����ϵ����ȥ���쳣��\\");
//		obj.readDataByHour("N:\\201601\\��·�μ���\\2016��1������С�ͳ�����ϵ����ȥ���쳣��\\");
		obj.readDataByHour("N:\\201601\\��ȫ·������\\2016��1�·�С�ͳ�����ϵ��\\","2015");
		obj.readDataByDay("N:\\201601\\��ȫ·������\\2016��1�·�С�ͳ�����ϵ��\\","2015");
	}
}
