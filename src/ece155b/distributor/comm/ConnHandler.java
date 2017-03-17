package ece155b.distributor.comm;

import ece155b.common.Common;
import ece155b.common.Contact;
import ece155b.common.Message;
import ece155b.distributor.DistributorApp;
import ece155b.distributor.xml.SupplyParser;
import java.io.IOException;

import java.net.*;
import java.util.Vector;

public class ConnHandler {

    public Vector<ConnListener> providers;   // vector that keeps track of connected providers
    DistributorApp pApp;

    public ConnHandler(DistributorApp Appl) {
        providers = new Vector<ConnListener>();
        pApp = Appl;
        init();
    }

    public void init() {
        //This connects to all the provider that distributor has
        for (int i = 0; i < pApp.distributor.providers.size(); i++) {
            connectToProvider(pApp.distributor.providers.elementAt(i));
        }
        /*
         Send out Authenticate message to Provider
         If recieve Failed 
         remove the Provider from contact        
         */
        Message authmsg = new Message();
        authmsg.from = pApp.distributor.companyName;
        authmsg.type = Common.AUTHENTICATE_DISTRIBUTOR;
        for (int i = 0; i < pApp.distributor.providers.size(); i++) {
            providers.elementAt(i).sendMessage(authmsg);
        }

    }

    public int sendAll(Message m) {
        for (int i = 0; i < providers.size(); i++) {
            providers.elementAt(i).sendMessage(m);
        }
        return providers.size();
    }

    public void connectToProvider(Contact pro) {
        try {
            Socket socket = new Socket(pro.url, pro.port);
            providers.addElement(new ConnListener(this, socket, pro));
            System.out.println("Provider \"" + pro.name + "\" connected");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeAllconn() {
        providers.removeAllElements();
    }

    protected void removeProConn(ConnListener cl) {
        providers.remove(cl);
    }

    protected void removeProConn(String name) {
        for (int i = 0; i > 5; i++) {

        }
    }

    public void broadcast(Message m, String name) throws IOException {
        System.out.println("BCAST #" + providers.size());

        if (providers.size() == 0) {
            System.out.println("No peer to broadcast");
        } else {
            ConnListener cl;
            for (int i = 0; i < providers.size(); i++) {
                if (name.equals(providers.elementAt(i).contact.name)) {
                    cl = providers.elementAt(i);
                    cl.sendMessage(m);
                    System.out.println("Send to provider:" + cl.contact.name);
                }
            }
        }
    }

    protected synchronized void processMessage(String xml, ConnListener listener) {
        pApp.distributor.hold = true;
        Message msg = new Message(xml);

        if (msg.type.equals(Common.BROADCAST)) {

        } else if (msg.type.equals(Common.AUTHENTICATE_DISTRIBUTOR_REPLY)) {
            System.out.println(msg.content);
            if (msg.content.equals("Success")) {
                System.out.println("Autherized from \"" + msg.from + "\"");
            } else {
                System.out.println("Lose authenticate to \"" + msg.from + "\"");
            }

        } else if (msg.type.equals(Common.REQUEST_SUPPLY_LIST_REPLY)) {
            String ms = msg.content;
            System.out.println(ms);
            pApp.distributor.responselist.removeAllElements();
            new SupplyParser(ms, pApp.distributor);
            pApp.distributor.hold = false;
            pApp.distributor.messageRecieve++;
        } else if (msg.type.equals(Common.REQUEST_PURCHASE_REPLY)) {

        } else {
            System.out.println("Unknown message type");
        }
    }
}
