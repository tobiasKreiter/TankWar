/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.packages;

import game.objects.GameObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author tobias
 */
public class NewObjectPackage extends DataPackage {

    public NewObjectPackage(int networkID, ConcurrentSkipListMap<Integer, GameObject> objects) {
        super(networkID);
        this.type = NEW_OBJECT_REQUEST;
        this.objects = objects;
        convertToString();
    }

    public NewObjectPackage(GameObject obj) {
        super(0); //Can be ignored because client is sending this package
        this.type = NEW_OBJECT_REQUEST;
        this.objects = new ConcurrentSkipListMap();
        this.objects.put(obj.getNetworkID(), obj);
        convertToString();
    }

}
