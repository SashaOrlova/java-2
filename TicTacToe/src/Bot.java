public interface Bot {
    Turn getNextTurn();
    void setTurn(Turn turn);
}
