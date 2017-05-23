package org.neu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;

public class AnalyzeData {
	private List excList = new ArrayList();
	private List Block1List = new ArrayList();
	private List Block2List = new ArrayList();
	private List Block3List = new ArrayList();
	private List Block4List = new ArrayList();
	private List discardList = new ArrayList();
	private List capacityList = new ArrayList();
	private List acmList =  new ArrayList();
	private int leghth_ATBM = 27;
	WRFile wrFile = new WRFile();
	public void analyzeViolate()
	{
		//1����ȡ�ļ�
		String filePath = "";
	}
	
	
	public void caculUsageRate()
	{
		
		int len = 27;
		String fileName[] = {"exc","block1","block2","block3","block4"};
		//"SQS","ATBMS",
		String folder[] = {"MATBM"};
		for(String folderName:folder)
		{
		String suffixname = "D:\\cloudsim\\log\\"+folderName+"\\Vm\\vm53";
		int vmCount = 21;
		double data[][] = new double[vmCount][5];
		for(int i=0;i<vmCount;i++)
		{
			String name = suffixname+i+"/";
			int col = 0;
		for(String str:fileName)
		{
			
			String temp = name+str+".xlsx";
			int usage[] = wrFile.readExcel(temp, "sheet");
			double usageRate[] = new double[usage.length];
			double averageRate = 0.0;
			for(int k = 0;k<usage.length;k++)
			{
				usageRate[k] = 1.0*usage[k]/len;
				averageRate+=usageRate[k];
			}
			data[i][col++] = averageRate/usage.length;
		}
	    }
		String filePath = "D:\\cloudsim\\log\\"+folderName+"\\Vm\\vmusage.xlsx";
		wrFile.writeCacheAndBlock(data,filePath , "sheet", vmCount, 5);
		}
	}
	public void getEachVmProcessRate()
	{
		
	}
	
	
	
