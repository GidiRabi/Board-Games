package Damca;

import java.util.*;

public class GameLogic_ implements PlayableLogic_ {

    public static final int BOARD_SIZE = 8;

    /**
     * The pieces of the game. <br>
     * from index 0 to 12 - defender pieces. (king_ is at index 6). <br>
     * from index 13 to 36 - attacker pawns.
     */
    private final ConcretePiece_[] gamePieces;
    /**
     * The position of the king_. <br>
     * Helps us to check if the game is finished.
     */
    private Position_ kingPosition;
    /**
     * The board of the game. <br>
     * The board is a 2D array of ConcretePiece. <br>
     */
    private ConcretePiece_[][] board;

    /**
     * The turn_ history of the game. <br>
     * It will be used to undo moves.
     */
    private final Stack<Turn_> turnsHistory;

    /**
     * The tracker of each cell in the board. <br>
     * Its tracks the pieces that were in each cell. <br>
     * The key is the position of the cell, and the value is a map of the pieces that were in the cell. <br>
     * the map key is the piece, and the value is the number of times that the piece was in the cell. <br>
     */
    private final Map<Position_, Map<ConcretePiece_, Integer>> boardStepTracer;

    /**
     * The first player_ of the game.
     */
    private final ConcretePlayer_ firstPlayer;

    /**
     * The second player_ of the game.
     */
    private final ConcretePlayer_ secondPlayer;

    /**
     * true if it's the second player_ turn_, false otherwise
     */
    private boolean secondPlayerTurn;

    /**
     * true if the game is finished, false otherwise
     */
    private boolean isGameFinished;

    /**
     * indicates how many pawns the second player_ has left
     * if the second player_ has 0 pawns left, the first player_ won. <br>
     * <p>
     * (the first player_ cant have 0 pawns left because the king_ cant be eaten)
     */
    private int playerTwoRemainingPawns;

    public GameLogic_() {
        // initialize the board, the tracker, the turns history, the pieces, the players, and the king_ position
        board = new ConcretePiece_[BOARD_SIZE][BOARD_SIZE];
        boardStepTracer = new HashMap<>();
        turnsHistory = new Stack<>();
        gamePieces = new ConcretePiece_[37]; // 12 defender pawns + 1 king_ + 24 attacker pawns
        isGameFinished = false;
        playerTwoRemainingPawns = 24;

        // create the players
        firstPlayer = new ConcretePlayer_(true);
        secondPlayer = new ConcretePlayer_(false);
        secondPlayerTurn = true;


        // create the pieces and place them on the board
        firstPlayerPieces();
        secondPlayerPieces();
    }


    /**
     * helper method to put a pawn on the board. <br>
     * will be used in the constructor. <br>
     * will update the board, the tracker, and the pieces moves list.
     *
     * @param pieceNum        the number of the piece
     * @param initialPosition the initial position of the piece
     * @param player_          the player_ that the piece belongs to
     */
    private void putPawnOnBoard(int pieceNum, Position_ initialPosition, Player_ player_) {
        Pawn_ newPawn = new Pawn_(player_, pieceNum, initialPosition);
        board[initialPosition.row()][initialPosition.col()] = newPawn;
        if (player_.isPlayerOne()) {
            gamePieces[pieceNum - 1] = newPawn;
        } else {
            gamePieces[pieceNum + 12] = newPawn;
        }
        boardStepTracer.put(initialPosition, new HashMap<>(Map.of(newPawn, 1)));
    }

