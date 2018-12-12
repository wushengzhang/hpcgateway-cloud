package hpcgateway.wp.desktop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hpcgateway.wp.core.page.TreeNode;
import hpcgateway.wp.metaconfig.service.MetadataService;


@Controller
@Scope("request")
@RequestMapping(value = "/cluster")
public class ClusterController
{
	@Resource
	private MetadataService metadataService;

	@RequestMapping(value="/items",method=RequestMethod.GET)
	public @ResponseBody List<TreeNode> queryClusterItems(
			@RequestParam(value="path",required=true) String path
	) 
	throws Exception
	{
		if( path == null || path.isEmpty() ) 
		{
			path = "/";
		}
		try
		{
			List<TreeNode> list = metadataService.queryTreeNode(path);
			return list;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new ArrayList<TreeNode>();
		}
	}
	
	
}
