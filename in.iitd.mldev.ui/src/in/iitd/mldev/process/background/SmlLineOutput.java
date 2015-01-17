package in.iitd.mldev.process.background;

public abstract class SmlLineOutput {

	private int startLine;
	private int startCol;
	private Integer endLine;
	private Integer endCol;
	private String message;

	public SmlLineOutput(SmlLine line, String errorMessage) {
		super();
		this.startLine = line.getStartLine();
		this.startCol = line.getStartCol();
		this.endLine = line.getEndLine();
		this.endCol = line.getEndCol();
		this.message = errorMessage;
	}

	public int getStartLine() {
		return startLine;
	}

	public int getStartCol() {
		return startCol;
	}

	public Integer getEndLine() {
		return endLine;
	}

	public Integer getEndCol() {
		return endCol;
	}

	public String getErrorMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		StringBuilder rtn = new StringBuilder();
		rtn.append(startLine).append(".").append(startCol);
		if(endLine != null) {
			rtn.append("-").append(endLine).append(".").append(endCol);
		}
		return rtn.append(" : ").append(message).toString();
	}

	public static class SmlErrorOutput extends SmlLineOutput {

		public SmlErrorOutput(SmlLine line, String errorMessage) {
			super(line, errorMessage);
		}

	}

	public static class SmlWarningOutput extends SmlLineOutput {
		public SmlWarningOutput(SmlLine line, String errorMessage) {
			super(line, errorMessage);
		}
	}

	public static SmlLine createLine(String lineInfo) {
		return new SmlLine(lineInfo);
	}

	public static class SmlLine {

		private String[] lineInfo;

		public SmlLine(String lineInfo) {
			this.lineInfo = lineInfo.split("-");
		}

		private int getStartLine() {
			return parseLine(lineInfo[0]);
		}
		private int getStartCol() {
			return parseCol(lineInfo[0]);
		}
		private Integer getEndLine() {
			return lineInfo.length > 1 ? parseLine(lineInfo[1]) : null;
		}
		
		private Integer getEndCol() {
			return lineInfo.length > 1 ? parseCol(lineInfo[1]) : null;
		}
		private int parseLine(String data) {
			return Integer.parseInt(data.split("\\.")[0]);
		}

		private int parseCol(String data) {
			return Integer.parseInt(data.split("\\.")[1]);
		}

	}

}
