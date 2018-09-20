package ahsan;

import java.util.Objects;

class Node{
	private int size;
	private int board[][];
	int zero_row;
	int zero_col;
	
	int fscore;
	int gscore;
	
	
	public int getValue(int r, int c){
		return board[r][c];
	}
	public Node(int size){
		this.size = size;
		board = new int[size][size]; 
	}
	public Node(Node another){
		this.size=another.size;

		this.board=new int[size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				this.board[i][j]=another.board[i][j];
			}

		}

		this.zero_row=another.zero_row;
		this.zero_col=another.zero_col;		
	}
	
	
	@Override
	public boolean equals(Object o){
		if(this==o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Node another=(Node) o;
		//System.out.println("hoccheeehoefh");
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if(this.board[i][j]!=  another.board[i][j]) return false;
			}
		}
		return true;
	}
	@Override
	public int hashCode(){
		int hash = 0;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				hash = Objects.hash(board[i][j],i,j);
			}
		}
		System.out.println(hash+"");
		return hash;
	}
	
	public void readBoard(boolean b){
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if(Puzzle.read.hasNextInt()) board[i][j]=Puzzle.read.nextInt();
				if(b && board[i][j]==0){
					zero_row = i;
					zero_col = j;
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public void printBoard(){
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				System.out.print(board[i][j]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public void swap(int old_r, int old_c, int new_r, int new_c){
		int temp = board[old_r][old_c];
		System.out.println(board[old_r][old_c]+" "+board[new_r][new_c]);
		board[old_r][old_c]=board[new_r][new_c];
		board[new_r][new_c]=temp;
		printBoard();
	}
}
