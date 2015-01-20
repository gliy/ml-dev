package in.iitd.mldev.process;

import in.iitd.mldev.ui.SmlUiPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IFile;

public class SmlLauncher {
	//private static List<ISmlListener> listeners;
	private SmlLauncher() {
	//	this.listeners = new ArrayList<ISmlListener>();

	}
	public static void registerListener(ISmlListener listener) {
		
	}

	public static ISmlProcess launch(IFile file) {
		return launch(file);
	}
	
	public static ISmlProcess launch(IFile file, ISmlListener ...listeners) {
		String smlPath = SmlUiPlugin.getDefault().getLaunchPath();
		if(smlPath == null) {
			return ISmlProcess.ERROR;
		}
		if(listeners == null) {
			listeners = new ISmlListener[0];
		}
		try {
			SmlProcess process = new SmlProcess(new ProcessBuilder(smlPath).directory(file
					.getParent().getLocation().toFile()),
					Arrays.asList(listeners));
			process.load(file);
			return process;
		} catch (Exception e) {
			return ISmlProcess.ERROR;
		}
	}

	private static class SmlProcess implements ISmlProcess {
		private Process process;
		private List<ISmlListener> listeners;
		private PrintWriter writer;
		private IFile activeFile;
		private Thread reader;
		public SmlProcess(ProcessBuilder processBuilder, 
				List<ISmlListener> listeners) throws IOException {
			this.process = processBuilder.start();
			this.listeners = listeners;
			this.writer = new PrintWriter(this.process.getOutputStream());
			this.reader = new Thread(new SmlReader());
			this.reader.start();
			
		}

		@Override
		public void terminate() {
			process.destroyForcibly();
		}

		@Override
		public void load(IFile file) {
			this.activeFile = file;
			send(String.format("use \"%s\";", file.getLocation().toFile().getPath().replaceAll("\\\\", "\\\\\\\\")));
		}

		@Override
		public void send(String data) {
			writer.println(data);
			writer.flush();
		}
		
		private class SmlReader implements Runnable {
			private BufferedReader reader;
			public SmlReader(){
				this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			}
			@Override
			public void run() {
				try {
					String t;
					while((t=reader.readLine()) != null) {
						for (ISmlListener listener : listeners) {
							listener.lineRead(activeFile, t);
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
