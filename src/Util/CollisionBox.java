package Util;
import processing.core.*;

public class CollisionBox {
    float x;
    float y;
    float width;
    float height;

    private boolean CheckCollisionX(CollisionBox platform) {
        return (this.x + this.width / 2 > platform.x - platform.width / 2 
                        && this.x - this.width / 2 < platform.x + platform.width / 2)
                        && (this.y + this.height / 2 > platform.y - platform.height / 2
                                && this.y - this.height / 2 < platform.y + platform.height / 2);
    }

    private boolean CheckCollisionY(CollisionBox platform) {
        return (this.x + this.width / 2 > platform.x - platform.width / 2
                        && this.x - this.width / 2 < platform.x + platform.width / 2)
                        && (this.y + this.height / 2 > platform.y - platform.height / 2
                                && this.y - this.height / 2 < platform.y + platform.height / 2);
    }
    
    public CollisionBox(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean IsColliding(CollisionBox collider){
        if (CheckCollisionX(collider) && CheckCollisionY(collider)){
            return true;
        }
        return false;
    }

    public boolean IsInOver(PVector corner1){
        return this.x - this.width / 2 < corner1.x && corner1.x < this.x + this.width / 2 && this.y - this.height / 2 < corner1.y && corner1.y < this.y + this.height / 2;
    }

    public void Update(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}