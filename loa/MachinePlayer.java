/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.Collections;
import java.util.List;

import static loa.Piece.BP;
import static loa.Piece.WP;

/** An automated Player.
 *  @author Sarah Hashemian
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;
        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /**
     * Used to convey moves discovered by findMove.
     */
    private Move _foundMove;

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _foundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _foundMove. If the game is over
     * on BOARD, does not set _foundMove.
     */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        int q = 0;
        Move m = null;

        if (board.gameOver() || depth == 0) {
            return heurHelper(board, sense);
        }

        if (sense == 1) {
            int max = -INFTY;
            for (Move b: board.legalMoves()) {
                board.makeMove(b);
                int eval = findMove(board, depth - 1,
                        false, -1, alpha, beta);

                board.retract();
                if (max < eval) {
                    m = b;
                    max = eval;
                    q++;
                }
                alpha = Integer.max(alpha, max);
                if (beta <= alpha) {
                    break;
                }
            }
            assert m != null;
            if (saveMove) {
                _foundMove = m;
            }
            return max;
        } else {
            int min = INFTY;
            for (Move b: board.legalMoves()) {
                board.makeMove(b);
                int eval = findMove(board, depth - 1,
                        false, 1, alpha, beta);
                board.retract();
                if (min > eval) {
                    m = b;
                    min = eval;
                }
                beta = Integer.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
                assert m != null;
                if (saveMove) {
                    _foundMove = m;
                }
            }
            return min;
        }
    }

    /**
     * Returns the heuristic function for B in X.
     */
    private int heurHelper(Board b, int x) {
        int eval = 0;
        eval += helper(b, x);
        if (b.winner() == BP) {
            return -WINNING_VALUE;
        }
        if (b.winner() == WP) {
            return WINNING_VALUE;
        }
        return eval;
    }

    /**
     * Returns a helper function for B in S.
     */
    private int helper(Board b, int s) {
        List<Integer> w = getBoard().getRegionSizes(WP);
        List<Integer> blk = getBoard().getRegionSizes(BP);
        int max = Collections.max(w);
        int x = Collections.max(blk);
        int wreg = getBoard().getRegionSizes(WP).size();
        int reg = getBoard().getRegionSizes(BP).size();
        return s * ((max - x) + (reg - wreg));
    }

    /**
     * Return a search depth for the current position.
     */
    private int chooseDepth() {
        return 5;
    }

}
