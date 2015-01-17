package in.iitd.mldev.process;

import in.iitd.mldev.ui.SmlUiPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;

public class SmlLauncher {
	private static List<ISmlListener> listeners;
	private SmlLauncher() {
		this.listeners = new ArrayList<ISmlListener>();

	}
	public static void registerListener(ISmlListener listener) {
		
	}

	public static ISmlProcess launch(IFile file) {
		return launch(file, SmlLauncher.listeners.toArray(new ISmlListener[0]));
	}
	
	public static ISmlProcess launch(IFile file, ISmlListener ...listeners) {
		String smlPath = SmlUiPlugin.getDefault().getLaunchPath();
		if(listeners == null) {
			listeners = new ISmlListener[0];
		}
		try {
			return new SmlProcess(new ProcessBuilder(smlPath).directory(file
					.getParent().getLocation().toFile()), file.getName(),
					Arrays.asList(listeners));
		} catch (Exception e) {
			return ISmlProcess.ERROR;
		}
	}

	private static class SmlProcess implements ISmlProcess {
		private Process process;
		private List<ISmlListener> listeners;
		private String fileName;
		private PrintWriter writer;
		private Thread reader;
		public SmlProcess(ProcessBuilder processBuilder, String fileName,
				List<ISmlListener> listeners) throws IOException {
			this.process = processBuilder.start();
			this.listeners = listeners;
			this.fileName = fileName;
			this.writer = new PrintWriter(this.process.getOutputStream());
			this.reader = new Thread(new SmlReader());
			this.reader.start();
			send(String.format("use \"%s\";", fileName));
		}

		@Override
		public void terminate() {
			process.destroyForcibly();
		}

		@Override
		public String getFileName() {
			return fileName;
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
							listener.lineRead(fileName, t);
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
