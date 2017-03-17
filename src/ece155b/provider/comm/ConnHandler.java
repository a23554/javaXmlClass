package ece155b.provider.comm;

import ece155b.common.Common;
import ece155b.common.Message;
import ece155b.provider.ProviderApp;
import ece155b.provider.xml.purchaseParser;
import java.net.*;
import java.util.Vector;

public class ConnHandler extends Thread {

    private int user = 0;
    private ServerSocket servers;
    Vector<ConnListener> handles;   // vector that keeps track of connected clients

    ProviderApp server;   // reference to main application 
    // ** you need to access doctor information

    public ConnHandler(ProviderApp proApp, int PortNo) {
        try {
            server = proApp;
            servers = new ServerSocket(PortNo);
            handles = new Vector<ConnListener>();
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Waiting connections...");
                Socket socket = servers.accept();
                System.out.println("Got one connection :) ");
                addUser(socket);
                System.out.println("Processed..");
            }
        } catch (Exception ex) {
        }
    }

    // add new client user
    public void addUser(Socket socket) {
        handles.addElement(new ConnListener(this, socket));
    }

    public boolean inUse() {
        return user > 0;
    }

    public ConnListener findUser(String distributorName) {
        return null;
    }

    // Find user and remove..
    public void removeUser(ConnListener client) {
        handles.remove(client);
    }

    public void broadcast(Message m) {
        System.out.println("BCAST #" + handles.size());

        if (handles.size() == 0) {
            System.out.println("No peer to broadcast");
        } else {
            for (int i = 0; i < handles.size(); i++) {
                ConnListener cl = (ConnListener) handles.elementAt(i);
                cl.sendMessage(m);
            }
        }
    }

    public synchronized void processMessage(String xml, ConnListener listener) throws InterruptedException {

        Message msg = new Message(xml);
        Message remsg = new Message();

        if (msg.type.equals(Common.BROADCAST)) {

        } else if (msg.type.equals(Common.AUTHENTICATE_DISTRIBUTOR)) {
            remsg.from = server.provider.companyName;
            remsg.type = Common.AUTHENTICATE_DISTRIBUTOR_REPLY;
            if (server.provider.isMember(msg.from)) {
                remsg.content = "Success";
            } else {
                remsg.content = "Failed";
            }
            listener.sendMessage(remsg);
        } else if (msg.type.equals(Common.REQUEST_SUPPLY_LIST)) {
            //Check if there is user asking t
            remsg.from = server.provider.companyName;
            remsg.type = Common.REQUEST_SUPPLY_LIST_REPLY;
            remsg.content = server.provider.stockItemList();
            
            listener.sendMessage(remsg);
        } else if (msg.type.equals(Common.REQUEST_PURCHASE)) {
            new purchaseParser(msg.content, server.provider);
            remsg.from = server.provider.companyName;
            remsg.content = "";

            if (server.provider.checkOut()) {
                remsg.type = Common.REQUEST_PURCHASE_REPLY;
                remsg.content = "Succes";
            } else {
                remsg.type = Common.REQUEST_PURCHASE_REPLY;
                remsg.content = "Failed";
            }
            listener.sendMessage(remsg);
        } else {
            System.out.println("Unknown message type");
        }

    }

}
