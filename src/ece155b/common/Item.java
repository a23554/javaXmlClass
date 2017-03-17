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
public class Item {

    public String id;
    public String name;
    public String brand;
    public int price;

    //Constuctors
    /*Constructors
     Item()
     Item(String ids, String names, String brands, int prices, int quantitys)    
     */
    public Item() {
    }

    public Item(String ids, String names, String brands, int prices) {
        id = ids;
        name = names;
        brand = brands;
        price = prices;
    }

    @Override
    public String toString() {
        String str = "";
        str += "||||Item\n";
        str += "|||||ItemID:\t" + id + "\n";
        str += "|||||ItemName:\t" + name + "\n";
        str += "|||||ItemBrand:\t" + brand + "\n";
        str += "|||||ItemPrice:\t" + price + "\n";
        str += "||||/Item\n";
        return str;
    }

    public String toXML() {
        String xml = "";
        xml += "<Item>";
        xml += "<ItemID>" + id + "</ItemID>";
        xml += "<ItemName>" + name + "</ItemName>";
        xml += "<ItemBrand>" + brand + "</ItemBrand>";
        xml += "<ItemPrice>" + price + "</ItemPrice>";
        xml += "</Item>";
        return xml;
    }
}
