package strategies;

import java.util.ArrayList;
import java.util.HashSet;

import clutches.Board;
import clutches.Hand;

import main.Dom;
import main.GameState;
import main.Loc;

public class JacobStrategy implements Strategy {

    private static HashSet<Dom> domsIPlayed;
    //next two variables are for holding domino sides that
    //the opponent was unable to match
    private static int boardSizeLastTurn;
    boolean[] isBeautiful;

    public JacobStrategy() {
        isBeautiful = new boolean[7];
        boardSizeLastTurn = 0;
        domsIPlayed = new HashSet<>();
    }

    @Override
    public String getName() {
        return "Jacob";
    }

    @Override
    public boolean playTile() {
        GameState game = GameState.getInstance();
        Dom midDom;
        Board board = game.getBoard();
        int currentPlayer = game.getCurrentPlayer();
        Hand hand = game.getHand(currentPlayer);
        ArrayList<Dom> doms = hand.getDoms();
        ArrayList<Play> possiblePlays = new ArrayList<>();

        for (Dom d : doms) {
            if (d.getLeft() == game.getBoard().getLeftEnd()) {
                possiblePlays.add(new Play(Loc.LEFT, d.getRight(), d));
            } else if (d.getRight() == game.getBoard().getLeftEnd()) {
                possiblePlays.add(new Play(Loc.LEFT, d.getLeft(), d));
            } else if (d.getLeft() == game.getBoard().getRightEnd()) {
                possiblePlays.add(new Play(Loc.RIGHT, d.getRight(), d));
            } else if (d.getRight() == game.getBoard().getRightEnd()) {
                possiblePlays.add(new Play(Loc.RIGHT, d.getLeft(), d));
            }
        }

        //make sure i have a move before doing a bunch of stuff
        if (!possiblePlays.isEmpty()) {
            //used to determine the quality of a move
            //may reduce a number partially if an opponent
            //probably doesn't have a certain domino
            double[] numNums = new double[7];
            for (int i = 0; i < 7; i++) {
                numNums[i] = 7;
            }

            //get rid of Doms/other info, from previous games
            if (board.getDoms().size() <= 2 && hand.getDoms().size() != 5) {
                boardSizeLastTurn = -1;
                domsIPlayed.clear();
                for (int i = 0; i < isBeautiful.length; i++) {
                    isBeautiful[i] = false;
                }
            }

            //start midDom as least likely middle domino
            midDom = new Dom(0, 1);
            int midLow;
            int dLow;

            //find middle Domino
            for (Dom d : board.getDoms()) {
                if (midDom.isDouble() && d.isDouble()) {
                    if (d.getLeft() > midDom.getLeft()) {
                        midDom = d;
                    }
                } else if (d.isDouble() && !midDom.isDouble()) {
                    midDom = d;
                } else if (!midDom.isDouble() && !d.isDouble()) {
                    if (d.points() > midDom.points()) {
                        midDom = d;
                    } else if (d.points() == midDom.points()) {
                        if (midDom.getLeft() > midDom.getRight()) {
                            midLow = midDom.getRight();
                        } else {
                            midLow = midDom.getLeft();
                        }
                        if (d.getLeft() > d.getRight()) {
                            dLow = d.getRight();
                        } else {
                            dLow = d.getLeft();
                        }
                        if (dLow > midLow) {
                            midDom = d;
                        }
                    }
                }
            }

            //In Other Hand Or in BoneYard
            //actually tries to only indicate Doms in other player's hand
            //but not that hard
            boolean[] IOHOBY = new boolean[28];
            for (int i = 0; i < 28; i++) {
                IOHOBY[i] = true;
            }

            int low;
            int high;
            int rindex;
            int reverseCount;

            //get all not played/owned dominos
            for (int i = 0; i < 7; i++) {
                for (int j = i; j < 7; j++) {
                    rindex = 0;
                    for (Dom d : hand.getDoms()) {
                        if (d.getLeft() < d.getRight()) {
                            low = d.getLeft();
                            high = d.getRight();
                        } else {
                            low = d.getRight();
                            high = d.getLeft();
                        }
                        if (low == i && high == j) {
                            reverseCount = 7;
                            while (low > 0) {
                                rindex += reverseCount;
                                reverseCount--;
                                low--;
                            }
                            IOHOBY[rindex + j - i] = false;
                        }
                    }
                    for (Dom d : board.getDoms()) {
                        if (d.getLeft() < d.getRight()) {
                            low = d.getLeft();
                            high = d.getRight();
                        } else {
                            low = d.getRight();
                            high = d.getLeft();
                        }
                        if (low == i && high == j) {
                            reverseCount = 7;
                            while (low > 0) {
                                rindex += reverseCount;
                                reverseCount--;
                                low--;
                            }
                            IOHOBY[rindex + j - i] = false;
                        }
                    }
                }
            }
            //can rule out doubles higher than midDom being in opponent's hand
            if (!midDom.isDouble()) {
                IOHOBY[0] = false;
                IOHOBY[7] = false;
                IOHOBY[13] = false;
                IOHOBY[18] = false;
                IOHOBY[22] = false;
                IOHOBY[25] = false;
                IOHOBY[27] = false;
            } else if (midDom.getLeft() == 0) {
                IOHOBY[7] = false;
                IOHOBY[13] = false;
                IOHOBY[18] = false;
                IOHOBY[22] = false;
                IOHOBY[25] = false;
                IOHOBY[27] = false;
            } else if (midDom.getLeft() == 1) {
                IOHOBY[13] = false;
                IOHOBY[18] = false;
                IOHOBY[22] = false;
                IOHOBY[25] = false;
                IOHOBY[27] = false;
            } else if (midDom.getLeft() == 2) {
                IOHOBY[18] = false;
                IOHOBY[22] = false;
                IOHOBY[25] = false;
                IOHOBY[27] = false;
            } else if (midDom.getLeft() == 3) {
                IOHOBY[22] = false;
                IOHOBY[25] = false;
                IOHOBY[27] = false;
            } else if (midDom.getLeft() == 4) {
                IOHOBY[25] = false;
                IOHOBY[27] = false;
            } else if (midDom.getLeft() == 5) {
                IOHOBY[27] = false;
            }
            HashSet<Dom> hePlayed = new HashSet<>();
            for (Dom d : game.getBoard().getDoms()) {
                if (!domsIPlayed.contains(d)) {
                    hePlayed.add(d);
                }
            }

            int m = game.getBoard().getDoms().indexOf(midDom);
            //tens digit of domsHePlayed holds the number opponent had to match
            //units digit holds the other side of the Dom
            HashSet<Integer> domsHePlayed = new HashSet<>();
            int holdDomIndex;

            //get the dominos opponent played with information about
            //the side he matched and the side he put up for play
            for (Dom d : hePlayed) {
                for (Dom dm : game.getBoard().getDoms()) {
                    if (d == dm) {
                        if (game.getBoard().getDoms().indexOf(dm) == m) {
                            //do nothing
                        } else if (game.getBoard().getDoms().indexOf(dm) < m) {
                            holdDomIndex = game.getBoard().getDoms().indexOf(dm) + 1;
                            if (d.getLeft() == game.getBoard().getDoms().get(holdDomIndex).getLeft()) {
                                domsHePlayed.add(d.getLeft() * 10 + d.getRight());
                            } else {
                                domsHePlayed.add(d.getRight() * 10 + d.getLeft());
                            }
                        } else {
                            holdDomIndex = game.getBoard().getDoms().indexOf(dm) - 1;
                            if (d.getLeft() == game.getBoard().getDoms().get(holdDomIndex).getLeft()) {
                                domsHePlayed.add(d.getLeft() * 10 + d.getRight());
                            } else {
                                domsHePlayed.add(d.getRight() * 10 + d.getLeft());
                            }
                        }
                        break;
                    }
                }
            }
            int k = 27;
            for (int i = 6; i >= 0; i--) {
                for (int j = 6; j >= i; j--) {
                    if (!IOHOBY[k--]) {
                        numNums[i]--;
                        if (j != i) {
                            numNums[j]--;
                        }
                    }
                }
            }
            //if, for example, the opponent plays the 3/2 where he/she
            //had to lay a 3, he/she probably doesn't have the 3/3, 3/4, 3/5 or 3/6
            //this next loop accounts for that
            int p;
            for (Integer i : domsHePlayed) {
                for (int j = 6; j > i % 10; j--) {
                    reverseCount = 7;
                    if (i % 10 > i / 10) {
                        high = i % 10;
                        low = i / 10;
                    } else {
                        high = i / 10;
                        low = i % 10;
                    }
                    p = low;
                    rindex = 0;
                    while (low > 0) {
                        rindex += reverseCount;
                        reverseCount--;
                        low--;
                    }
                    if (IOHOBY[rindex + high - p]) {
                        numNums[i / 10] -= .3;
                    }
                }
            }

            if (board.getDoms().size() == boardSizeLastTurn
                    || (board.getDoms().size() == 1) && hand.getDoms().size() == 6) {
                isBeautiful[board.getLeftEnd()] = true;
                isBeautiful[board.getRightEnd()] = true;
            }
            //end of information gathering!

            Play bestPlay;

            bestPlay = possiblePlays.get(0);

            for (Play play : possiblePlays) {
                if (play.d.points() > bestPlay.d.points()) {
                    bestPlay = play;
                }
            }
            for (Play play : possiblePlays) {
                //if there is another domino i can play that has more pips score--
                play.score -= (bestPlay.d.points() - play.d.points());
                //if there is a good chance the other player can
                //match the domino score--
                play.score -= numNums[play.nonmatchInt] * 2;
                //if i'm giving up a high side to match score--
                play.score -= play.nonmatchInt / 2;
                //if the opponent definitely won't be able to play
                //on the domino score++
                if (isBeautiful[play.nonmatchInt]) {
                    play.score += 5;
                }
                //if the nonmatching side of the domino is shared with
                //other dominos in my hand score++
                for (Dom d : hand.getDoms()) {
                    if (d != play.d) {
                        if (d.getLeft() == play.nonmatchInt
                                || d.getRight() == play.nonmatchInt) {
                            play.score += 1;
                        }
                    }
                }
                if (play.d.isDouble() && play.d.getLeft() != 0) {
                    play.score += 4;
                }
            }

            for (Play play : possiblePlays) {
                if (play.score > bestPlay.score) {
                    bestPlay = play;
                }
            }

            //flip dom for other peeps' strategies
            if (bestPlay.pos == Loc.LEFT) {
                if (!bestPlay.d.isDouble() && bestPlay.d.getLeft() == board.getLeftEnd()) {
                    bestPlay.d.flip();
                }
            } else {
                if (!bestPlay.d.isDouble() && bestPlay.d.getRight() == board.getRightEnd()) {
                    bestPlay.d.flip();
                }
            }

            game.getBoard().addDom(bestPlay.pos, bestPlay.d);
            game.getHand(currentPlayer)
                    .removeDom(doms.indexOf(bestPlay.d));
            domsIPlayed.add(bestPlay.d);

            boardSizeLastTurn = board.getDoms().size();

            return true;
        }

        boardSizeLastTurn = board.getDoms().size();
        return false;
    }

    class Play {

        private Dom d;
        private int pos;
        private int nonmatchInt;
        private double score;

        public Play(int i, int m, Dom dm) {
            d = dm;
            pos = i;
            nonmatchInt = m;
            score = 25;
        }
    }
}
