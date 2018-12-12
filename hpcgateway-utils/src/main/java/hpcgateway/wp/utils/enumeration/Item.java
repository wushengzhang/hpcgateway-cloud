package hpcgateway.wp.utils.enumeration;

public class Item 
{
	private int index;
	private String name;
	private String value;
	private String description;
	
	public Item()
	{
		
	}
	
	public Item(String name,String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public Item(Descriptable d)
	{
		index = d.getIndex();
		name = d.getName();
		value = d.getValue();
		description = d.getDescription();
	}
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
