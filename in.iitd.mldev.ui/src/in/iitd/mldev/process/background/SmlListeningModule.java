package in.iitd.mldev.process.background;

import in.iitd.mldev.process.background.SmlLineOutput.SmlErrorOutput;
import in.iitd.mldev.process.background.SmlLineOutput.SmlWarningOutput;
import in.iitd.mldev.process.background.parse.ISmlParseListener;

import java.util.Collection;
import java.util.List;

public class SmlListeningModule implements ISmlModule {

	private SmlModule module;
	private ISmlParseListener[] listeners;

	public ISmlModule addModule(String name) {
		SmlModule newModule = new SmlModule(name, this);
		return module.addModule(new SmlListeningModule(newModule, listeners));
	}

	public void add(SmlObject function) {
		module.add(function);
		fireParseEvent(this, function);
	}

	public void add(SmlLineOutput error) {
		module.add(error);
		fireParseEvent(this, error);
	}

	public boolean equals(Object obj) {
		return module.equals(obj);
	}

	public ISmlModule getModule(String moduleName) {
		return module.getModule(moduleName);
	}

	public ISmlModule getParent() {
		return module.getParent();
	}
	@Override
	public ISmlModule addModule(ISmlModule module) {
		return module.addModule(module);
	}

	public Collection<ISmlModule> getModules() {
		return module.getModules();
	}

	public List<SmlLineOutput> getErrors() {
		return module.getErrors();
	}

	public List<SmlObject> getDeclaredObjects() {
		return module.getDeclaredObjects();
	}

	public String getName() {
		return module.getName();
	}

	public int hashCode() {
		return module.hashCode();
	}

	public String toString() {
		return module.toString();
	}

	private void fireParseEvent(ISmlModule cur, SmlObject obj) {
		if (obj instanceof SmlVal) AGGREGATE_LISTENER.parsedVal(cur, (SmlVal)obj);
		if (obj instanceof SmlList) AGGREGATE_LISTENER.parsedList(cur, (SmlList)obj);
		if (obj instanceof SmlTuple) AGGREGATE_LISTENER.parsedTuple(cur, (SmlTuple)obj);
		if (obj instanceof SmlRecord) AGGREGATE_LISTENER.parsedRecord(cur, (SmlRecord)obj);
		if (obj instanceof SmlException) AGGREGATE_LISTENER.parsedException(cur, (SmlException)obj);
		if (obj instanceof SmlFunction) AGGREGATE_LISTENER.parsedFunction(cur, (SmlFunction)obj);
		if (obj instanceof SmlDatatype) AGGREGATE_LISTENER.parsedDatatype(cur, (SmlDatatype)obj);
	}
	
	private void fireParseEvent(ISmlModule cur, SmlLineOutput obj) {
		if (obj instanceof SmlErrorOutput) AGGREGATE_LISTENER.parsedError(cur, (SmlErrorOutput)obj);
		if (obj instanceof SmlWarningOutput) AGGREGATE_LISTENER.parsedWarning(cur, (SmlWarningOutput)obj);
	}

	@SuppressWarnings("unchecked")
	private static <E extends SmlObject> E cast(SmlObject e) {
		return (E) e;
	}

	public SmlListeningModule(SmlModule module, ISmlParseListener... listeners) {
		super();
		this.module = module;
		this.listeners = listeners;
	}

	private final ISmlParseListener AGGREGATE_LISTENER = new ISmlParseListener() {

		@Override
		public void parsedVal(ISmlModule module, SmlVal val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedVal(module, val);
			}
		}

		@Override
		public void parsedList(ISmlModule module, SmlList val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedList(module, val);
			}
		}

		@Override
		public void parsedTuple(ISmlModule module, SmlTuple val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedTuple(module, val);
			}
		}

		@Override
		public void parsedRecord(ISmlModule module, SmlRecord val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedRecord(module, val);
			}
		}

		@Override
		public void parsedException(ISmlModule module, SmlException val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedException(module, val);
			}
		}

		@Override
		public void parsedFunction(ISmlModule module, SmlFunction val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedFunction(module, val);
			}
		}

		@Override
		public void parsedDatatype(ISmlModule module, SmlDatatype val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedDatatype(module, val);
			}
		}

		@Override
		public void parsedError(ISmlModule module, SmlErrorOutput val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedError(module, val);
			}
		}

		@Override
		public void parsedWarning(ISmlModule module, SmlWarningOutput val) {
			for (ISmlParseListener listener : listeners) {
				listener.parsedWarning(module, val);
			}
		}

	};

}
