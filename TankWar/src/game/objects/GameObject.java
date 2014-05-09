/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects;

import java.awt.Color;
import network.ObjectParser;
import processing.core.PApplet;

/**
 *
 * @author tobias
 */
public abstract class GameObject extends PhysicsObject {

    protected int team;
    
    protected Color teamColor;

    protected int type;

    protected Color borderColor = Color.BLUE;
    
    private final int TEAM_RED = 0;
    private final int TEAM_BLUE = 1;
    private final int TEAM_GREEN = 2;
    private final int TEAM_YELLOW = 3;

    public static final int WALL = 0;
    public static final int TANK = 1;
    public static final int BULLET = 2;
    public static final int EXPLOSION = 3;

    public GameObject(double x, double y, double width, double height, int id, float velocity) {
        super(x, y, width, height, id, velocity);
        findTeamColor();
    }

    public GameObject(double x, double y, double width, double height, int id, float velocity, float rotation) {
        super(x, y, width, height, id, velocity, rotation);
        findTeamColor();
    }

    public GameObject(double x, double y, double width, double height, int id, float velocity, int team) {
        super(x, y, width, height, id, velocity);
        findTeamColor();
        this.team = team;
    }

    public GameObject(double x, double y, double width, double height, int id, float velocity, float rotation, int team) {
        super(x, y, width, height, id, velocity, rotation);
        findTeamColor();
        this.team = team;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
    
    private void findTeamColor() {
        switch (team) {
            case TEAM_RED:
                teamColor = Color.RED;
                break;
            case TEAM_BLUE:
                teamColor = Color.BLUE;
                break;
            case TEAM_GREEN:
                teamColor = Color.GREEN;
                break;
            case TEAM_YELLOW:
                teamColor = Color.YELLOW;
                break;
        }
    }

    public void tickProxy() {
        move();
        tick();
        updateVectors();
    }

    public abstract void draw(PApplet p);

    public abstract void tick();

    public abstract void collide(GameObject otherObject);

    public abstract void parseObject(ObjectParser oP);
}
