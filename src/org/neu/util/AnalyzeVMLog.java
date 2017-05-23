package org.neu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;

public class AnalyzeVMLog {
	public int clock = 0;
	private List excList = new ArrayList();
	private List Block1List = new ArrayList();
	private List Block2List = new ArrayList();
	private List Block3List = new ArrayList();
	private List Block4List = new ArrayList();
	private List PauseList = new ArrayList();
	private List discardList = new ArrayList();
	private List burstyList = new ArrayList();
	private List capacityList = new ArrayList();
	private List acmList =  new ArrayList();
    public String[] getFileUnderFile(String filePath)
    {
    	File fileDir = new File(filePath);
    	String fileName[] = null;
        if(fileDir.isDirectory())
        {
        	fileName= fileDir.list();
        }
        return fileName;
    }
    public void getClockTime(String time)
    {
    	time = time.trim();
    	if("0.0".equals(time))
    		return;
    	else if("0.101".equals(time))
    	{
    		this.clock = 1;
    		//System.out.println("here");
    	}
    	else
    		this.clock+=1;
    	
    	/*int point = time.indexOf(".");
    	//System.out.println(time);
    	String start = time.substring(0, point);
    	String end = time.substring(point+1, point+1+1);//[point+1,point+1+1)
    	int result = 0;
    	result=Integer.parseInt(end)+(Integer.parseInt(start))*10;//小数部分
    	
    	//System.out.println(result);
    	this.clock = result;*/
    }
    private int countCacheinClock1 = 0;
    private int countBlock1inClock1 = 0;
    private int countBlock2inClock1 = 0;
    private int countBlock3inClock1 = 0;
    private int countBlock4inClock1 = 0;
    private int countDiscardinClock1 = 0;
    private int countPauseinClock1 = 0;
    private double burstyClock1 = 0;
    
    
    private boolean flag1 = true;
    private boolean flag2 = true;
    private boolean flag3 = true;
    public void storeBasicInfo(String qName,double value)
    {
    	if("capacity".equals(qName))
    	{
    		if(clock==1&&flag1)
            {
         	  capacityList.add(value);
         	  flag1 = false;
            }
            else if(clock>1)
            {
            	 capacityList.add(value);
            }
    	}
    	else   if("bursty".equals(qName))
    	{
    		if(clock==1&&flag2)
            {
         	  burstyList.add(value);
         	  flag2 = false;
            }
            else if(clock>1)
            {
            	 burstyList.add(value);
            }
    	}
    	else   if("acm".equals(qName))
    	{
    		if(clock==1&&flag3)
            {
         	  acmList.add(value);
         	  flag3 = false;
            }
            else if(clock>1)
            {
            	 acmList.add(value);
            }
    	}
    }
    
    
    
    
    
