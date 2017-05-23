package org.neu.core;

/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.PrintFile;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.neu.util.WRFile;

/**
 * A simple example showing how to create a datacenter with one host and run one
 * cloudlet on it.
 */
public class BurstyCoping {

	/** The cloudlet list. */
	public static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;
	public static int pesNumber = 1;
	//public static int vmid = 0;
	public static int brokerId;
	public static DatacenterBroker broker;
    public static int cloudletCounts[];
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
   

		Log.printLine("Starting CloudSimExample1...");

		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			Datacenter datacenter0 = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			broker = createBroker();
			brokerId = broker.getId();
		

			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();

			// VM description
			
			int mips = CloudSim.VM_MIPS;
			long size = 10000; // image size (MB)
			int ram = 512; // vm memory (MB)
			long bw = 1000;
			//int pesNumber = 1; // number of cpus
			String vmm = "Xen"; // VMM name
			
			//int pesNumber = 1;

			// create VM
			for(int i = 0;i<CloudSim.VM_INIT_COUNT;i++)
			{
			Vm vm = new Vm(i, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			vmlist.add(vm);
			}
			// add the VM to the vmList
			

			// submit vm list to the broker
			broker.submitVmList(vmlist);

			// Fifth step: Create one Cloudlet
			// Sixth step: Starts the simulation
			//for(int i = 0; i < 3; i++){
			CloudSim.startSimulation();	
			CloudSim.stopSimulation();
			//	CloudSim.init(num_user, calendar, trace_flag);
			//	try{
			//		Thread.sleep(1000);
			//	}catch(Exception e){
			//		e.getStackTrace();
			//	}
			//}
		

			

			//Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			String rejectedFile = "reject.xlsx";
			analyzeRejected(newList,rejectedFile);
			String cloudletFile =  "cloudlet.xlsx";
	        writeCloudletList(newList,cloudletFile);
			Log.printLine("CloudSimExample1 finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}

	private static Datacenter createDatacenter(String name) {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.

		
		// of machines
		int hostId = 0;
		int ram = 2048; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 10000;
        for(int j = 0;j<CloudSim.HOST_COUNT;j++)
        {
    		List<Pe> peList = new ArrayList<Pe>();
    		// 3. Create PEs and add these into a list.
    		for(int i = 0;i<CloudSim.HOST_PES_COUNT;i++)
    		{
    			peList.add(new Pe(i, new PeProvisionerSimple(CloudSim.HOST_MIPS))); // need to store Pe id and MIPS Rating
    		}
    		hostList.add(
    				new Host(
    					j,
    					new RamProvisionerSimple(ram),
    					new BwProvisionerSimple(bw),
    					storage,
    					peList,
    					new VmSchedulerTimeShared(peList)
    				)
    			);
       }
 // This is our machine

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a data center: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/Pe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 *
	 * @param list list of Cloudlets
	 */
	private  double[] analyzeRejected(List<Cloudlet> list,String filePath)
	{
		double[] result = new double[3];
		Cloudlet cloudlet;
		String indent = "    ";
		double start;
		double finish;
		double temp = 0;
		double count = 0;
		List<Cloudlet> cloudletIdList = new ArrayList<Cloudlet>();
		List<Cloudlet> discardList = new ArrayList<Cloudlet>();
	    double discardCount = 0;
	    double totalRPT = 0;
	    double successRT = 0;
		for (int i = 0; i < list.size(); i++) {
			cloudlet = list.get(i);
			finish = cloudlet.getFinishTime();
			start = cloudlet.getSubmissionTime();
            temp= finish-start;
            if(temp>CloudSim.RESPONSE_TIME_LIMIT)
            {
            	count++;
            	cloudletIdList.add(cloudlet);
            }	
            if(Cloudlet.CANCELED==cloudlet.getCloudletStatus())
            {
            	discardCount++;
            	discardList.add(cloudlet);
            }
            else 
            {
            	totalRPT+=temp;
            	successRT++;
            }
            
		}	

		//WRFile wrFile = new WRFile();  
		String violateFolder = "violatecloudlet";
		WRFile wrFile = new WRFile();
		wrFile.writeViolateCloudlets(cloudletIdList, filePath.replaceAll("reject", "violate"),violateFolder);
		String msg = "day"+broker.day+"--->"+"违例率   "+count/list.size() +"  拒绝率   "+discardCount/list.size()+ "  平均响应时间"+totalRPT/successRT;
		
		String rejectFolder = "rejectcloudlet";
		String rejectFileName = "";
		if(CloudSim.CURRENT_PATTERN==CloudSimTags.PROCESS_SIMPLE_QUEUE)
		{
			rejectFileName = "SQ"+broker.day;
		}
		else if(CloudSim.CURRENT_PATTERN==CloudSimTags.PROCESS_ATBM)
		{
			rejectFileName = "ATBM"+broker.day;
		}
		wrFile.writeRejectCloudlets(discardList, filePath,rejectFolder);
		
		System.out.println("day is"+broker.day);
		result[0] = count/list.size();
		result[1] = discardCount/list.size();
		result[2] = totalRPT/successRT;
		
		
		String fileName = "";
		String processEffectfolder = "";
		
		if(CloudSim.CURRENT_PATTERN==CloudSimTags.PROCESS_SIMPLE_QUEUE)
		{
			fileName = "SQ"+broker.day;
			processEffectfolder = "SQPE";
		}
		else if(CloudSim.CURRENT_PATTERN==CloudSimTags.PROCESS_ATBM)
		{
			fileName = "ATBM"+broker.day;
			processEffectfolder = "ATBMPE";
		}
		
		PrintFile.addInfoToFile(msg, fileName, processEffectfolder);
		return result;
	}
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;
        System.out.println("返回的总的请求数 "+size);
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ indent +"arrival time"+ indent + "CPU time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");}
			else if(cloudlet.getCloudletStatus() == Cloudlet.CANCELED) {
				Log.print("CANCEL");}

				Log.printLine(
						indent + indent
						+ dft.format(cloudlet.getSubmissionTime())
						+ indent + indent
						
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			
		}
	}
	private static void writeCloudletList(List<Cloudlet> list,String filePath)
	{
		String folder = "Cloudlet";
		WRFile wrFile = new WRFile();
		wrFile.writeCloudletListIntoExcel(list, filePath,folder);
	}

}
