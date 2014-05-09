/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.packages;

import java.io.Serializable;

/**
 *
 * @author tobias
 */
public class LoginPackage extends DataPackage {

    public LoginPackage(int id) {
        super(0);
        this.type = NEW_ID;
        this.id = id;
        convertToString();
    }

}
