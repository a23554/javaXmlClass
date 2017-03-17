/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ece155b.common;

/**
 *
 * @author 哲宇
 */
public class StockItem {

    public Item item;
    public int quantity;

    public StockItem() {
        item = new Item();
        quantity = 0;
    }

    public StockItem(Item iteminfo, int quant) {
        item = iteminfo;
        quantity = quant;
    }

    public String toString() {
        String str = "";
        str += "|||StockItem\n";
        str += item.toString();
        str += "||||Quantity:" + quantity + "\n";
        str += "|||/StockItem\n";
        return str;
    }

    public String toXML() {
        String xml = "";
        xml += "<StockItem>";
        xml += item.toXML();
        xml += "<Quantity>" + quantity + "</Quantity>";
        xml += "</StockItem>";
        return xml;
    }
}
