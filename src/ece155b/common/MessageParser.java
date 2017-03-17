package ece155b.common;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

public class MessageParser extends DefaultHandler {

    public Message message;
    private StringBuffer accumulator = new StringBuffer();
    String temp = "";

    /**
     * Creates a new instance of MessageParser
     */
    public MessageParser(String xml, Message msg) {
        try {
            message = msg;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(xml)), this);
        } catch (Exception ex) {
            System.out.println("Parsing error: " + ex);
        }
    }

    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
    }

    public void startElement(String uri, String lname, String name, Attributes attributes) {
        accumulator.setLength(0);
        switch (name) {
            case "MessageType":
            case "MessageFrom":
                break;
            case "StockItem":
                temp += "<StockItem>";
                break;
            case "Item":
                temp += "<Item>";
                break;
            case "ItemID":
                temp += "<ItemID>";
                break;
            case "ItemName":
                temp += "<ItemName>";
                break;
            case "ItemBrand":
                temp += "<ItemBrand>";
                break;
            case "ItemPrice":
                temp += "<ItemPrice>";
                break;
            case "Quantity":
                temp += "<Quantity>";
                break;
        }
    }

    public void endElement(String uri, String lname, String name) {
        String value = accumulator.toString();

        if (name.equals("MessageType")) {
            message.type = value;
        } else if (name.equals("MessageFrom")) {
            message.from = value;
        } else if (name.equals("MessageContent")) {
            message.content = temp;
        } else if (name.equals("StockItem")) {
            temp += "</StockItem>";
        } else if (name.equals("Quantity")) {
            temp += value + "</Quantity>";
        } else if (name.equals("ItemPrice")) {
            temp += value + "</ItemPrice>";
        } else if (name.equals("ItemBrand")) {
            temp += value + "</ItemBrand>";
        } else if (name.equals("ItemName")) {
            temp += value + "</ItemName>";
        } else if (name.equals("ItemID")) {
            temp += value + "</ItemID>";
        } else if (name.equals("Item")) {
            temp += "</Item>";
        }

    }
}
