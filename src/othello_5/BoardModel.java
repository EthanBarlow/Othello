/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package othello_5;

/**
 *
 * BoardModel is the java class that will take care of all of the backend processing.
 * It handles all of the game logic such as checking if a move is valid, flipping the chips that are sandwiched, and changing who's turn it is.
 * 
 * @author Ethan
 */
public class BoardModel {
     
    /**
     * 
     * Player1 will be using WHITE Markers
     * Player2 will be using BLACK Markers
     */
    
     /** 
     * the Marker enum class will be used to easily identify if a space on the board is occupied and who is occupying it
     * The values for the Marker enum class are: EMPTY, WHITE, BLACK
     */    
    public static enum Marker{ 

        /**
         *Denotes that no player has placed a chip on the given location of the board
         */
        EMPTY, 

        /**
         *Denotes player 2's chips as WHITE even if the chip color changes during the game
         */
        WHITE, 

        /**
         *Denotes player 1's chips as BLACK even if the chip color changes during the game         *
         */
        BLACK}
    /**
     * the Turn enum class will be used to easily determine if it is player 1's turn or player 2's turn without having to use modulus
     * the values are PLAYER1 and PLAYER2
     */
    public static enum Turn{

        /**
         *PLAYER1 denotes that it is player 1's turn, this enumeration is easier to track than a digit or char
         */
        PLAYER1, 

        /**
         *PLAYER2 denotes that it is player 2's turn, this enumeration is easier to track than a digit or char         *
         */
        PLAYER2}
    
    /**
     * The constants named by directions (North, south, ....) will be used to check in that direction from a chosen spot on the board
     */
    private final int NORTH=-1;//-1 because you are moving up on the board which == a decrease in the row
    private final int SOUTH=1;
    private final int EAST=1;
    private final int WEST=-1;
    private final int FIXED=0;//0 because that part of the array does not need to change
    
    private final int SIZE = 8;//the size of the board
    
    private Marker board [][];//the representation of the board as a 2D array
    private Turn currentTurn;

    /**
     * Default constructor - initializes board, a Marker[][], that is SIZExSIZE
     */
    public BoardModel(){//the class constructor
        board= new Marker[SIZE][SIZE];
        //set up the board
        reset();
    }
    
    /**
     *resets the entire board to the original state, also used in the constructor for initial setup
     *Every space in the boardModel object is set to EMPTY except for the for center spots
     */
    public void reset(){//resets the entire board to the original state, also used in the constructor for initial setup
        for(int row = 0; row<SIZE;row++){
            for(int col = 0; col<SIZE; col++){
                board[row][col]=Marker.EMPTY;
            }
        }
        
        //setup the initial four pieces
        board[3][3]=Marker.WHITE;
        board[4][4]=Marker.WHITE;
        
        board[3][4]=Marker.BLACK;
        board[4][3]=Marker.BLACK;
        
        setCurrentTurn(Turn.PLAYER1);
    }
    
    /**
     * 
     * @param row passed in from the front end
     * @param column passed in from the front end and decided by user
     * @return a boolean value to let the main program know that a move either succeeded or failed 
     */
    public boolean makeMove(final int row, final int column){
        //successCount will hold boolean values showing whether or not a certain direction succeeded
        boolean successCount[] = new boolean [8];
//        System.out.println("\n\nRow " + row + " column " + column +"\n");
        //check all 8 directions
//        System.out.println("Check north: ");
        successCount[0]=isValidAndFlipped(row,column, NORTH, FIXED);
//        System.out.println("Check south: ");
        successCount[1]=isValidAndFlipped(row,column,SOUTH,FIXED);
//        System.out.println("Check east ");
        successCount[2]=isValidAndFlipped(row, column, FIXED, EAST);
//        System.out.println("check west ");
        successCount[3]=isValidAndFlipped(row,column,FIXED,WEST);
//        System.out.println("check north east ");
        successCount[4]=isValidAndFlipped(row,column,NORTH,EAST);
//        System.out.println("check north west ");
        successCount[5]=isValidAndFlipped(row,column,NORTH,WEST);
//        System.out.println("Check south east ");
        successCount[6]=isValidAndFlipped(row,column,SOUTH,EAST);
//        System.out.println("check south west ");
        successCount[7]=isValidAndFlipped(row,column,SOUTH,WEST);
        
        //check for at least one true value in successCount
        for(int i=0;i<SIZE;i++){
            if(successCount[i]){
                setCurrentTurn();
                return true;
            }
        }
        
        return false;
    }
    
