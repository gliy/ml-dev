package in.iitd.mldev.process;

import org.eclipse.core.resources.IFile;

public interface ISmlProcess {


	void load(IFile file);
	void terminate();
	void send(String data);
	
	
	public static final ISmlProcess ERROR = new ISmlProcess() {
		
		@Override
		public void terminate() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void send(String data) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void load(IFile file) {
		}
		
		
	};
}
