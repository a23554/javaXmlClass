package ece155b.distributor.xml;

import ece155b.common.Contact;
import ece155b.common.Item;
import ece155b.distributor.data.Distributor;
import ece155b.common.StockItem;
import java.io.File;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class DisParser extends DefaultHandler {

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
    public DisParser(File file, Distributor dis) {
        try {
            distributor = dis;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(file, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DisParser(String xml, Distributor dis) {
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
                item = new Item();
                break;
            case "Contact":
                provider = new Contact();
                break;
        }
    }

    @Override
    public void endElement(String uri, String lname, String name) {
        String value = accumulator.toString();
        switch (name) {
            case "CompanyName":
                distributor.companyName = value;
                break;
            case "CompanyContact":
                distributor.companyContact = value;
                break;
            case "CompanyAddress":
                distributor.companyAddress = value;
                break;
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
                distributor.addStock(stockitem);
                break;
            case "Name":
                provider.name = value;
                break;
            case "URL":
                provider.url = value;
                break;
            case "Port":
                provider.port = Integer.valueOf(value);
                break;
            case "Contact":
                distributor.addProviderContact(provider);
                break;
        }
    }

}