    /**
     * helper method to initialize the pieces of the first player_. <br>
     * will be used in the constructor. <br>
     * will update the board, the tracker, and the pieces moves list.
     */
    private void firstPlayerPieces() {
        // create the pieces for player_ one:

        // create the defender pawns
        putPawnOnBoard(1, new Position_(0, 7), firstPlayer);
        putPawnOnBoard(2, new Position_(2, 7), firstPlayer);
        putPawnOnBoard(3, new Position_(4, 7), firstPlayer);
        putPawnOnBoard(4, new Position_(6, 7), firstPlayer);

        // row 1
        putPawnOnBoard(5, new Position_(1, 6), firstPlayer);
        putPawnOnBoard(6, new Position_(3, 6), firstPlayer);
        putPawnOnBoard(7, new Position_(5, 6), firstPlayer);
        putPawnOnBoard(8, new Position_(7, 6), firstPlayer);

        // row 4
        putPawnOnBoard(9, new Position_(0, 5), firstPlayer);
        putPawnOnBoard(10, new Position_(2, 5), firstPlayer);
        putPawnOnBoard(11, new Position_(4, 5), firstPlayer);
        putPawnOnBoard(12, new Position_(6, 5), firstPlayer);
    }

    /**
     * helper method to initialize the pieces of the second player_. <br>
     * will be used in the constructor. <br>
     * will update the board, the tracker, and the pieces moves list.
     */
    private void secondPlayerPieces() {
        //  row 0
        putPawnOnBoard(1, new Position_(1, 0), secondPlayer);
        putPawnOnBoard(2, new Position_(3, 0), secondPlayer);
        putPawnOnBoard(3, new Position_(5, 0), secondPlayer);
        putPawnOnBoard(4, new Position_(7, 0), secondPlayer);

        // row 1
        putPawnOnBoard(5, new Position_(0, 1), secondPlayer);
        putPawnOnBoard(6, new Position_(2, 1), secondPlayer);
        putPawnOnBoard(7, new Position_(4, 1), secondPlayer);
        putPawnOnBoard(8, new Position_(6, 1), secondPlayer);

        // row 4
        putPawnOnBoard(9, new Position_(1, 2), secondPlayer);
        putPawnOnBoard(10, new Position_(3, 2), secondPlayer);
        putPawnOnBoard(11, new Position_(5, 2), secondPlayer);
        putPawnOnBoard(12, new Position_(7, 2), secondPlayer);

    }

