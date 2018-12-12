package hpcgateway.wp.core.page;

public class Pager
{
	private int count;
	private int pageNo;
	private int pageSize;
	private int pageNumber;
	
	public Pager()
	{
		this(0,1,20);
	}
	public Pager(int count)
	{
		this(count,2,20);
	}
	public Pager(int count,int pageNo,int pageSize)
	{
		this.count = count;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.pageNumber = (this.count+this.pageSize-1)/this.pageSize;
		if( this.pageNumber == 0 || this.pageNo <= 0 )
		{
			this.pageNo = 1;
		}
		else if( this.pageNo >= this.pageNumber )
		{
			this.pageNo = this.pageNumber;
		}
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	
}
