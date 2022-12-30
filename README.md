# Irate-Avians
/**********************************************************************
 *  readme template
 *  Irate Avians
 * CIS 1100 Project

 **********************************************************************/

 Name: John Otto

/**********************************************************************
 *  How to run the game
 
 *********************************************************************/
 * Put all files into an IDE then compile and type java IrateAvians [filename]. The filename is targetFile1.txt or you could make your own target data
 and put that in. 


/**********************************************************************
 *  If you completed the extra credit or added any additional features,
 *  provide DETAILED and CLEAR instructions for how to use them and 
 *  what we should look for when grading your assignment.
 
 *********************************************************************/
 * Extra Credit #1: Detailed Drawing.
 I added detail to the targets by making them green, added a nose 
 with nostrils, and two eyes to make them look like actual angry bird targets.
 I added these additional features in the draw section of the target.java file.


 * Extra Credit #2: Bouncing Bird
 In the method "bird.testAndHandleCollision()", I used the formula r = d - 2*(d*n)n
 to reflect the velocity vector of the about another colliding object. But, if
 the target was stationary then I just flipped the direction of the bird's x and y1
 velocity. These lines of code can be found from lines 168 to 180 of the bird.java 
 file.


 * Extra Credit #3: Force Fields
 I created 3 new methods between lines 275 to 370 of arena.java that would check
 if you pressed 'i', 'o', or clicked the mouse. If you hold the mouse click and 
 press 'i', then an orange in force field will be drawn where you mouse is.
 If you hold the mouse click and press 'o', then a pueple out force field will be 
 drawn where the mouse is. The orange in force field should suck the bird 
 into the middle of the force field, while the purple out force field should 
 will take in bird into the force field and push it out. I implemented these
 within the arena.draw method between lines 261 to 263.
 I also wrote a few lines of code in arena.java from lines 193 to 198 that would
 reset the bird's position and mouse listening mode.
 

 * Extra Credit #4: Changing Target Appearance with Health
 Every time a target gets is hit and there hit points decrease, the size of
 the target's radius gets decremented by 20%. 
 I added a helper method that implements the above feature around bewteen line 
 144 - 146 of the target.java file. This method is actually implemented in
 arena.java on line 208. The when is only called with mouseListeningMode 
 is false and the target has been hit.

