package in.iitd.mldev.utils;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class DisplayUtil {
	
	public static void asyncExec(Runnable r) {
		Display display = PlatformUI.getWorkbench().getDisplay();
		if (!display.isDisposed()) {
			display.asyncExec(r);
		}
	}

	public static void syncExec(Runnable r) {
		if (Display.getCurrent() == null) {
			Display display = PlatformUI.getWorkbench().getDisplay();
			if (!display.isDisposed()) {
				display.syncExec(r);
			}
		} else {
			r.run();
		}
	}

}