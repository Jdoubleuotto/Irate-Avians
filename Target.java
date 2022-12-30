/*  Name: John Otto
*  PennKey: jwotto
*  Recitation: #217 on Tuesday from 1:45 - 2:45 pm
*
*  A class that represents a target to be hit in
*  Irate Avians. Can update its own position based
*  on velocity and time.
*/

public class Target {
    
    // variables for width and height of screen
    private double width, height;
    
    // Position and radius
    private double xPos, yPos, radius;
    
    // Velocity components
    private double xVel, yVel;
    
    /**
    * When a target's hit points reach zero,
    * it has been destroyed by the bird.
    */
    private int hitPoints;
    
    // Track if target has been hit this shot.
    private boolean hitThisShot;
    
    /**
    * Given a position, a radius, a velocity, and a number of hit points,
    * construct a Target.
    */
    public Target(double width, double height, double xPos, double yPos,
    double radius, double xVel, double yVel, int hitPoints) {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.xVel = xVel;
        this.yVel = yVel;
        this.hitPoints = hitPoints;
        hitThisShot = false;
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: Draw a circle centered at the target's position
    * with a radius equal to the target's radius.
    * Only draw a Target if it has more than zero
    * hit points.
    */
    public void draw() {
        if (hitPoints > 0) {
            //draw the targets with a filled black circle
            PennDraw.setPenColor(73, 255, 47);
            PennDraw.filledCircle(xPos, yPos, radius);
            
            // write text that shows how many hit points each target
            String displayPoints = Integer.toString(hitPoints);
            PennDraw.setPenColor(0, 0, 0);
            PennDraw.text(xPos, yPos + radius + .1, displayPoints);
            
            //EXTRA CREDIT #1: Detailed Drawing
            // step 1. draw a elliptical nose
            PennDraw.setPenColor(50, 205, 50);
            PennDraw.filledEllipse(xPos, yPos - radius / 3, radius / 2,
            radius / 3.5);
            
            // step 2. draw green nostrils
            PennDraw.setPenColor(34, 139, 34);
            PennDraw.filledCircle(xPos + radius / 4, yPos - radius / 3, radius / 8);
            PennDraw.filledCircle(xPos - radius / 4, yPos - radius / 3, radius / 8);
            
            //step 3. draw dark green cirular eyes
            PennDraw.setPenColor(255, 255, 255);
            PennDraw.filledCircle(xPos + radius / 2, yPos + radius / 3,
            radius / 4.5);
            PennDraw.filledCircle(xPos - radius / 2, yPos + radius / 3,
            radius / 4.5);
            PennDraw.setPenColor(0, 0, 0);
            PennDraw.filledCircle(xPos + radius / 1.75, yPos + radius / 3,
            radius / 15);
            PennDraw.filledCircle(xPos - radius / 1.75, yPos + radius / 2.75,
            radius / 15);
        }
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: Given the change in time, update the target's
    * position based on its x and y velocity. When
    * a target is completely offscreen horizontally,
    * its position should wrap back around to the opposite
    * horizontal side. For example, if the target moves off the
    * right side of the screen, its xPos should be set to the
    * left side of the screen minus the target's radius.
    * The same logic should apply to the target's vertical
    * position with respect to the vertical screen boundaries.
    */
    public void update(double timeStep) {
        // Step 1 and 2: update the x and y posotion of the target objects with the
        // general formula: p = p + v * timeStep
        xPos = xPos + xVel * timeStep;
        yPos = yPos + yVel * timeStep;
        
        // Step 3: wrapping the target around the x axis
        if (xPos - radius > width) {
            xPos = 0 - radius;
        } else if (xPos + radius < 0) {
            xPos = width + radius;
        }
        
        // Step 4: wrapping the target around the y axis
        if (yPos - radius > height) {
            yPos = 0 - radius;
        } else if (yPos + radius < 0) {
            yPos = height + radius;
        }
        
    }
    
    // Decrement the target's hit points by 1.
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: create a method that will decrease the health of the target when
    * the bird collides with the target
    */
    public void decreaseHP() {
        --hitPoints;
    }
    
    // EXTRA CREDIT #4: Changing target appearance with health
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: decrease the the radius of the target by 20% only when it is hit
    * by the bird
    */
    public void decreaseRadius() {
        radius *= .8;
    }
    
    /**
    * Inputs: boolean hit
    * Outputs: void
    * Description: Setter function for whether or not target hit this round.
    */
    public void setHitThisShot(boolean hit) {
        hitThisShot = hit;
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: Return whether or not this target is hit this round.
    */
    public boolean isHit() {
        return hitThisShot;
    }
    
    /**
    * Inputs: N/A
    * Outputs: return hitPoints of a target
    * Description: Getter functions that return a copy of the indicated member 
    * variable.
    */
    public int getHitPoints() {
        return hitPoints;
    }
    
    /**
    * Inputs: N/A
    * Outputs: the x positions of a target
    * Description: create a getter function that allows other class obejcts to
    * acces the private instance of the target's x position.
    */
    public double getXpos() {
        return xPos;
    }
    
    /**
    * Inputs: N/A
    * Outputs: the y position of a target
    * Description: create a getter function that allows other class obejcts to
    * acces the private instance of the target's y position.
    */
    public double getYpos() {
        return yPos;
    }
    
    /**
    * Inputs: N/A
    * Outputs: the radius of a target
    * Description: create a getter function that allows other class objects to
    * access the private instance of the target's radius
    */
    public double getRadius() {
        return radius;
    }
    
    //Extra Credit #4: Bouncing Bird
    /**
    * Inputs: N/A
    * Outputs: the x velocity of a target
    * Description: create a getter function that allows other class objects to
    * access the private instance of the target's xVel
    */
    public double getXvel() {
        return xVel;
    }
    
    //Extra Credit #4: Bouncing Bird
    /**
    * Inputs: N/A
    * Outputs: the y velocity of a target
    * Description: create a getter function that allows other class objects to
    * access the private instance of the target's yVel
    */
    public double getYvel() {
        return yVel;
    }
}
