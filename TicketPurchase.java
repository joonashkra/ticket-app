package TicketApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TicketPurchase extends JPanel {
    private int price;
    private int quantity;
    private JLabel typeLabel;
    private JLabel priceLabel;
    private JLabel quantityLabel;
    private JComboBox<String> discountTypeSelection;
    private JTextField selectedDiscountType;
    private JButton addToCartButton;
    private JLabel errorMessage = new JLabel();
    private JTextField nameTextField;
    private JTextField parentsPhoneTextField;
    private List<String> purchasedTickets;
    private List<String> childrenTicketData = new ArrayList<>();

    public TicketPurchase(String ticketType, int price) {
        this.price = price;
        this.quantity = 0;
        this.purchasedTickets = new ArrayList<>();

        typeLabel = new JLabel(ticketType);
        priceLabel = new JLabel("Price: " + price + "€");
        quantityLabel = new JLabel("Quantity: " + quantity);
        errorMessage.setText("");

        String[] discountTypes = {"Select discount:", "Student", "Military"};
        discountTypeSelection = new JComboBox<>(discountTypes);

        selectedDiscountType = new JTextField(15);
        selectedDiscountType.setEditable(false);

        discountTypeSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDiscountType.setText(getDiscountType());
            }
        });

        nameTextField = new JTextField(15);
        parentsPhoneTextField = new JTextField(15);

        addToCartButton = new JButton("Add to cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Discount Ticket".equals(ticketType)) {
                    purchaseDiscountTicket();
                } 
                else if ("Children's Ticket".equals(ticketType)) {
                    purchaseChildrenTicket();
                } 
                else {
                    purchaseTicket();
                }
            }
        });

        setLayout(new GridLayout(10, 1));

        add(typeLabel);
        add(priceLabel);
        add(quantityLabel);

        if (ticketType.equals("Children's Ticket")) {
            add(nameTextField);
            add(parentsPhoneTextField);
        }

        if (ticketType.equals("Discount Ticket")) {
            add(errorMessage);
            add(discountTypeSelection);
        }

        add(addToCartButton);
    }

    private void purchaseDiscountTicket() {
        String selectedDiscountType = getDiscountType();
        if ("Select discount:".equals(selectedDiscountType)) {
            add(errorMessage);
            errorMessage.setText("Select a discount type.");
        } else {
            quantity++;
            quantityLabel.setText("Quantity: " + quantity);
            errorMessage.setText("");
            purchasedTickets.add(getDiscountType());
            discountTypeSelection.setSelectedIndex(0);
        }
    }

    private void purchaseChildrenTicket() {
        if (nameTextField.getText().equals("") || parentsPhoneTextField.getText().equals("")) {
            add(errorMessage);
            errorMessage.setText("Fill in required fields.");
        } else {
            quantity++;
            quantityLabel.setText("Quantity: " + quantity);
            errorMessage.setText("");

            String childrenTicketInput = nameTextField.getText() + ", " + parentsPhoneTextField.getText();
            childrenTicketData.add(childrenTicketInput);

            nameTextField.setText("");
            parentsPhoneTextField.setText("");
        }
    }

    private void purchaseTicket() {
        quantity++;
        quantityLabel.setText("Quantity: " + quantity);
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public String getTicketType() {
        return typeLabel.getText();
    }

    public String getDiscountType() {
        return (String) discountTypeSelection.getSelectedItem();
    }

    public List<String> getPurchasedTickets() {
        return purchasedTickets;
    }

    public List<String> getChildrenData() {
        return childrenTicketData;
    }
    
    public String getChildrenData(int index) {
        if (index >= 0 && index < childrenTicketData.size()) {
            return childrenTicketData.get(index);
        } else {
            return "";
        }
    }
    
    public void reset() {
        quantity = 0;
        quantityLabel.setText("Quantity: " + quantity);

        discountTypeSelection.setSelectedItem("Select discount:");
        selectedDiscountType.setText("");
        nameTextField.setText("");
        parentsPhoneTextField.setText("");

        errorMessage.setText("");
        purchasedTickets.clear();
        childrenTicketData.clear();
    }
}