package hpcgateway.wp.orm.hibernate.mapping;
// Generated Aug 27, 2018 1:18:52 AM by Hibernate Tools 5.3.0.Beta2

import java.math.BigDecimal;
import java.util.Date;

/**
 * HpcPayment generated by hbm2java
 */
public class HpcPayment implements java.io.Serializable
{

	private Long hpcPaymentId;
	private HpcClusterUser hpcClusterUser;
	private Date hpcPaymentTime;
	private BigDecimal hpcPaymentAmount;
	private BigDecimal hpcPaymentBalance;

	public HpcPayment()
	{
	}

	public HpcPayment(Date hpcPaymentTime, BigDecimal hpcPaymentAmount, BigDecimal hpcPaymentBalance)
	{
		this.hpcPaymentTime = hpcPaymentTime;
		this.hpcPaymentAmount = hpcPaymentAmount;
		this.hpcPaymentBalance = hpcPaymentBalance;
	}

	public HpcPayment(HpcClusterUser hpcClusterUser, Date hpcPaymentTime, BigDecimal hpcPaymentAmount, BigDecimal hpcPaymentBalance)
	{
		this.hpcClusterUser = hpcClusterUser;
		this.hpcPaymentTime = hpcPaymentTime;
		this.hpcPaymentAmount = hpcPaymentAmount;
		this.hpcPaymentBalance = hpcPaymentBalance;
	}

	public Long getHpcPaymentId()
	{
		return this.hpcPaymentId;
	}

	public void setHpcPaymentId(Long hpcPaymentId)
	{
		this.hpcPaymentId = hpcPaymentId;
	}

	public HpcClusterUser getHpcClusterUser()
	{
		return this.hpcClusterUser;
	}

	public void setHpcClusterUser(HpcClusterUser hpcClusterUser)
	{
		this.hpcClusterUser = hpcClusterUser;
	}

	public Date getHpcPaymentTime()
	{
		return this.hpcPaymentTime;
	}

	public void setHpcPaymentTime(Date hpcPaymentTime)
	{
		this.hpcPaymentTime = hpcPaymentTime;
	}

	public BigDecimal getHpcPaymentAmount()
	{
		return this.hpcPaymentAmount;
	}

	public void setHpcPaymentAmount(BigDecimal hpcPaymentAmount)
	{
		this.hpcPaymentAmount = hpcPaymentAmount;
	}

	public BigDecimal getHpcPaymentBalance()
	{
		return this.hpcPaymentBalance;
	}

	public void setHpcPaymentBalance(BigDecimal hpcPaymentBalance)
	{
		this.hpcPaymentBalance = hpcPaymentBalance;
	}

}
