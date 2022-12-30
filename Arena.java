/*  Name: John Otto
*  PennKey: jwotto
*  Recitation: #217 on Tuesday at 1:45 - 2:45 pm
*
*  A class representing the arena in which the Irate Avians
*  game takes place. Keeps track of the game's Bird and
*  Targets and receives the player's input to control the bird.
*
*/

public class Arena {
    
    // The width and height of the PennDraw screen
    private int width, height;
    
    // All the targets in the Arena
    private Target[] targets;
    
    // The one and only bird in the game
    private Bird bird;
    
    /**
    * Whether the game is currently listening for
    * the player's mouse input, or letting the bird
    * fly. Begins as true.
    */
    private boolean mouseListeningMode;
    
    /**
    * Tells the program if the user was pressing
    * the mouse in the previous update call. Lets
    * the program know if the user has just released
    * the mouse because mouseWasPressedLastUpdate will
    * be true, but PennDraw.mousePressed() will be false
    * in the current update call. This enables the game
    * to transition from the mouse listening state into
    * the bird flight state.
    */
    private boolean mouseWasPressedLastUpdate;
    
    /**
    * Given a file that describes the contents of the
    * Arena, parse the file and initialize all member
    * variables of the Arena.
    * The file will be in the following format:
    * numTargets width height
    * bird.numThrows
    * target0.xPos target0.yPos target0.radius target0.xVel target0.yVel
    * target0.hitPoints target1.xPos... etc.
    */
    public Arena(String filename) {
        // read with In
        In in = new In(filename);
        
        // declare and intialize the necessary variables for the arena from the file
        int numTargets = in.readInt();
        width = in.readInt();
        height = in.readInt();
        int numThrowsRemaining  = in.readInt();
        
        PennDraw.setXscale(0, width);
        PennDraw.setYscale(0, height);
        
        // intiliaze a bird with new x/y positions, radius, and # of throws remaning.
        bird = new Bird(1, 1, .25, numThrowsRemaining);
        
        // initialize the Target array to length of numTargets
        targets = new Target [numTargets];
        
        // create new arrays for each variable in the file with length numTargets
        double [] xPos = new double[numTargets];
        double [] yPos = new double[numTargets];
        double [] radius = new double[numTargets];
        double [] xVel = new double[numTargets];
        double [] yVel = new double[numTargets];
        int [] hitPoints = new int [numTargets];
        
        // store the values from the file in each array then the in the target array
        for (int i = 0; i < numTargets; i++) {
            xPos[i] = in.readDouble();
            yPos[i] = in.readDouble();
            radius[i] = in.readDouble();
            xVel[i] = in.readDouble();
            yVel[i] = in.readDouble();
            hitPoints[i] = in.readInt();
            
            targets[i] = new Target(width, height, xPos[i], yPos[i], radius[i],
            xVel[i], yVel[i], hitPoints[i]);
        }
        
        // intitialze listening mmode and mouse last pressed to there correct values
        mouseListeningMode = true;
        mouseWasPressedLastUpdate = false;
        
        in.close();
    }
    
