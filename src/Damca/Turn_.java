package Damca;

import java.util.ArrayList;
import java.util.List;

/**
 * A turn in the game.<br>
 * A turn is defined by a piece, a starting position, an ending position, and a list of eaten pawns.
 */
public class Turn_ {
    /**
     * the piece that was moved in the turn
     */
    private final ConcretePiece_ piece;
    /**
     * the starting position of the piece
     */
    private final Position_ from;
    /**
     * the ending position of the piece
     */
    private final Position_ to;
    /**
     * the list of eaten pawns in the turn (max 4)
     */
    private final List<Pawn_> eatenPawn;

    public Turn_(ConcretePiece_ piece, Position_ from, Position_ to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.eatenPawn = new ArrayList<>(4); // max 4 pawns can be eaten in a turn
    }

    public ConcretePiece_ getPiece() {
        return piece;
    }

    public Position_ getFrom() {
        return from;
    }

    public Position_ getTo() {
        return to;
    }

    public List<Pawn_> getEatenPawn() {
        return eatenPawn;
    }

    public void addEatenPawn(Pawn_ pawn) {
        this.eatenPawn.add(pawn);
    }
}
