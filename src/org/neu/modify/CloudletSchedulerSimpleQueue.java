package org.neu.modify;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Consts;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.PrintFile;

public class CloudletSchedulerSimpleQueue extends CloudletScheduler{

	private List<ResCloudlet> cloudletExecList;
	private List<ResCloudlet> discardList;
	private int LENGTH_SQ = 10;
	/**  ��־�ļ�������  **/
	private String logFileName = "";
	private List<Double> currentMipsShare;
	/** The cloudlet paused list. */
	private List<ResCloudlet> cloudletPausedList;

	/** The cloudlet finished list. */
	private List< ResCloudlet> cloudletFinishedList;
	
	
	public List<ResCloudlet> getCloudletFinishedList() {
		return cloudletFinishedList;
	}

	public void setCloudletFinishedList(List<ResCloudlet> cloudletFinishedList) {
		this.cloudletFinishedList = cloudletFinishedList;
	}

	public CloudletSchedulerSimpleQueue()
	{
		super();
		cloudletExecList = new ArrayList<ResCloudlet>();
		discardList = new ArrayList<ResCloudlet>();
	}
	
    public double SQSubmit(Cloudlet cl)
    {
		//������Ҫ���ֶ��ָ��˼��
		int count_cloudlet = this.getCloudletExecList().size(); //��ȡcloudlet�еĸ���
		double finishTime = CloudSim.CLOUDLET_SUPPLY_PEROID;
		   if(count_cloudlet<this.LENGTH_SQ)
		   {
			   finishTime = cloudletSubmit(cl); //����ִ��ʱ��
		   }
		   else
		   {
			  cloudletCancel(cl);
			   
		   }
		return finishTime;
    }
	public 	double cloudletListSubmit(List<Cloudlet> list) {
	
		return 0;
	}

	@Override
	public double updateVmProcessing(double currentTime, List<Double> mipsShare) {

		setCurrentMipsShare(mipsShare);
	
        PrintFile.addtoVMFile("/********************************clock  #"+currentTime+"#************************/"+"\n", this.getLogFileName());
		double timeSpam = currentTime - getPreviousTime(); //�����current���ǵ�ǰ��clockʱ�䡣
        if(currentTime == getPreviousTime())
		{
			//System.out.println("scheduler�� �˹�����ʱ��");
			timeSpam = CloudSim.CLOUDLET_SUPPLY_PEROID;
			
		}  		
        if(getCloudletExecList().size()==0)
            {
            	setPreviousTime(currentTime);
            	return 0.0;
            }   
        
		for (ResCloudlet rcl : getCloudletExecList()) {
			
			long instruction= (long) (getCapacity(mipsShare) * timeSpam * rcl.getNumberOfPes() * Consts.MILLION);
			
			rcl.updateCloudletFinishedSoFar(instruction);
		}
		
		
		// check finished cloudlets
		double nextEvent = Double.MAX_VALUE;
		
		List<ResCloudlet> toRemove = new ArrayList<ResCloudlet>();
		for (ResCloudlet rcl : getCloudletExecList()) {
			long remainingLength = rcl.getRemainingCloudletLength();
			
			//System.out.println("--------ʣ��ָ����Ŀ-----" +remainingLength);
			if (remainingLength == 0) {// finished: remove from the list
				toRemove.add(rcl);
				cloudletFinish(rcl);
				continue;
			}
				
		}
		
		getCloudletExecList().removeAll(toRemove);//�Ѿ���ɵ�cloudlet��
		
	
		// estimate finish time of cloudlets
		for (ResCloudlet rcl : getCloudletExecList()) {
			double estimatedFinishTime = currentTime
					+ (rcl.getRemainingCloudletLength() / (getCapacity(mipsShare) * rcl.getNumberOfPes()));
			if (estimatedFinishTime - currentTime < CloudSim.getMinTimeBetweenEvents()) {
				estimatedFinishTime = currentTime + CloudSim.getMinTimeBetweenEvents();
			}

			if (estimatedFinishTime < nextEvent) {
				nextEvent = estimatedFinishTime;//��̵����ʱ��
			}
		}
		setPreviousTime(currentTime);
		return nextEvent;//�������ֵ��������ʲô��
	}
	@Override
	public double cloudletSubmit(Cloudlet cl) {
		
		return cloudletSubmit(cl, 0.0);
	}

	
	public void cloudletCancel(Cloudlet cloudlet) {
		ResCloudlet rcl = new ResCloudlet(cloudlet);
           this.discardList.add(rcl);
	}
	
	@Override
	public Cloudlet cloudletCancel(int cloudlet) {
        return null;
	}

	/**
	 * Pauses execution of a cloudlet.
	 * 
	 * @param cloudletId ID of the cloudlet being paused
	 * @return $true if cloudlet paused, $false otherwise
	 * @pre $none
	 * @post $none
	 */
	@Override
	public boolean cloudletPause(int cloudletId) {
		return false;
	}

