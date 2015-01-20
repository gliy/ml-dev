package in.iitd.mldev.process.background;

import in.iitd.mldev.process.background.parse.SmlOutputParser;

import java.io.IOException;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;

public class SmlBackgroundProgram {

	private IProcess process;

	public SmlBackgroundProgram(final IProcess process, String toWrite,
			String fileName) throws IOException {
		this.process = process;
		// Control.Print.linewidth:=900;
		this.process.getLaunch().removeProcess(process);
		final SmlModule root = new SmlModule(fileName);
		final SmlOutputParser parser = new SmlOutputParser(root);
		this.process.getStreamsProxy().getOutputStreamMonitor()
				.addListener(new IStreamListener() {
					int count = 0;
					@Override
					public void streamAppended(String text,
							IStreamMonitor monitor) {
						for (String line : text.split("\n")) {
							String trimmed = line.trim();
							parser.lineRead(null,trimmed);
							if(trimmed.startsWith("-")) {
								count++;
							}
						}
						if(count == 2) {
							fireParseEvent(root);
						}
						
//						if(count >= 2) {
//							System.out.println("FIRING PARSE EVENT");
//							fireParseEvent(root);
//						}
					}
				});
		this.process.getStreamsProxy().write(getSettings());
		this.process.getStreamsProxy().write(toWrite);
		DebugPlugin.getDefault().addDebugEventListener(
				new IDebugEventSetListener() {

					@Override
					public void handleDebugEvents(DebugEvent[] events) {
						for (DebugEvent debugEvent : events) {
							if (debugEvent.getSource() == process
									&& debugEvent.getKind() == DebugEvent.TERMINATE) {

							System.out.println("SHUTDOWN");
							}
						}

					}
				});
	}

	private String getSettings() {
		return String.format("%s;%s;", "Control.Print.linewidth:=900", "Control.Print.signatures := 0");
	}
	private void fireParseEvent(ISmlModule root) {
		SmlProgramOutput output = new SmlProgramOutput(
				root);
//		for (ISmlParsed parseListener : SmlLaunchPlugin
//				.getDefault().getParseListeners()) {
//			parseListener.programParsed(output);
//		}
	}

}
