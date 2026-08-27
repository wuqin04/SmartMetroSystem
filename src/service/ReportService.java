package service;

import java.util.ArrayList;
import model.Ticket;
import enums.TicketStatus;

public class ReportService {

    private ArrayList<Ticket> tickets;

    public ReportService(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void showTotalSales() {
        int count = 0;
        for (Ticket t : tickets) {
            if (t.getStatus() != TicketStatus.CANCELLED) {
                count++;
            }
        }
        
        System.out.printf(" %-25s : %d tickets\n", "Total Valid Tickets Sold", count);
        System.out.println(" *(Excludes cancelled tickets)*");
    }

    public void showTotalRevenue() {
        double revenue = 0.0;
        for (Ticket t : tickets) {
            if (t.getStatus() != TicketStatus.CANCELLED) {
                revenue += t.getFare();
            }
        }
        
        System.out.printf(" %-25s : RM %.2f\n", "Total Revenue Generated", revenue);
        System.out.println(" *(Based on active/completed tickets)*");
    }

    public void showCancelledTickets() {
        boolean found = false;
        int count = 0;
        double totalRefunded = 0.0;

        for (Ticket t : tickets) {
            if (t.getStatus() == TicketStatus.CANCELLED) {
                
                if (!found) {
                    System.out.printf(" %-15s | %-15s\n", "Ticket ID", "Fare (RM)");
                    System.out.println(" ----------------|-----------------");
                    found = true;
                }
                
                System.out.printf(" %-15s | %-15.2f\n", t.getTicketId(), t.getFare());
                
                count++;
                totalRefunded += t.getFare();
            }
        }

        if (!found) {
            System.out.println(" No cancelled tickets found in the system.");
        } else {
            System.out.println(" ----------------|-----------------");
            System.out.printf(" %-15s : %d\n", "Total Cancelled", count);
            System.out.printf(" %-15s : RM %.2f\n", "Total Refunded", totalRefunded);
        }
    }
}