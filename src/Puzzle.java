package ahsan;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Puzzle {
	private Node initial, goal;
	private int size;
	
	
	public static Scanner read;
	public final int dx[]={1,0,-1,0};
	public final int dy[]={0,1,0,-1};
	Map<Node,Node> parent;
	Map<Integer,Integer> goalRowMap;
	Map<Integer,Integer> goalColumnMap;
	
	public void readPuzzle(){
		System.out.println("Enter number of tiles:");
		size=read.nextInt();
		size=(int) Math.sqrt(size+1);		
		
		initial=new Node(size);
		goal = new Node(size);
		
		System.out.println("Enter initial board state:");
		initial.readBoard(true);
		System.out.println("Enter goal board state:");
		goal.readBoard(false);
	}
	
	public void goalMap(){
		goalRowMap = new HashMap<Integer,Integer>();
		goalColumnMap = new HashMap<Integer,Integer>();
		
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				int v = goal.getValue(i,j);
				goalRowMap.put(v,i);
				goalColumnMap.put(v, j);
			}
		}
	}
	
	
	int getInvCount()
	{
	    int inv_count = 0;
	    for (int i = 0; i < size * size - 1; i++)
	    {
	        for (int j = i + 1; j < size * size; j++)
	        {
	            // count pairs(i, j) such that i appears
	            // before j, but i > j.
	        	int aj=initial.getValue(j/size, j%size);
	        	int ai= initial.getValue(i/size, i%size);
	        	
	        	int aiPos= goalRowMap.get(ai)*size+ goalColumnMap.get(ai);
	        	int ajPos= goalRowMap.get(aj)*size+ goalColumnMap.get(aj);
	            if (aj!=0 && ai!=0 &&  aiPos> ajPos) inv_count++;
	        }
	    }
	    return inv_count;
	}
	
	public boolean isSolvabe(){
		int invCount = getInvCount();
		System.out.println("inv: "+ invCount);
	    // If grid is odd, return true if inversion
	    // count is even.
	    if (size % 2==1) return (invCount %2 == 0);
	    else{
	    	int pos = size-initial.zero_row;
	    	if(pos%2==1){
	    		return (invCount%2==0);
	    	}
	    	else{
	    		return (invCount%2==1);
	    	}
	    	
	    }
	}
	public boolean solve(){
		readPuzzle();
		goalMap();
		
		
		System.out.println("done taking input");
		initial.printBoard();
		goal.printBoard();
		
		
		if(!isSolvabe()){
			return false;
		}
		
		PriorityQueue <Node> q =new PriorityQueue<Node>(new Comparator<Node>(){
			@Override
			public int compare(Node a, Node b) {
				// TODO Auto-generated method stub
				return a.fscore-b.fscore;
			}
			
		});
		parent = new HashMap<Node,Node>();
		Map<Node, Boolean> closedSet = new HashMap<Node,Boolean>();
		Map<Node, Boolean> openSet = new HashMap<Node,Boolean>();
		
		
		///////////////////////////////////////////////////////////////////
		initial.gscore = 0;
		initial.fscore = initial.gscore+heuristics1(initial);
		q.add(initial);
		openSet.put(initial, true);
		while(!q.isEmpty()){
			Node u = q.remove();
			u.printBoard();
			
			openSet.put(u,false);
			if(u.equals(goal)){
				System.out.println("got the solution");
				constructSolution(goal,0);
				return true;
			}
			System.out.println("current fscore: "+u.fscore);
			
			closedSet.put(u, true);
			for(int i = 0; i < 4; i++){
				Node v=new Node(u);
				
				v.zero_row = u.zero_row + dx[i];
				v.zero_col = u.zero_col + dy[i];

				if(v.zero_row < 0 || v.zero_row >=size || v.zero_col <0 ||v.zero_col >=size) continue;
				
				//else swap
				v.swap(v.zero_row,v.zero_col,u.zero_row,u.zero_col);
				v.printBoard();
				
				///neighbor calculation done
				/////////////////////////////////////////////
				if(closedSet.containsKey(v)) continue;
				System.out.println("hererr");

				int tentative_gscore = u.gscore + 1;
				
				if(!openSet.containsKey(v)|| tentative_gscore< v.gscore){
					parent.put(v,u);
					v.gscore=tentative_gscore;
					v.fscore=v.gscore+heuristics1(v);
					System.out.println("neighbor fscore: "+v.fscore);

					if(true){
						//q.remove(v);
						q.add(v);
					}
					
				}
				
				System.out.println("ok so far");
				//read.next();
			}
		}
		return false;
	}
	
	void constructSolution(Node cur, int length){
		if(cur.equals(initial)){
			System.out.println("Solution Length: "+length);
			cur.printBoard();
			return;
		}
		constructSolution(parent.get(cur),length+1);
		cur.printBoard();
	}
	
	int heuristics1(Node a){
		int manhattan = 0, linearConflict = 0;
		for(int i = 0; i < size; i++){
			for(int j = 0; j <size; j++){
				int v = a.getValue(i, j);
				if(v==0) continue;
				manhattan += Math.abs(goalRowMap.get(v)-i)+Math.abs(goalColumnMap.get(v)-j);
			}
		}
		int rowConflict = 0;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				for(int k = j+1; k <size; k++){
					if(goalRowMap.get(a.getValue(i, j))==i && goalRowMap.get(a.getValue(i, k))==i){
						if(goalColumnMap.get(a.getValue(i, j)) > goalColumnMap.get(a.getValue(i, k))){
							rowConflict++;
						}
					}
				}
			}
		}
		System.out.println(rowConflict+"");
		
		int colConflict = 0;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				for(int k = i+1; k <size; k++){
					if(goalColumnMap.get(a.getValue(i, j))==j && goalColumnMap.get(a.getValue(k,j))==j){
						if(goalRowMap.get(a.getValue(i, j)) > goalRowMap.get(a.getValue(k,j))){
							colConflict++;
						}
					}
				}
			}
		}
		System.out.println(colConflict+"");
		
		linearConflict = rowConflict + colConflict;
		return manhattan+2*linearConflict;
	}
	
	
	public static void main(String[] args){
		read = new Scanner(System.in);
		boolean success = new Puzzle().solve();
		if(success) System.out.println("done");
		else System.out.println("impossible");
		read.close();
	}
}
