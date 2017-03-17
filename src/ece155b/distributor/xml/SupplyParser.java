package ece155b.distributor.xml;

import ece155b.common.Contact;
import ece155b.common.Item;
import ece155b.distributor.data.Distributor;
import ece155b.common.StockItem;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class SupplyParser extends DefaultHandler {

    private StringBuffer accumulator = new StringBuffer();
    public Distributor distributor;
    private Contact provider;
    private StockItem stockitem;
    private Item item;

    /**
     *
     * @param file
     * @param dis
     */
    public SupplyParser(String xml, Distributor dis) {
        try {
            distributor = dis;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(xml)), this);
        } catch (Exception ex) {
            ex.printStackTrace();
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
                item=new Item();
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
                distributor.addResponseData(stockitem);
                break;
        }
    }

}
