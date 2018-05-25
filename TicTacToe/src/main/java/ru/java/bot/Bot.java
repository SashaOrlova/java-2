package ru.java.bot;
import ru.java.logic.Turn;

/**
 * Common interface for all bots
 */
public interface Bot {
    /** Ask bot about his next turn
     * @return turn witch bot make
     */
    Turn getNextTurn() throws PlayerClient.ConnectException;

    /** Inform bot about opponent turn
     * @param turn opponent turn
     */
    void setTurn(Turn turn) throws PlayerClient.ConnectException;
}
