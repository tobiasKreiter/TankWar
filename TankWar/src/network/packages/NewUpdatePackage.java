/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package network.packages;

import java.util.Set;

/**
 *
 * @author Tobi
 */
public class NewUpdatePackage extends DataPackage {
    
    public NewUpdatePackage(int netVersion, int type) {
        super(netVersion);
        this.type = NEW_UPDATE_REQUEST;
        this.updateType = type;
        convertToString();
    }
    
}
