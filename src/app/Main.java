package app;

import java.util.Scanner;


public class Main {
	public static void main(String[] args) {
		System.out.println("[SMART METRO SYSTEM]");
		System.out.println("(1) Login ");
		System.out.println("(2) Register");
		System.out.print("Enter your choice: ");
		
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		
		choice = sc.nextInt();
		
		switch (choice) {
		case 1:
			System.out.println("Entering Login page.");
			break;
		case 2:
			System.out.println("Entering Register page.");
			break;
		default:
			System.out.println("Invalid choice, only input 1-2.");
			break;
				
		}
		
	}
}
