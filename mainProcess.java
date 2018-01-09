package changtongzhishu;

public class mainProcess {
	//这是整个程序的主入口，修改输入和日期，运行该程序就能得到系数计算结果
	public mainProcess() {
		// TODO Auto-generated constructor stub
		String time = "201801";
		String ctime = "2018年01月";
		//step1 将一个月的原始刷卡数据转换成按小时存储的数据
		splitByhourTxt step1 = new splitByhourTxt(time);
		step1.splitByHour("C:\\Users\\xibol\\Desktop\\2018年01月高速出口刷卡数据.txt",time);
		//step2 计算小时粒度的出行系数、延误时间、延误系数、行程时间
		newRuleCalNormal step2=new newRuleCalNormal("J:\\高速出入口数据\\重庆地图\\标准行程时间（90per_h）.csv");
		step2.readData("J:\\高速出入口数据\\2018年小时数据\\"+time,time,ctime);
		//step3 计算小时粒度的车辆出行量
		chuxingliang step3 = new chuxingliang();
		step3.readData("J:\\高速出入口数据\\2018年小时数据\\"+time,time,ctime);
		//step4 计算按天计算的出行系数与平均延误时间
		xishuFabu step4 = new xishuFabu();
		step4.ReadData("H:\\重庆畅通指数的计算\\"+time+"\\"+ctime+"份的系数结果（去掉异常）\\"+time,time,ctime);
		//step5 计算格式化后的按小时的出行系数、平均延误时间、出行量
		formatKxiantu step5 = new formatKxiantu();
		step5.readChuxingliang("H:\\重庆畅通指数的计算\\"+time+"\\"+ctime+"份小型车出行量（去掉异常）\\",time,ctime);
		step5.readData("H:\\重庆畅通指数的计算\\"+time+"\\"+ctime+"份小型车出行系数\\",time,ctime,"出行系数");
		step5.readData("H:\\重庆畅通指数的计算\\"+time+"\\"+ctime+"份小型车平均延误时间\\",time,ctime,"平均延误时间");
		//step6 计算格式化后的按天的出行系数、平均延误时间、出行量
		formatKxiantuByday step6 = new formatKxiantuByday();
		step6.readChuxingliang("H:\\重庆畅通指数的计算\\"+time+"\\"+ctime+"份小型车出行量（去掉异常）\\",time,ctime);
		step6.readData("H:\\重庆畅通指数的计算\\"+time+"\\"+ctime+"份小型车出行系数\\",time,ctime,"出行系数");
		step6.readData("H:\\重庆畅通指数的计算\\"+time+"\\"+ctime+"份小型车平均延误时间\\",time,ctime,"平均延误时间");
		//顺便计算将原始数据转换成按day存储的数据
		splitByday obj = new splitByday(time);
		obj.processing(time,ctime);
		
	}
}
