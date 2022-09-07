public class Character {
    public PVector velocity = new PVector();
    public PVector position = new PVector(width/2,height/2);
    private PVector startPosition = new PVector(width/2,height/2);
    private PVector acceleration = new PVector();
    public PImage charImage = requestImage("./Images/Character.png");
    private int charWidth = 200;
    private int charHeight = 200;
    private float gravity = 0.4;
    private int mass = 5;
    public boolean onGround = false;

    public CollisionBox collisionBox;
    public boolean isDragging = false;

    public Character(int mass, int width, int height, PVector startPos) {
        this.mass = mass;
        this.charWidth = width;
        this.charHeight = height;
        this.startPosition.set(startPos);
        this.position.set(startPos);
        this.collisionBox = new CollisionBox(this.position.x, this.position.y, this.charWidth, this.charHeight);
    }

    public void Update() {
        this.acceleration.y = gravity;
        if (this.position.y == height - this.charHeight) {
            this.onGround = true;
        } else {
            this.onGround = false;
        }
        this.velocity.x = (this.velocity.x * 0.95);
        this.velocity.add(acceleration);
        this.velocity.x = constrain(this.velocity.x, -30, 30);
        this.velocity.y = constrain(this.velocity.y, -30, 30);

        this.position.add(this.velocity);

        this.acceleration.mult(0);

        int halfWidth = this.charWidth / 2;
        int halfHeight = this.charHeight / 2;
        if (this.position.x > (width - this.charWidth) || this.position.x < 0) {
            this.position.x = constrain(this.position.x, 0, (width - this.charWidth));
            this.velocity.x = 0;
        }
        if (this.position.y > (height - this.charHeight) || this.position.y < 0) {
            this.position.y = constrain(this.position.y, 0, (height - this.charHeight));
            this.velocity.y = -(this.velocity.y * mass/20);
        } 
        this.collisionBox.Update(this.position.x, this.position.y);
    }

	public void SetVelocity(PVector velocity) {
		this.velocity.set(velocity);
	}

	public void AddVelocity(PVector velocity) {
		this.velocity.add(velocity);
	}

    public PVector GetPosition() {
        return this.position;
    }

    public void UpdateStartPosition(PVector position) {
        this.position.set(position);
        this.startPosition.set(position);
        this.collisionBox.Update(this.position.x, this.position.y);
    }

    public void ResetPosition() {
        this.position.set(this.startPosition);
        this.velocity.set(new PVector());
        this.collisionBox.Update(this.position.x, this.position.y);
    }
}