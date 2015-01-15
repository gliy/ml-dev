package in.iitd.mldev.launch.background.parse;

import in.iitd.mldev.launch.SmlLaunchPlugin;
import in.iitd.mldev.launch.background.SmlModule;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class SmlOutputParser {
	private Stack<SmlModule> currentModules;
	//private static final ILog LOGGER = SmlLaunchPlugin.getDefault().getLog();
	private List<SmlParseAction> parseActions;
	private SmlModule root;

	public SmlOutputParser(SmlModule root) {
		this.root = root;
		this.currentModules = new Stack<SmlModule>();
		this.currentModules.push(root);
		this.parseActions = ParseActions.getActions();
	}

	public SmlModule getCurrent() {
		if(currentModules.isEmpty()) {
			return root;
		}
		return currentModules.peek();
	}

	public void setCurrent(SmlModule module) {
		this.currentModules.push(module);
	}

	public void unsetCurrent() {
		if(!this.currentModules.isEmpty()) {
			SmlModule old = this.currentModules.pop();
			System.out.println("Popping " + old.getName());
		}
		System.out.println("Stack is empty!");
	}

	public void parse(String data) {
		Matcher matcher = null;
		for (SmlParseAction action : parseActions) {
			if(parse(action, matcher, data)) {
				return;
			}
		}
		//System.out.println("no match");
		//LOGGER.log(status(IStatus.WARNING, "No match found for string :'"
		//		+ data + "'"));
	}
	
	private boolean parse(SmlParseAction action, Matcher matcher, String data) {
		for (Pattern pattern : action.getPatterns()) {
			if (matcher == null) {
				matcher = pattern.matcher(data);
			} else {
				//matcher.reset();
				matcher.usePattern(pattern);
			}

			if (matcher.matches()) {
				//System.out.println("Match found for '" + data + "' with regex " + pattern);
				action.parse(this, toCaptureGroups(matcher));
				return true;
			}
		}
		return false;
	}

	private String[] toCaptureGroups(Matcher matcher) {
		if (matcher.groupCount() <= 0) {
			//System.out.println("No  capture groups found " + matcher.pattern());
			//LOGGER.log(status(IStatus.WARNING, "No capture groups found"));
			return new String[0];
		}
		String[] rtn = new String[matcher.groupCount()];
		for (int i = 0; i < matcher.groupCount(); i++) {
			rtn[i] = matcher.group(i + 1);
		}
		return rtn;
	}

	private IStatus status(int severity, String message) {
		return new Status(severity, "SmlLaunchPlugin", message);
	}
}