    //finished ... I think
    private boolean isValidAndFlipped(final int ogRow, final int ogCol, final int rowDirection, final int colDirection){
        boolean valid = false;
        //System.out.println("...checking...");
        //checking validity
        //System.out.println("\nBefore the first check\n");
        
        Marker currentChip = (getCurrentTurn()==Turn.PLAYER1) ? Marker.BLACK : Marker.WHITE;
        //check for an empty space and any spaces that are outofbounds or their adjacent spaces being out of bounds
        if((getSpace(ogRow, ogCol)!=Marker.EMPTY&&getSpace(ogRow,ogCol)!=currentChip) || ogRow<0 || ogRow>=SIZE || ogCol<0 || ogCol>=SIZE || ogRow+rowDirection<0 || ogRow+rowDirection>= SIZE || ogCol+colDirection<0 || ogCol+colDirection>=SIZE){return false;}
        //System.out.println("\nAfter first check\n");
        
        
        int tempRow = ogRow + rowDirection;
        int tempCol = ogCol + colDirection;
        
        int endRow=0;//initialized to make the compiler happy
        int endCol=0;
        
        //if the adjacent spot is the same color or empty, return false
        if(getSpace(tempRow, tempCol)==currentChip||getSpace(tempRow, tempCol)==Marker.EMPTY){return false;}
        //increment the temp variables again
        tempRow+=rowDirection;
        tempCol+=colDirection;
        //System.out.println("\nAfter second check\n");
        //adjacent spaces have already been cleared so now the space to check is at least 2 from the original
        while(!valid && tempRow>=0 && tempRow<SIZE && tempCol>=0 && tempCol<SIZE ){
            //if(getSpace(tempRow, tempCol)==Marker.EMPTY){return false;}
           // System.out.println("\nInside the while loop\n");
            if(getSpace(tempRow, tempCol)==currentChip){
                endRow=tempRow;
                endCol=tempCol;
                valid=true;
            }
            
            else if(getSpace(tempRow, tempCol)==Marker.EMPTY){
                return false;
            }
            
            //increment the temp variables again
            tempRow+=rowDirection;
            tempCol+=colDirection;
            
        }
        
        //end validity check
        
        //System.out.println("the status of valid is " + valid);
        
        //begin flipping in one direction if valid
        if(valid){
            int row = ogRow;
            int col = ogCol;
            
            if(rowDirection==0){//row doesn't change so don't check it in the while loop
                while(col!=endCol){
                    setSpace(row,col,currentChip);
                   // System.out.println("\nChanging chip " + row + " " +col + " to " + currentChip);
                    row+=rowDirection;
                    col+=colDirection;
                }
            }
            
            else if(colDirection==0){//col doesn't change so don't check it in the while loop
                while(row!=endRow){
                    setSpace(row,col,currentChip);
                   // System.out.println("\nChanging chip " + row + " " +col + " to " + currentChip);
                    row+=rowDirection;
                    col+=colDirection;
                }
            }
            
            else{
                while(row!=endRow && col!=endCol){
                    setSpace(row,col,currentChip);
                     //System.out.println("\nChanging chip " + row + " " +col + " to " + currentChip);
                    row+=rowDirection;
                    col+=colDirection;
                }
            }
            
        }
        
        
        return valid;
    }
    
    
    private void setSpace(int row, int col, Marker chip){
        this.board[row][col]=chip;
    }
    
    /**
     *
     * @param row the row of the space to check
     * @param col the column of the space to check
     * @return an enumerated variable of type Marker: WHITE, BLACK, or EMPTY
     */
    public Marker getSpace(int row, int col){
        return this.board[row][col];
    }
    
    /**
     * This method returns a variable of enumerated type: Turn
     * @return either Turn.PLAYER1 or Turn.PLAYER2
     */
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    private void setCurrentTurn() {
        this.currentTurn= (Turn.PLAYER1==this.currentTurn) ? Turn.PLAYER2 : Turn.PLAYER1;
    }    
    
    
    private void setCurrentTurn(Turn firstTurn) {
        this.currentTurn= firstTurn;
    } 
    
    /**
     * Technically performs the same action as the setCurrentTurn() method, 
     * but this is publicly accessible and should only be called once in Othello.java
     */
    public void skipCurrentTurn(){
        this.currentTurn= (Turn.PLAYER1==this.currentTurn) ? Turn.PLAYER2 : Turn.PLAYER1;
    }
    
    @Override
    /**
     * returns a string representation of the board
     */
    public String toString(){
        String display="       0      1        2        3      4      5        6        7\n";
        
        for(int row = 0; row<SIZE;row++){
            display+=row+" ";
            for(int col = 0; col<SIZE; col++){
                display+="   " + board[row][col];
            }
            display+="\n";
        }
        
        return display;
    }
    
    
}
