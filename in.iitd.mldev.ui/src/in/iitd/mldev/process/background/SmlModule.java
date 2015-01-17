package in.iitd.mldev.process.background;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmlModule {
	private List<SmlObject> objects;
	private List<SmlLineOutput> errors;
	private Map<String, SmlModule> modules;
	private SmlModule parent;
	private String name;
	
	public SmlModule(String name) {
		this(name, null);
	}
	public SmlModule(String name, SmlModule parent) {
		this.name = name;
		this.parent = parent;
		this.objects = new ArrayList<SmlObject>();
		this.errors = new ArrayList<SmlLineOutput>();
		this.modules = new HashMap<String, SmlModule>();
	}
	
	public SmlModule getModule(String moduleName) {
		return modules.get(moduleName);
	}
	
	public SmlModule getParent() {
		return parent;
	}

	public SmlModule addModule(String name) {
		SmlModule module = new SmlModule(name, this);
		modules.put(name, module);
		return module;
	}
	
	public void add(SmlObject function) {
		System.out.println("\tAdding function " + function.toString());
		this.objects.add(function);
	}
	public void add(SmlLineOutput error){
		System.out.println("\tAdding error " + error.toString());
		this.errors.add(error);
	}
	public Collection<SmlModule> getModules() {
		return modules.values();
	}
	public List<SmlLineOutput> getErrors() {
		return errors;
	}
	public List<SmlObject> getDeclaredObjects() {
		return objects;
	}
	public String getName() {
		return name;
	}
	
}