    /**
    * Inputs: N/A
    * Outputs: returns a true boolean if the player has won and false if they have 
    * not
    * Description: Returns true when all targets' hit points are 0.
    * Returns false in any other scenario.
    */
    private boolean didPlayerWin() {
        for (int i = 0; i < targets.length; i++) {
            if (targets[i].getHitPoints() != 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
    * Inputs: N/A
    * Outputs: returns a true boolean when player has lost and false is they have
    * not
    * Description: Returns true when the bird's remaining throw count is 0
    * when the game is in mouse-listening mode.
    * Returns false in any other scenario.
    */
    private boolean didPlayerLose() {
        return mouseListeningMode && bird.getNumThrowsRemaining() == 0;
    }
    
   /**
    * Inputs: N/A
    * Outputs: returns a boolean if the game is over when a player won or lost
    * Description: 
    * Returns true when either the win or lose
    * condition is fulfilled.
    * Win: All targets' hit points are 0.
    * Lose: The bird's remaining throw count reaches 0.
    *       Additionally, the game must be in mouse listening
    *       mode for the player to have lost so that the bird
    *       can finish its final flight and potentially hit
    *       the last target(s).
    */
    public boolean gameOver() {
        return didPlayerWin() || didPlayerLose();
    }
    

    /**
    * Inputs: double timeStep
    * Outputs: void
    * Description: Update each of the entities within the arena.
    * 1. Call each Target's update function
    * 2. Check the game state (mouse listening or bird moving)
    * and invoke the appropriate functions for the bird.
    */
    public void update(double timeStep) {
        for (int i = 0; i < targets.length; i++) {
            targets[i].update(timeStep);
        }
        
        /**
        * If the mose is currently pressed, then
        * set mouseWasPressedLastUpdate to true, and
        * call bird.setVelocityFromMousePos.
        * If the mouse is NOT currently pressed, AND
        * mouseWasPressedLastUpdate is currently true,
        * that means the player has just released the
        * mouse button, and the game should transition
        * from mouse-listening mode to bird-flight mode.
        */
        if (mouseListeningMode) {
            if (PennDraw.mousePressed()) {
                mouseWasPressedLastUpdate = true;
                bird.setVelocityFromMousePos();
            }
            
            if (!PennDraw.mousePressed() && mouseWasPressedLastUpdate) {
                mouseListeningMode = false;
                mouseWasPressedLastUpdate = false;
                bird.decrementThrows();
            }
            
        } else {
            // bird flight mode
            bird.update(timeStep);
            
            // iterate through each target and determine if they have been hit
            for (int i = 0; i < targets.length; i++) {
                bird.testAndHandleCollision(targets[i]);
            }
            
            // Extra Credit #3: reset if r is pressed
            if (PennDraw.hasNextKeyTyped()) {
                if (PennDraw.nextKeyTyped() == 'r') {
                    bird.reset();
                    mouseListeningMode = true;
                }
            }
            
            /** test to see if the bird is offscreen and if it is then iterate
            * through the array of tagets and check if it hit. If so, then decrease
            * health, set hit this shot ot be false, and decrease the radius.
            * ALso, we want ot reset the bird's position and set mouseListeningMode
            * be true
            */
            if (birdIsOffscreen()) {
                for (int i = 0; i < targets.length; i++) {
                    if (targets[i].isHit()) {
                        targets[i].decreaseHP();
                        targets[i].setHitThisShot(false);
                        // EXTRA CREDIT #4: Changing Target Appearance
                        targets[i].decreaseRadius();
                    }
                }
                bird.reset();
                mouseListeningMode = true;
            }
        }
    }
    
    /**
    * Inputs: N/A
    * Outputs: boolean value depending on whether the bird is offscreen screen 
    * by being too low, too much to the left, or too much to the right.
    * Description: A helper function for the Arena class that lets
    * it know when to reset the bird's position and velocity
    * along with the game state.
    * Returns true when the bird is offscreen to the left, right,
    * or bottom. However, the bird is allowed to go above the top
    * of the screen without resetting.
    */
    private boolean birdIsOffscreen() {
        return bird.getYpos() + bird.getRadius() < 0 ||
        bird.getXpos() + bird.getRadius() < 0 ||
        bird.getXpos() - bird.getRadius() > width;
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description:  
    * 1. Clear the screen
    * 2. Draw each target
    * 3. Draw the bird
    * 4. If in mouse listening mode and
    *    the mouse was pressed last update,
    *    draw the bird's velocity as a line.
    * 5. Advance PennDraw.
    */
    public void draw() {
        PennDraw.clear();
        
        for (int i = 0; i < targets.length; i++) {
            targets[i].draw();
        }
        
        // Extra Credit #3: Force Field
        checkIOrOKeyPressed();
        inForceField();
        outForceField();

        bird.draw();
        
        if (mouseListeningMode &&  mouseWasPressedLastUpdate) {
            bird.drawVelocity();
        }
        
        PennDraw.advance();
    }
    
    /**
    * Intilize the x and y position of the force fields offscreen and update them 
    * when certain parameters are meet. 
    */
    double pxIn = -1;
    double pyIn = -1;
    double pxOut = -1;
    double pyOut = -1;
    
    
    // Extra Credit #3: Force Fields
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: First, the method checks if 'i' has been pressed.
    * Then, if the player is holding down 'i' and clicks the mouse on the screen
    * px and py will be updated to the location of the mouse pressed. Also, 
    * the method checks if 'o' has been pressed. Then, if the player is holding down 
    * 'o' and clicks the mouse on the screen px and py will be updated to the 
    * location of the mouse pressed.
    */
    

    public void checkIOrOKeyPressed() {
        if (PennDraw.hasNextKeyTyped()) {
            // need to intialize a variable that stores the most recent key pressed
            char mostRecentCharPressed = PennDraw.nextKeyTyped();

            // use two nested if statements to check if a 'i' or 'o' & the mouse
            // has been pressed 
            if (mostRecentCharPressed == 'i') {
                if (PennDraw.mousePressed()) {
                    pxIn = PennDraw.mouseX();
                    pyIn = PennDraw.mouseY();
                }
            }
            if (mostRecentCharPressed == 'o') {
                if (PennDraw.mousePressed()) {
                    pxOut = PennDraw.mouseX();
                    pyOut = PennDraw.mouseY();
                }
            }
        }
    }
    
    //Extra Credit #3: Helper distance function
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: A helper function used to find the distance
    * between two 2D points. Remember to use the
    * Pythagorean Theorem.
    */
    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    //Extra Credit #3: Drawing In Force Field
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: create a method that will draw an orange out force field and
    * suck the bird into the center of force field
    */
    public void inForceField() {
        // draw the orange force field
        PennDraw.setPenColor(255, 165, 0);
        PennDraw.filledCircle(pxIn, pyIn, 1);
        PennDraw.setPenColor(255, 95, 31);
        PennDraw.circle(pxIn, pyIn, 1);
        
        // affect the birds motion to bring it into the center
        if (distance(pxIn, pyIn, bird.getXpos(), bird.getYpos()) <
        1 + bird.getRadius()) {
            bird.setXVel(pxIn - bird.getXpos());
            bird.setYVel(pyIn - bird.getYpos());
        }
    }
    
    // Extra Credit #3: Drawing Out Force Field
    /**
    * Inputs: N/A
    * Outputs: void
    * Description:  create a method that will draw an purple out force field and
    * will take in bird into the force field and push it out
    */
    public void outForceField() {
        // draw a purple force field
        PennDraw.setPenColor(106, 13, 173);
        PennDraw.filledCircle(pxOut, pyOut, 1);
        PennDraw.setPenColor(85, 10, 138);
        PennDraw.circle(pxOut, pyOut, 1);
        
        // affect the bird's motion to bring it into the force field and push it out
        if (distance(pxOut, pyOut, bird.getXpos(), bird.getYpos()) <
        1 + bird.getRadius()) {
            bird.setXVel((bird.getXpos() - pxIn) * .1);
            bird.setYVel((bird.getYpos() - pyIn) * .1);
        }
    }
    
    /**
    * Inputs: N/A
    * Outputs: void
    * Description: Draws either the victory or loss screen.
    * If all targets have 0 hit points, the player has won.
    * Otherwise they have lost.
    */
    public void drawGameCompleteScreen() {
        PennDraw.setPenColor(0, 0, 0);
        if (gameOver()) {
            PennDraw.clear();
            if (didPlayerWin()) {
                PennDraw.text(5, 2.5, "You Win!");
            } else if (didPlayerLose()) {
                PennDraw.text(5, 2.5, "You have lost ...");
            }
            PennDraw.advance();
        }
    }
}