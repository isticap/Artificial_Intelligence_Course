import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Stack;
import java.util.List;
import java.util.Arrays;

public class AStarAlgo {
	
	// cost of vertical/horizontal moves
	public static final int V_H_COST = 10;
	
	// cells for grid
	private Cell[][] grid;
	
	// priority queue for open cells.
	// open cells get evaluated
	// cells with lowest cost come first
	private PriorityQueue<Cell> openCells;
	
	// closed cells
	private boolean[][] closedCells;
	
	// scandro start cell
	private int sX, sY;
	
	// new scandro start cell;
	private int nsX=0, nsY=0;
	
	// wall array
	private int[][] wallArray;
	
	// barber array
	private int[][] barberArray;
	
	// goal cell
	private int gX, gY;
	
	// timestep tracker
	private static int Tstep = 0;
	
	// maintainer
	private static int mainTain = 0;
	
	
	public AStarAlgo(int xDim, int yDim, int sx, int sy, int gx, int gy, int[][] walls, int[][] barbArr)
	{
		grid = new Cell[xDim][yDim];
		closedCells = new boolean[xDim][yDim];
		/* openCells = new PriorityQueue<Cell>((Cell cl, Cell c2) -> {
			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		}); */
		
		Comparator<Cell> CellPriorityComparitor = new Comparator<Cell>() {
			@Override
			public int compare(Cell c1, Cell c2) 
			{
				return c1.finalCost< c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1:0; 
			}
		};
        openCells = new PriorityQueue<Cell>(CellPriorityComparitor);
		
		startCell(sx, sy);
		goalCell(gx, gy);
		
		// initialize heuristic(hints) and cells
		for (int i=0; i<grid.length; i++)
		{
			for (int j=0; j < grid[i].length; j++)
			{
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i - gx) + Math.abs(j - gy);
				grid[i][j].solution = false;
			}
		}
		
		grid[sX][sY].finalCost = 0;
		
		//lets put them walls in... this will also include barbers(eventually)
		for (int i=0; i < walls.length; i++)
		{
			addWallOnCell(walls[i][0], walls[i][1]);
		}
		wallArray = walls;
		