    /**
     * Try to move a piece from position a to position b.
     * if the move is valid, update the board, the tracker, the turns history, the piece moves list, and the turn_ owner.
     *
     * @param a The starting position of the piece.
     * @param b The destination position for the piece.
     * @return true if the move was successful, false otherwise
     */
    @Override
    public boolean move(Position_ a, Position_ b) {
        // check if a and b are in the board
        checkIfPositionIsInBoard(a);
        checkIfPositionIsInBoard(b);

        ConcretePiece_ piece = board[a.row()][a.col()];
        Player_ currentPlayer = isSecondPlayerTurn() ? secondPlayer : firstPlayer;
        // check if there is a piece at position a
        if (piece == null)
            return false; // there is no piece at position a

        // check if the piece at position a belongs to the player_ who is playing
        if (piece.getOwner() != currentPlayer)
            return false; // the piece at position a does not belong to the player_ who is playing

        // if he tries to move to the same position
        if (a.equals(b))
            return false;

        //eating player
        /**
         * CHECK IF HES NOT ON THE EDGE OF THE GAME OR 1 NEAR THE EDGE
         */
        if(a.col() == b.col() - 2 || a.col() == b.col() + 2) {
            if (board[b.row()][b.col()] == null) {
                if (isSecondPlayerTurn()) {
                    if (board[a.row() + 1][a.col() -1] != null && board[a.row() + 1][a.col() -1].owner == firstPlayer) {
                        board[a.row() + 1][a.col() -1] = null;
                        board[b.row()][b.col()] = board[a.row()][a.col()];
                        board[a.row()][a.col()] = null;
                        secondPlayerTurn = !secondPlayerTurn;
                        updateGameStats();
                        return true;
                    }else if(board[a.row() + 1][a.col() +1] != null && board[a.row() + 1][a.col() +1].owner == firstPlayer){
                        board[a.row() + 1][a.col() +1] = null;
                        board[b.row()][b.col()] = board[a.row()][a.col()];
                        board[a.row()][a.col()] = null;
                        secondPlayerTurn = !secondPlayerTurn;
                        updateGameStats();
                        return true;
                    }
                } else {
                    if (board[a.row() - 1][a.col() -1] != null && board[a.row() - 1][a.col() -1].owner == secondPlayer) {
                        board[a.row() - 1][a.col() -1] = null;
                        board[b.row()][b.col()] = board[a.row()][a.col()];
                        board[a.row()][a.col()] = null;
                        secondPlayerTurn = !secondPlayerTurn;
                        updateGameStats();
                        return true;
                    }else if(board[a.row() - 1][a.col() +1] != null && board[a.row() - 1][a.col() +1].owner == secondPlayer){
                        board[a.row() - 1][a.col() +1] = null;
                        board[b.row()][b.col()] = board[a.row()][a.col()];
                        board[a.row()][a.col()] = null;
                        secondPlayerTurn = !secondPlayerTurn;
                        updateGameStats();
                        return true;
                    }
                }

            }
        }


//        if(!(piece instanceof King_))



        if(!(piece instanceof King_)){

            if(!(a.col() + 1 == b.col()  || a.col() - 1 == b.col())){
                return false;
            }
            if(isSecondPlayerTurn()) {
                if (!(b.row() - 1 == a.row())) {
                    return false;
                }
            }
            if(!isSecondPlayerTurn()) {
                if (!(b.row() + 1 == a.row())) {
                    return false;
                }
            }
        } else {
                if (Math.abs(a.col() - a.row()) != Math.abs(b.col()) - b.row()) {
                    return false;
                }
        }

        if(piece instanceof Pawn_) {
            //if theres nothing in that position move him there
            if (board[b.row()][b.col()] == null) {
                board[b.row()][b.col()] = piece;
                board[a.row()][a.col()] = null;
                secondPlayerTurn = !secondPlayerTurn;
                updateGameStats();
                return true;
            }
                //if theres an ally in that position do nothing
                if (board[b.row()][b.col()].getOwner() == currentPlayer) {
                    return false;
                }
                //if theres an enemy in that position eat him while jumping one over him if its free there
                if (board[b.row()][b.col()].getOwner() != currentPlayer) {
                    return false;
                }



        }

        return true;
    }

//    public boolean checkIfKing(Position_ pos){
//        //if its second player then he should be on bottom row, first player should be top row
//        if(isSecondPlayerTurn() && board[pos.row()][pos.col()].owner == secondPlayer){
//            if(pos.row() == 0){
//                board[pos.row()][pos.col()] = null;
////                ConcretePiece_[pos.row()][pos.col()] = new King_(secondPlayer,board[pos.row()][pos.col()].getPieceNum() , pos);
//                putPawnOnBoard(1, new Position_(0, 7), firstPlayer);
//            }
//        } else if(!isSecondPlayerTurn() && board[pos.row()][pos.col()].owner == firstPlayer){
//            if(pos.row() == 7){
//
//            }
//        }
//    }


    /**
     * Get the piece at a given position.
     *
     * @param position The position to get the piece from.
     * @return The piece at the given position, or null if there is no piece at the given position.
     */
    @Override
    public Piece_ getPieceAtPosition(Position_ position) {
        checkIfPositionIsInBoard(position);
        return board[position.row()][position.col()];
    }

    /**
     * @return the first player_
     */
    @Override
    public Player_ getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * @return the second player_
     */
    @Override
    public Player_ getSecondPlayer() {
        return secondPlayer;
    }


