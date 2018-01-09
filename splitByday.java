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
 * ��������һ���ļ����µ������ļ�
 * ����Сʱ�������ϵ����ʱ���Խ�������շ�վΪ׼��
 * �����ݻ��ֳɰ�������
 * @author NLSDE
 *
 */
public class splitByday{
	Map<String,FileWriter> map;
	PrintWriter pw;
	public splitByday(String time){//��Ҫ��������Ŀ¼�ļ�
		String year = time.substring(0,4);
		try{
			map= new HashMap<String,FileWriter>();
			for(int i=12;i<13;i++){
				File f=new File("J:\\���ٳ��������\\"+year+"�갴������\\"+year+i+"\\");
				if(!f.exists())
					f.mkdirs();
				for(int j=1;j<=9;j++){
					String filepath="J:\\���ٳ��������\\"+year+"�갴������\\"+year+i+"\\"+year+i+"0"+j+".csv";
					File file=new File(filepath);
					if(!file.exists())
						file.createNewFile();
					map.put(filepath, new FileWriter(filepath));
				}
				for(int j=10;j<=31;j++){
					String filepath="J:\\���ٳ��������\\"+year+"�갴������\\"+year+i+"\\"+year+i+j+".csv";
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
	//��ʽ�����ڣ������ȹ̶�Ϊ��λ��
	public String Format(String mTime){
		String time="";
		if(mTime.length()==2)
			return mTime;
		time = "0"+mTime;
		return time;
	}
	//������ʱ��ת��Ϊ������20170408101030��ʱ���ʽ 2017/11/01 00:00:00
	public String FormatTime(String inputTime){
		String outTime="";
		outTime = inputTime.substring(0,4)+inputTime.substring(5,7)+inputTime.substring(8,10);
		outTime += inputTime.substring(11,13)+inputTime.substring(14,16)+inputTime.substring(17,19);
		return outTime;
	}
	//����ʽ����׼�ĸ�ʽת��Ϊ201704101230��ʱ���ʽ
	public String FormatYMDHMMTime(String inputTime){
		String output="";
		String y="",m="",d="",Hour="",Minute="",Second="";
		String[] ymdHMS = inputTime.split(" ");
		String ymd = ymdHMS[0];
		String HMS = ymdHMS[1];
		if(ymd.contains("-")){//����-��ʱ���ʽ
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
		else if(ymd.contains("/")){//����-��ʱ���ʽ
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
	//�ų���������1970�����
	public boolean IsLegal(String input){
		if(input.contains("-") || input.contains("/")){
			String[] ymdHMS = input.split(" ");
			if(ymdHMS.length==1)
				return false;
		}
		return true;
	}
	//׷���ļ�
	public void appendFile(String fileName, String content) {   
        FileWriter writer = null;  
        try {     
            // ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�     
            writer = new FileWriter(fileName, true);     
            writer.write(content+"\n");       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    }
	/**
	 * 
	 */
	public void processing(String time,String cTime){
		String outputpath="J:\\���ٳ��������\\"+time.substring(0,4)+"�갴������\\";
		String path="";
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(new File("C:\\Users\\xibol\\Desktop\\"+cTime+"���ٳ���ˢ������.txt")));
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
				//�����ȡ���ļ���txt��ʽ������ʱ���ʽ����ȷ��������Ҫ��ʱ��ĸ�ʽת��Ϊ������ʱ�������ʽ
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
				path = outputpath+st[10].substring(0,6)+"\\"+st[10].substring(0,8)+".csv";
				if(map.get(path)!=null){
					pw = new PrintWriter(map.get(path));
					pw.println(record);
					pw.flush();
				}
				else{
					String tempDir = outputpath+st[10].substring(0,6)+"\\";
					File dirname = new File(tempDir); 
					if (dirname.isDirectory()) 
						appendFile(path,record);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		try{
			splitByday obj = new splitByday("201712");
			obj.processing("201712","2017��12��");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
