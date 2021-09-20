/*
	file input/output code
	------------------------------------------------------
	future goal to have everything outputed via text file
	Simulation Format
	M- Map Size followed by X,Y coordinate:   Eg:  M 6 8
	S- Start location followed by X,Y coordinate: Eg: S 2 3
	W- Wall followed by XY coordinate:             Eg:  W 2 3
	E- End of file
	All X Y coordinates start at index of 0. The smallest X coordinate is at the left of the screen.  The smallest Y coordinate is at the top of the screen.

	general format
	M NumberOfX NumberOfY
*/

/*
	two files to read in:
		1. Map File
		2. Barber File

	Things to consider:
		1. Files follow similar format(x,y)

*/

/*
	Final program
		Input:  Prompt the user for two files in this order: mapfile then barberfile.  Only one map file and one barber file will be used.  Your program must have no other input. 

		Output: Display a graphical representation of the path that your Super Scandro travels during each time step.  Display the walls.   Display the barbers at each time step.  Use the following text values:
		Scandro:  S
		Walls:    W
		Barbers: B
		Path: *   (Show a path from Scandro's CURRENT location to the goal)

		Display the current timestep somewhere on the screen:  Time = 0, Time =1, etc.  
		Your final timestep should show how long it took to get Super Scandro to his goal.  
		If at any point you discover there is no path to the end, display "NO PATH."  
		Do not wait around several time steps to see if the barbers decide to move.
	*/

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.PriorityQueue; 
import java.util.Scanner;

