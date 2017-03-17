package ece155b.distributor.data;

import ece155b.common.Common;
import ece155b.common.StockItem;
import ece155b.common.Contact;
import ece155b.common.Item;
import ece155b.common.Message;
import ece155b.distributor.DistributorApp;
import ece155b.distributor.xml.DisParser;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Distributor {

    public String companyName;
    public String companyAddress;
    public String companyContact;
    public int messageNumber;
    public int messageRecieve;
    public Vector<StockItem> stockitems;
    public Vector<StockItem> puchaseitems;
    public Vector<Contact> providers;

    public Vector<StockItem> responselist;

    public Vector<Vector<String>> responseData;
    public boolean allServerResponse;
    public boolean hold;

    public Distributor() {
        providers = new Vector<Contact>();
        stockitems = new Vector<StockItem>();
        puchaseitems = new Vector<StockItem>();
    }

    public Distributor(File f, DistributorApp d) {
        providers = new Vector<Contact>();
        stockitems = new Vector<StockItem>();
        puchaseitems = new Vector<StockItem>();
        responseData = new Vector<Vector<String>>();
        responselist = new Vector<StockItem>();
        new DisParser(f, this);
    }

    public void addResponseData(StockItem stockitem) {
        responselist.addElement(stockitem);
        System.out.println("Add" + stockitem.item.name);
    }

    public void addStock(StockItem stockitem) {
        stockitems.addElement(stockitem);
    }

    public void addStock(Item item, int quant) {
        stockitems.add(new StockItem(item, quant));
    }

    public void removeStockItem(StockItem stockitem) {
        for (int i = 0; i < stockitems.size(); i++) {
            if (stockitems.elementAt(i).equals(stockitem)) {
                stockitems.removeElementAt(i);
            }
        }
    }

    public void addProviderContact(Contact provider) {
        providers.addElement(provider);
    }

    public void removeProviderContact(Contact contact) {
        for (int i = 0; i < providers.size(); i++) {
            if (providers.elementAt(i).equals(contact)) {
                providers.removeElementAt(i);
            }
        }
    }

    public void addRow(DistributorApp dis) {
        getItemLists(dis);
        while (hold) {
            messageRecieve++;
        }
        if (!hold) {
            responseData = new Vector<Vector<String>>();
            System.out.println(responselist.size());
            for (int i = 0; i < responselist.size(); i++) {
                Vector<String> row = new Vector<String>();
                row.add(responselist.elementAt(i).item.id);
                row.add(responselist.elementAt(i).item.name);
                row.add(responselist.elementAt(i).item.brand);
                row.add(String.valueOf(responselist.elementAt(i).item.price));
                responseData.add(row);
            }

            JFrame response = new JFrame();
            JPanel main = new JPanel();
            JButton addapp = new JButton("Add to ");
            Vector<String> responseHead = new Vector<String>();
            responseHead.addElement("ID");
            responseHead.addElement("Name");
            responseHead.addElement("Brand");
            responseHead.addElement("Price");
            DefaultTableModel responseDisplay = new DefaultTableModel(responseData, responseHead);
            JTable responseTable = new JTable(responseDisplay);
            addapp.addActionListener((ActionEvent ee) -> {
                int i = responseTable.getSelectedRow();
                puchaseitems.addElement(new StockItem(
                        new Item(responseTable.getValueAt(i, 0).toString(),
                                responseTable.getValueAt(i, 1).toString(),
                                responseTable.getValueAt(i, 2).toString(),
                                Integer.valueOf(responseTable.getValueAt(i, 3).toString())
                        ), 0));
                response.dispose();
            });
            responseTable.setPreferredScrollableViewportSize(new Dimension(600, 250));
            main.add(new JScrollPane(responseTable));
            main.add(addapp);
            response.add(main);
            response.setSize(600, 350);
            response.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "SomeServerIsLockDown");
        }
    }

    private void getItemLists(DistributorApp dis) {
        startMultipleMessage();
        Message listmsg = makeMessage(Common.REQUEST_SUPPLY_LIST);
        messageNumber = dis.handler.sendAll(listmsg);
    }

    private void startMultipleMessage() {
        allServerResponse = true;
        messageNumber = 0;
        messageRecieve = 0;
    }

    public Message makeMessage(String type) {
        Message msg = new Message(companyName);
        msg.from = companyName;
        if (type.equals(Common.REQUEST_SUPPLY_LIST)) {
            msg.type = Common.REQUEST_SUPPLY_LIST;
            return msg;
        }
        return null;
    }

    public void getAllResponse(Boolean status) {
        allServerResponse = status;
    }

    //Create Test informations for coding convience
    public void createTestInfo() {
        companyName = "chips";
        companyAddress = "AH ha";
        companyContact = "GGG";

        //Create two fake contact into distributor
        Contact contact = new Contact();
        contact.url = "localhost";
        contact.port = 1111;
        contact.name = "HTC";
        addProviderContact(contact);

        contact = new Contact();
        contact.url = "localhost";
        contact.port = 2222;
        contact.name = "Sony";
        addProviderContact(contact);

        //Create som fake Items and show two ways to create new Item and StockItem
        Item item = new Item();
        item.id = "001";
        item.brand = "Sony";
        item.name = "banana";
        item.price = 300;
        StockItem sitem = new StockItem();
        sitem.item = item;
        sitem.quantity = 50;
        addStock(sitem);

        Item item2 = new Item("002", "mango", "HTC", 350);
        StockItem sitem2 = new StockItem(item2, 500);
        addStock(sitem2);
    }

    /**
     *
     * @param f
     * @param dis
     */
    public void saveto(File f) {
        try {
            try (BufferedWriter br = new BufferedWriter(new FileWriter(f))) {
                br.write("<?xml version='1.0' ?>");
                br.write(this.toXML());
            }
            JOptionPane.showMessageDialog(null, "Succesfully saved to " + f.getPath(), "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | HeadlessException ex) {
            System.out.println("Exception at write: " + ex);
        }
    }

    @Override
    public String toString() {
        String str = "";

        str += "|Distributor\n";
        str += "||Information\n";
        str += "|||CompanyName:\t" + companyName + "\n";
        str += "|||CompanyContact:\t" + companyContact + "\n";
        str += "|||CompanyAddress:\t" + companyAddress + "\n";
        str += "||/Information\n";

        // Stocks
        str += "||Stock\n";
        for (int i = 0; i < stockitems.size(); i++) {
            str += stockitems.elementAt(i).toString();
        }
        str += "||/Stock\n";

        //ProviderContacts
        str += "||ProviderContact\n";
        for (int i = 0; i < providers.size(); i++) {
            str += providers.elementAt(i).toString();
        }
        str += "||/ProviderContact\n";

        str += "|/Distributor\n";
        return str;
    }

    public String toXML() {
        String xml = "";
        xml += "<Distributor>";

        xml += "<Information>";
        xml += "<CompanyName>" + companyName + "</CompanyName>";
        xml += "<CompanyContact>" + companyContact + "</CompanyContact>";
        xml += "<CompanyAddress>" + companyAddress + "</CompanyAddress>";
        xml += "</Information>";

        // Stocks
        xml += "<Stock>";
        for (int i = 0; i < stockitems.size(); i++) {
            xml += stockitems.elementAt(i).toXML();
        }
        xml += "</Stock>";

        //ProviderContacts
        xml += "<ProviderContact>";
        for (int i = 0; i < providers.size(); i++) {
            xml += providers.elementAt(i).toXML();
        }
        xml += "</ProviderContact>";

        xml += "</Distributor>";
        return xml;
    }
}
