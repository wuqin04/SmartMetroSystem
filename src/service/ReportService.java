package service;

import java.util.ArrayList;
import model.Ticket;
import enums.TicketStatus;

import model.Passenger;
import model.Station;
import enums.UserRole;
import enums.TicketType;
import java.time.LocalDate;

public class ReportService {

    private ArrayList<Ticket> tickets;

    public ReportService(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    //Identify the sold tickets from the array list
    public void showTotalSales() {
    	int count = 0;
    	for(Ticket t: tickets) {
    		if(t.getStatus() != TicketStatus.CANCELLED) {
    			count++;
    		}
    	}
    	System.out.println("Total tickets sold: " + count);
    }

    //add up the revenue based on sold ticket
    public void showTotalRevenue() {
    	double revenue = 0.0;
    	for(Ticket t: tickets) {
    		if(t.getStatus() != TicketStatus.CANCELLED) {
    			revenue += t.getFare();
    		}
    	}
    	System.out.printf("Total revenue: RM %.2f%n", revenue);
    }	

    //List out the cancelled tickets from the array list
    public void showCancelledTickets() {
    	boolean found = false;
    	for(Ticket t: tickets) {
    		if(t.getStatus() == TicketStatus.CANCELLED) {
    			System.out.println("Cancelled Ticket ID: " + t.getTicketId());
    			found = true;
				}
			}
			if(found != true) {
				System.out.println("No Cancelled Tickets.");
			} Repush
	}	
}