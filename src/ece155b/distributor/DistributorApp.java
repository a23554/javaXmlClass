package ece155b.distributor;

import ece155b.common.Common;
import ece155b.common.Contact;
import ece155b.common.Message;
import ece155b.distributor.comm.ConnHandler;
import ece155b.distributor.comm.ConnListener;
import ece155b.distributor.data.Distributor;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DistributorApp extends JFrame {

    /*
     You Should Set your own filename and path
     */
    File f;
    private String filename;
    private String path;

    public Distributor distributor;
    public Contact contact ;
    public final ConnHandler handler;

    public static void main(String[] args) {
        JFrame qq = new JFrame("haha");
        JPanel createNew = new JPanel();
        createNew.setBorder(BorderFactory.createTitledBorder("New Distributor?"));
        JLabel newName = new JLabel("Name : ");
        JTextField tnewName = new JTextField(20);
        JButton createButton = new JButton("create");
        createButton.addActionListener((ActionEvent ee) -> {
            String name = tnewName.getText();
            DistributorApp distributorApp = new DistributorApp(name);
            qq.setVisible(false);
        });
        createNew.add(newName);
        createNew.add(tnewName);
        createNew.add(createButton);
        qq.add(createNew);
        qq.setSize(400, 400);
        qq.setVisible(true);
        qq.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public DistributorApp(String url) {
        //Sets up Distributor informations
        filename = url + ".xml";
        path = ".\\" + filename;
        if (new File(path).exists()) {
            JOptionPane.showMessageDialog(null, "Provider '" + url + "' already exists.\n Loading File instead...");
            f = new File(path);
            distributor = new Distributor(f, this);
            distributor.addProviderContact(new Contact("sun","127.0.0.1",6666));
            GUI();
            //refreshFrame();
        } else {
            try {
                f = new File(path);
                f.createNewFile();
                JOptionPane.showMessageDialog(null, filename + " Created At : " + path);
                System.out.println(filename + " created");
                distributor = new Distributor(f, this);
                GUI();
            } catch (Exception e) {

            }
        }
        //Add an ConnectionHandler to this APP
        handler = new ConnHandler(this);

        //handler.connectToProvider(contact);
    }

    JTextField tcompanyName;
    JTextField tcompanyContact;
    JTextField tcompanyAddress;
    JPanel companyInfoPanel;
    JPanel connect;
    JPanel stockInfo;
    JPanel needInfo;
    DefaultTableModel stockDisplay;
    DefaultTableModel needDisplay;
    Vector<Vector<String>> stockData;
    Vector<Vector<String>> needData;
    Vector<String> stockTableHead;
    Vector<String> needTableHead;
    JButton save;
    JButton stockadd = new JButton("Add a row");
    JButton stockdel = new JButton("Delete selected row");
    JButton needadd = new JButton("Add a row");
    JButton needdel = new JButton("Delete selected row");
    DefaultListModel listmodel = new DefaultListModel();
    JTextArea texta;

    private void GUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        createCompanyInfo();
        createStockTable();
        careateNeedTable();
        crearConeectTable();
        tabbedPane.addTab("CompnayInfomation", companyInfoPanel);
        tabbedPane.addTab("Connect", connect);
        tabbedPane.addTab("Stock Items", stockInfo);
        tabbedPane.addTab("Need Items", needInfo);
        getContentPane().add(tabbedPane);

        setTitle(distributor.companyName + " - Distributor Application");
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
        tcompanyName.setText(distributor.companyName);
        GridBagConstraints gtname = new GridBagConstraints();
        gtname.gridx = 1;
        gtname.gridy = 0;
        tcompanyContact = new JTextField(15);
        tcompanyContact.setText(distributor.companyContact);
        GridBagConstraints gtcontact = new GridBagConstraints();
        gtcontact.gridx = 1;
        gtcontact.gridy = 1;
        tcompanyAddress = new JTextField(15);
        tcompanyAddress.setText(distributor.companyAddress);
        GridBagConstraints gtaddress = new GridBagConstraints();
        gtaddress.gridx = 1;
        gtaddress.gridy = 2;

        FocusListener offFocusSave = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                distributor.companyName = tcompanyName.getText();
                distributor.companyContact = tcompanyContact.getText();
                distributor.companyAddress = tcompanyAddress.getText();
                refreshFrame();
                System.out.print(distributor.toString());
            }
        };

        tcompanyName.addFocusListener(offFocusSave);
        tcompanyAddress.addFocusListener(offFocusSave);
        tcompanyContact.addFocusListener(offFocusSave);

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
        companyInfoPanel.add(cField, gcfield);

        JPanel but = new JPanel();
        GridBagConstraints gbut = new GridBagConstraints();
        gbut.gridx = 0;
        gbut.gridy = 2;
        save = new JButton("Save");
        save.addActionListener((ActionEvent ee) -> {
            distributor.saveto(f);
            System.out.println("saved");
        });
        but.add(save);
        companyInfoPanel.add(but, gbut);
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
        JTable stockTable = new JTable(stockDisplay);
        stockTable.setPreferredScrollableViewportSize(new Dimension(600, 250));
        stockInfo.add(new JScrollPane(stockTable));
        stockInfo.add(stockadd);
        stockInfo.add(stockdel);
    }

    private void careateNeedTable() {
        needInfo = new JPanel();
        needData = new Vector<Vector<String>>();
        needTableHead = new Vector<String>();
        needTableHead.addElement("ID");
        needTableHead.addElement("Name");
        needTableHead.addElement("Brand");
        needTableHead.addElement("Price");
        needTableHead.addElement("Quantity");
        needDisplay = new DefaultTableModel(needData, needTableHead);
        JTable needTable = new JTable(needDisplay);
        needTable.setPreferredScrollableViewportSize(new Dimension(600, 250));
        needInfo.add(new JScrollPane(needTable));

        needadd.addActionListener((ActionEvent ee) -> {
            distributor.addRow(this);
        });
        needdel.addActionListener((ActionEvent ee) -> {
            DefaultTableModel tableModel = (DefaultTableModel) needTable.getModel();
              if(needTable.getRowCount()>0 && needTable.getSelectedRow()!=-1)
            tableModel.removeRow(needTable.getSelectedRow());
        else
            JOptionPane.showMessageDialog(null, "Please select a row! ", "Error",JOptionPane.ERROR_MESSAGE);
        });
       

        needInfo.add(needadd);
        needInfo.add(needdel);
    }

    private void refreshFrame() {
        tcompanyName.setText(distributor.companyName);
        tcompanyContact.setText(distributor.companyContact);
        tcompanyAddress.setText(distributor.companyAddress);

        //Read out all the Items info from distributor to Frame
        while (stockDisplay.getRowCount() > 0) {
            stockDisplay.removeRow(stockDisplay.getRowCount() - 1);
        }
        for (int i = 0; i < distributor.stockitems.size(); i++) {
            Vector row = new Vector<String>();
            row.addElement(distributor.stockitems.elementAt(i).item.id);
            row.addElement(distributor.stockitems.elementAt(i).item.name);
            row.addElement(distributor.stockitems.elementAt(i).item.brand);
            row.addElement(distributor.stockitems.elementAt(i).item.price);
            row.addElement(distributor.stockitems.elementAt(i).quantity);
            stockDisplay.addRow(row);
        }
        stockDisplay.fireTableDataChanged();

        //Read out all the NeedItem info from distributor to Frame
        while (needDisplay.getRowCount() > 0) {
            needDisplay.removeRow(needDisplay.getRowCount() - 1);
        }
        for (int i = 0; i < distributor.puchaseitems.size(); i++) {
            Vector row = new Vector<String>();
            row.addElement(distributor.puchaseitems.elementAt(i).item.id);
            row.addElement(distributor.puchaseitems.elementAt(i).item.name);
            row.addElement(distributor.puchaseitems.elementAt(i).item.brand);
            row.addElement(distributor.puchaseitems.elementAt(i).item.price);
            row.addElement(distributor.puchaseitems.elementAt(i).quantity);
            needDisplay.addRow(row);
        }
        needDisplay.fireTableDataChanged();
    }

    private void crearConeectTable() {
        connect = new JPanel();
        connect.setLayout(new BorderLayout());
        JPanel showport = new JPanel();
        showport.setLayout(new BorderLayout());
        JButton refresh = new JButton("Provider Refresh");
        JList portlist = new JList(listmodel);
        portlist.setVisible(true);
        portlist.setVisibleRowCount(17);
        JScrollPane listscroll = new JScrollPane(portlist);
        // listscroll.setMaximumSize(new Dimension(10, 10));
        portlist.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof ConnListener) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((ConnListener) value).contact.name);
                }
                return renderer;
            }
        });
        showport.add(listscroll, BorderLayout.NORTH);
        showport.add(refresh, BorderLayout.SOUTH);

        texta = new JTextArea();
        JScrollPane scroll = new JScrollPane(texta);
        texta.setLineWrap(true);
        texta.setWrapStyleWord(true);

        JButton testme = new JButton("Send message");
        testme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (portlist.getSelectedValue() != null) {
                    //int port=Integer.valueOf(portlist.getSelectedValue().toString());
                    Message m = new Message();
                    m.type = Common.AUTHENTICATE_DISTRIBUTOR;
                    m.content = "";
                    m.from = distributor.companyName;
                    try {
                        handler.broadcast(m, portlist.getSelectedValue().toString());
                    } catch (IOException ex) {
                        Logger.getLogger(DistributorApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You didn't choose a provider  ");
                }
            }
        });
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                append("\nRefreshing provider list...");
                listmodel.clear();
                try {
                    handler.removeAllconn();
                    for (int i = 0; i < distributor.providers.size(); i++) {
                        handler.connectToProvider(distributor.providers.elementAt(i));
                    }
                    for (int i = 0; i < handler.providers.size(); i++) {
                        listmodel.addElement(handler.providers.elementAt(i));
                    }
                } catch (Exception e) {
                } finally {
                    texta.append("\nRefresh complete");
                }
            }
        });
        connect.add(showport, BorderLayout.WEST);
        connect.add(scroll, BorderLayout.CENTER);
        connect.add(testme, BorderLayout.SOUTH);
    }

    public void append(String str) {
        texta.append("\n" + str);
    }
}
