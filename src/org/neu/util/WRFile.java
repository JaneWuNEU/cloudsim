package org.neu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.PrintFile;

public class WRFile {
	public int[] readExcel(String filePath,String sheetName){
		
		File POIExcelFile = new File(filePath);
		try {
			InputStream inputStream = new FileInputStream(POIExcelFile);
			
			XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
			//创建sheet
			XSSFSheet sheet = workBook.getSheet(sheetName);
			int firstRowNum = sheet.getFirstRowNum(); // 第一行的行号
	        int lastRowNum = sheet.getLastRowNum(); // 最后一行的行号
	        int[] temp = new int[lastRowNum+1];//读取表中数据并存入数组
	        int i = 0;
	        // 循环遍历每一个单元格
	        XSSFRow eachRow = null;
	        XSSFCell cell = null;
	       // System.out.println(CloudSim.WORKLOAD_SUBMIT_TIMES);
	        for (int row = firstRowNum; row <=lastRowNum; row++) {
	        	eachRow = sheet.getRow(row); // 每一行
	        	 // 第一列的列号
	        	cell = eachRow.getCell(0);	
	        	temp[i ++] = (int)(Math.ceil(cell.getNumericCellValue()));
	        }
	        workBook.close();
	        inputStream.close();
			return temp;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	    
	}
	//读取Excel文件
	public int[] readExcel(String filePath,String sheetName,int cols){
		
		File POIExcelFile = new File(filePath);
		try {
			InputStream inputStream = new FileInputStream(POIExcelFile);
			
			XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
			//创建sheet
			XSSFSheet sheet = workBook.getSheet(sheetName);
			int firstRowNum = sheet.getFirstRowNum(); // 第一行的行号
	        int lastRowNum = sheet.getLastRowNum(); // 最后一行的行号
	        int[] temp = new int[lastRowNum+1];//读取表中数据并存入数组
	        int i = 0;
	        System.out.println(lastRowNum);
	        // 循环遍历每一个单元格
	        XSSFRow eachRow = null;
	        XSSFCell cell = null;
	       // System.out.println(CloudSim.WORKLOAD_SUBMIT_TIMES);
	        for (int row = firstRowNum; row <=lastRowNum; row++) {
	        	eachRow = sheet.getRow(row); // 每一行
	        	 // 第一列的列号
	        	cell = eachRow.getCell(cols);	
	        	temp[i ++] = (int)(cell.getNumericCellValue());
	        	
	        }
	        workBook.close();
	        inputStream.close();
			return temp;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	    
	}
	public void writeRejectCloudlets(List<Cloudlet> list,String filePath,String folder)
	{
		  try
	       {
		    	File parentFile = new File(PrintFile.filePath);
		    	//System.out.println(PrintFile.filePath);
	            //判断目录是否存在
		    	if(!parentFile.exists())
		    	{
		    		 parentFile.mkdirs();
		    		
		    	}
		    	String neccessaryFolder=PrintFile.filePath+"/"+folder;	
		    	File neccessaryFolderFile = new File(neccessaryFolder);
		    	
		    	String objFilePath = neccessaryFolder+"/"+filePath;
		        File objFile = new File(objFilePath);
		        
		    	 if(!neccessaryFolderFile.exists())
		    	 {
		    		 neccessaryFolderFile.mkdirs();
		    		 System.out.println(neccessaryFolder);
		    		 objFile.createNewFile();
		    	 }
	             	            
	             XSSFWorkbook wb = new XSSFWorkbook();
	             XSSFSheet sheet = wb.createSheet( "sheet");
	             int k = 0;
	             Cloudlet cloudlet = null;
	             FileOutputStream fOut = new FileOutputStream(objFilePath);
	             
	            
	             for(int i=0; i< list.size(); i++){
	            	 cloudlet = (Cloudlet)(list.get(i));
	            	 XSSFRow rows = sheet.createRow(i);

	                XSSFCell cell1 = rows.createCell(0);
	                cell1.setCellValue(1.0*cloudlet.getCloudletId());
	                XSSFCell cell2 = rows.createCell(1);
	                cell2.setCellValue(1.0*cloudlet.getSendTimePoint());

	            }
	             
	             wb.write(fOut);                        
	             // 操作结束，关闭文件
	             fOut.close(); 
	             }
		          catch(FileNotFoundException e ) {
		        	  e.printStackTrace();
		          }catch(IOException e)
		          {
		        	  e.printStackTrace();
		          }catch(NotOfficeXmlFileException e)
		          { 
		        	  e.printStackTrace();
	              }		
	}
	public void writeViolateCloudlets(List<Cloudlet> list,String filePath,String folder)
	{
	     
		    try
	       {
		    	File parentFile = new File(PrintFile.filePath);
		    	//System.out.println(PrintFile.filePath);
	            //判断目录是否存在
		    	if(!parentFile.exists())
		    	{
		    		 parentFile.mkdirs();
		    		
		    	}
		    	String neccessaryFolder=PrintFile.filePath+"/"+folder;	
		    	File neccessaryFolderFile = new File(neccessaryFolder);
		    	
		    	String objFilePath = neccessaryFolder+"/"+filePath;
		        File objFile = new File(objFilePath);
		        
		    	 if(!neccessaryFolderFile.exists())
		    	 {
		    		 neccessaryFolderFile.mkdirs();
		    		 System.out.println(neccessaryFolder);
		    		 objFile.createNewFile();
		    	 }
	             	            
	             XSSFWorkbook wb = new XSSFWorkbook();
	             XSSFSheet sheet = wb.createSheet( "sheet");
	             int k = 0;
	             Cloudlet cloudlet = null;
	             FileOutputStream fOut = new FileOutputStream(objFilePath);
	             
	            
	             for(int i=0; i< list.size(); i++){
	            	 cloudlet = (Cloudlet)(list.get(i));
	            	 XSSFRow rows = sheet.createRow(i);

	                XSSFCell cell1 = rows.createCell(0);
	                cell1.setCellValue(1.0*cloudlet.getCloudletId());
	                XSSFCell cell2 = rows.createCell(1);
	                cell2.setCellValue(1.0*cloudlet.getSendTimePoint());
	                XSSFCell cell3 = rows.createCell(2);
	                cell3.setCellValue(1.0*(cloudlet.getFinishTime()-cloudlet.getSubmissionTime()));

	            }
	             
	             wb.write(fOut);                        
	             // 操作结束，关闭文件
	             fOut.close(); 
	             }
		          catch(FileNotFoundException e ) {
		        	  e.printStackTrace();
		          }catch(IOException e)
		          {
		        	  e.printStackTrace();
		          }catch(NotOfficeXmlFileException e)
		          { 
		        	  e.printStackTrace();
	              }		
	}
	public void writeCloudletListIntoExcel(List<Cloudlet> list,String filePath,String folder)
	{
        
       //工作簿名称,可以取中文(就是另存为名称).
       
	    try
       {
	    	File parentFile = new File(PrintFile.filePath);
	    	//System.out.println(PrintFile.filePath);
            //判断目录是否存在
	    	if(!parentFile.exists())
	    	{
	    		 parentFile.mkdirs();

	    		
	    	}
	    	String neccessaryFolder=PrintFile.filePath+"/"+folder;	
	    	File neccessaryFolderFile = new File(neccessaryFolder);
	    	
	    	String objFilePath = neccessaryFolder+"/"+filePath;
	        File objFile = new File(objFilePath);
	    	 if(!neccessaryFolderFile.exists())
	    	 {
	    		 neccessaryFolderFile.mkdirs();
	    		 System.out.println(neccessaryFolder);
	    		 objFile.createNewFile();
	    	 }
             
            
             XSSFWorkbook wb = new XSSFWorkbook();
             XSSFSheet sheet = wb.createSheet( "sheet");
             int k = 0;
             Cloudlet cloudlet = null;
             FileOutputStream fOut = new FileOutputStream(objFilePath);
             
            
             for(int i=0; i< list.size(); i++){
            	 
            	cloudlet = (Cloudlet)(list.get(i));
                XSSFRow rows = sheet.createRow(i);                       
                XSSFCell cell1 = rows.createCell(k);
                cell1.setCellValue(cloudlet.getCloudletId());
                k++;
                
                XSSFCell cell2 = rows.createCell(k);
                cell2.setCellValue(cloudlet.getCloudletStatus());
                k++;
                
                XSSFCell cell3 = rows.createCell(k);
                cell3.setCellValue(cloudlet.getSubmissionTime());
                k++;
                
                XSSFCell cell0 = rows.createCell(k);
                cell0.setCellValue(1.0*cloudlet.getSendTimePoint());
                k++;
                
                
                XSSFCell cell4 = rows.createCell(k);
                cell4.setCellValue(cloudlet.getVmId());
                k++;
                
                XSSFCell cell5 = rows.createCell(k);
                cell5.setCellValue(cloudlet.getActualCPUTime());
                k++;
                
                XSSFCell cell6 = rows.createCell(k);
                cell6.setCellValue(cloudlet.getFinishTime());
                k++;
                
                
                XSSFCell cell7 = rows.createCell(k);
                cell7.setCellValue(cloudlet.getFinishTime());
               
                if(cloudlet.getFinishTime()-cloudlet.getSubmissionTime()>CloudSim.RESPONSE_TIME_LIMIT)
                {
                	cell7.setCellValue(1.0);
                	k++;
                    XSSFCell cell8 = rows.createCell(k);
                    cell8.setCellValue(cloudlet.getFinishTime()-cloudlet.getSubmissionTime());
                }
                else
                {
                	cell7.setCellValue(0.0);
                	k++;
                    XSSFCell cell8 = rows.createCell(k);
                    cell8.setCellValue(0.0);
                }
              k = 0;
            }
             
             wb.write(fOut);                        
             // 操作结束，关闭文件
             fOut.close(); 
             }
	          catch(FileNotFoundException e ) {
	        	  e.printStackTrace();
	          }catch(IOException e)
	          {
	        	  e.printStackTrace();
	          }catch(NotOfficeXmlFileException e)
	          { 
	        	  e.printStackTrace();
              }		
	}
	public void writeInExcel(double[] data1, String filePath){
		
		 //Excel 文件要存放的位置，假定在D盘JTest目录下
        
       //工作簿名称,可以取中文(就是另存为名称).
       String outputFile=filePath;	
	    try
       {

            
             File objFile = new File(outputFile);
            
             XSSFWorkbook wb = new XSSFWorkbook();
             XSSFSheet sheet = wb.createSheet( "sheet");

             FileOutputStream fOut = new FileOutputStream(filePath);
             for(int i=0; i< data1.length; i++){
                    XSSFRow rows = sheet.createRow(i);
                            //在索引0的位置创建单元格（左上端)

                        XSSFCell cell1 = rows.createCell(0);
                        cell1.setCellValue(data1[i]);

                    }

             wb.write(fOut);                        
             // 操作结束，关闭文件
             fOut.close(); 
             }
	          catch(FileNotFoundException e ) {
	        	  e.printStackTrace();
	          }catch(IOException e)
	          {
	        	  e.printStackTrace();
	          }catch(NotOfficeXmlFileException e)
	          { 
	        	  e.printStackTrace();
              }
     
}
	public void writeInExcel(int[] data1, String filePath){
		
		 //Excel 文件要存放的位置，假定在D盘JTest目录下
         
        //工作簿名称,可以取中文(就是另存为名称).
        String outputFile=filePath;	
	    try
        {

             
              File objFile = new File(outputFile);
             
              XSSFWorkbook wb = new XSSFWorkbook();
              XSSFSheet sheet = wb.createSheet( "sheet");

              FileOutputStream fOut = new FileOutputStream(filePath);
              for(int i=0; i< data1.length; i++){
                     XSSFRow rows = sheet.createRow(i);
                             //在索引0的位置创建单元格（左上端)

                         XSSFCell cell1 = rows.createCell(0);
                         cell1.setCellValue((double)data1[i]);

                     }

              wb.write(fOut);                        
              // 操作结束，关闭文件
              fOut.close(); 
              }
	          catch(FileNotFoundException e ) {
	        	  e.printStackTrace();
	          }catch(IOException e)
	          {
	        	  e.printStackTrace();
	          }catch(NotOfficeXmlFileException e)
	          { 
	        	  e.printStackTrace();
               }
      
}
	public void writeListInExcel(List data1, String filePath){
		
		 //Excel 文件要存放的位置，假定在D盘JTest目录下
        
       //工作簿名称,可以取中文(就是另存为名称).
       String outputFile=filePath;	
       String  folders = outputFile.substring(0, outputFile.lastIndexOf("/"));
       File fileFolder = new File(folders);
       if(!fileFolder.exists())
    	   fileFolder.mkdirs();
	    try
       {

            
             File objFile = new File(outputFile);
             XSSFWorkbook wb = new XSSFWorkbook();
             XSSFSheet sheet = wb.createSheet( "sheet");

             FileOutputStream fOut = new FileOutputStream(filePath);
             for(int i=0; i< data1.size(); i++){
                    XSSFRow rows = sheet.createRow(i);
                            //在索引0的位置创建单元格（左上端)

                        XSSFCell cell1 = rows.createCell(0);
                        if(data1.get(i) instanceof Double)
                            cell1.setCellValue(1.0*(double)(data1.get(i)));
                        else
                        	cell1.setCellValue(1.0*(int)(data1.get(i)));

                    }

             wb.write(fOut);                        
             // 操作结束，关闭文件
             fOut.close(); 
             }
	          catch(FileNotFoundException e ) {
	        	  e.printStackTrace();
	          }catch(IOException e)
	          {
	        	  e.printStackTrace();
	          }catch(NotOfficeXmlFileException e)
	          { 
	        	  e.printStackTrace();
              }
     
}
	public static void main(String args[])
	{
		
		//for(int i =54;i<=54;i++)
		{
		//String day = ""+i;
	    String filePath = "F:\\one\\predict\\traindata/day53_60inmin.xlsx";
	    String sheetName = "sheet";
	    WRFile wrFile = new WRFile();
	    int []data = wrFile.readExcel(filePath, sheetName);
	    wrFile.writeInExcel(data, filePath.replace("inmin", "modified"));
		}
	}
	public StoreCloudlet[] readStoreCloudlet(String filePath,String sheetName)
	{
		
		File POIExcelFile = new File(filePath);
		try {
			InputStream inputStream = new FileInputStream(POIExcelFile);
			
			XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
			//创建sheet
			XSSFSheet sheet = workBook.getSheet(sheetName);
			int firstRowNum = sheet.getFirstRowNum(); // 第一行的行号
	        int lastRowNum = sheet.getLastRowNum(); // 最后一行的行号
	        StoreCloudlet[] temp = new StoreCloudlet[lastRowNum+1];//读取表中数据并存入数组
	        int i = 0;
	        // 循环遍历每一个单元格
	        XSSFRow eachRow = null;
	        XSSFCell cell = null;
	        //System.out.println(firstRowNum);
	       // System.out.println(CloudSim.WORKLOAD_SUBMIT_TIMES);
	        for (int row = firstRowNum; row <=lastRowNum; row++) {
	        	eachRow = sheet.getRow(row); // 每一行
	        	 // 第一列的列号
	        	temp[row] = new StoreCloudlet();
	        	temp[row].setId((int)(eachRow.getCell(0).getNumericCellValue())) ;
	        	temp[row].setStatus((int)(eachRow.getCell(1).getNumericCellValue()));
	        	temp[row].setSubmisstionTime((eachRow.getCell(2).getNumericCellValue()));
	        	temp[row].setSendTimePoint((int)(eachRow.getCell(3).getNumericCellValue()));
	        	temp[row].setVmId((int)(eachRow.getCell(4).getNumericCellValue()));
	        	temp[row].setActualCpuTime((eachRow.getCell(5).getNumericCellValue()));
	        	temp[row].setFinishTime((eachRow.getCell(6).getNumericCellValue()));
	        	temp[row].setIsViolated((int)(eachRow.getCell(7).getNumericCellValue()));
	        	temp[row].setViolateTime((eachRow.getCell(8).getNumericCellValue()));
	        }
	        workBook.close();
	        inputStream.close();
			return temp;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	    
	}
	public void writeCacheAndBlock(double[][] data,String filePath,String sheetName,int row,int cols)
	{
		//工作簿名称,可以取中文(就是另存为名称).
	       
	    try
       {
             File objFilePath = new File(filePath);
            
             XSSFWorkbook wb = new XSSFWorkbook();
             XSSFSheet sheet = wb.createSheet( "sheet");
             int k = 0;
             StoreCloudlet cloudlet = null;
             FileOutputStream fOut = new FileOutputStream(objFilePath);
             
            
             for(int i=0; i< row; i++){
            	 
                XSSFRow rows = sheet.createRow(i); 
                for(int j= 0;j<cols;j++)
                {
                XSSFCell cell1 = rows.createCell(j);
                cell1.setCellValue(1.0*data[i][j]);
                }
                } 
             wb.write(fOut);                        
             // 操作结束，关闭文件
             fOut.close(); 
             }
	          catch(FileNotFoundException e ) {
	        	  e.printStackTrace();
	          }catch(IOException e)
	          {
	        	  e.printStackTrace();
	          }catch(NotOfficeXmlFileException e)
	          { 
	        	  e.printStackTrace();
              }		
	}
	public void writeStoreClooudlet(List<StoreCloudlet> list,String filePath,String sheetName)
	{
	     //工作簿名称,可以取中文(就是另存为名称).
	       
		    try
	       {
                 File objFilePath = new File(filePath);
	            
	             XSSFWorkbook wb = new XSSFWorkbook();
	             XSSFSheet sheet = wb.createSheet( "sheet");
	             int k = 0;
	             StoreCloudlet cloudlet = null;
	             FileOutputStream fOut = new FileOutputStream(objFilePath);
	             
	            
	             for(int i=0; i< list.size(); i++){
	            	 
	            	cloudlet = (StoreCloudlet)(list.get(i));
	                XSSFRow rows = sheet.createRow(i); 
	                 
	                XSSFCell cell1 = rows.createCell(k);
	                cell1.setCellValue(1.0*cloudlet.getId());
	                k++;
	                
	                XSSFCell cell2 = rows.createCell(k);
	                cell2.setCellValue(1.0*cloudlet.getStatus());
	                k++;
	                
	                XSSFCell cell3 = rows.createCell(k);
	                cell3.setCellValue(cloudlet.getSubmisstionTime());
	                k++;
	                
	                XSSFCell cell0 = rows.createCell(k);
	                cell0.setCellValue(1.0*cloudlet.getSendTimePoint());
	                k++;
	                
	                
	                XSSFCell cell4 = rows.createCell(k);
	                cell4.setCellValue(1.0*cloudlet.getVmId());
	                k++;
	                
	                XSSFCell cell5 = rows.createCell(k);
	                cell5.setCellValue(cloudlet.getActualCpuTime());
	                k++;
	                
	                	                
	                XSSFCell cell7 = rows.createCell(k);
	                cell7.setCellValue(cloudlet.getFinishTime());
	                 k++;
	                 XSSFCell cell8 = rows.createCell(k);
	                 cell8.setCellValue(1.0*cloudlet.getIsViolated());
	                 k++;
	                 XSSFCell cell9 = rows.createCell(k);
	                 cell9.setCellValue(1.0*cloudlet.getViolateTime());
	                 
	                 k = 0;
	                } 
	             wb.write(fOut);                        
	             // 操作结束，关闭文件
	             fOut.close(); 
	             }
		          catch(FileNotFoundException e ) {
		        	  e.printStackTrace();
		          }catch(IOException e)
		          {
		        	  e.printStackTrace();
		          }catch(NotOfficeXmlFileException e)
		          { 
		        	  e.printStackTrace();
	              }		
	}
}
