/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.objects;

/**
 *
 * @author Tobi
 */
public abstract class NetworkObject extends BasicObject{
    
    protected int netID;
    
    public NetworkObject(double x, double y, double width, double height, int id) {
        super(x, y, width, height);
        this.netID = id;
    }

    public int getNetworkID() {
        return netID;
    } 
    
    public void setNetworkID(int id) {
        this.netID = id;
    }
    
}
