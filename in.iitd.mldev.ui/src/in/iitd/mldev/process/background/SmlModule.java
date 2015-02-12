package in.iitd.mldev.process.background;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SmlModule implements ISmlModule {
	//set allows us to only suggest the last function if 2 functions share the same name
	private Set<SmlObject> objects;
	private List<SmlLineOutput> errors;
	private Map<String, ISmlModule> modules;
	private ISmlModule parent;
	private String name;
	public SmlModule(String name) {
		this(name, null);
	}
	public SmlModule(String name, ISmlModule parent) {
		this.name = name;
		this.parent = parent;
		this.objects = new LinkedHashSet<SmlObject>();
		this.errors = new ArrayList<SmlLineOutput>();
		this.modules = new HashMap<String, ISmlModule>();
	}
	
	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#getModule(java.lang.String)
	 */
	@Override
	public ISmlModule getModule(String moduleName) {
		return modules.get(moduleName);
	}
	
	
	
	@Override
	public ISmlModule getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#addModule(java.lang.String)
	 */
	@Override
	public ISmlModule addModule(String name) {
		return addModule(new SmlModule(name, this));
	}
	
	@Override
	public ISmlModule addModule(ISmlModule module) {
		modules.put(name, module);
		return module;
	}
	
	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#add(in.iitd.mldev.process.background.SmlObject)
	 */
	@Override
	public void add(SmlObject function) {
		System.out.println("\tAdding function " + function.toString());
		this.objects.add(function);
	}
	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#add(in.iitd.mldev.process.background.SmlLineOutput)
	 */
	@Override
	public void add(SmlLineOutput error){
		System.out.println("\tAdding error " + error.toString());
		this.errors.add(error);
	}
	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#getModules()
	 */
	@Override
	public Collection<ISmlModule> getModules() {
		return modules.values();
	}
	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#getErrors()
	 */
	@Override
	public List<SmlLineOutput> getErrors() {
		return errors;
	}
	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#getDeclaredObjects()
	 */
	@Override
	public Collection<SmlObject> getDeclaredObjects() {
		return objects;
	}
	/* (non-Javadoc)
	 * @see in.iitd.mldev.process.background.ISmlModule#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
}
