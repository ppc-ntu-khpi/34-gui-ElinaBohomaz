package com.mybank.gui;

import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class SWINGDemo {
    
    private final JEditorPane log;
    private final JEditorPane reportPane;
    private final JButton show;
    private final JButton report;
    private final JComboBox clients;
    
    public SWINGDemo() {
        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(250, 150));
        log.setEditable(false);
        
        reportPane = new JEditorPane("text/html", "");
        reportPane.setPreferredSize(new Dimension(250, 150));
        reportPane.setEditable(false);
        
        show = new JButton("Show");
        report = new JButton("Report");
        clients = new JComboBox();
        
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            clients.addItem(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }
    }
    
    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());
        
        // Create top panel with controls
        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 3));
        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);
        
        // Create split pane for log and report
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(log),
                new JScrollPane(reportPane));
        splitPane.setResizeWeight(0.5);
        
        frame.add(cpane, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
        
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer current = Bank.getCustomer(clients.getSelectedIndex());
                String accType = current.getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";                
                String custInfo = "<html><body style='padding:10px;'>" +
                        "<b><span style='font-size:1.2em;'>" + current.getLastName() + ", " +
                        current.getFirstName() + "</span></b><hr>" +
                        "<b>Account Type: </b>" + accType + "<br>" +
                        "<b>Balance: </b><span style='color:red;'>$" + 
                        current.getAccount(0).getBalance() + "</span></body></html>";
                log.setText(custInfo);                
            }
        });
        
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder reportText = new StringBuilder();
                reportText.append("<html><body style='padding:10px;'>");
                reportText.append("<h2>Customer Report</h2>");
                reportText.append("<table border='1' cellpadding='5'>");
                reportText.append("<tr><th>#</th><th>Name</th><th>Account Type</th><th>Balance</th></tr>");
                
                for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
                    Customer cust = Bank.getCustomer(i);
                    String accType = cust.getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";
                    reportText.append("<tr>")
                            .append("<td>").append(i + 1).append("</td>")
                            .append("<td>").append(cust.getLastName()).append(", ").append(cust.getFirstName()).append("</td>")
                            .append("<td>").append(accType).append("</td>")
                            .append("<td style='color:red;'>$").append(cust.getAccount(0).getBalance()).append("</td>")
                            .append("</tr>");
                }
                
                reportText.append("</table>");
                reportText.append("<p>Total customers: ").append(Bank.getNumberOfCustomers()).append("</p>");
                reportText.append("</body></html>");
                
                reportPane.setText(reportText.toString());
            }
        });
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setResizable(true);
        frame.setVisible(true);        
    }
    
    public static void main(String[] args) {
        Bank.addCustomer("John", "Doe");
        Bank.addCustomer("Fox", "Mulder");
        Bank.addCustomer("Dana", "Scully");
        Bank.getCustomer(0).addAccount(new CheckingAccount(2000));
        Bank.getCustomer(1).addAccount(new SavingsAccount(1000, 3));
        Bank.getCustomer(2).addAccount(new CheckingAccount(1000, 500));
        
        SWINGDemo demo = new SWINGDemo();        
        demo.launchFrame();
    }
}