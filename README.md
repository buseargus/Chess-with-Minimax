# Chess-with-Minimax
Development of artificial intelligence playing chess using minimax search.

## The Steps of the Game
First of all, I designed my own chess game. It's simple and works on the console. Before the game starts, program ask for the game mode. If "1" is selected, the game is played with the minimax AI, if "2" is selected, it's played by two people taking turns making moves.

### When "2" is selected;
  - The game board is printed on the screen. 
  - The location of the chess piece to be played and the target location to which this piece is intented to be taken are asked. 
  - All the rules of chess are implemented, so it is checked if the move of the selected piece is valid fot this target. If so, a move is made and the new board is created and rotated for the other user, otherwise an error message is given. 
  - After that, it's checked whether the king is in danger.

### When "1" is selected

  - After the user makes a move, it's the turn of the AI.
  - A tree is created which takes the current state of the board as root.
  - Then, program enters into a recursive method that takes the root, depth, and the information of moving piece color as parameters.
    - This method checks the depth of the root sent. If the desired value has been reached, that is, the leaf nodes have been reached, it calculates the score of the board. If not, it chooses one of the two conditions according to moving piece color. Here nodes that take the newly reached current state of the board as their parents are created. These nodes contain all possible moves that can be made based on the newly reached current state of the board.
    - The score of the board is calculated according to the table below:

    ![alt text](https://github.com/buseargus/Chess-with-Minimax/blob/master/ChessPieceTable.jpg?raw=true)
    
  - The tree is sent to the minimax method. The most profitable move that can be possibly made is calculated.
    - If the calculated score of this move is 0, that is, there is no chance to eat any of the pieces, a random move is made. If the score of the move is not 0, the move returned from the minimax method is made.
