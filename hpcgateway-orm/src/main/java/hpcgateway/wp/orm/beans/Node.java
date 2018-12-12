package hpcgateway.wp.orm.beans;

import java.util.ArrayList;
import java.util.List;

public class Node
{
	protected Long id;
	protected String name;
	protected String aliasname;
	
	protected List<PropertyValue> properties;
	protected List<ParameterValue> parameters;
	protected List<Connect> connects;
	protected List<NodeRoleAlloc> roles;
	protected List<Interface> ifcs;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}




	public List<Connect> getConnects()
	{
		return connects;
	}

	public void setConnects(List<Connect> connects)
	{
		this.connects = connects;
	}

	public void setConnects(Connect[] connects)
	{
		if (connects == null)
		{
			this.connects = null;
		}
		else
		{
			this.connects = new ArrayList<Connect>();
			for (int i = 0; i < connects.length; i++)
			{
				this.connects.add(connects[i]);
			}
		}
	}

	public void addConnect(Connect connect)
	{
		if (connects == null)
		{
			connects = new ArrayList<Connect>();
		}
		connects.add(connect);
	}


	public List<Interface> getIfcs()
	{
		return ifcs;
	}

	public void setIfcs(List<Interface> ifcs)
	{
		this.ifcs = ifcs;
	}

	public void setIfcs(Interface[] ifcs)
	{
		if (ifcs == null)
		{
			this.ifcs = null;
		}
		else
		{
			this.ifcs = new ArrayList<Interface>();
			for (int i = 0; i < ifcs.length; i++)
			{
				this.ifcs.add(ifcs[i]);
			}
		}
	}

	public void addIfc(Interface ifc)
	{
		if (this.ifcs == null)
		{
			this.ifcs = new ArrayList<Interface>();
		}
		this.ifcs.add(ifc);
	}

	public String getAliasname()
	{
		return aliasname;
	}

	public void setAliasname(String aliasname)
	{
		this.aliasname = aliasname;
	}

	public List<PropertyValue> getProperties()
	{
		return properties;
	}

	public void setProperties(List<PropertyValue> properties)
	{
		this.properties = properties;
	}
	
	public void addProperty(PropertyValue property)
	{
		if( this.properties == null )
		{
			this.properties = new ArrayList<PropertyValue>();
		}
		this.properties.add(property);
	}

	public List<ParameterValue> getParameters()
	{
		return parameters;
	}

	public void setParameters(List<ParameterValue> parameters)
	{
		this.parameters = parameters;
	}
	
	public void addParameter(ParameterValue parameter)
	{
		if( this.parameters == null )
		{
			this.parameters = new ArrayList<ParameterValue>();
		}
		this.parameters.add(parameter);
	}

	public List<NodeRoleAlloc> getRoles()
	{
		return roles;
	}

	public void setRoles(List<NodeRoleAlloc> roles)
	{
		this.roles = roles;
	}

	public void addRole(NodeRoleAlloc role)
	{
		if( this.roles == null )
		{
			this.roles = new ArrayList<NodeRoleAlloc>();
		}
		this.roles.add(role);
	}
	
	public String getRolesList()
	{
		if( roles == null || roles.isEmpty() )
		{
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for(NodeRoleAlloc ra : roles)
		{
			NodeRole role = ra.getRole();
			if( role != null )
			{
				if( buffer.length() > 0 ) buffer.append(",");
				buffer.append(role.getName());
			}
		}
		return buffer.toString();
	}
}
