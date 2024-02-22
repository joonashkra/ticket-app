package TicketApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketApplication {
    static int allSales = 0;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ticket Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 10);

        TicketPurchase normalTicket = new TicketPurchase("Normal Ticket", 22);
        TicketPurchase childTicket = new TicketPurchase("Children's Ticket", 26);
        TicketPurchase discountTicket = new TicketPurchase("Discount Ticket", 20);

        frame.add(normalTicket);
        frame.add(childTicket);
        frame.add(discountTicket);

        JButton printReceiptButton = new JButton("Complete Purchase");
        printReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReceipt(normalTicket, childTicket, discountTicket);
            }
        });

        JLabel errorLabel = new JLabel("");

        frame.add(printReceiptButton);
        frame.add(errorLabel);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    private static void printReceipt(TicketPurchase... ticketPurchases) {
        try {
            String timestamp = getCurrentTimestamp();
            int totalSales = 0;
            boolean saleCompleted = false;
            String errorMessage = "Purchase at least one ticket.";

            for (TicketPurchase ticketPurchase : ticketPurchases) {
                int totalCost = ticketPurchase.getTotalPrice();
                if (totalCost > 0) {
                    saleCompleted = true;
                    break;
                }
            }

            if (saleCompleted) {
                String receiptFile = "TicketApplication\\receipt_" + timestamp.replace(" ", "_") + ".txt";

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile, true))) {
                    writer.write("Receipt\nHupiPark\n" + timestamp + "\n\n");

                    for (TicketPurchase ticketPurchase : ticketPurchases) {
                        int totalCost = ticketPurchase.getTotalPrice();
                        if (totalCost > 0) {
                            String discountType = "";

                            if ("Discount Ticket".equals(ticketPurchase.getTicketType())) {
                                int index = 0;
                                for (String ticket : ticketPurchase.getPurchasedTickets()) {
                                    discountType += ticket;

                                    if (index < ticketPurchase.getPurchasedTickets().size() - 1) {
                                        discountType += ", ";
                                    }
                                    index++;
                                }
                            }

                            int quantity = ticketPurchase.getQuantity();
                            if("Discount Ticket".equals(ticketPurchase.getTicketType())) {
                                String newDiscountTicket = ticketPurchase.getTicketType() + " (" + discountType + ") x " + quantity + " : " + totalCost + "€\n";
                                writer.write(newDiscountTicket);
                                writeTodaysSales(newDiscountTicket);
                            }
                            else if ("Children's Ticket".equals(ticketPurchase.getTicketType())) {
                                for (int i = 0; i < ticketPurchase.getChildrenData().size(); i++) {
                                    String newChildTicket = ticketPurchase.getTicketType() + " (" + ticketPurchase.getChildrenData(i) + ") x 1" + " : " + "26€\n";
                                    writer.write(newChildTicket);
                                    writeTodaysSales(newChildTicket);
                                }
                            }
                            else {
                                String newTicket = ticketPurchase.getTicketType() + " x " + quantity + " : " + totalCost + "€\n";
                                writer.write(newTicket);
                                writeTodaysSales(newTicket);
                            }
                            
                            totalSales += totalCost;

                            ticketPurchase.reset();
                        }
                    }

                    allSales += totalSales;

                    writeTotalSales(allSales);
                    
                    writer.write("\nTotal: " + totalSales + "€\n");
                    JOptionPane.showMessageDialog(null, "Purchase completed!");
                } 
                catch (IOException ex) {
                    System.out.println("Error writing to file: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } 
            else {
                JOptionPane.showMessageDialog(null, errorMessage);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        return dateFormat.format(new Date());
    }

    private static void writeTotalSales(int allSales) {
        try (FileWriter writer2 = new FileWriter("TicketApplication\\totalSales.txt", false);){
            writer2.write("Total Sales Report\n" + getCurrentTimestamp() + "\n" + allSales + "€");
        } 
        catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void writeTodaysSales(String newTicket) {
        String currentTimestamp = getCurrentTimestamp();
        String currentDate = currentTimestamp.substring(0, 10);
    
        try (FileWriter dateWriter = new FileWriter("TicketApplication\\todaysSales.txt", true)) {
            String existingContent = readContent("TicketApplication\\todaysSales.txt");

            if (!existingContent.startsWith(currentDate)) {
                clearFile("TicketApplication\\todaysSales.txt");
                dateWriter.write(currentDate + "\n\n");
            }
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
            ex.printStackTrace();
        }

        try (FileWriter todaysSalesWriter = new FileWriter("TicketApplication\\todaysSales.txt", true)) {
            todaysSalesWriter.write(newTicket.substring(0, newTicket.length() - 1) + ", " + currentTimestamp.substring(10, 16) + "\n");
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String readContent(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    private static void clearFile(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("");
        }
    }
}