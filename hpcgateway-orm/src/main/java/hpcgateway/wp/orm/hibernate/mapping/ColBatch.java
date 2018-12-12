package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ColBatch generated by hbm2java
 */
public class ColBatch implements java.io.Serializable
{

	private Long colBatchId;
	private HpcCluster hpcCluster;
	private String colBatchName;
	private Date colBatchTime;
	private String colBatchOperator;
	private int colBatchTotal;
	private int colBatchIdle;
	private int colBatchCollecting;
	private int colBatchSuccess;
	private int colBatchError;
	private String colBatchState;
	private String colBatchMessage;
	private Set colInfos = new HashSet(0);

	public ColBatch()
	{
	}

	public ColBatch(String colBatchName, Date colBatchTime, String colBatchOperator, int colBatchTotal, int colBatchIdle, int colBatchCollecting, int colBatchSuccess, int colBatchError, String colBatchState, String colBatchMessage)
	{
		this.colBatchName = colBatchName;
		this.colBatchTime = colBatchTime;
		this.colBatchOperator = colBatchOperator;
		this.colBatchTotal = colBatchTotal;
		this.colBatchIdle = colBatchIdle;
		this.colBatchCollecting = colBatchCollecting;
		this.colBatchSuccess = colBatchSuccess;
		this.colBatchError = colBatchError;
		this.colBatchState = colBatchState;
		this.colBatchMessage = colBatchMessage;
	}

	public ColBatch(HpcCluster hpcCluster, String colBatchName, Date colBatchTime, String colBatchOperator, int colBatchTotal, int colBatchIdle, int colBatchCollecting, int colBatchSuccess, int colBatchError, String colBatchState, String colBatchMessage, Set colInfos)
	{
		this.hpcCluster = hpcCluster;
		this.colBatchName = colBatchName;
		this.colBatchTime = colBatchTime;
		this.colBatchOperator = colBatchOperator;
		this.colBatchTotal = colBatchTotal;
		this.colBatchIdle = colBatchIdle;
		this.colBatchCollecting = colBatchCollecting;
		this.colBatchSuccess = colBatchSuccess;
		this.colBatchError = colBatchError;
		this.colBatchState = colBatchState;
		this.colBatchMessage = colBatchMessage;
		this.colInfos = colInfos;
	}

	public Long getColBatchId()
	{
		return this.colBatchId;
	}

	public void setColBatchId(Long colBatchId)
	{
		this.colBatchId = colBatchId;
	}

	public HpcCluster getHpcCluster()
	{
		return this.hpcCluster;
	}

	public void setHpcCluster(HpcCluster hpcCluster)
	{
		this.hpcCluster = hpcCluster;
	}

	public String getColBatchName()
	{
		return this.colBatchName;
	}

	public void setColBatchName(String colBatchName)
	{
		this.colBatchName = colBatchName;
	}

	public Date getColBatchTime()
	{
		return this.colBatchTime;
	}

	public void setColBatchTime(Date colBatchTime)
	{
		this.colBatchTime = colBatchTime;
	}

	public String getColBatchOperator()
	{
		return this.colBatchOperator;
	}

	public void setColBatchOperator(String colBatchOperator)
	{
		this.colBatchOperator = colBatchOperator;
	}

	public int getColBatchTotal()
	{
		return this.colBatchTotal;
	}

	public void setColBatchTotal(int colBatchTotal)
	{
		this.colBatchTotal = colBatchTotal;
	}

	public int getColBatchIdle()
	{
		return this.colBatchIdle;
	}

	public void setColBatchIdle(int colBatchIdle)
	{
		this.colBatchIdle = colBatchIdle;
	}

	public int getColBatchCollecting()
	{
		return this.colBatchCollecting;
	}

	public void setColBatchCollecting(int colBatchCollecting)
	{
		this.colBatchCollecting = colBatchCollecting;
	}

	public int getColBatchSuccess()
	{
		return this.colBatchSuccess;
	}

	public void setColBatchSuccess(int colBatchSuccess)
	{
		this.colBatchSuccess = colBatchSuccess;
	}

	public int getColBatchError()
	{
		return this.colBatchError;
	}

	public void setColBatchError(int colBatchError)
	{
		this.colBatchError = colBatchError;
	}

	public String getColBatchState()
	{
		return this.colBatchState;
	}

	public void setColBatchState(String colBatchState)
	{
		this.colBatchState = colBatchState;
	}

	public String getColBatchMessage()
	{
		return this.colBatchMessage;
	}

	public void setColBatchMessage(String colBatchMessage)
	{
		this.colBatchMessage = colBatchMessage;
	}

	public Set getColInfos()
	{
		return this.colInfos;
	}

	public void setColInfos(Set colInfos)
	{
		this.colInfos = colInfos;
	}

}