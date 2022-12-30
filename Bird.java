/*  Name: John Otto
*  PennKey: jwotto
*  Recitation: #217 on Tuesday from 1:45 - 2:45 pm
*
*  A class that represents the bird projectile in
*  Irate Avians. Can update its own position based
*  on velocity and time, and can compute whether
*  it overlaps a given Target.
*
*/

public class Bird {
    // The position, velocity, and radius members of the bird.
    private double xPos, yPos, xVel, yVel, radius;
    
    /**
    * How many more times the player can throw the bird
    * before losing the game.
    */
    private int numThrowsRemaining;
    
    /**
    * Initialize the bird's member variables
    * with the same names as the inputs to those values.
    * Initializes the bird's velocity components to 0.
    */
    public Bird(double xPos, double yPos, double radius, int numThrows) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.numThrowsRemaining = numThrows;
        this.xVel = 0.0;
        this.yVel = 0.0;
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: Draws a circle centered at the bird's position
    * with a radius equal to the bird's radius.
    * Additionally, draw a triangular beak and two
    * circular eyes somewhere on the circle to make
    * the bird look more like a bird. Additional details
    * are up to your discretion.
    * Also draws the bird's remaining throws 0.1 units
    * above its circular body.
    */
    public void draw() {
        // draw an orange beak for the bird
        PennDraw.setPenColor(255, 180, 20);
        PennDraw.filledPolygon(xPos + radius * .8 , yPos + radius / 2, xPos +
        radius * .8, yPos - radius / 2, xPos + radius * 1.8, yPos);
        
        // draw filled red circle
        PennDraw.setPenColor(160, 205, 225);
        PennDraw.filledCircle(xPos, yPos, radius);
        
        // draw black eyes for the bird
        PennDraw.setPenColor(0, 0, 0);
        PennDraw.filledCircle(xPos + radius / 3.2, yPos + radius / 2, radius / 10);
        PennDraw.filledCircle(xPos + radius / 1.60, yPos + radius / 2, radius / 10);
        
        // draw the text for the number of throws remaining over the birds head
        String throwsRemaining = Integer.toString(numThrowsRemaining);
        PennDraw.text(xPos, yPos + radius + 0.1, throwsRemaining);
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: Draw the line representing the bird's initial velocity
    * when the player is clicking and dragging the mouse.
    */
    public void drawVelocity() {
        double timeStep = 1.0;
        PennDraw.line(xPos, yPos, xPos + xVel * timeStep, yPos + yVel * timeStep);
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: Set xPos and yPos to 1.0,
    * set xVel and yVel to 0.0,
    * and clear the list of targets hit this launch.
    */
    public void reset() {
        xPos = 1.0;
        yPos = 1.0;
        xVel = 0.0;
        yVel = 0.0;
    }
    
    /**
    * Inputs: N/A
    * Outputs: N/A
    * Description:Compute the bird's initial velocity as the
    * vector from the mouse's current position to
    * the bird's current position. This will be used
    * in mouse listening mode to update the launch
    * velocity.
    */
    public void setVelocityFromMousePos() {
        xVel = xPos - PennDraw.mouseX();
        yVel = yPos - PennDraw.mouseY();
    }
    
    /**
    * Inputs: N/A
    * Outputs: N/A
    * Description: Given the change in time, compute the bird's
    * new position and new velocity.
    */
    public void update(double timeStep) {
        // step 1: update the x and y position of the bird
        xPos = xPos + xVel * timeStep;
        yPos = yPos + yVel * timeStep;
        
        // step 2: update the y velocity by subtracitng from it the value 0.25 * t
        yVel = yVel - .25 * timeStep;
    }
    
    /**
    * Inputs: double of x1, x2, y1, and y2
    * Outputs: N/A
    * Description: A helper function used to find the distance
    * between two 2D points. Remember to use the
    * Pythagorean Theorem.
    */
    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    /**
    /**
    * Inputs: target t 
    * Outputs: N/A
    * Description: Given a Target, determine if the bird should
    * test for collision against it. If the bird
    * *should* see if it collides with the target,
    * then perform that test. If the bird collides,
    * then decrease the target's HP by 1 and add
    * the target to the bird's list of targets hit
    * during this launch.
    */
    public void testAndHandleCollision(Target t) {
        if (t.getHitPoints() > 0) {
            if (distance(xPos, yPos, t.getXpos(), t.getYpos()) <
            radius + t.getRadius()) {
                t.setHitThisShot(true);
                
                // Extra credit #2: Bouncing Bird
                if (t.getXvel() == 0 && t.getYvel() == 0) {
                    /**
                    * if assume that the target is a perfectly inelastic and
                    * immovable object, then reflect bird's velocity with the
                    * same magnitude in the opposite diretion
                    */
                    xVel = -xVel;
                    yVel = -yVel;
                } else {
                    /**
                    * use the formula r = d - 2(d*n)n, where:
                    * r = reflected velocity after the collisoin
                    * d = the intial velocity vector
                    * d * n = is the dot product
                    * n = the velocity vector that must be normalized
                    */
                    double xVeloNormal = -t.getYvel();
                    double yVeloNormal = t.getXvel();
                    double distanceNormal = Math.sqrt(xVeloNormal * xVeloNormal +
                                                      yVeloNormal * yVeloNormal);
                    double normalizedXVelo = xVeloNormal / distanceNormal;
                    double normalizedYVelo = yVeloNormal / distanceNormal;
                    double dotProductOfnd = xVel * normalizedXVelo +
                    yVel * normalizedYVelo;
                    // seperate the formula: r = d-2(d*n)*n into x and y components
                    xVel = xVel - 2 * dotProductOfnd * normalizedXVelo;
                    yVel = yVel - 2 * dotProductOfnd * normalizedYVelo;
                }
            }
        }
    }
    
    // Reduce numThrowsRemaining by 1.
    /**
    * Inputs: N/A
    * Outputs: N/A
    * Description: create a method that will decrease the number of throws everytime
    * you launch the bird
    */
    public void decrementThrows() {
        numThrowsRemaining--;
    }
    /**
    * Getter functions that return a copy
    * of the indicated member variable.
    */
    public double getXpos() {
        return xPos;
    }
    /**
    * Inputs: N/A
    * Outputs: return the y posiition of the bird
    * Description: create a getter function that allows other class obejcts to
    * acces the private instance of the bird's y position.
    */
    public double getYpos() {
        return yPos;
    }
    
    /**
    * Inputs: N/A
    * Outputs: returns the radius of the bird
    * Description: create a getter function that allows other class objects to
    * access the private instance of the bird's radius
    */
    public double getRadius() {
        return radius;
    }
    
    /**
    * Inputs: N/A
    * Outputs: returns the radius of the bird
    * Description:  create a getter function that allows other class objects to
    * access the private instance of the bird's number of throws remaining.
    */
    public int getNumThrowsRemaining() {
        return numThrowsRemaining;
    }
    
    // Extra credit #4: Bouncing Bird
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: create a setter in order to modify the x vel of the bird
    */
    public void setXVel(double xVelNew) {
        xVel = xVelNew;
    }
    
    // Extra credit #4: Bouncing Bird
    /**
    * Inputs: NA
    * Outputs: void
    * Description: create a setter in order to modify the y vel of the bird
    */
    public void setYVel(double yVelNew) {
        yVel = yVelNew;
    }
}
