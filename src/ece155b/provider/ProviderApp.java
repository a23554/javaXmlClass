package ece155b.provider;

import ece155b.common.Contact;
import ece155b.common.Item;
import ece155b.common.StockItem;
import ece155b.provider.comm.ConnHandler;
import ece155b.provider.data.Provider;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProviderApp extends JFrame {

    /*
     You Should Set your own filename and path
     */
    private String filename;
    private String path;

    private File f;
    public Provider provider;
    private ConnHandler connhandler;

    public static void main(String[] args) {
        JFrame qq = new JFrame("haha");

        JPanel createNew = new JPanel();
        createNew.setBorder(BorderFactory.createTitledBorder("New Provider?"));
        JLabel newName = new JLabel("Name : ");
        JTextField tnewName = new JTextField(20);
        JButton createButton = new JButton("create");
        createButton.addActionListener((ActionEvent ee) -> {
            String name = tnewName.getText();
            ProviderApp providerApp = new ProviderApp(name);
            qq.setVisible(false);
        });
        createNew.add(newName);
        createNew.add(tnewName);
        createNew.add(createButton);
        qq.add(createNew);
        qq.setSize(400, 400);
        qq.setVisible(true);
        qq.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public ProviderApp(String url) {
        filename = url + ".xml";
        path = ".\\" + filename;
        if (new File(path).exists()) {
            JOptionPane.showMessageDialog(null, "Provider '" + url + "' already exists.\n Loading File instead...");
            f = new File(path);
            provider = new Provider(f);
            newGUI();
        } else {
            try {
                f = new File(path);
                f.createNewFile();
                JOptionPane.showMessageDialog(null, filename + " Created At : " + path);
                System.out.println(filename + " created");
                provider = new Provider(f);
                newGUI();
            } catch (Exception e) {

            }
        }
        connhandler = new ConnHandler(this, provider.operatingPort);
    }

    //New GUI here
    JTextField tcompanyName;
    JTextField tcompanyContact;
    JTextField tcompanyAddress;
    JTextField tport;
    JPanel companyInfoPanel;
    JButton save;

    //Component use in Stock Table
    JPanel stockInfo;
    JTable stockTable;
    DefaultTableModel stockDisplay;
    Vector<Vector<String>> stockData;
    Vector<String> stockTableHead;
    JButton removeRowButton;
    JButton addRowButton;

    //Component use in ContactList
    JPanel contactInfo;
    JList contactList;
    DefaultListModel contactDisplay;
    JButton addContactButton;
    JButton removeContactButton;

    private void newGUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        createCompanyInfo();
        createStockTable();
        createContactList();
        tabbedPane.addTab("CompnayInfo", companyInfoPanel);
        tabbedPane.addTab("Stocks", stockInfo);
        tabbedPane.addTab("Contacts", contactInfo);
        getContentPane().add(tabbedPane);
        setTitle("Provider - " + provider.companyName + " listening on Port " + provider.operatingPort);

        //After creating GUIs read out Provider information and Display
        refreshFrame();  //Use to refresh the frame 
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createCompanyInfo() {

        companyInfoPanel = new JPanel();
        companyInfoPanel.setLayout(new GridBagLayout());
        JLabel companyName = new JLabel("Company Name : ");
        GridBagConstraints gname = new GridBagConstraints();
        gname.gridx = 0;
        gname.gridy = 0;
        JLabel companyContact = new JLabel("Company Contact : ");
        GridBagConstraints gcontact = new GridBagConstraints();
        gcontact.gridx = 0;
        gcontact.gridy = 1;
        JLabel companyAddress = new JLabel("Company Address : ");
        GridBagConstraints gaddress = new GridBagConstraints();
        gaddress.gridx = 0;
        gaddress.gridy = 2;
        tcompanyName = new JTextField(15);
        GridBagConstraints gtname = new GridBagConstraints();
        gtname.gridx = 1;
        gtname.gridy = 0;
        tcompanyContact = new JTextField(15);
        GridBagConstraints gtcontact = new GridBagConstraints();
        gtcontact.gridx = 1;
        gtcontact.gridy = 1;
        tcompanyAddress = new JTextField(15);
        GridBagConstraints gtaddress = new GridBagConstraints();
        gtaddress.gridx = 1;
        gtaddress.gridy = 2;
        JLabel portlabel = new JLabel("Using port : ");
        GridBagConstraints gport = new GridBagConstraints();
        gport.gridx = 0;
        gport.gridy = 3;
        tport = new JTextField(6);
        GridBagConstraints gtport = new GridBagConstraints();
        gtport.gridx = 1;
        gtport.gridy = 3;
        gtport.anchor=GridBagConstraints.WEST;

        //Listener Saves the Value when the TextField is Off Focus
        FocusListener offFocusSave = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                provider.companyName = tcompanyName.getText();
                provider.companyContact = tcompanyContact.getText();
                provider.companyAddress = tcompanyAddress.getText();
                provider.operatingPort = Integer.valueOf(tport.getText());
            }
        };

        tcompanyName.addFocusListener(offFocusSave);
        tcompanyAddress.addFocusListener(offFocusSave);
        tcompanyContact.addFocusListener(offFocusSave);
        tport.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                provider.operatingPort = Integer.valueOf(tport.getText());
                provider.saveto(f);
                JOptionPane.showMessageDialog(null, "The port will only change untill the next time you start the app");
            }
            
        });

        //connhandler = new ConnHandler(this, provider.operatingPort);
        JPanel cField = new JPanel();
        cField.setLayout(new GridBagLayout());
        GridBagConstraints gcfield = new GridBagConstraints();
        gcfield.gridx = 0;
        gcfield.gridy = 0;
        cField.add(companyName, gname);
        cField.add(tcompanyName, gtname);
        cField.add(companyContact, gcontact);
        cField.add(tcompanyContact, gtcontact);
        cField.add(companyAddress, gaddress);
        cField.add(tcompanyAddress, gtaddress);
        cField.add(portlabel,gport);
        cField.add(tport,gtport);
        companyInfoPanel.add(cField, gcfield);

        companyInfoPanel.add(cField);
        
        JPanel but = new JPanel();
        GridBagConstraints gbut = new GridBagConstraints();
        gbut.gridx = 0;
        gbut.gridy = 2;
        save = new JButton("Save");
        save.addActionListener((ActionEvent ee) -> {
            provider.stockitems.clear();
            //Add Stock Items to Provider on Frame
            for (int i = 0; i < stockTable.getRowCount(); i++) {
                provider.stockitems.add(
                        new StockItem(
                                new Item(stockTable.getValueAt(i, 0).toString(),
                                        stockTable.getValueAt(i, 1).toString(),
                                        stockTable.getValueAt(i, 2).toString(),
                                        Integer.valueOf(stockTable.getValueAt(i, 3).toString())
                                ), Integer.valueOf(stockTable.getValueAt(i, 4).toString())
                        )
                );
            }
            //Add Contacts to Provider on Frame
            provider.distributors.clear();
            for (int i = 0; i < contactDisplay.getSize(); i++) {
                provider.addDistributorContact(new Contact(contactDisplay.elementAt(i).toString()));
                System.out.println("save" + contactDisplay.elementAt(i).toString());
            }
            provider.saveto(f);
        });
        but.add(save);
        companyInfoPanel.add(but,gbut);

    }

    private void createStockTable() {
        stockInfo = new JPanel();
        stockData = new Vector<Vector<String>>();
        stockTableHead = new Vector<String>();
        stockTableHead.addElement("ID");
        stockTableHead.addElement("Name");
        stockTableHead.addElement("Brand");
        stockTableHead.addElement("Price");
        stockTableHead.addElement("Quantity");
        stockDisplay = new DefaultTableModel(stockData, stockTableHead);
        stockTable = new JTable(stockDisplay);
        stockTable.setPreferredScrollableViewportSize(new Dimension(550, 250));

        removeRowButton = new JButton("Remove Row");
        addRowButton = new JButton("Add Row");

        removeRowButton.addActionListener((ActionEvent ee) -> {
            int select = stockTable.getSelectedRow();
            if (select == -1) {

            } else {
                stockDisplay.removeRow(select);
            }
        });

        stockInfo.add(new JScrollPane(stockTable));
        
        addRowButton.addActionListener((ActionEvent ee) -> {
            stockDisplay.addRow(new Vector());
            stockTable.revalidate();
        
        });
        
        stockInfo.add(removeRowButton);
        
        stockInfo.add(addRowButton);
    }

    private void createContactList() {
        contactInfo = new JPanel();
        contactInfo.setLayout(new GridBagLayout());
        contactDisplay = new DefaultListModel();
        JLabel clist=new JLabel("Contact List");
        GridBagConstraints gclist = new GridBagConstraints();
        gclist.gridx = 0; gclist.gridy = 0;
        contactList = new JList(contactDisplay);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        contactList.setLayoutOrientation(JList.VERTICAL);
        contactList.setVisibleRowCount(10);
        JScrollPane contactListScroller = new JScrollPane(contactList);
        contactListScroller.setPreferredSize(new Dimension(250, 80));
        GridBagConstraints gtclist = new GridBagConstraints();
        gtclist.gridx = 0; gtclist.gridy = 1;
        contactInfo.add(clist,gclist);
        contactInfo.add(contactListScroller,gtclist);

        addContactButton = new JButton("AddContant");
        GridBagConstraints gaddb = new GridBagConstraints();
        gaddb.gridx = 0; gaddb.gridy = 3;gaddb.anchor=GridBagConstraints.WEST;
        removeContactButton = new JButton("RemoveContant");
         GridBagConstraints gdelb = new GridBagConstraints();
        gdelb.gridx = 0; gdelb.gridy = 3;gdelb.anchor=GridBagConstraints.EAST;
        JTextField contactName = new JTextField(10);
        GridBagConstraints gtadd = new GridBagConstraints();
        gtadd.gridx = 0; gtadd.gridy = 2;gtadd.anchor=GridBagConstraints.EAST;
        JLabel concactl = new JLabel("Name:");
        GridBagConstraints gaddl = new GridBagConstraints();
        gaddl.gridx = 0; gaddl.gridy = 2;gaddl.anchor=GridBagConstraints.WEST;
        //Remove Contact Button
        removeContactButton.addActionListener((ActionEvent ee) -> {
            int index = contactList.getSelectedIndex();
            System.out.println(index);
            int size = contactDisplay.getSize();
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "No Contact Selected");
            } else {
                provider.removeDistributor(new Contact(contactList.getSelectedValue().toString()));
                refreshFrame();
            }
            if (size == 0) { //Nobody's left, disable firing.
                removeContactButton.setEnabled(false);

            } else { //Select an index.
                if (index == contactDisplay.getSize()) {
                    //removed item in last position
                    index--;
                }

                contactList.setSelectedIndex(index);
                contactList.ensureIndexIsVisible(index);
            }

        });
        //Add Contact Button
        addContactButton.addActionListener((ActionEvent e) -> {

            String name = contactName.getText();

            //User did not type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                contactName.requestFocusInWindow();
                contactName.selectAll();
                return;
            }

            int index = contactList.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            provider.addDistributorContact(new Contact(name));
            refreshFrame();
            //Reset the text field.
            contactName.requestFocusInWindow();
            contactName.setText("");

            //Select the new item and make it visible.
            contactList.setSelectedIndex(index);
            contactList.ensureIndexIsVisible(index);
        });

        //ADD the buttons and textfield to panel
        contactInfo.add(removeContactButton,gdelb);
        contactInfo.add(contactName,gtadd);
        contactInfo.add(addContactButton,gaddb);
        contactInfo.add(concactl,gaddl);
    }

    private boolean alreadyInList(String name) {
        for (int i = 0; i < contactDisplay.getSize(); i++) {
            if (name.equals(contactDisplay.elementAt(i).toString())) {
                return true;
            }
        }
        return false;
    }

    private void refreshFrame() {
        tcompanyName.setText(provider.companyName);
        tcompanyContact.setText(provider.companyContact);
        tcompanyAddress.setText(provider.companyAddress);
        tport.setText(String.valueOf(provider.operatingPort));

        //Read out all the Items info from provider to Frame
        while (stockDisplay.getRowCount() > 0) {
            stockDisplay.removeRow(stockDisplay.getRowCount() - 1);
        }
        for (int i = 0; i < provider.stockitems.size(); i++) {
            Vector row = new Vector<String>();
            row.addElement(provider.stockitems.elementAt(i).item.id);
            row.addElement(provider.stockitems.elementAt(i).item.name);
            row.addElement(provider.stockitems.elementAt(i).item.brand);
            row.addElement(provider.stockitems.elementAt(i).item.price);
            row.addElement(provider.stockitems.elementAt(i).quantity);
            stockDisplay.addRow(row);
        }
        stockDisplay.fireTableDataChanged();

        //Read out all the Contact info from provider to Frame
        contactDisplay.removeAllElements();
        for (int i = 0; i < provider.distributors.size(); i++) {
            contactDisplay.addElement(provider.distributors.elementAt(i).name);
            //System.out.println("Add" + provider.distributors.elementAt(i).name);
        }
    }
}
