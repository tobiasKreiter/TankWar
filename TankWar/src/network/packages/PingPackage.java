/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package network.packages;

/**
 *
 * @author tobias
 */
public class PingPackage extends DataPackage{
    
    public PingPackage() {
        super(0);
        this.type = PING;
        convertToString();
    }
    
}