    public void storeElement(String qName,int totalCount)
    {

    	
    	if("cache".equals(qName))
    	{
           if(clock==1)
           {
        	   countCacheinClock1+=totalCount;
           }
           else if(clock==2)
           {
        	   this.excList.add(countCacheinClock1);
        	   this.excList.add(totalCount);
        	   countCacheinClock1 = 0;
           }
           else
           {
        	   this.excList.add(totalCount);
           }
    	}
    	else if("block1".equals(qName))
    	{
            if(clock==1)
            {
            	countBlock1inClock1+=totalCount;
            }
            else if(clock==2)
            {
         	   this.Block1List.add(countBlock1inClock1);
         	   this.Block1List.add(totalCount);
         	  countBlock1inClock1 = 0;
            }
            else
            {
         	   this.Block1List.add(totalCount);
            }
    	}
    	else if("block2".equals(qName))
    	{
            if(clock==1)
            {
            	countBlock2inClock1+=totalCount;
            }
            else if(clock==2)
            {
         	   this.Block2List.add(countBlock2inClock1);
         	   this.Block2List.add(totalCount);
         	  countBlock2inClock1 = 0;
            }
            else
            {
         	   this.Block2List.add(totalCount);
            }
    	}
    	else if("block3".equals(qName))
    	{
            if(clock==1)
            {
            	countBlock3inClock1+=totalCount;
            }
            else if(clock==2)
            {
         	   this.Block3List.add(countBlock3inClock1);
         	   this.Block3List.add(totalCount);
         	  countBlock3inClock1 = 0;
            }
            else
            {
         	   this.Block3List.add(totalCount);
            }
    	}
    	else if("block4".equals(qName))
    	{
            if(clock==1)
            {
            	countBlock4inClock1+=totalCount;
            }
            else if(clock==2)
            {
         	   this.Block4List.add(countBlock4inClock1);
         	   this.Block4List.add(totalCount);
         	  countBlock4inClock1 = 0;
            }
            else
            {
         	   this.Block4List.add(totalCount);
            }
    	}
    	else if("discardList".equals(qName))
    	{
            if(clock==1)
            {
            	countDiscardinClock1+=totalCount;
            }
            else if(clock==2)
            {
         	   this.discardList.add(countDiscardinClock1);
         	   this.discardList.add(totalCount);
         	  countDiscardinClock1 = 0;
            }
            else
            {
         	   this.discardList.add(totalCount);
            }
    	}
    	else if("pause".equals(qName))
    	{
            if(clock==1)
            {
            	countPauseinClock1+=totalCount;
            }
            else if(clock==2)
            {
         	   this.PauseList.add(countPauseinClock1);
         	   this.PauseList.add(totalCount);
         	  countPauseinClock1 = 0;
            }
            else
            {
         	   this.PauseList.add(totalCount);
            }
    	}
    }
    public void getStorageCondition(String fileName)
    {
    	try{
    	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
    	String temp = null;
    	int i = 0;
    	while((temp = br.readLine())!=null)
    	{
    		//System.out.println("---------------"+i);
    		if(temp.indexOf("clock", 0)!=-1) //包含clock信息
    		{
    			int start = temp.indexOf("#");
    			int end = temp.lastIndexOf("#");
    			String time = temp.substring(start+1, end);//[start+1,end)
    			//System.out.println("time"+time);
    			getClockTime(time);
    		}
    		else 
    		{
    			temp = temp.trim();
    			if(temp.startsWith("#"))
    			{
    				//获取队列名称
    				int QPoint = temp.indexOf(":");
    				String qName = temp.substring(1,QPoint);//获取队列名称
    				int total = Integer.parseInt(temp.substring(temp.lastIndexOf(":")+1,temp.length()));//获取队列中元素个数
    				this.storeElement(qName, total);
    				System.out.println(qName+"  "+ total);
    			}
    		}
    		i++;
    	}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    public void writeBasicInfo(String filePath)
    {
    	WRFile wrFile = new WRFile();
    	wrFile.writeListInExcel(this.capacityList, filePath+"capacity.xlsx");
    	wrFile.writeListInExcel(this.burstyList, filePath+"bursty.xlsx");
    	wrFile.writeListInExcel(this.acmList, filePath+"acm.xlsx");
    	this.capacityList.clear();
    	this.burstyList.clear();
    }
    public void writeQueueInfoIntoFile(String filePath)
    {
    	WRFile wrFile = new WRFile();
    	wrFile.writeListInExcel(this.excList, "/"+filePath+"/exc.xlsx");
    	this.excList.clear();
    	if(this.PauseList.size()>0)
    	{
    		wrFile.writeListInExcel(this.PauseList, "/"+filePath+"/pause.xlsx");
    		this.PauseList.clear();
    		return;
    	}
    	wrFile.writeListInExcel(this.Block1List, "/"+filePath+"/block1.xlsx");
    	wrFile.writeListInExcel(this.Block2List, "/"+filePath+"/block2.xlsx");
    	wrFile.writeListInExcel(this.Block3List,  "/"+filePath+"/block3.xlsx");
    	wrFile.writeListInExcel(this.Block4List,  "/"+filePath+"/block4.xlsx");
    	wrFile.writeListInExcel(this.discardList,  "/"+filePath+"/discard.xlsx");  
    	this.excList.clear();
    	this.Block1List.clear();
    	this.Block2List.clear();
    	this.Block3List.clear();
    	this.Block4List.clear();
    	this.discardList.clear();
    }
    public void getBasicInfo(String fileName)
    {
    	try{
        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        	String temp = null;
        	int i = 0;
        	while((temp = br.readLine())!=null)
        	{
        		//System.out.println("---------------"+i);
        		if(temp.indexOf("clock", 0)!=-1) //包含clock信息
        		{
        			int start = temp.indexOf("#");
        			int end = temp.lastIndexOf("#");
        			String time = temp.substring(start+1, end);//[start+1,end)
        			//System.out.println("time"+time);
        			getClockTime(time);
        		}
        		else 
        		{
        			temp = temp.trim();
        			if(temp.indexOf("capacity")>0)
        			{
        				//获取队列名称
        				int start0 = temp.indexOf("|");
        				int end0 = temp.lastIndexOf("|");
        				String qName = temp.substring(start0+1,end0).trim();//获取队列名称
        				double capacity0 = Double.parseDouble(qName);//获取队列中元素个数
        				this.storeBasicInfo("capacity", capacity0);
        				
        			}
        			else if(temp.indexOf("bursty")>0)
        			{
        				int start0 = temp.indexOf("|");
        				int end0 = temp.lastIndexOf("|");
        				String qName = temp.substring(start0+1,end0).trim();//获取队列名称
        				double bursty0 = Double.parseDouble(qName);//获取队列中元素个数
        				this.storeBasicInfo("bursty", bursty0);
        			}
        			else if(temp.indexOf("acm")>0)
        			{
        				int start0 = temp.indexOf("|");
        				int end0 = temp.lastIndexOf("|");
        				String qName = temp.substring(start0+1,end0).trim();//获取队列名称
        				double bursty0 = Double.parseDouble(qName);//获取队列中元素个数
        				this.storeBasicInfo("acm", bursty0);
        			}
        		}
        		i++;
        	}
        	}catch(Exception e)
        	{
        		e.printStackTrace();
        	}
    	
    }
	public static void main(String[] args) {
		AnalyzeVMLog  analyzer = new AnalyzeVMLog();
		String fileFolder[] = {"MATBM"};
		//,"minus20","minus30","minus40"
		
		//System.out.println(fileNameList[0]);
		for(String folder:fileFolder)
		{
		 String filePath = "D:/cloudsim/log/"+folder+"/Vm";
		 String fileNameList[] = analyzer.getFileUnderFile(filePath);
        for(String fileName:fileNameList)
        {
        	if(fileName.indexOf(".txt")>-1)
        	{
        	analyzer.getStorageCondition(filePath+"/"+fileName);

        	String  fileNameWithoutSuffix = fileName.substring(0, fileName.indexOf(".")).trim();
        	String objFileName = filePath+"/"+fileNameWithoutSuffix;
        	System.out.println(objFileName);
        	analyzer.writeQueueInfoIntoFile(objFileName);	//存在一个文件夹里
        	//analyzer.writeBasicInfo(objFileName);
        	}
        }
		}
	}

}