	public void caculRejectRate()
	{
		String reject = "D:\\cloudsim\\log\\90th\\SQ\\rejectcloudlet/SQ"+53+"rejectAnalyze.xlsx";
		String sheetName ="sheet";
		int rejectData[] = wrFile.readExcel(reject, sheetName,0);
		
		String original = "F:\\one\\predict\\traindata\\modified/workload53modified.xlsx";
		int originalData[] = wrFile.readExcel(original, sheetName,0);
		
		double rejectRate[] = new double[CloudSim.WORKLOAD_SUBMIT_TIMES];
		
		for(int i = 0;i<originalData.length;i++)
		{
			rejectRate[i] =  1.0*rejectData[i]/originalData[i];
		}
		wrFile.writeInExcel(rejectRate, reject.replaceAll("rejectAnalyze", "rejectRate"));
	}
	
	
	/**
	 * 
	 * @param k  �����k����ͻ��ǿ��
	 * @return
	 */
	private int updateAvailableCloudletCount(double k)
	{
		
        
		int result = (int)(this.leghth_ATBM*(CloudSim.REJECTED_RATE*(k+1)+CloudSim.m-k)/CloudSim.m/(1-CloudSim.REJECTED_RATE));
		if(result<0)
		{
			result = this.leghth_ATBM;
		}	
	     return result;
	}
	  private void cloudletBlock()
	  {
		  int rcl = 1;	
		  if(this.Block1List.size()<this.leghth_ATBM)
			{
			  
			  Block1List.add(1);
				//System.out.println("Ԫ�� "+rcl.getCloudletId()+"�����block1�� ");
				return;
			}
			if(this.Block2List.size()<this.leghth_ATBM)
			{
			  
	           Block2List.add(rcl);		
				 return;
			}
			if(this.Block3List.size()<this.leghth_ATBM)
			{
				
			Block3List.add(rcl);
				
				return;
		    }
			if(this.Block4List.size()<this.leghth_ATBM)
			{
				
				Block4List.add(rcl);
				return;
		    }
			else
			{
			   discardList.add(rcl);
			}
	  }
	private void addCloudletIntoATBM(int cl)
	{
		//������Ҫ���ֶ��ָ��˼��
		 //��ȡcloudlet�еĸ���
       for(int i = 0;i<cl;i++)
       {
    	   int count_cloudlet = this.excList.size();
		   if(count_cloudlet<this.leghth_ATBM)
		   {
			    //����ִ��ʱ��
			   excList.add(1);
		   }
		   else
		   {
			  
			   this.cloudletBlock();
			   
		   }
       }
			
	}
	private double updateBursty()
	{
		 //���㻺����еĴ洢���
		
		double bursty_block1 = (double)this.Block1List.size()/this.leghth_ATBM;
		double bursty_block2 = (double)this.Block2List.size()/this.leghth_ATBM;                                         
		double bursty_block3 = (double)this.Block3List.size()/this.leghth_ATBM;
		double bursty_block4 = (double)this.Block4List.size()/this.leghth_ATBM;
		
		double bursty = (bursty_block1+bursty_block2+bursty_block3+bursty_block4);
		return bursty;
	} 
	public void updateATBM()
	{
		 if(excList.size()<this.leghth_ATBM)
	        {
	            if(this.Block1List.size()>0)
	            	this.moveBlockToCache();
	            else if(excList.size()==0&&Block1List.size()==0)
	            {
	            return;

	            }
	        }
		 //����excList�е�����
		 this.excList.clear();
	}
	private void moveBlockToCache()
	{
		//ExcList�п��г�����λ��
		int availableSpace = this.leghth_ATBM-this.excList.size();
		if(availableSpace<=0)
			return  ;//�����ƶ�cloudlet
		
		if(this.Block1List.size()>0) //q1������ݣ���Ҫ���д���
		{
			//ȡ���ö����е����ݣ����뵽ִ�ж�����
			int count = 0; 
			if(availableSpace<=Block1List.size()) //��block1���Ƴ�space��cloudlet��Exec��
			{
				List toRemove = new ArrayList();
				try{
				for(int i = 0;i<availableSpace;i++)
				{
					int temp = (int)Block1List.get(i);

					toRemove.add(temp);
					
				}  //��block1�е�Ԫ��ȫ���ƶ���cache��
				
				Block1List.removeAll(toRemove);
				}catch(Exception e)
				{
					e.toString();
					
				}
			}
			
			else if(availableSpace>this.Block1List.size()) //block1��������������С�ڻ����������
			{
				
				for(int j = 0;j<Block1List.size();j++)
				{
					this.addCloudletIntoATBM(1);
				}
				
				this.Block1List.clear();
			}
		
			
			//��block2...4�е�Ԫ����������
			if(moveBlock(this.Block1List,this.Block2List))
			{
				if(moveBlock(this.Block2List,this.Block3List))
					if(moveBlock(this.Block3List,this.Block4List))
					{
						
					}
					else
					{
						System.out.println("��cloudlet��block4�ƶ���block3ʱ��������");
						
					}
				else
				{
					System.out.println("��cloudlet��block3�ƶ���block2ʱ��������");
					
				}
			}
			else
			{
				System.out.println("��cloudlet��block2�ƶ���block1ʱ��������");
				
			}
						
		}
		else
		{
			System.out.println("no cloudlet in blocklist 1");
		}
		
	}
	/**
	 * ���ǰ�dest�е�Ԫ���ƶ���src��
	 * @param src
	 * @param dest
	 * @return
	 */
	private boolean moveBlock(List src, List dest)
	{
		//���г�����λ��
	   int availableSpace = this.leghth_ATBM-src.size(); //���е�λ��
	   int blockSizeofDest = dest.size();
	   if(availableSpace<0)
			return false;//�����ƶ�cloudlet
	   
	   
		if(blockSizeofDest>0) //q1������ݣ���Ҫ���д���
		 {
			//ȡ���ö����е����ݣ����뵽ִ�ж�����
			
            if(availableSpace<=blockSizeofDest) //��block1���Ƴ�space��cloudlet��Exec��
			   {
            	List toRemove = new ArrayList();
				for(int i = 0;i<availableSpace;i++)
				   {
					int temp = (int)dest.get(i);
					src.add(temp);
					toRemove.add(temp);
					
			       }  //��block1�е�Ԫ��ȫ���ƶ���cache��
				dest.removeAll(toRemove);
               }
              
		     else if(availableSpace>dest.size()) //block1��������������С�ڻ����������
			   {
						src.addAll(dest);
						dest.clear();
			   }
			 
	      }
		 return true;
	}
	private final int cloudletSubmitTimes = 20;
	private int[] cloudletList = new int[this.cloudletSubmitTimes];
	private double[] burstyList = new double[this.cloudletSubmitTimes];
	public void caculACM()
	{
		

		cloudletList[0] = 60;
		int requests = 0;
		for(int i = 0;i<this.cloudletSubmitTimes;i++)
		{
		addCloudletIntoATBM(cloudletList[i]);//atbm�ĳ�ʼ״̬
		double bursty = this.updateBursty();
		burstyList[i] = bursty;
	    requests = this.updateAvailableCloudletCount(bursty);//��ȡ��һ�����ڿɽ��ܵ�cloudlet��
	    if(i+1<this.cloudletSubmitTimes)
	    {
	      cloudletList[i+1] = requests;
	      updateATBM();
	    }
	    else
	    	break;
		} 
		//�Ѽ���õ���cloudlet��������
		String filePath = "D:\\cloudsim\\log\\acm/acm.xlsx";
		wrFile.writeInExcel(cloudletList, filePath);
		wrFile.writeInExcel(burstyList, filePath.replace("acm.xlsx", "bursty.xlsx"));
	}
	public void createTestAcmData()
	{
		String filePath = "D:\\cloudsim\\log\\acm/acm.xlsx";
		int originalData[] = wrFile.readExcel(filePath, "sheet",0);
		double times[] = {1.1,1.2,1.5};
		int[] cloudletList = new int[this.cloudletSubmitTimes];
		for(int i =0;i<times.length;i++)
		{
			for(int k = 0;k<this.cloudletSubmitTimes;k++)
			{
				int temp = (int)Math.ceil(originalData[k]*times[i]);
				cloudletList[k] = temp;
			}
			wrFile.writeInExcel(cloudletList, filePath.replace("acm.xlsx", "acm"+i+".xlsx"));
		}
	}
	
