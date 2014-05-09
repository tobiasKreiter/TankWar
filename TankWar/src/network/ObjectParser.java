/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package network;

import network.packages.DataPackage;

/**
 *
 * @author Tobi
 */
public class ObjectParser {
    
    
    private String values = "";
    
    public ObjectParser() {
    }
    
    public void write(String value) {
        values+=value+";";
    }
    
    public void close() {
        values = values.substring(0, values.length()-1);
    }
    
    public String getValue() {
        return values;
    }   
    
}