public class astar
{
	public static void main( String [] args )
	{
		System.out.println("Please enter the name of the map file: ");
		Scanner in = new Scanner(System.in);
		String mapFile = in.nextLine();
		System.out.println("Please enter the name of the barber file: ");
		String barberFile = in.nextLine();
		///////////////////////////////MAP_READ_AND_PARSE//////////////////////////////////////////
		List<Inventory> mapItem = new ArrayList<>();
		try
        {
            // create a Buffered Reader object instance with a FileReader
            BufferedReader br = new BufferedReader(new FileReader(mapFile));

            // read the first line from the text file
            String fileRead = br.readLine();

            // loop until all lines are read
            while (fileRead.equals("E") == false)
            {
            	// use string.split to load a string array with the values from each line of
                // the file, using a space as the delimiter
            	String[] tokenize = fileRead.split(" ");

            	// assume file is made correctly
                // and make temporary variables for the three types of data
            	String locType = tokenize[0];
            	int xAxis = Integer.parseInt(tokenize[1]);
            	int yAxis = Integer.parseInt(tokenize[2]);

            	// create temporary instance of Inventory object
                // and load with three data values
            	Inventory tempObj = new Inventory(locType, xAxis, yAxis);

            	// add to array list
            	mapItem.add(tempObj);

            	// read next line before looping
                // if end of file reached 
            	fileRead = br.readLine();
            }
            // close file stream
            br.close();
        }

        // handle exceptions
        catch (FileNotFoundException fnfe)
        {
            System.out.println("file not found");
        }

        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        // testing mapItem data
        /*for (Inventory each : mapItem)
    	{
        	System.out.println(each.toString());
        	//System.out.println(each.getItem());
    	}*/
    	////////////////////////////////////////////////////////////////////////////////////////////





		
    	////////////////////////////DRAWING_THE_MAP/////////////////////////////////////////////////
    	String[][] mapArray = null;
    	int xRows=0, yColumns=0, scanX=0, scanY=0, goalX=0, goalY=0, wallCount=0;
    	for (Inventory each : mapItem)
    	{
			if (each.getItem().equals("M"))
    		{
    			xRows = each.getX();
    			yColumns = each.getY();
    			mapArray = new String[xRows][yColumns];
    		}

    		if (each.getItem().equals("S"))
    		{
    			//System.out.println(each.getX() + " " + each.getY());
				scanX = each.getX();
				scanY = each.getY();
    			mapArray[scanX][scanY] = "S";

    		}
			
			if (each.getItem().equals("G"))
    		{
				goalX = each.getX();
				goalY = each.getY();
    			mapArray[goalX][goalY] = "G";
    		}

    		if (each.getItem().equals("W"))
    		{
    			mapArray[each.getX()][each.getY()] = "W";
				wallCount++;
    		} 

    	}


		for (int i=0; i<xRows; i++)
    	{
    		for(int j=0; j<yColumns; j++)
    		{
    			if (mapArray[i][j] == null)
    			{
    				mapArray[i][j] = " ";
    			}
    		}
    	} 
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		/*
		/////////////////////////////////PAINTING_THE_MAP(NO_BARBERS_YET)/////////////////////////////////////////////////
		
		// line used to print array to screen. useful.
		System.out.println(Arrays.deepToString(mapArray).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
		System.out.println("\n----------------------------------------------------------------\n");
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		*/
		////////////////////////////////ADDING_BARBERS///////////////////////////////////////////////
		int barbCount = 0;
		List<bInventory> barbItem = new ArrayList<>();
		try
        {
            // create a Buffered Reader object instance with a FileReader
            BufferedReader br = new BufferedReader(new FileReader(barberFile));

            // read the first line from the text file
            String fileRead = br.readLine();

            // loop until all lines are read
            while (fileRead.equals("-1") == false)
            {
            	// use string.split to load a string array with the values from each line of
                // the file, using a space as the delimiter
            	String[] tokenize = fileRead.split(" ");

            	// assume file is made correctly
                // and make temporary variables for the three types of data
            	double timestep = Double.parseDouble(tokenize[0]);
            	int xAxisBarb = Integer.parseInt(tokenize[1]);
            	int yAxisBarb = Integer.parseInt(tokenize[2]);

            	// create temporary instance of Inventory object
                // and load with three data values
            	bInventory tempObj = new bInventory(timestep, xAxisBarb, yAxisBarb);

            	// add to array list
            	barbItem.add(tempObj);

            	// read next line before looping
                // if end of file reached 
            	fileRead = br.readLine();
				barbCount++;
            }
			// adding -1 for purpose later. may not need it.
			barbItem.add(new bInventory(-1, 0, 0));
            // close file stream
            br.close();
        }

        // handle exceptions
        catch (FileNotFoundException fnfe)
        {
            System.out.println("file not found");
        }

        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
		
		int[][] wallArr = new int[wallCount][2];
		int counter = 0;
		for (Inventory each : mapItem)
    	{
			
			if (each.getItem().equals("W"))
    		{
    			wallArr[counter][0] = each.getX();
				wallArr[counter][1] = each.getY();
				counter++;
    		} 
		}
		int[][] barbArr = new int[barbCount+1][3];
		
		for (bInventory each : barbItem)
		{
			if (barbCount == 0)
			{
				barbArr[barbCount][0] = (int)each.getTstep();
				barbArr[barbCount][1] = each.getX();
				barbArr[barbCount][2] = each.getY();
				break;
			}
			else
			{
				barbArr[barbCount][0] = (int)each.getTstep();
				barbArr[barbCount][1] = each.getX();
				barbArr[barbCount--][2] = each.getY();
			}
			
		}
		
		int my_cols = barbArr.length;
		int my_rows = barbArr[0].length;
		int[][] revArray = new int[my_cols][my_rows];
		for(int i = my_cols-1; i >= 0; i--) 
		{
			for(int j = my_rows-1; j >= 0; j--) 
			{
				revArray[my_cols-1-i][my_rows-1-j] = barbArr[i][j];
			}
		}
		
		//System.out.println(revArray[barbArr.length-1][0] + "  " + revArray[barbArr.length-1][1] + "  " + revArray[barbArr.length-1][2]);
		
		//public AStarAlgo(int xDim, int yDim, int sx, int sy, int gx, int gy, int[][] walls)
		double tCount = 0;
		Cell hold = new Cell(scanX, scanY);
		AStarAlgo ai = null;
		while ((hold.i != goalX) || (hold.j != goalY))
		{
			ai = new AStarAlgo(xRows, yColumns, hold.i, hold.j, goalX, goalY, wallArr, revArray);
			ai.process();
			hold = ai.displaySolution();
			//System.out.println(hold);
			
			//System.out.println(hold.i + " " + goalX + " " + hold.j + " " + goalY);
		}
		//System.out.println(hold.i + " " + goalX + " " + hold.j + " " + goalY);
		
		
		//I had Scandro reaching the goal at one point...
		//That is no longer working. passing in 
		ai = new AStarAlgo(xRows, yColumns, hold.i, hold.j, goalX, goalY, wallArr, revArray);
		ai.process();
		hold = ai.displaySolution();
		
		
		if (hold.i == goalX && hold.j == goalY)
		{
			System.out.println("The goal was reached!");
			ai.setGoalCellS();
		}
		
		
		System.out.println("EXIT");
		/*
		// testing barbItem data
		for (bInventory each : barbItem)
    	{
        	System.out.println(each.toString());
        	//System.out.println(each.getItem());
    	} 
		
		double tCount = 0;
		String[][] newMapArray = mapArray.clone();
		for (bInventory each : barbItem)
    	{
			if (each.getTstep() < 0)
			{
				System.exit(0);
			}
			if (tCount == each.getTstep())
			{
				newMapArray[each.getX()][each.getY()] = "B";
			}
			if (tCount != each.getTstep())
			{
				System.out.println(Arrays.deepToString(newMapArray).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
				System.out.println("\n----------------------------------------------------------------\n");
				//////////////////THERE'S_A_LOT_THAT_NEEDS_TO_HAPPEN_HERE!/////////////////////////////////////////////
				
				///////////////////////////////////////////////////////////////////////////////////////////////////////
				newMapArray = mapArray.clone();
				tCount++;
			}
		}
		*/

	}
	
	
} 

//create class similar to Inventory but for the barbers. It will take in 3 integers or doubles...

