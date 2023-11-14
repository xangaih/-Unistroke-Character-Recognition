import java.awt.Point;
import java.io.*;

/**
 * This class performs unistroke handwriting recognition using an algorithm
 * known as "elastic matching."
 * 
 * @author Dave Berque
 * @version August, 2004 Slightly modified by David E. Maharry and Carl Singer
 *          10/27/2004
 * 
 */

public class Recognizer {
    public static final int STROKESIZE = 150; // Max number of points in each
                                                // stroke
    private static final int NUMSTROKES = 10; // Number of strokes in the base
                                                // set (0 through 9)
    private Point[] userStroke; // holds points that comprise the user's stroke
    private int nextFree; // next free cell of the userStroke array

    private Point[][] baseSet; // holds points for each stroke (0-9) in the
                                // base set.

    // baseset is an array of arrays, a 2-D array.

    /**
     * Constructor for the recognizer class. Sets up the arrays and loads the
     * base set from an existing data file which is assumed to have the right
     * number of points in it. The file is organized so that there are 150
     * points for stroke 0 followed by 150 points for stroke 1, ... 150 poinpts
     * for stroke 9. Each stroke is organized as an alternating series of x, y
     * pairs. For example, stroke 0 consists of 300 lines with the first line
     * being x0 for stroke 0, the next line being y0 for stroke 0, the next line
     * being x1 for stroke 0 and so on.
     */
    public Recognizer()
    {
        int row, col, stroke, pointNum, x, y;
        String inputLine;

        userStroke = new Point[STROKESIZE];
        baseSet = new Point[NUMSTROKES][STROKESIZE];

        try {
            FileReader myReader = new FileReader("strokedata.txt");
            BufferedReader myBufferedReader = new BufferedReader(myReader);
            for (stroke = 0; stroke < NUMSTROKES; stroke++)
                for (pointNum = 0; pointNum < STROKESIZE; pointNum++) {
                    inputLine = myBufferedReader.readLine();
                    x = Integer.parseInt(inputLine);
                    inputLine = myBufferedReader.readLine();
                    y = Integer.parseInt(inputLine);
                    baseSet[stroke][pointNum] = new Point(x, y);
                }
            myBufferedReader.close();
            myReader.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to file.\n");
        }
    } 
    
    public void translate()
    {
         int minX = findMinX();
         int minY = findMinY();
        for(int i = 0; i < nextFree; i++)
        {
            userStroke[i].x = userStroke[i].x - minX;
            userStroke[i].y = userStroke[i].x - minY;
        }
    }
    // TO DO: Write translate
    /**
     * translate - Translates the points in the userStroke array by sliding them
     * as far to the upper-left as possible. It does this by finding the minX
     * value and the minY value. Then each point (x, y) is replaced with the
     * point (x-minX, y-minY). Note: you can use the translate method of the
     * Point class
     */
    public void scale()
    {   double scaleFactorX = 250/findMaxX();
        double scaleFactorY = 250/findMaxY();
        
        for(int i = 0; i < nextFree; i++)
        {
            userStroke[i].x = (int) (userStroke[i].x * scaleFactorX);  
            userStroke[i].y = (int) (userStroke[i].y * scaleFactorY);
        }
    }
    // TO DO: Write scale
    /**
     * scale - Scales the points in the user array by stretching the user's
     * stroke to fill the canvas as nearly as possible while maintaining the
     * aspect ratio of the stroke.
     */
    // Note: scaleFactor should be declated as a double.
    // the canvas size is 250 x 250 or you can getWidth(), getHeight() from the
    // canvas.
    
    // TO DO: Finish insertOnePoint as indicated
    /**
     * insertOnePoint - inserts a new point between the two points that are the
     * farthest apart in the userStroke array. There must be at least two points
     * in the array
     */
    private void insertOnePoint()
    {
        int maxPosition = 0, newX, newY, distance;
        // compute distance between point 0 and point 1
        int maxDistance = (int) userStroke[0].distance(userStroke[1]);
      // TO DO: finish finding maxDistance and maxPosition

        // slide that are to the right of cell maxPosition right by one
        for (int i = 1; i > maxPosition + 1; i--)
            userStroke[i] = userStroke[i - 1];

        // Insert the average
        newX = (int) (userStroke[maxPosition].getX() + userStroke[maxPosition + 2]
                .getX()) / 2;
        newY = (int) (userStroke[maxPosition].getY() + userStroke[maxPosition + 2]
                .getY()) / 2;
        userStroke[maxPosition + 1] = new Point(newX, newY);

        nextFree++;
    }

