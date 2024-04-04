package Damca;

public class Pawn_ extends ConcretePiece_ {
    private static String PLAYER_ONE_TYPE = "♙";
    private static String PLAYER_TWO_TYPE = "♟";
    private static String PLAYER_ONE_PROMOTED_TYPE = "♔";
    private static String PLAYER_TWO_PROMOTED_TYPE = "♛";

    private int numberOfEatenPawns;

    public Pawn_(Player_ owner, int pieceNum, Position_ initialPosition) {
        super(owner, pieceNum, initialPosition);
        this.numberOfEatenPawns = 0;
    }

    public String getType() {
        if (isPromoted) {
            return getOwner().isPlayerOne() ? PLAYER_ONE_PROMOTED_TYPE : PLAYER_TWO_PROMOTED_TYPE;
        }else {
            return getOwner().isPlayerOne() ? PLAYER_ONE_TYPE : PLAYER_TWO_TYPE;
        }
    }

    @Override
    public void setType(String type) {

    }

    public void promote() {
        this.isPromoted = true;
    }



    /**
     * Increment the number of eaten pawns by 1.
     */
    public void eat() {
        this.numberOfEatenPawns++;
    }

    @Override
    public int getNumberOfEats() {
        return numberOfEatenPawns;
    }

    @Override
    public void setNumberOfEats(int i) {
        this.numberOfEatenPawns = i;
    }

    @Override
    public String getName() {
        String prefix = super.getOwner().isPlayerOne() ? "D" : "A";
        return prefix + super.getPieceNum();
    }
}
