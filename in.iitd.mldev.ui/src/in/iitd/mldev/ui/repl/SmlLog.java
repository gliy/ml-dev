package in.iitd.mldev.ui.repl;

import in.iitd.mldev.ui.PreferenceConstants;
import in.iitd.mldev.ui.SmlUiPlugin;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class SmlLog {
	private SmlLog() {}
	private static final ILog LOGGER = SmlUiPlugin.getDefault().getLog();
	private static final String PLUGIN_ID = PreferenceConstants.SML_PLUGIN_ID;

	public static void logError(String err) {
		LOGGER.log(new Status(IStatus.ERROR, PLUGIN_ID, err));
	}
	public static void logError(String err, Throwable throwable) {
		LOGGER.log(new Status(IStatus.ERROR, PLUGIN_ID, err, throwable));
	}
	public static void logError(Throwable throwable) {
		logError("", throwable);
	}
}
