package hpcgateway.wp.apps.materials.controller;



import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@Scope("request")
@RequestMapping(value = "/apps/materials")
public class MaterialsControler
{
	//@Resource
	//private MetadataService metadataService;
	//@Resource
	//private DefaultSessionController sessionController;
	@RequestMapping(value="/db/vasp/incar",method=RequestMethod.POST)
	public @ResponseBody String getVaspDbIncar()
	{
		return "/db/vasp/incar";
	}
}
