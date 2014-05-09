/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.packages;

import game.objects.GameObject;
import java.util.ArrayList;
import network.ObjectParser;

/**
 *
 * @author Tobi
 */
public class RemovePackage extends DataPackage {

    public RemovePackage(ArrayList<GameObject> obj) {
        super(0);
        this.type = REMOVE;
        this.trash = obj;
    }

    protected void convertToString() {
        data += type + "/";
        for (GameObject gObj : trash) {
            ObjectParser oP = new ObjectParser();
            gObj.parseObject(oP);
            data += oP.getValue() + "/";
        }
    }

}
