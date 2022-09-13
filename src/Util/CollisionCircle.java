package Util;
import processing.core.*;

public class CollisionCircle {
	private int radius;
    private float x;
    private float y;
    private double closestX;
    private double closestY;

    float colliderWidth;
    float colliderHeight;


    public CollisionCircle(float x, float y, int radius){
        this.radius = radius;
        this.x = x;
        this.y = y;
    }
    public boolean IsColliding(CollisionCircle collider){

        // checker at de ikke er inden for hinandens radius
        if (this.radius + collider.radius > Math.sqrt(Math.pow(x, collider.x) + Math.pow(y, collider.y))){
            return true;
        }
        return false;
    }

    public boolean IsColliding(CollisionBox collider){
        PVector leftCorner = new PVector(collider.x, collider.y);
        PVector rightCorner = new PVector(collider.x + collider.width, collider.y);
        PVector lowerLeftCorner = new PVector(collider.x, collider.y + collider.height);
        PVector lowerRightCorner = new PVector(collider.x + collider.width, collider.y + collider.height);
        double fullLength = Math.sqrt(Math.pow(x, collider.x + collider.width/2) + Math.pow(y, collider.y + collider.height/2));
        double sizeDiff = this.radius / fullLength;
        colliderWidth = collider.width;
        colliderHeight = collider.height;

        // finder det sted på cirklen som er tættest firkanten
        closestX = Math.abs(x - (collider.x + collider.width/2)) * sizeDiff;
        closestY = Math.abs(y - (collider.y + collider.height/2)) * sizeDiff;
        
        // checker om firkantens hjørner er inden over cirklen
        if (IsInOver(leftCorner) || IsInOver(rightCorner) || IsInOver(lowerLeftCorner) || IsInOver(lowerRightCorner)){
            return true;
        }

        // checker om firkantens linjer er inden over cirklen
        if (collider.x < this.x && this.x < collider.x + collider.width && this.radius > Math.abs(this.y - collider.y)){
            return true;
        } else if (collider.y < this.y && this.y < collider.y + collider.height && this.radius > Math.abs(this.x - collider.x)){
            return true;
        }
        return false;
    }

    public boolean IsInOver(PVector corner1){
        return corner1.x < closestX && closestX < corner1.x + colliderWidth && corner1.y < closestY && closestY < corner1.y + colliderHeight;
    }

    public void Update(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