	/**
	 * Processes a finished cloudlet.
	 * 
	 * @param rcl finished cloudlet
	 * @pre rgl != $null
	 * @post $none
	 */
	@Override
	public void cloudletFinish(ResCloudlet rcl) {
		rcl.setCloudletStatus(Cloudlet.SUCCESS);
		rcl.finalizeCloudlet();
		getCloudletFinishedList().add(rcl);
	}

	/**
	 * Resumes execution of a paused cloudlet.
	 * 
	 * @param cloudletId ID of the cloudlet being resumed
	 * @return expected finish time of the cloudlet, 0.0 if queued
	 * @pre $none
	 * @post $none
	 */
	@Override
	public double cloudletResume(int cloudletId) {

		return 0.0;
	}

	@Override
	public int getCloudletStatus(int clId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFinishedCloudlets() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cloudlet getNextFinishedCloudlet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int runningCloudlets() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cloudlet migrateCloudlet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getTotalUtilizationOfCpu(double time) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Double> getCurrentRequestedMips() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getTotalCurrentAvailableMipsForCloudlet(ResCloudlet rcl, List<Double> mipsShare) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTotalCurrentRequestedMipsForCloudlet(ResCloudlet rcl, double time) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTotalCurrentAllocatedMipsForCloudlet(ResCloudlet rcl, double time) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrentRequestedUtilizationOfRam() {
		// TODO Auto-generated method stub
		return 0;
	}
    
	
	public double getCurrentRequestedUtilizationOfBw() {
		// TODO Auto-generated method stub
		return 0;
	}
	public List<ResCloudlet> getCloudletExecList() {
		return cloudletExecList;
	}
	public void setCloudletExecList(List<ResCloudlet> cloudletExecList) {
		this.cloudletExecList = cloudletExecList;
	}
	public List<ResCloudlet> getCloudletDiscardList() {
		return discardList;
	}
	public void setCloudletDiscardList(List<ResCloudlet> cloudletDiscardList) {
		this.discardList = cloudletDiscardList;
	}
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public String getLogFileName() {
		return logFileName;
	}
	protected double getCapacity(List<Double> mipsShare) {
		double capacity = 0.0;
		int cpus = 0;
		for (Double mips : mipsShare) {
			capacity += mips; //��ǰVM��ȡ����mips
			if (mips > 0.0) {
				cpus++;
			}
		}		
		capacity/=(this.LENGTH_SQ/CloudSim.m);
		return capacity;
		
	}
	public double cloudletSubmit(Cloudlet cloudlet, double fileTransferTime) {
		ResCloudlet rcl = new ResCloudlet(cloudlet);
		rcl.setCloudletStatus(Cloudlet.INEXEC);
		for (int i = 0; i < cloudlet.getNumberOfPes(); i++) {
			rcl.setMachineAndPeId(0, i);//����ƽ�����䡣�������pe��id���ǰ���������ô��
		}
		this.getCloudletExecList().add(rcl);
		// use the current capacity to estimate the extra amount of
		// time to file transferring. It must be added to the cloudlet length
		double extraSize = getCapacity(getCurrentMipsShare()) * fileTransferTime;
		long length = (long) (cloudlet.getCloudletLength() + extraSize);
		cloudlet.setCloudletLength(length);
		double  finishTime = cloudlet.getCloudletLength() / getCapacity(getCurrentMipsShare());
		//System.out.println("Ԥ�����ʱ��"+finishTime);
		return finishTime; //����cloudletԤ�Ƶ����ʱ�䡣	
	}
	public double getTotalCapacity(List<Double> mipsList)
	    {
	    	double total = 0;
	    	for(Double mips: mipsList)
	    	{
	    		total+=mips.doubleValue();
	    	}
	    	return total;
	    }
	private void setlength_SQ(List<Double> currentMipsShare)
	{
       if(this.LENGTH_SQ!=10)
       {
    	   return;
       }
		double capacity = this.getTotalCapacity(currentMipsShare);
		//System.out.println("������   "+capacity);
		int q = (int)(CloudSim.CLOUDLET_SUPPLY_PEROID*capacity/CloudSim.CLOUDLET_INSTRUCTION_LEN);
		System.out.println("q is          "+q);
		if(q==0)
		{
			System.out.println("ATBM���г��ȼ���Ϊ0 �������쳣");
		}
		else
		{
			this.LENGTH_SQ = q*CloudSim.m;
			CloudSim.VM_LENGTH_ATBM = q*CloudSim.m;
			System.out.println("ATBM���г��ȼ���Ϊ  "+q);
		}
	}
	public void setCurrentMipsShare(List<Double> currentMipsShare) {
		this.currentMipsShare = currentMipsShare;
		this.setlength_SQ(currentMipsShare);
		
	}
}
