package org.neu.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
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
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class CloudSimExample2 {
    /**
     * 这个例子中Datacenter包含2个host，
     * 有两个具有相同指令请求的cloudlet运行在这2个host中，
     * 它们在同一时间完成处理
     *1、有几个VM
     */
	private static List<Cloudlet> cloudletlist;
	private static List<Vm> vmList;
	public static void main(String[] args) {
	Log.printLine("starting the exmaple2");
	try{
	int num_user = 1;
	Calendar canlendar = Calendar.getInstance();
	boolean traceFlag = false; 
	CloudSim.init(num_user, canlendar, traceFlag);
	
	Datacenter datacenter0 = createDatacenter("DC_1");
	Datacenter datacenter1 = createDatacenter("DC_2");
	DatacenterBroker broker = createBroker();	
	
	int brokerId = broker.getId();
	vmList = new ArrayList<Vm>();
	
	int vmid = 0;
	int mips = 150;
	long size = 10000;
	int ram = 512; //vm memory (MB)
	long bw = 1000;
	int pesNumber = 1; //number of cpus
	String vmm = "Xen"; //VMM name
	
	Vm vm1 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
	vmid++;
	//Vm vm2 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
	vmList.add(vm1);
	//vmList.add(vm2);
	
	broker.submitVmList(vmList);
	
	cloudletlist = new ArrayList<Cloudlet>();
	int id = 0;
	long length = 250000;
	long fileSize = 300;
	long outputSize = 300;
	UtilizationModel utilizationModel = new UtilizationModelFull();

	Cloudlet cloudlet1 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	cloudlet1.setUserId(brokerId);

	id++;
	Cloudlet cloudlet2 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	cloudlet2.setUserId(brokerId);
	// Sets the user or owner ID of this Cloudlet. It is VERY important to set the user ID,
	//otherwise this Cloudlet will not be executed in a CloudResource.

	//add the cloudlets to the list
	cloudletlist.add(cloudlet1);
	cloudletlist.add(cloudlet2);	
	
	//submit cloudlet list to the broker
	Log.printLine("cloudletid"+cloudlet1.getCloudletId()+"  vmid "+vm1.getId());
	broker.submitCloudletList(cloudletlist);
	
	//broker.bindCloudletToVm(cloudlet1.getCloudletId(),vm1.getId());
	//broker.bindCloudletToVm(cloudlet2.getCloudletId(),vm2.getId());
	// Sixth step: Starts the simulation
	CloudSim.startSimulation();


	// Final step: Print results when simulation is over
	List<Cloudlet> newList = broker.getCloudletReceivedList();

	CloudSim.stopSimulation();

	printCloudletList(newList);

	Log.printLine("CloudSimExample2 finished!");	
	}catch(Exception e)
	{
		e.printStackTrace();
		Log.printLine("exmaple2 has unexpected errors");
	}
	

	}
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try{
			broker = new DatacenterBroker("wj_Broker");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return broker;
	}
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet; 
		String indent = "       " ;
		Log.printLine();
		Log.printLine("-------------output------------------");
		Log.printLine("Cloudlet ID"+indent +"STATUS"+indent
				+"Data center ID"+indent+"VM ID"+indent+"Time"+indent+"Start Time"+indent+"Finisth Time");
		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0;i<size;i++)
		{
			cloudlet = list.get(i);
			String[] res = cloudlet.getAllResourceName();
			Log.print(indent+ cloudlet.getCloudletId()+indent+indent);
			if (cloudlet.getCloudletStatus()==cloudlet.SUCCESS)
			{

				Log.print("SUCCESS");
				Log.printLine(indent+indent+cloudlet.getReservationId()
				+indent+indent+cloudlet.getVmId()
				+indent+indent+dft.format(cloudlet.getActualCPUTime())
				+indent+indent+dft.format(cloudlet.getExecStartTime())
				+indent+indent+dft.format(cloudlet.getFinishTime())); //这个id指的就是Data Center ID(保留地),其中ActualCpuTime和StartTIME有什么区别
				
			}
					
		}
		
	}
	private static Datacenter createDatacenter(String DCName)
	{
		Datacenter dc = null;
		
		//创建DC中的host
		int hostNum = 2;
		List<Host> hostList = new ArrayList<Host>();
		//定义host的属性
		int hostId = 0;
		int ram = 2048;
		int storage = 1000000;
		int pes  = 1;
		int mips = 300;
		int bw = 10000;
		List<Pe> peList = new ArrayList<Pe>();
		
		for(int i = 0;i<pes;i++)
		{
			peList.add(new Pe(i,new PeProvisionerSimple(mips)));//
		}
	    
		hostList.add(new Host(hostId,
				new RamProvisionerSimple(ram),
		        new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList)
				));
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.001;	// the cost of using storage in this resource
        double costPerBw = 0.0;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            dc = new Datacenter(DCName, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
            //Log.printLine("************ID of DataCenter is "+ dc.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return dc;
	}

}
