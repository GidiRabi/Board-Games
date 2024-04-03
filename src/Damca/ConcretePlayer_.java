package Damca;

public class ConcretePlayer_ implements Player_ {
    private final boolean isPlayerOne;
    private int wins;

    public ConcretePlayer_(boolean isPlayerOne) {
        this.isPlayerOne = isPlayerOne;
        this.wins = 0;
    }

    @Override
    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    @Override
    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins++;
    }
}