	public void analyzeReject()
	{
		String filePath = "D:\\cloudsim\\log\\90th\\SQ\\rejectcloudlet/SQ"+53+"reject.xlsx";
		String sheetName ="sheet";
		int data[] = wrFile.readExcel(filePath, sheetName,1);
		//System.out.println(data.length);
		//1.�������ݣ�ͳ��cancel��������Щʱ�̣����Ƕ�Ӧ��count�Ƕ���
		Map map = new HashMap();
		for(int temp:data)
		{
			//System.out.println(temp);
			if(map.containsKey(temp))
			{
				int currentCount = (int)(map.get(temp));
				currentCount++;
				map.replace(temp, currentCount);
			}
			else
			{
				map.put(temp, 1);
			}
		}
		
		//2.�����ɵ���������excel�� 
		int rejectData[] = new int[CloudSim.WORKLOAD_SUBMIT_TIMES];
		for(int i = 0;i<CloudSim.WORKLOAD_SUBMIT_TIMES;i++)
		{
			if(map.containsKey(i))
			{
				rejectData[i] = (int)(map.get(i));
			}
			else
				rejectData[i] = 0;
		}
		
		wrFile.writeInExcel(rejectData, filePath.replace("reject.xlsx", "rejectAnalyze.xlsx"));
	}
	public void createVmCloudletFile()
	{
		Map<Integer,List<StoreCloudlet>> vmMap = new HashMap<Integer,List<StoreCloudlet>>();
		String filePath = "D:\\cloudsim\\log\\minus30simpleTest\\Cloudlet/ATBM53cloudlet.xlsx";
		String sheetName = "sheet";
		StoreCloudlet[] data = wrFile.readStoreCloudlet(filePath, sheetName);
		StoreCloudlet temp = null;
		for(int i = 0;i<data.length;i++)
		{
			temp = data[i];
			int vmId = temp.getVmId();
			if(vmMap.containsKey(vmId))
			{
				List<StoreCloudlet> list = (List<StoreCloudlet>)(vmMap.get(vmId));
				list.add(temp);
			}
			else
			{
				List<StoreCloudlet> list = new ArrayList<StoreCloudlet>();
				list.add(temp);
				vmMap.put(vmId, list);
			}
		}
		//��map�е�Ԫ�ش�����
		Set set = vmMap.keySet();
		System.out.println(set.size());
		for(Object elem:set)
		{
			int vmId = (int)elem;
			List<StoreCloudlet> list = vmMap.get(vmId);
			wrFile.writeStoreClooudlet(list, filePath.replace(".xlsx", ""+vmId+".xlsx"),sheetName);
		}
	}
	public void cacualRepeateProcess()
	{
		String filePath = "D:\\cloudsim\\log\\both\\Cloudlet/ATBM53cloudlet.xlsx";
		String sheetName = "sheet";
		StoreCloudlet[] data = wrFile.readStoreCloudlet(filePath, sheetName);
		double index[] = new double[37163];//index[0]����
		for(int i = 0;i<data.length;i++)
		{
			StoreCloudlet temp = data[i];
			index[temp.getId()]++;
			if(temp.getStatus()==CloudSimTags.CLOUDLET_CANCEL)
			    index[temp.getId()]*=-1;
			if(index[temp.getId()]>1)
				System.out.println("id  "+temp.getId()+"  vmId "+temp.getVmId()+"  "+temp.getIsViolated()+"  status "+temp.getStatus()+"send point "+temp.getSendTimePoint());
		}
		wrFile.writeInExcel(index, filePath.replace(".xlsx", "replace.xlsx"));
	}
	public void cacualViolateAndRejectRateOfVm()
	{
		//1�����ȶ�ȡ�ļ�
		String filePath = "D:\\cloudsim\\log\\minus30simpleTest\\Cloudlet/ATBM53cloudlet.xlsx";
		String sheetName = "sheet";
		
		for(int i = 0;i<27;i++)
		{
		StoreCloudlet[] data = wrFile.readStoreCloudlet(filePath.replace(".xlsx",""+i+".xlsx"), sheetName);
		int violateCount = 0;
		int rejectCount = 0;
		double proccessTime = 0;
		System.out.println("usage rate of vm "+i+"  "+1.0*data.length/37162);
		for(StoreCloudlet element:data) 
		{
			//�����ж��Ƿ�Υ��
			if(element.getIsViolated()==1.0)
			{
				violateCount++;
				proccessTime+=(element.getFinishTime()-element.getSubmisstionTime());
			}
			else if(element.getStatus()==6.0)
			{
				rejectCount++;
			}
			
			else if(element.getStatus()==4.0)
			{
				proccessTime+=(element.getFinishTime()-element.getSubmisstionTime());
			}
		}
		 System.out.println("vmId "+i+" Υ���� "+ 1.0*violateCount/data.length+"  �ܾ���  "+1.0*rejectCount/data.length+"  "+"ƽ����Ӧʱ�� "+proccessTime/data.length);
		}
	}

	public static void main(String[] args) {
		AnalyzeData analyzer = new AnalyzeData();
		analyzer.caculACM();
		//analyzer.createTestAcmData();
		//analyzer.analyzeReject();
       //analyzer.caculRejectRate();
	      analyzer.caculUsageRate();
		//analyzer.caculACM();
		//analyzer.createTestAcmData();
       // analyzer.createVmCloudletFile();
		//analyzer.cacualViolateAndRejectRateOfVm();
		//analyzer.cacualRepeateProcess();
	}

}
