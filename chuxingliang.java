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

//����OD�ϵĳ�����
public class chuxingliang {
	//�洢����OD֮��ľ���
	HashMap<String,Double> distanceMap=new HashMap<String,Double>();
	DateFormat 
	sdf_1501=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),//2015��ר��
	sdf_1502=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),//2015��ר��
	sdf_1503=new SimpleDateFormat("yyyyMMddHHmmss");//2015��ר��
	/**
	 * ��ȡ���������ͣ���Ϊ�߸����ͣ�
	 * 0��С�ͳ�    1��С�ͳ�    2:1�ͻ���  3��2�ͻ���  4:3�ͻ���  5:4�ͻ���  6:5�ͻ���
	 * @param Flag
	 * @param vType
	 * @return
	 */
	public int getVehicleType(String Flag,String vType){
		if(Flag.equals("0")){//�����ǿͳ���Ȼ���������С�ͺʹ��п�
			if(Integer.valueOf(vType)<=1){
				return 0;
			}
			else{
				return 1;
			}
		}
		if(Flag.equals("1")){//�����ǻ�����Ȼ������Ϊ5�೵��
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
	 * ��ȡcsv�ļ���д��map
	 */
	public void readODCsv_new(){
		try {
			BufferedReader newbr = new BufferedReader(new FileReader(new File("J:\\���ٳ��������\\���쾲̬��������\\�շ�վ����\\�շ�վ��̾���20160623104640.csv")));
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
	 * �����ڸ�ʽת��Ϊʱ�䣬����Ϊ��λ
	 * @param date ����
	 * @return ��
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
			String Filepath = "H:\\���쳩ָͨ���ļ���\\"+date+"\\"+cTime+"��С�ͳ���������ȥ���쳣��\\"+date+"Сʱ������\\"+files1[i].getName()+"\\";
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
					String Filename = Filepath+files2[j].getName().split("\\.")[0]+"������.csv";
					try {
						bw = new BufferedWriter(new FileWriter(new File(Filename)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while((record=br.readLine())!=null){
						st=record.split(";");//txt�ļ�ʱ�ķָ����Ƿֺţ������Ƕ���
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
						//�˴������������޳��ٶȴ���120�Լ�����20�ĳ�����������ͳ��
						double speed=0;
						speed = 3600*distanceMap.get(ODname)/(extime-entime);
						if(speed<=20 || speed>=120)continue;
						//txt�ļ����ж���Ŀո�ȥ���ո�
						st[28] = st[28].replaceAll(" ", "");
						st[11] = st[11].replaceAll(" ", "");
						type = getVehicleType(st[28],st[11]);
						if(type!=0)//�˴��ı䣬���Եõ����ڲ�ͬ���͵ĳ������������
							continue;
						if(!ODcarnum.containsKey(ODname)){
							ODcarnum.put(ODname, 1);
							continue;
						}
						for(String ODName:ODcarnum.keySet()){
							if(ODName.equals(ODname)){
								int tempNUm=ODcarnum.get(ODname);
								tempNUm = tempNUm + 1 ;//�����������ϵ��
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
		obj.readData("J:\\���ٳ��������\\2017��Сʱ����\\201712","201712","2017��12��");
	}
}
