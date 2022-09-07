class CollisionBox {
    private float x;
    private float y;
    private int width;
    private int height;

    CollisionBox(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    boolean IsColliding(CollisionBox collider){
        PVector leftCorner = new PVector(collider.x, collider.y);
        PVector rightCorner = new PVector(collider.x + collider.width, collider.y);
        PVector lowerLeftCorner = new PVector(collider.x, collider.y + collider.height);
        PVector lowerRightCorner = new PVector(collider.x + collider.width, collider.y + collider.height);

        // checker at deres venstre hjørne ikke er inden over vores nedre højre hjørne
        if (IsInOver(leftCorner) || IsInOver(rightCorner) || IsInOver(lowerLeftCorner) || IsInOver(lowerRightCorner)){
            println("you hit something bitch");
            return true;
        }
        return false;
    }

    boolean IsInOver(PVector corner1){
        return this.x < corner1.x && corner1.x < this.x + this.width && this.y < corner1.y && corner1.y < this.y + this.height;
    }

    void Update(float x, float y) {
        this.x = x;
        this.y = y;
    }
}