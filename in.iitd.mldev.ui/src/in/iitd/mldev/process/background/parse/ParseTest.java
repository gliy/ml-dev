package in.iitd.mldev.process.background.parse;

import in.iitd.mldev.process.background.ISmlModule;
import in.iitd.mldev.process.background.SmlFunction;
import in.iitd.mldev.process.background.SmlLineOutput;
import in.iitd.mldev.process.background.SmlModule;
import in.iitd.mldev.process.background.SmlObject;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class ParseTest {

	public static void main(String[] args) throws Exception {

		
		
		SmlModule root = new SmlModule("root");
		SmlOutputParser parser = new SmlOutputParser(root);
		Scanner scanner = new Scanner(new File("output/interpreter.out"));
		int i = 1;
		while (scanner.hasNextLine()) {
			// System.out.print((i++) +": ");
			//parser.lineRead(scanner.nextLine());
			//System.out.println(i++);
		}
		scanner.close();

		output(root);
	}

	private static void output(ISmlModule root) {
		System.out.println("\n" + root.getName());
		System.out.println("===============Functions===========\n");
		for (SmlObject fn : root.getDeclaredObjects()) {
		//	System.out.println(fn);
		}
		
		for (SmlLineOutput line : root.getErrors()) {
			System.out.println(line);
		}
		for (ISmlModule fn : root.getModules()) {
			
			output(fn);
		}

		

	}
}
