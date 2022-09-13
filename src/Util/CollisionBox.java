package Util;
import processing.core.*;

public class CollisionBox {
    float x;
    float y;
    float width;
    float height;
    PVector TLCorner, TRCorner, BLCorner, BRCorner;

    public CollisionBox(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean IsColliding(CollisionBox collider){
        this.TLCorner = new PVector(collider.x, collider.y);
        this.TRCorner = new PVector(collider.x + collider.width, collider.y);
        this.BLCorner = new PVector(collider.x, collider.y + collider.height);
        this.BRCorner = new PVector(collider.x + collider.width, collider.y + collider.height);

        // checker at deres venstre hjørne ikke er inden over vores nedre højre hjørne
        if (IsInOver(this.TLCorner) || IsInOver(this.TRCorner) || IsInOver(this.BLCorner) || IsInOver(this.BRCorner)){
            return true;
        }
        return false;
    }

    public boolean IsInOver(PVector corner1){
        return this.x < corner1.x && corner1.x < this.x + this.width && this.y < corner1.y && corner1.y < this.y + this.height;
    }

    public void Update(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}