    /**
     * normalizeNumPoints - Adds points to the userStroke by inserting points
     * repeatedly until there are STROKESIZE points in the stroke
     */
    public void normalizeNumPoints()
    {
        while (nextFree < STROKESIZE) {
            insertOnePoint();
        }
    }

    // TO DO: Write computeScore()
    public int ComputeScore(int j)
    { 
        int score = 0;
        for(int i = 0; i < nextFree; i++)
        {
            score = score + (int)userStroke[i].distance(baseSet[i][j]);
        }
        return score;
    }
    /**
     * computeScore Computes and returns a "score" that is a measure of how
     * closely the normalized userStroke array matches a given pattern array in
     * the baseset array. The score is the sum of the distances between
     * corresponding points in the userStroke array and the pattern array.
     * 
     * @param digitToCompare
     *            The index of the pattern in the baseset with which to compute
     *            the score
     */

    // TO DO: Write findMatch()
    /**
     * findMatch - Finds and returns the index (an int) of the base set pattern
     * which most closely matches the user stroke.
     */
    public int findMatch()
    {
        
        
        // Process the user's stroke: 1) translate, 2) scale, 3) normalize
        // Compare the resulting userStroke array with each array in the baseset
        // array
        // You are to return the digit with the smallest score, using the
        // computeScore method above
        
        return 0; // Replace this line
                  // (it is just here to get the imcomplete version to compile)
    }

    // TO DO: Write findMinX
    //
     //* findMinX - returns the smallest x value in the userStroke array of points
     //*/
    // nextFree is the index of the NEXT element to use in the userStroke array
    // The x coordinate of a point in the userStroke array can be referenced
    // as: userStroke[i].x where i is the index of the Point you are
    // referencing.
        public int findMinX()
    {   int minx = userStroke[0].x;
        for(int i = 0; i < nextFree; i++)
        {
            if (userStroke[i].x < minx )
            {
                minx = userStroke[i].x;
            }
        } return minx;
    } 
    // TO DO: Write findMinY
    public int findMinY()
    {   int miny = userStroke[0].x;
        for(int i = 0; i < nextFree; i++)
        {
            if (userStroke[i].y < miny )
            {
                miny = userStroke[i].y;
            }
        } return miny;
    } 
     /* findMinY - returns the smallest y value in the userStroke array of points
     */
    // nextFree is the index of the NEXT element to use in the userStroke array
    // The y coordinate of a point in the userStroke array can be referenced
    // as: userStroke[i].y where i is the index of the Point you are
    // referencing.
    
    // TO DO: Write findMaxX
    public int findMaxX()
    {   int maxx = userStroke[0].x;
        for(int i = 0; i < nextFree; i++)
        {
            if (userStroke[i].x > maxx )
            {
                maxx = userStroke[i].x;
            }
        } return maxx;
    } 
    // * findMaxX - returns the largest x value in the userStroke array of points
     //*/
    // nextFree is the index of the NEXT element to use in the userStroke array
    // The x coordinate of a point in the userStroke array can be referenced
    // as: userStroke[i].x where i is the index of the Point you are
    // referencing.
    
    // TO DO: Write findMaxY
    public int findMaxY()
    {   int maxy = userStroke[0].y;
        for(int i = 0; i < nextFree; i++)
        {
            if (userStroke[i].y > maxy )
            {
                maxy = userStroke[i].y;
            }
        } return maxy;
    } 
    /**
    // * findMaxY - returns the largest y value in the userStroke array of points
     //*/
    // nextFree is the index of the NEXT element to use in the userStroke array
    // The y coordinate of a point in the userStroke array can be referenced
    // as: userStroke[i].y where i is the index of the Point you are
    // referencing.
    // After this function is called new points will be added starting at cell 0
    // of the user stroke array.
    
    public void resetUserStroke()
    {
        nextFree = 0;
    }

    // Returns the number of points currently in the user stroke array.
    public int numUserPoints()
    {
        return nextFree;
    }

    // This returns the x portion of the i'th point in the user array
    public int getUserPointX(int i)
    {
        if ((i >= 0) && (i < nextFree))
            return ((int) userStroke[i].getX());
        else {
            System.out.println("Invalid value of i in getUserPoint");
            return (0);
        }
    }

    // This returns the y portion of the i'th point in the user array
    public int getUserPointY(int i)
    {
        if ((i >= 0) && (i < nextFree))
            return ((int) userStroke[i].getY());
        else {
            System.out.println("Invalid value of i in getUserPoint");
            return (0);
        }
    }

    public void addUserPoint(Point newPoint)
    {
        if (nextFree < STROKESIZE) {
            userStroke[nextFree] = newPoint;
            nextFree++;
        }
    }
}
