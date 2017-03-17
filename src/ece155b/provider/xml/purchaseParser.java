package ece155b.provider.xml;

import ece155b.common.Contact;
import ece155b.common.Item;
import ece155b.common.Message;
import ece155b.common.StockItem;
import ece155b.provider.data.Provider;
import java.io.File;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class purchaseParser extends DefaultHandler {

    private final StringBuffer accumulator = new StringBuffer();

    public Provider provider;
    private Item item;
    private StockItem stockitem;
    private Contact distributor;

    public purchaseParser(String xml, Provider p) {
        try {
            provider = p;
            SAXParserFactory factory = SAXParserFactory.newInstance(  );
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(xml)), this);
        } catch (Exception ex) {
            System.out.println("Parsing error: "+ex);
        }
    }

    @Override
    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
    }

    @Override
    public void startElement(String uri, String lname, String name, Attributes attributes) {
        accumulator.setLength(0);
        switch (name) {
            case "StockItem":
                stockitem = new StockItem();
                break;
            case "Item":
                item = new Item();
                break;
            case "Contact":
                distributor = new Contact();
                break;
               
        }
    }

    @Override
    public void endElement(String uri, String lname, String name) {
        String value = accumulator.toString();
        switch (name) {
            case "ItemID":
                item.id = value;
                break;
            case "ItemName":
                item.name = value;
                break;
            case "ItemBrand":
                item.brand = value;
                break;
            case "ItemPrice":
                item.price = Integer.valueOf(value);
                break;
            case "Item":
                stockitem.item = item;
                break;
            case "Quantity":
                stockitem.quantity = Integer.valueOf(value);
                break;
            case "StockItem":
                provider.addPurchase(stockitem);
                break;
           
        }
    }

}
