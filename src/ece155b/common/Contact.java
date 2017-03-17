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
public class Contact {

    public String url;
    public int port;
    public String name;

    public Contact() {
    }

    public Contact(String n, String u, int p) {
        name = n;
        url = u;
        port = p;
    }

    public Contact(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String str = "";
        str += "|||Contact\n";
        str += "||||Name:\t" + name + "\n";
        str += "||||URL:\t" + url + "\n";
        str += "||||Port:\t" + port + "\n";
        str += "|||/Contact\n";
        return str;
    }

    public String toXML() {
        String xml = "";
        xml += "<Contact>";
        xml += "<Name>" + name + "</Name>";
        xml += "<URL>" + url + "</URL>";
        xml += "<Port>" + port + "</Port>";
        xml += "</Contact>";
        return xml;
    }

}
