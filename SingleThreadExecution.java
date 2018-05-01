
/****************************************************************************
 *
 * File: SingleThreadExecution.java                     
 * Author: Pushpalata Rajupeta
 *
 * Description: Example to achieve doSomething() method to be called by one thread at a time in multithread environment
 * across JVMs  .
 * This program depends on external file to read the current state. First thread which is accessing doSomething() looks for
 * "Ready" state (read from file lock.txt) if not ready then waits in a loop with 1 second interval.
 * If state is ready then changes state to "Wait" and performs the required job and changes the state back to "Ready" (write to file)
 *
 * Modifications
 *
 *    DATE     PROGRAMMER   VER   MODIFICATION
 * ----------  ----------- -----  ---------------------------------------------
 * 
 * 2018.05.01 prajupeta         CREATED
 *
 ****************************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecution {
	// To identify which instance of the program
	final String name;
	// file to write the state
	final String fileName = "lock.txt";

	/**
	 * Constructor, sets the name of current execution
	 */
	public SingleThreadExecution(String name) {
		this.name = name;
	}

	/**
	 * reads the external file to find the state, if Ready, changes the state to
	 * wait and executes the doSomething logic which is TODO when done, changes
	 * state back to ready
	 * 
	 * @throws IOException
	 */
	public synchronized void doSomething() throws IOException {

		// wait till "Ready" state read from file
		while (true) {
			String val = readFile();
			if (val != null && val.contains("Ready"))
				break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(name + " start " + LocalDateTime.now() + " " + Thread.currentThread().getName());
		// change the state to Wait as already one thread is accessing this code
		writeToFile("Wait");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO do something

		// change state back to Ready
		writeToFile("Ready");

		System.out.println(name + " end   " + LocalDateTime.now() + " " + Thread.currentThread().getName());
	}

	/**
	 * Creates a fixed thread pool, doSomething() is called in a loop by pool of
	 * threads
	 */
	public void testSingleThread() {

		ExecutorService es = Executors.newFixedThreadPool(2);
		Runnable r1 = () -> {
			try {
				doSomething();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		Runnable r2 = () -> {
			try {
				doSomething();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		for (int i = 0; i < 10; i++) {
			es.execute(r1);
			es.execute(r2);
		}
		es.shutdown();

	}

	/**
	 * Reads the file
	 * 
	 * @return String
	 */
	private String readFile() {
		// This will reference one line at a time
		String line = null;
		String retVal = "";
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				retVal = line;
			}

			// close files
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return retVal;
	}

	/**
	 * Writes to the file
	 * 
	 * @param text
	 */
	private void writeToFile(String text) {
		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fileName);

			// wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			System.out.println("Write " + text);
			bufferedWriter.write(text);
			// lose files.
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new SingleThreadExecution(args[0]).testSingleThread();
	}
}
