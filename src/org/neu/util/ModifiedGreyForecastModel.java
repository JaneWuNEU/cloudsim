package org.neu.util;
import java.util.Arrays;

import org.apache.commons.math3.stat.regression.SimpleRegression;


public class ModifiedGreyForecastModel {
	
	private int[] x_1; //暂时设置为类成员变量
	private int[] x_0;
	//private double p;
	private int n;
	private int periods;
	private double[] changeRate = null;
	//private double[] predictSeries = new double[1400];
	private String sheetName = "1";
	//private String filePath = "F:/Data/workload53inmin.xlsx";
    public int[] GMPredict;
    private WRFile wrFile = new WRFile();
	public ModifiedGreyForecastModel(int periods)
	{
		this.periods = periods;
		this.GMPredict = wrFile.readExcel("D:\\cloudsim\\log\\workload/GM.xlsx", "sheet");
		
	}
	
	public int[] getAGO(int[] x_0){
		int data_volume = x_0.length;
		x_1 = new int[data_volume];
		
		for(int i = 0; i < data_volume; i ++){
			int temp = 0;
			int k = 0;
			while(k <= i){
				temp += x_0[k];
				k += 1;
			}
			x_1[i] = temp;
		}
		//System.out.println("AGO ");
		for(int i:x_1)
			System.out.print(" "+i);
		//System.out.println(" ");
		return x_1;  
	}
	
	//get the coefficients a and b of the grey model
	public double func(double p, double x){
		double a = p;
		double b = p;
		return -(a * x) + b;
	}
	
	public double error(double p, double x, double y){
		return func(p, x) - y;
	}
	
   
	public double[] coefficientAandB(){
		double[] z = new double[this.n];
		double[] x = new double[this.n-1];
		for(int i = 1; i < this.n; i ++ ){    //这里的循环次数是否n？
			z[i] = (x_1[i] + x_1[i - 1]) / 2;
			x[i - 1] = z[i];
		}
		/*//create the matrix U
		double[][] U_max = new double[n - 1][2]; //GM最小二乘矩阵为（n-1）*2
		for(int j = 0; j < n - 1; j ++){
			for(int k = 0; k < 2; k ++){
				if(k == 1)
					U_max[j][k] = 1;
				else
					U_max[j][k] = -z[j + 1];  //这里数组下标可能出错
			}	
		}  */
		
		
		//y = self.x_0[1:]  y的值
		double[] y = new double[this.n - 1];
		for(int m = 0; m < this.n - 1; m ++){
			y[m] = x_0[m + 1]; 
		}
		//double[][] data = new double[n - 1][n - 1];
		
		SimpleRegression regression = new SimpleRegression();
		//OLSMultipleLinearRegression regression2 = new OLSMultipleLinearRegression();
		for(int i = 0; i < this.n - 1; i ++){
			regression.addData(x[i], y[i]); 
			
		}
		double[] Para = new double[2];
		Para[0] = regression.getSlope();
		Para[1] = regression.getIntercept();
		return Para;
		
		
	}

	/*     计算使用傅里叶优化的残差的系数   
	                  输入的x_0是上个窗口内实际的并发量,x_p是预测值  */
	public double funcForFResidual(double[] p, double[] x){
		double[] c = p;
		double result = 0;
		for(int i = 0; i < p.length; i ++){
			result += c[i] * x[i];
		}
		return result;
	}
	
	public double errorForResidual(double[] p, double[] x, double y){
		return funcForFResidual(p, x) - y;
	}
	

	public int predictGMValue(int[] x_0, int t){
		int n = x_0.length;
		if(n < 4){
			System.out.println("training data is less than 4 that can not meet the model's minimum demand");
		    return 0;
		}
		this.n = n;
		this.x_0 = x_0;
		this.x_1 = getAGO(this.x_0);
		
		double[] Para = coefficientAandB();
		double a = Para[0];
		double b = Para[1];
		
		//预测的是t时刻的并发量
		double predict = (1 - Math.exp(a)) * (this.x_0[0] - b / a) * Math.exp(-a * this.n);//这一步是否可以放到coefficientAandB中
		
		return (int)(predict);
			
	}
	
	public double predictRGMValue(int[] x_0, int t, double[] predictSeries){
		int n = x_0.length;
		if(n < 4){
			System.out.println("training data is less than 4 that can not meet the model's minimum demand");
		    return 0;
		}
		this.n = n;
		this.x_0 = x_0;
		this.x_1 = getAGO(this.x_0);
		
		double[] Para = coefficientAandB();
		double a = Para[0];
		double b = Para[1];
		
		//预测的是t时刻的并发量
		double predict = (1 - Math.exp(a)) * (this.x_0[0] - b / a) * Math.exp(-a * this.n);//这一步是否可以放到coefficientAandB中
		
		//计算t时刻前,窗口大小为periods的残差均值
		int start = t - this.periods;
		double[] residual = new double[predictSeries.length];
		int sum = 0;
		for(int i = 0; i < t; i ++){
			residual[i] = predictSeries[start ++] - x_0[i];
			
			sum += residual[i];
		}
		int avgResidual = sum / this.periods;
		predict += avgResidual;
		
		return predict;
			
	}
	
	public int predictMRGMValue(int[] trainData, int t, int[] mRGMPredictResults){
		int n = trainData.length;
		if(n < 4){
			System.out.println("training data is less than 4 that can not meet the model's minimum demand");
		    return 0;
		}
		this.n = n;
		this.x_0 = trainData;
		this.x_1 = getAGO(this.x_0);
		
		double[] Para = coefficientAandB();
		double a = Para[0];
		//System.out.println("系数a "+a);
		double b = Para[1];
		//System.out.println("系数b "+b);
		//预测的是t时刻的并发量
		if(a==0)
			a = 0.0000001;
		double predict = (1 - Math.exp(a)) * (this.x_0[0] - b / a) * Math.exp(-a * this.n);//这一步是否可以放到coefficientAandB中
		//System.out.println("预测值"+predict);
		//计算t时刻前,窗口大小为periods的残差均值
		int start = t - this.periods;
		double[] residual = new double[this.periods];
		double sum = 0;
		for(int i = 0; i < this.periods; i ++){
			residual[i] = mRGMPredictResults[start ++] - trainData[i];
			
			if(residual[i] > 0) 
				residual[i] = 0;
			else 
				residual[i] *= -1;
			
			sum += residual[i];
		}
		double avgResidual = sum / this.periods;
		predict += avgResidual;
		
		return (int)(Math.ceil(predict));
		
		
	}
	
    
	


	public static void main(String[] args){
		ModifiedGreyForecastModel MG = new ModifiedGreyForecastModel(5);
		
	}
	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

}