    private void updateGameStats() {
        int P1count=0;
        int P2count=0;
        for(int i=0;i<BOARD_SIZE;i++)
        {
            for(int j=0;j<BOARD_SIZE;j++)
            {
                if(board[i][j]!= null) {
                    if (board[i][j].owner == firstPlayer)
                        P1count++;
                    else if (board[i][j].owner == secondPlayer)
                        P2count++;
                }
            }
        }
        if(P2count==0)
        {
            firstPlayer.addWin();
            isGameFinished=true;
        }
        else if(P1count==0)
        {
            secondPlayer.addWin();
            isGameFinished=true;
        }

    }

    /**
     * check if the game is finished
     *
     * @return true if the game is finished, false otherwise
     */
    @Override
    public boolean isGameFinished() {
        return isGameFinished;
    }


    /**
     * check if it is the second player_'s turn_
     *
     * @return true if it is the second player_'s turn_, false otherwise
     */
    @Override
    public boolean isSecondPlayerTurn() {
        return secondPlayerTurn;
    }

    /**
     * reset the game to the initial state.
     * including the board and the data of the players
     */
    @Override
    public void reset() {
        // set the player_ turn_ to the second player_
        secondPlayerTurn = true;

        // reset the board
        for (Piece_[] row : board)
            Arrays.fill(row, null);

        // put the pieces on the board
        firstPlayerPieces();
        secondPlayerPieces();

        // clear the turns history
        turnsHistory.clear();
        // clear the tracker
        boardStepTracer.clear();
    }

    /**
     * undo the last move, restore the state of the game to the state before the last move, and the turn_'s owner, and update all the data in all the objects.
     */
    @Override
    public void undoLastMove() {

        // check if there is a move to undo
        if (turnsHistory.isEmpty())
            return;

        // get the last turn_
        Turn_ lastTurn = turnsHistory.pop();
        // get the piece of the last turn_
        ConcretePiece_ piece = lastTurn.getPiece();
        // return the piece to the last position
        board[lastTurn.getFrom().row()][lastTurn.getFrom().col()] = piece;
        board[lastTurn.getTo().row()][lastTurn.getTo().col()] = null;

        // update the piece moves list
        piece.getMoves().remove(piece.getMoves().size() - 1);

        //update the piece number of eats
        piece.setNumberOfEats(piece.getNumberOfEats() - lastTurn.getEatenPawn().size());

        // return the eaten pawns to the board
        for (Pawn_ pawn : lastTurn.getEatenPawn()) {
            // update the number of pawns that the second player_ has left
            if (pawn.getOwner() == secondPlayer) {
                playerTwoRemainingPawns++;
            }
            // get the last position that the pawn was in
            Position_ pawnLastPosition = pawn.getMoves().get(pawn.getMoves().size() - 1);
            board[pawnLastPosition.row()][pawnLastPosition.col()] = pawn;
        }

        // update the tracker - remove the piece from the set of pieces that were in the cell
        Map<ConcretePiece_, Integer> pieceToNumOfStep = boardStepTracer.get(lastTurn.getTo());

        if (pieceToNumOfStep.get(piece) == 1) { // if the piece was in the cell only one time
            pieceToNumOfStep.remove(piece); // remove the piece from the map of pieces that were in the cell
            if (pieceToNumOfStep.isEmpty()) // if the map is empty, remove it from the tracker
                boardStepTracer.remove(lastTurn.getTo()); // remove the map from the tracker -> there is no pieces that were in the cell
        } else { // if the piece was in the cell more than one time
            pieceToNumOfStep.put(piece, pieceToNumOfStep.get(piece) - 1); // remove 1 from the number of times that the piece was in the cell
        }

        // update the king_ position
        if (piece instanceof King_)
            kingPosition = lastTurn.getFrom();


        // update the turn_ owner
        secondPlayerTurn = !secondPlayerTurn;
    }

    /**
     * @return the size of the board
     */
    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * print the game statistics according to the assignment instructions
     */
    public void printGameStats(boolean isPlayerOneWon) {
        printMovesHistory(isPlayerOneWon);
        printDivider();

        printEatsCount(isPlayerOneWon);
        printDivider();

        printDistances(isPlayerOneWon);
        printDivider();

        printCellsHistory();
        printDivider();

    }

