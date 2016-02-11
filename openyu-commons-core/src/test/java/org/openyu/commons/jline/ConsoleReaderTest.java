package org.openyu.commons.jline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.openyu.commons.thread.ThreadHelper;

import jline.AnsiWindowsTerminal;
import jline.Terminal;
import jline.TerminalSupport;
import jline.WindowsTerminal;
import jline.console.ConsoleReader;

//jvm args
//-Djline.WindowsTerminal.directConsole=false 
public class ConsoleReaderTest {

	private static int lineNumber = 1;

	public static void main(String[] args) throws Exception {
		// String dir =
		// System.getProperty("jline.WindowsTerminal.directConsole");
		// System.out.println(dir);
		// System.setProperty("jline.WiendowsTerminal.directConsole", "false");
		String dir = System.getProperty("jline.WindowsTerminal.directConsole");
		System.out.println(dir);

		System.out.println(System.getProperty("file.encoding"));
		System.out.println(System
				.getProperty("jline.WindowsTerminal.input.encoding"));

		// ConsoleReader reader = new ConsoleReader(System.in,
		// new ByteArrayOutputStream());

		ConsoleReader reader = new ConsoleReader();
		// reader.setPrompt("prompt> ");

		// WindowsTerminal terminal = (WindowsTerminal) reader.getTerminal();
		// // terminal.setDirectConsole(false);
		// System.out.println(terminal.getDirectConsole());

		// WindowsTerminal terminal = new WindowsTerminal();
		// terminal.setDirectConsole(false);
		// Terminal terminal =
		// Terminal.getTerminal();//IncompatibleClassChangeError
		// System.out.println(terminal);

		// ConsoleReader reader = new ConsoleReader(System.in, System.out,
		// terminal);

		Character mask = (args.length == 0) ? new Character((char) 0)
				: new Character(args[0].charAt(0));

		// String line;

		// while (true) {
		//
		// do {
		// line = reader.readLine("Enter password> ", mask);
		// System.out.println("Got password: " + line);
		// } while (line != null && line.length() > 0);
		//
		// ThreadHelper.sleep(50);
		// }

		// PrintWriter out = new PrintWriter(reader.getOutput());
		// while ((line = reader.readLine("prompt> ")) != null) {
		// line = line.trim();
		// if (line.isEmpty()) {
		// continue;
		// }
		// ThreadHelper.sleep(50);
		// }

		String prompt;
		String line = "";
		String currentStatement = "";
		boolean inCompoundStatement = false;

		while (line != null) {
			prompt = (inCompoundStatement) ? "...\t" : "[" + "user@]";

			try {
				line = reader.readLine(prompt);
			} catch (IOException e) {
				// retry on I/O Exception
			}

			if (line == null)
				return;

			line = line.trim();

			if (line.isEmpty())
				continue;

			currentStatement += line;

			if (line.endsWith(";") || line.equals("?")) {
				processStatement(currentStatement);
				currentStatement = "";
				inCompoundStatement = false;
			} else {
				currentStatement += " "; // ready for new line
				inCompoundStatement = true;
			}
		}
	}

	public static void processStatement(String query) {
		lineNumber++;
	}
}

// cassandra cli
// String prompt;
// String line = "";
// String currentStatement = "";
// boolean inCompoundStatement = false;
//
// while (line != null)
// {
// prompt = (inCompoundStatement) ? "...\t" : getPrompt(cliClient);
//
// try
// {
// line = reader.readLine(prompt);
// }
// catch (IOException e)
// {
// // retry on I/O Exception
// }
//
// if (line == null)
// return;
//
// line = line.trim();
//
// // skipping empty and comment lines
// if (line.isEmpty() || line.startsWith("--"))
// continue;
//
// currentStatement += line;
//
// if (line.endsWith(";") || line.equals("?"))
// {
// processStatement(currentStatement);
// currentStatement = "";
// inCompoundStatement = false;
// }
// else
// {
// currentStatement += " "; // ready for new line
// inCompoundStatement = true;
// }
// }