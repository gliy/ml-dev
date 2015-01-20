package in.iitd.mldev.process.background;

import java.util.Collection;
import java.util.List;

public interface ISmlModule {

	public abstract ISmlModule getModule(String moduleName);

	public abstract ISmlModule getParent();

	public abstract ISmlModule addModule(String name);
	public abstract ISmlModule addModule(ISmlModule module);

	public abstract void add(SmlObject function);

	public abstract void add(SmlLineOutput error);

	public abstract Collection<ISmlModule> getModules();

	public abstract List<SmlLineOutput> getErrors();

	public abstract List<SmlObject> getDeclaredObjects();

	public abstract String getName();


}