    /**
     * print the moves history according to the assignment instructions. <br>
     * The instructions are:
     * after the game is over, print the moves history of each piece, in ascending order of the number of moves. <br>
     * the order of the pieces is: <br>
     * first the pieces of the winner, then the pieces of the loser. <br>
     * in these sides, the pieces are sorted by the number of moves, and then by there number.
     *
     * @param isPlayerOneWon true if the first player_ won, false otherwise
     */
    private void printMovesHistory(boolean isPlayerOneWon) {
        // split the pieces to two arrays - one for each player_
        ConcretePiece_[] playerOnePieces = Arrays.copyOfRange(gamePieces, 0, 13);
        ConcretePiece_[] playerTwoPieces = Arrays.copyOfRange(gamePieces, 13, 37);

        // sort the pieces according to the assignment instructions
        Comparator<ConcretePiece_> comp = (piece1, piece2) -> {
            // sort by the number of moves
            if (piece1.getMoves().size() != piece2.getMoves().size())
                return piece1.getMoves().size() - piece2.getMoves().size();

            // the number of moves is the same, so sort by the number of the pieces
            return piece1.getPieceNum() - piece2.getPieceNum();
        };
        // sort the pieces of each player_ separately
        Arrays.sort(playerOnePieces, comp);
        Arrays.sort(playerTwoPieces, comp);

        // print the sorted pieces
        // first the pieces of the winner, then the pieces of the loser
        ConcretePiece_[] winnerPieces, loserPieces;
        if (isPlayerOneWon) {
            winnerPieces = playerOnePieces;
            loserPieces = playerTwoPieces;
        } else {
            winnerPieces = playerTwoPieces;
            loserPieces = playerOnePieces;
        }
        for (ConcretePiece_ piece : winnerPieces) {
            if (piece.getMoves() != null && piece.getMoves().size() > 1) {
                System.out.println(piece.getName() + ": " + piece.getMoves());
            }
        }
        for (ConcretePiece_ piece : loserPieces) {
            if (piece.getMoves() != null && piece.getMoves().size() > 1) {
                System.out.println(piece.getName() + ": " + piece.getMoves());
            }
        }
    }

    /**
     * print the eats count according to the assignment instructions. <br>
     * The instructions are:
     * print each piece sorted in descending order by the number of eaten pawns. <br>
     * if two pieces have the same number of eaten pawns, sort them by there number in ascending order. <br>
     * if the pieces have the same number of eaten pawns and the same number, sort them by the winner (first the winner, then the loser).
     *
     * @param isPlayerOneWon true if the first player_ won, false otherwise
     */
    private void printEatsCount(boolean isPlayerOneWon) {
        // filter the pieces that not eaten any pawn
        ConcretePiece_[] filtered = Arrays.stream(gamePieces).filter(piece -> piece.getNumberOfEats() > 0).toArray(ConcretePiece_[]::new);
        // sort the pieces according to the assignment instructions
        Arrays.sort(filtered, (piece1, piece2) -> {
            // sort by the number of eaten pawns in descending order
            if (piece1.getNumberOfEats() != piece2.getNumberOfEats())
                return piece2.getNumberOfEats() - piece1.getNumberOfEats();
                // the number of eaten pawns is the same, so sort by there number in ascending order
            else if (piece1.getPieceNum() != piece2.getPieceNum())
                return piece1.getPieceNum() - piece2.getPieceNum();

            // the number of eaten pawns and the number of the pieces are the same, them by the winner (first the winner, then the loser)
            if (isPlayerOneWon)
                return piece1.getOwner().isPlayerOne() ? -1 : 1;
            else // the second player_ won
                return piece1.getOwner().isPlayerOne() ? 1 : -1;
        });

        // print the sorted pieces
        for (ConcretePiece_ piece : filtered) {
            System.out.println(piece.getName() + ": " + piece.getNumberOfEats() + " kills");
        }

    }