		//System.out.println(barbArr[mainTain][2]);
		
		
		while (Tstep == barbArr[mainTain][2])
		{
			addWallOnCell(barbArr[mainTain][1],barbArr[mainTain++][0]);
		}
		barberArray = barbArr;
	}
	
	public void addWallOnCell(int x, int y)
	{
		grid[x][y] = null;
	}
	
	/* public void wallorBarber(int x, int y)
	{
		for (int i=0; i < wallArray.length; i++)
		{
			if (x == wallArray[i][0] && y == wallArray[i][1])
			{
				System.out.print("W   ");
			}
			else if (x == barberArray[i][1] && y == barberArray[i][0])
				System.out.print("B   ");
		}
	}  */
	
	public void startCell(int x, int y)
	{
		sX = x;
		sY = y;
	}
	
	public void goalCell(int x, int y)
	{
		gX = x;
		gY = y;
	}
	
	public void updateCostIfNeeded(Cell current, Cell t, int cost)
	{
		if (t == null || closedCells[t.i][t.j])
		{
			return;
		}
		
		int tFinalCost = t.heuristicCost + cost;
		boolean isOpen = openCells.contains(t);
		
		if (!isOpen || tFinalCost < t.finalCost)
		{
			t.finalCost = tFinalCost;
			t.parent = current;
			
			if (!isOpen)
			{
				openCells.add(t);
			}
		}
	}
	
	public void process()
	{
		// add start location to list...
		openCells.add(grid[sX][sY]);
		Cell current;
		
		while (true)
		{
			current = openCells.poll();
			
			if (current == null)
			{
				break;
			}
			
			closedCells[current.i][current.j] = true;
			
			if (current.equals(grid[gX][gY]))
			{
				return;
			}
			
			Cell t;
			
			
			//up
			if (current.j - 1 >= 0)
			{
				t = grid[current.i][current.j - 1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}
			
			//down
			if (current.j + 1 < grid[0].length)
			{
				t = grid[current.i][current.j + 1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}
			
			//right
			if (current.i + 1 < grid.length)
			{
				t = grid[current.i + 1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}
			
			//left
			if (current.i - 1 >= 0)
			{
				t = grid[current.i - 1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}
			

		}
	}
	
	public void display()
	{
		System.out.println("Grid: ");
		
		for (int i=0; i<grid.length; i++)
		{
			for (int j=0; j<grid[i].length; j++)
			{
				if (i == sX && j == sY)
				{
					System.out.print("S   "); //Scandro Start Cell
				}
				else if (i == gX && j == gY)
				{
					System.out.print("G   "); //Goal Cell
				}
				else if (grid[i][j] != null)
				{
					System.out.printf("%-3d ",0); //empty cell
				}
				else
				{
					System.out.print("W   "); //Wall Cell
				}
			}
		
			System.out.println();
		}
		
		System.out.println();
		
	}
	
	public void displayScore()
	{
		System.out.println("\nScores for cells :");
		
		for (int i=0; i<grid.length; i++)
		{
			for (int j=0; j<grid[i].length; j++)
			{
				if (grid[i][j] != null)
				{
					System.out.printf("%-3d  ",grid[i][j].finalCost);
				}
				else
				{
					System.out.print("W  ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/* public void newLocationSet(int x, int y)
	{
		startCell(x,y);
		AStarAlgo ai = new AStarAlgo(grid.length, grid[0].length, x, y, gX, gY, new int[][] {{5,2},{6,1}});
		ai.process();
	} */
	
	public void setGoalCellS()
	{
		System.out.println();
		for (int i=0; i<grid.length; i++)
		{
			for (int j=0; j<grid[i].length; j++)
			{
				if (i == sX && j == sY)
				{
					System.out.print("0   "); //Scandro Start Cell
				}
				else if (i == gX && j == gY)
				{
					System.out.print("S   "); //Goal Cell
				} 
				else if (grid[i][j] != null)
				{
					System.out.printf("%-3s ",grid[i][j].solution ? "*" : "0"); //empty cell
				} 
				else
				{
					
					System.out.print("W   "); //Wall Cell
				} 
			}
	
			System.out.println();
		}
	}
	
	public Cell displaySolution()
	{
		
		Stack<Cell> pathStack = new Stack<>();
		if (closedCells[gX][gY])
		{
			//track back path
			System.out.println("timestep: " + Tstep++);
			System.out.println("Path :");
			Cell current = grid[gX][gY];
			System.out.print(current);
			grid[current.i][current.j].solution = true;
			
			while(current.parent != null)
			{
				System.out.print(" -> " + current.parent);
				grid[current.parent.i][current.parent.j].solution = true;
				pathStack.add(current); // first cell in stack is location Scandro moves to.
				current = current.parent; // current cell is now Scandros current location.
				
			}
			
			System.out.println();
			for (int i=0; i<grid.length; i++)
			{
				for (int j=0; j<grid[i].length; j++)
				{
					if (i == sX && j == sY)
					{
						System.out.print("S   "); //Scandro Start Cell
					}
					else if (i == gX && j == gY)
					{
						System.out.print("G   "); //Goal Cell
					}
					else if (grid[i][j] != null)
					{
						System.out.printf("%-3s ",grid[i][j].solution ? "*" : "0"); //empty cell
					}
					else	
					{
						System.out.print("W   ");
					}
				}
		
				System.out.println();
			}
			System.out.println();
			if (pathStack.empty())
			{
				System.exit(0);
			}
			if (pathStack.peek() != null){
				return pathStack.peek();
			}
			else
			{
				return null;
			}
			
		}
		else
		{
			System.out.println("NO POSSIBLE SOLUTION");
			return null;
		}
		
	}
}
////////////////////////////////////////////////////////////////////////////////////////////////////////