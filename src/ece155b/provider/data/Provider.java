package ece155b.provider.data;

import ece155b.common.Contact;
import ece155b.common.Item;
import ece155b.common.StockItem;
import ece155b.provider.xml.ProParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Provider {

    public String companyName;
    public String companyAddress;
    public String companyContact;

    public int operatingPort;
    public Vector<Contact> distributors;
    public Vector<StockItem> stockitems;
    public Vector<StockItem> sellItems;

    public Provider() {
        distributors = new Vector<Contact>();
        stockitems = new Vector<StockItem>();
        sellItems = new Vector<StockItem>();
    }

    public Provider(File f) {
        distributors = new Vector<Contact>();
        stockitems = new Vector<StockItem>();
        sellItems = new Vector<StockItem>();

        new ProParser(f, this);
    }

    public void addStock(Item item, int quant) {
        stockitems.addElement(new StockItem(item, quant));
    }

    public void addStock(StockItem stockitem) {
        stockitems.addElement(stockitem);
    }

    public void addPurchase(StockItem stockitem) {
        sellItems.addElement(stockitem);
    }

    public boolean checkOut() {
        for (int sell = 0; sell < sellItems.size(); sell++) {
            for (int stock = 0; stock < stockitems.size(); stock++) {
                if (sellItems.elementAt(sell).item.equals(stockitems.elementAt(stock).item)) {
                    stockitems.elementAt(stock).quantity -= sellItems.elementAt(sell).quantity;                   
                }
            }
        }
        if(sellItems.size()!=0){
            sellItems.clear();
            return false;
        }
        sellItems.clear();
        return true;
    }

    public void removePurchase(StockItem stockItem) {
        for (int i = 0; i < sellItems.size(); i++) {
            if (sellItems.elementAt(i).equals(stockItem)) {
                sellItems.removeElementAt(i);
            }
        }
    }

    public void addDistributorContact(Contact distributor) {
        distributors.addElement(distributor);
    }

    public void removeDistributor(Contact distributor) {
        for (int i = 0; i < distributors.size(); i++) {
            if (distributors.elementAt(i).name.equals(distributor.name)) {
                distributors.removeElementAt(i);
            }
        }
    }

    public boolean isMember(String name) {
        // Check if distributor exists
        for (int i = 0; i < distributors.size(); i++) {
            if (name.equals(distributors.elementAt(i).name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String str = "";
        str += "|Provider\n";
        str += "||Information\n";
        str += "|||CompanyName:\t" + companyName + "\n";
        str += "|||CompanyContact:\t" + companyContact + "\n";
        str += "|||CompanyAddress:\t" + companyAddress + "\n";
        str += "|||OperatingPort:\t" + operatingPort + "\n";
        str += "||/Information\n";
        // Stocks
        str += "||Stock\n";
        for (int i = 0; i < stockitems.size(); i++) {
            str += stockitems.elementAt(i).toString();
        }
        str += "||/Stock\n";

        //ProviderContacts
        str += "||DistributorContact\n";
        for (int i = 0; i < distributors.size(); i++) {
            str += distributors.elementAt(i).toString();
        }
        str += "||/DistributorContact\n";
        str += "|/Provider\n";
        return str;
    }

    public String stockItemList() {
        String xml = "";
        xml += "<Stock>";
        for (int i = 0; i < stockitems.size(); i++) {
            xml += stockitems.elementAt(i).toXML();
        }
        xml += "</Stock>";
        return xml;
    }

    public String toXML() {
        String xml = "<Provider>";
        xml += "<Information>";
        xml += "<CompanyName>" + companyName + "</CompanyName>";
        xml += "<CompanyAddress>" + companyAddress + "</CompanyAddress>";
        xml += "<CompanyContact>" + companyContact + "</CompanyContact>";
        xml += "<OperatingPort>" + operatingPort + "</OperatingPort>";
        xml += "</Information>";

        xml += "<Stock>";
        for (int i = 0; i < stockitems.size(); i++) {
            xml += stockitems.elementAt(i).toXML();
        }
        xml += "</Stock>";

        xml += "<DistributorContact>";
        for (int i = 0; i < distributors.size(); i++) {
            xml += distributors.elementAt(i).toXML();
        }
        xml += "</DistributorContact>";
        xml += "</Provider>";
        return xml;
    }

    public void createTestInfo() {
        companyName = "HTC";
        companyContact = "John";
        companyAddress = "LA";
        operatingPort = 5555;
        addStock(new StockItem(new Item("FR1", "pineapple", companyName, 500), 6000));
        addStock(new StockItem(new Item("FR2", "apple", companyName, 25), 500));
        addStock(new StockItem(new Item("EC1", "Iphone", companyName, 20000), 40));

        addDistributorContact(new Contact("MC"));
    }

    public void saveto(File f) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(f));
            br.write("<?xml version='1.0' ?>");
            br.write(this.toXML());
            br.close();
            JOptionPane.showMessageDialog(null, "Succesfully saved", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            System.out.println("Exception at write: " + ex);
        }
    }

    public void loadfrom(File f) {
        new ProParser(f, this);
    }

}
