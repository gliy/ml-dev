package in.iitd.mldev.process;

public interface ISmlProcess {


	void terminate();
	String getFileName();
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
		public String getFileName() {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
