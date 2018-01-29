package repl;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class REPL {
	
	private static Scanner stdin = null;
	private static final int DELAY_MILLISECONDS = 0;

	
	private static Scanner getStdIn() {
		if(stdin == null) {
			stdin = new Scanner(System.in);
		}
		return stdin;
	}
	
	public static String readLine() {
		try {
			return getStdIn().nextLine();
		} catch (NoSuchElementException e) {
			REPL.println("Thanks for playing!");
			System.exit(0); //on cmd
		}
		return null;
	}
	
	public static void print(String s) {
		System.out.print(s);
	}
	
	public static void println(String s) {
		System.out.println(s);
		delay();
	}

	public static void delay() {
		try {
			Thread.sleep(DELAY_MILLISECONDS);
		} catch (InterruptedException e) {}
	}
}
