package changtongzhishu;

public class mainProcess {
	//�����������������ڣ��޸���������ڣ����иó�����ܵõ�ϵ��������
	public mainProcess() {
		// TODO Auto-generated constructor stub
		String time = "201801";
		String ctime = "2018��01��";
		//step1 ��һ���µ�ԭʼˢ������ת���ɰ�Сʱ�洢������
		splitByhourTxt step1 = new splitByhourTxt(time);
		step1.splitByHour("C:\\Users\\xibol\\Desktop\\2018��01�¸��ٳ���ˢ������.txt",time);
		//step2 ����Сʱ���ȵĳ���ϵ��������ʱ�䡢����ϵ�����г�ʱ��
		newRuleCalNormal step2=new newRuleCalNormal("J:\\���ٳ��������\\�����ͼ\\��׼�г�ʱ�䣨90per_h��.csv");
		step2.readData("J:\\���ٳ��������\\2018��Сʱ����\\"+time,time,ctime);
		//step3 ����Сʱ���ȵĳ���������
		chuxingliang step3 = new chuxingliang();
		step3.readData("J:\\���ٳ��������\\2018��Сʱ����\\"+time,time,ctime);
		//step4 ���㰴�����ĳ���ϵ����ƽ������ʱ��
		xishuFabu step4 = new xishuFabu();
		step4.ReadData("H:\\���쳩ָͨ���ļ���\\"+time+"\\"+ctime+"�ݵ�ϵ�������ȥ���쳣��\\"+time,time,ctime);
		//step5 �����ʽ����İ�Сʱ�ĳ���ϵ����ƽ������ʱ�䡢������
		formatKxiantu step5 = new formatKxiantu();
		step5.readChuxingliang("H:\\���쳩ָͨ���ļ���\\"+time+"\\"+ctime+"��С�ͳ���������ȥ���쳣��\\",time,ctime);
		step5.readData("H:\\���쳩ָͨ���ļ���\\"+time+"\\"+ctime+"��С�ͳ�����ϵ��\\",time,ctime,"����ϵ��");
		step5.readData("H:\\���쳩ָͨ���ļ���\\"+time+"\\"+ctime+"��С�ͳ�ƽ������ʱ��\\",time,ctime,"ƽ������ʱ��");
		//step6 �����ʽ����İ���ĳ���ϵ����ƽ������ʱ�䡢������
		formatKxiantuByday step6 = new formatKxiantuByday();
		step6.readChuxingliang("H:\\���쳩ָͨ���ļ���\\"+time+"\\"+ctime+"��С�ͳ���������ȥ���쳣��\\",time,ctime);
		step6.readData("H:\\���쳩ָͨ���ļ���\\"+time+"\\"+ctime+"��С�ͳ�����ϵ��\\",time,ctime,"����ϵ��");
		step6.readData("H:\\���쳩ָͨ���ļ���\\"+time+"\\"+ctime+"��С�ͳ�ƽ������ʱ��\\",time,ctime,"ƽ������ʱ��");
		//˳����㽫ԭʼ����ת���ɰ�day�洢������
		splitByday obj = new splitByday(time);
		obj.processing(time,ctime);
		
	}
}
