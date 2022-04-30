package org.ss.demo.airplane.seating.io.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.ss.demo.airplane.seating.arrange.SeatingArrangement;
import org.ss.demo.airplane.seating.io.ReadFromConsole;
import org.ss.demo.airplane.seating.util.Util;;

@Component
public class ReadFromConsoleImpl implements ReadFromConsole{

	private static final String CORRECT_INPUT_LABEL = "Please enter an integer value between 1 and %d";

	private static final String INVALID_MESSAGE_LABEL = "Input contains other than numeric, square-brackets and comma, Please re-enter input";

	private static final String ENTER_INPUT_LABEL = "Enter input in similar format [[3,2],	[4,3],[2,3], [3,4]] 30: ";

	private static final String MAIN_MENU_HEADING = "---Main Menu---";

	private static final String[] MENU_OPTIONS = {"1 - Enter Seating Plan",
			"2 - Display last entered seating plan",
			"3 - Display seating status",
			"4 - Exit",
	};
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SeatingArrangement seatingArrangement;
	
	private final int EXIT_OPTION=4;  
	
	public void mainMenu() {
		int option = 1;
		Scanner scanner = new Scanner(System.in);
		while (option!=EXIT_OPTION){
			printMenu(MENU_OPTIONS);
			try {
				option = scanner.nextInt();

				switch (option){
				case 1: showInputMessage(); break;
				case 2: printSeatingArrangement(); break;
				case 3: printStatus(); break;
				case EXIT_OPTION: exit(0);break;
				default: printEnterCorrectInputMessage();
				}
			}
			catch (InputMismatchException ex){
				printEnterCorrectInputMessage();
				scanner.next();
			}
			catch (Exception ex){
				ex.printStackTrace();
				System.out.println("An unexpected error happened. Please try again");
				scanner.next();
			}
		}
		scanner.close();
	}

	private void printEnterCorrectInputMessage() {
		System.out.println(String.format(CORRECT_INPUT_LABEL, MENU_OPTIONS.length));
	}

	private void showInputMessage() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.print(ENTER_INPUT_LABEL);

		try {
			String input;
			input=reader.readLine();
			if(Util.doesInputContainAllNumbers(input)) {
				seatingArrangement.arrange(input);
			}else{
				System.out.println(INVALID_MESSAGE_LABEL);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printMenu(String[] options){
		System.out.println(MAIN_MENU_HEADING);
		for (String option : options){
			System.out.println(option);
		}
		System.out.print("Choose your option : ");
	}

	private void printSeatingArrangement() {
		seatingArrangement.print();
	}
	
	private void printStatus() {
		seatingArrangement.printStatus();
	}

	private void exit(int i) {
		System.out.println("Goodbye!");
	    System.exit(SpringApplication.exit(applicationContext, (ExitCodeGenerator) () -> 0));

	}
}
