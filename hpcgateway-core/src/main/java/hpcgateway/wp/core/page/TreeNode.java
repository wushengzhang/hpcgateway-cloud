package hpcgateway.wp.core.page;

public class TreeNode 
{
	private String name;
	private String title;
	private int level;
	private String path;
	private boolean isParent;
	private String identifier;
	private boolean checked;
	private String data;
	private String type;
	
	public TreeNode()
	{
		this((String)null,null,null,true);
	}
	
	public TreeNode(String name,String path)
	{
		this(name,name,path,true);
	}
	
	public TreeNode(String name,String path,boolean parent)
	{
		this(name,name,path,parent);
	}
	
	public TreeNode(String identifier,String name,String path)
	{
		this(identifier,name,path,true);
	}
	
	public TreeNode(String identifier,String name,String path,boolean parent)
	{
		this.identifier = identifier;
		this.name = name;
		this.path = path;
		this.isParent = parent;
	}
	
	public TreeNode(Long identifier,String name,String path,boolean parent)
	{
		this.identifier = String.valueOf(identifier);
		this.name = name;
		this.path = path;
		this.isParent = parent;
	}
	
	public TreeNode(String type,String identifier,String name,String path)
	{
		this(type,identifier,name,path,true);
	}
	
	public TreeNode(String type,String identifier,String name,String path,boolean parent)
	{
		this.type = type;
		this.identifier = identifier;
		this.name = name;
		this.path = path;
		this.isParent = parent;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public boolean isIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public void setIdentifier(Long identifier) {
		this.identifier = identifier==null ? null : String.valueOf(identifier);
	}
	
	public void setIdentifier(Integer identifier) {
		this.identifier = identifier==null ? null : String.valueOf(identifier);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	
	
}
