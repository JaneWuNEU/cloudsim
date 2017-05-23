package org.neu.util;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

class StoreCloudlet 
{

	private int id;
	private int status;
	private double submisstionTime;
	private int sendTimePoint;
	private int vmId;
	private double actualCpuTime;
	private double finishTime;
	private double isViolated;
	private double violateTime;
	public static final int elementCounts = 9;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getSubmisstionTime() {
		return submisstionTime;
	}
	public void setSubmisstionTime(double submisstionTime) {
		this.submisstionTime = submisstionTime;
	}
	public int getSendTimePoint() {
		return sendTimePoint;
	}
	public void setSendTimePoint(int sendTimePoint) {
		this.sendTimePoint = sendTimePoint;
	}
	public int getVmId() {
		return vmId;
	}
	public void setVmId(int vmId) {
		this.vmId = vmId;
	}
	public double getActualCpuTime() {
		return actualCpuTime;
	}
	public void setActualCpuTime(double actualCpuTime) {
		this.actualCpuTime = actualCpuTime;
	}
	public double getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(double finishTime) {
		this.finishTime = finishTime;
	}
	public double getIsViolated() {
		return isViolated;
	}
	public void setIsViolated(double isViolated) {
		this.isViolated = isViolated;
	}
	public double getViolateTime() {
		return violateTime;
	}
	public void setViolateTime(double violateTime) {
		this.violateTime = violateTime;
	}
	
}