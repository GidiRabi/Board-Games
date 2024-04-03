package Damca;

import java.util.ArrayList;
import java.util.List;

public abstract class ConcretePiece_ implements Piece_ {
    protected final Player_ owner;
    protected final List<Position_> moves;
    protected final int pieceNum;
    protected int distance;
    protected int kills;

    public ConcretePiece_(Player_ owner, int pieceNum, Position_ initialPosition) {
        this.owner = owner;
        this.pieceNum = pieceNum;
        this.moves = new ArrayList<>();
        this.moves.add(initialPosition);
        distance = 0;
    }

    public abstract int getNumberOfEats();

    public abstract void setNumberOfEats(int i);

    /**
     * @return the representation of the piece.
     */
    public abstract String getName();

    /**
     * add a move to the move list.
     * update the distance that the piece has moved.
     *
     * @param position_ the position_ to add to the moves list.
     */
    public void addMove(Position_ position_) {
        if (moves.size() > 0) {
            distance += moves.get(moves.size() - 1).distance(position_);
        }
        this.moves.add(position_);
    }

    /**
     * @return the distance that the piece has moved.
     */
    public List<Position_> getMoves() {
        return moves;
    }

    /**
     * @return the distance that the piece has moved.
     */
    public int getDistance() {
        return distance;
    }

    @Override
    public Player_ getOwner() {
        return this.owner;
    }


    public int getPieceNum() {
        return pieceNum;
    }


}