    /**
     * print the distances according to the assignment instructions. <br>
     * The instructions are:
     * print each piece that moved at least once, sorted in descending order by the distance that the piece has moved. <br>
     * if two pieces have the same distance, sort them by their number in ascending order. <br>
     * if the pieces have the same distance and the same number, sort them by the winner (first the winner, then the loser).
     * <br>
     *
     * @param isPlayerOneWon true if the first player_ won, false otherwise
     */
    private void printDistances(boolean isPlayerOneWon) {
        // filter the pieces that not moved
        ConcretePiece_[] filtered = Arrays.stream(gamePieces).filter(piece -> piece.getMoves().size() > 1).toArray(ConcretePiece_[]::new);
        // sort the pieces according to the assignment instructions
        Arrays.sort(filtered, (piece1, piece2) -> {
            // sort by the distance that the piece has moved in descending order
            if (piece1.getDistance() != piece2.getDistance())
                return piece2.getDistance() - piece1.getDistance();
                // the distance that the piece has moved is the same, so sort by there number in ascending order
            else if (piece1.getPieceNum() != piece2.getPieceNum())
                return piece1.getPieceNum() - piece2.getPieceNum();

            // the distance that the piece has moved and the number of the pieces are the same, them by the winner (first the winner, then the loser)
            if (isPlayerOneWon)
                return piece1.getOwner().isPlayerOne() ? -1 : 1;
            else // the second player_ won
                return piece1.getOwner().isPlayerOne() ? 1 : -1;
        });

        // print the sorted pieces
        for (ConcretePiece_ piece : filtered) {
            System.out.println(piece.getName() + ": " + piece.getDistance() + " squares");
        }
    }

    /**
     * print the cells history according to the assignment instructions. <br>
     * The instructions are:
     * print each cell that had more than one piece in it, in descending order of the number of different pieces that were in the cell. <br>
     * if two cells had the same number of different pieces, sort them by the x coordinate of the cell. <br>
     * if the x coordinate of the cell is the same, sort them by the y coordinate of the cell.
     */
    private void printCellsHistory() {
        // add the positions that had more than one piece in them to a list
        List<Position_> positions = new ArrayList<>();
        for (Map.Entry<Position_, Map<ConcretePiece_, Integer>> entry : boardStepTracer.entrySet()) {
            if (entry.getValue().size() > 1)
                positions.add(entry.getKey());
        }

        // sort the positions according to the assignment instructions
        positions.sort((position1, position2) -> {
            // sort by the number of different pieces that were in the cell in descending order
            if (boardStepTracer.get(position1).size() != boardStepTracer.get(position2).size())
                return boardStepTracer.get(position2).size() - boardStepTracer.get(position1).size();

            // the number of different pieces that were in the cell is the same, so sort by the x coordinate of the cell
            if (position1.col() != position2.col())
                return position1.col() - position2.col();

            // the x coordinate of the cell is the same, so sort by the y coordinate of the cell
            return position1.row() - position2.row();
        });

        // print the sorted positions
        for (Position_ position : positions) {
            System.out.println(position.toString() + boardStepTracer.get(position).size() + " pieces");
        }
    }

    /**
     * print a divider line. the divider line is 75 '*' characters.
     */
    private void printDivider() {
        System.out.println("***************************************************************************");
    }

    /**
     * check if the given position is in the board. <br>
     * if it is not, throw an exception.
     *
     * @param position the position to check
     * @throws PositionOutOfBoardException if the position is not in the board
     */
    private void checkIfPositionIsInBoard(Position_ position) {
        if (position.row() < 0 || position.row() > 7 || position.col() < 0 || position.col() > 7)
            throw new PositionOutOfBoardException();
    }

    private static class PositionOutOfBoardException extends IllegalArgumentException {
        public PositionOutOfBoardException() {
            super("The position is not in the board");
        }
    }
}


