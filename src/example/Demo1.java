/*
 * Project Name: EE2311 Project - Gems Crush
 * Student Name: Bobby Chu Tsz Kit / Sin Bo Chi
 * Student ID: 5440 5635 / 54412731
 * 
 */
package example;

import game.GameConsole;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Demo for the use of:
 *
 * 1. create and display game console 2. start a game loop 3. create and display
 * gems 4. detect mouse click and toggle gem's selection 5. update screen at
 * predefined interval 6. draw text to show score/time information
 *
 * @author Bobby Chu Tsz Kit 5440 5635
 * @author Sin Bo Chi 54412731
 */
public class Demo1 {

    // create the game console for drawing, the buttons for changing modes, the timer, the score display and the background music          
    // singleton, always return the same instance
    private final GameConsole console = GameConsole.getInstance();

    private final Gem startButton = new Gem("start.png", -3, 4);
    private final Gem saveButton = new Gem("save.png", -2, 4);
    private final Gem loadButton = new Gem("load.png", -3, 5);
    private final Gem changeButton = new Gem("change.png", -2, 5);

    private final Sound selectGems = new Sound("/assets/select.wav");
    private final Sound matchGems = new Sound("/assets/match.wav");
    private final Sound fallGems = new Sound("/assets/fall.wav");
    private final Sound backgroundMusic1 = new Sound("/assets/Greensleeves.wav");
    private final Sound backgroundMusic2 = new Sound("/assets/crazy_battle_music.wav");
    
    private Gem gem[][] = null;
    private Gem preview[][] = null;

    public static String mode = "/assets/";

    private int score = 0;
    private final Timer timer = new Timer();
    private boolean initialTimer = true;

    public Demo1() {
        // make the console visible
        console.show();
    }

    
    public static void main(String[] args) {
        // a more OO approach to write the main method
        Demo1 game = new Demo1();
        game.startGame();
    }

    
    public void startGame() {

        // board dimension can be obtained from console
        int width = console.getBoardWidth();
        int height = console.getBoardHeight();

        // set custom background image
        console.setBackground("/assets/board.png");

        // enter the main game loop
        while (true) {

            // get whatever inputs
            Point point = console.getClickedPoint();
            if (point != null) {
                if (startButton.isAt(point)) {
                    startButton();
                } else if (saveButton.isAt(point)) {
                    saveProgressButton();
                } else if (loadButton.isAt(point)) {
                    loadProgressButton();
                } else if (changeButton.isAt(point)) {
                    changePictureButton();
                } else if (gem != null) {
                    detectGems(point);
                }
            }

            // refresh at the specific rate, default 25 fps
            if (console.shouldUpdate()) {
                updateGame();
            }

            // the idle time affects the no. of iterations per second which 
            // should be larger than the frame rate
            // for fps at 25, it should not exceed 40ms
            console.idle(10);
        }
    }

    private void updateGame() {
        console.clear();

        if (initialTimer == true) {
            console.drawText(60, 150, "[TIME]", new Font("Helvetica", Font.BOLD, 20), Color.white);
            console.drawText(60, 180, timer.getInitialTimeString(), new Font("Helvetica", Font.PLAIN, 20), Color.white);
        } else {
            console.drawText(60, 150, "[TIME]", new Font("Helvetica", Font.BOLD, 20), Color.white);
            console.drawText(60, 180, timer.getGameTimeString(), new Font("Helvetica", Font.PLAIN, 20), Color.white);
        }
        console.drawText(60, 250, "[SCORE]", new Font("Helvetica", Font.BOLD, 20), Color.white);
        console.drawText(60, 280, String.valueOf(score), new Font("Helvetica", Font.PLAIN, 20), Color.white);

        //show the buttons
        startButton.display();
        loadButton.display();
        saveButton.display();
        changeButton.display();

        if (gem != null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    gem[i][j].display();
                }
            }
        }

        if (preview != null) {
            for (int i = 0; i < 8; i++) {
                preview[i][0].display();
            }
        }
        console.update();
    }

    
    private void startButton() {
        backgroundMusic1.stopSound();
        backgroundMusic2.stopSound();
        initialTimer = false;
        String[] color = {"gemBlue.png", "gemGreen.png", "gemOrange.png", "gemPurple.png", "gemRed.png", "gemWhite.png", "gemYellow.png"};
        Random randomNum = new Random();
        gem = new Gem[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gem[i][j] = new Gem(color[randomNum.nextInt(7)], i, j);
            }
        }

        preview = new Gem[8][1];
        for (int i = 0; i < 8; i++) {
            preview[i][0] = new Gem(color[randomNum.nextInt(7)], i, -1);
        }

        matchGems();

        backgroundMusic1.playSound();
        score = 0;                      //to prevent score gained before the start of the game 
        timer.start();
    }

    private void changePictureButton() {
        System.out.println("Pictures changed");
        if (mode.equals("/assets/")) {
            mode = "/assets/warriors/";
            backgroundMusic1.stopSound();
            backgroundMusic2.playSound();
        } else {
            mode = "/assets/";
            backgroundMusic1.playSound();
            backgroundMusic2.stopSound();
        }
    }

    private void saveProgressButton() {
        try {
            //String input = JOptionPane.showInputDialog("Do you want to save your current game progress?");
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("object.dat"));
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j <= 7; j++) {
                    output.writeUTF(gem[i][j].getPic());
                }
            }
            for (int i = 0; i < 8; i++) {
                output.writeUTF(preview[i][0].getPic());
            }
            output.writeUTF(mode);
            output.writeInt(score);
            output.close();
        } catch (IOException ioe) {

        }
    }

    private void loadProgressButton() {
        try {
            //String input = JOptionPane.showInputDialog("Do you want to load your previous game progress?");
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("object.dat"));
            
            backgroundMusic1.stopSound();
            backgroundMusic2.stopSound();
            
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    gem[i][j].setPic(input.readUTF());
                }
            }
            for (int i = 0; i < 8; i++) {
                preview[i][0].setPic(input.readUTF());
            }
            
            mode = input.readUTF();
            score = input.readInt();
            input.close();
            
            if ("/assets/".equals(mode)) {
                backgroundMusic1.playSound();
            } else {
                backgroundMusic2.playSound();
            }
            
        } catch (IOException ioe) {

        }
    }    
    
    
    private void detectGems(Point point) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gem[i][j].isAt(point)) {
                    gem[i][j].toggleFocus();
                    selectGems.playSound();
                    updateGame();
                }
            }
        }

        Gem former = null;
        Gem latter = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gem[i][j].isSelected()) {
                    if (former == null) {
                        former = gem[i][j];
                    } else {
                        latter = gem[i][j];
                    }
                }
            }
        }

        if (former != null && latter != null) {
            if ((former.getPosX() == latter.getPosX())) {
                if (former.getPosY() - latter.getPosY() == 1) {
                    swapVertically(latter, former);
                }
                if (former.getPosY() - latter.getPosY() == -1) {
                    swapVertically(former, latter);
                }
            } else if ((former.getPosY() == latter.getPosY())) {
                if (former.getPosX() - latter.getPosX() == 1) {
                    swapHorizontally(latter, former);
                }
                if (former.getPosX() - latter.getPosX() == -1) {
                    swapHorizontally(former, latter);
                }
            }

            former.toggleFocus();
            former = null;
            latter.toggleFocus();
            latter = null;
        }
    }

    //Animation for swaping horizontally  (a gem box contains 64 intervals)
    private void swapHorizontally(Gem former, Gem latter) {
        for (int i = 0; i < 65; i++) {
            GameConsole.getInstance().drawImage((int) (former.getPosX() * Gem.w + Gem.orgX + i), (int) (former.getPosY() * Gem.h + Gem.orgY), former.getImage());
            GameConsole.getInstance().drawImage((int) (latter.getPosX() * Gem.w + Gem.orgX - i), (int) (latter.getPosY() * Gem.h + Gem.orgY), latter.getImage());
            console.update();
        }
        realSwap(former, latter);
        matchGems();
    }

    //Animation for swaping vertically    (a gem box contains 64 intervals)
    private void swapVertically(Gem former, Gem latter) {
        for (int i = 0; i < 65; i++) {
            GameConsole.getInstance().drawImage((int) (former.getPosX() * Gem.w + Gem.orgX), (int) (former.getPosY() * Gem.h + Gem.orgY + i), former.getImage());
            GameConsole.getInstance().drawImage((int) (latter.getPosX() * Gem.w + Gem.orgX), (int) (latter.getPosY() * Gem.h + Gem.orgY - i), latter.getImage());
            console.update();
        }
        realSwap(former, latter);
        matchGems();
    }

    // Swap images 
    private void realSwap(Gem former, Gem latter) {
        String file = former.getPic();
        former.setPic(latter.getPic());
        latter.setPic(file);
    }

    private void matchGems() {
        boolean matched = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                if (gem[i][j].getPic().equals(gem[i][j + 1].getPic()) && gem[i][j + 1].getPic().equals(gem[i][j + 2].getPic())) {
                    gem[i][j].same = true;
                    gem[i][j + 1].same = true;
                    gem[i][j + 2].same = true;
                    matched = true;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                if (gem[j][i].getPic().equals(gem[j + 1][i].getPic()) && gem[j + 1][i].getPic().equals(gem[j + 2][i].getPic())) {
                    gem[j][i].same = true;
                    gem[j + 1][i].same = true;
                    gem[j + 2][i].same = true;
                    matched = true;
                }
            }
        }

        if (matched == true) {
            deleteGems();
            matchGems();
        }
    }

    private void deleteGems() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gem[i][j].same == true) {
                    gem[i][j].setPic("deleted.png");
                    gem[i][j].same = false;
                    score += 10;                     //score increases for every delected (null) image
                }
            }
        }
        matchGems.playSound();
        updateGame();
        console.idle(50);
        addNewGems();
    }

    private void addNewGems() {
        ArrayList<Gem> myList = new ArrayList<Gem>();
        boolean still = true;
        while (still == true) {
            still = false;
            myList.clear();
            for (int i = 6; i >= 0; i--) {
                for (int j = 0; j < 8; j++) {
                    if (gem[j][i + 1].getPic().equals("deleted.png")) {
                        still = true;
                        myList.add(gem[j][i]);
                    }
                }
            }
            if (myList.isEmpty() == false) {
                for (int x = 0; x < 65; x++) {
                    for (int k = 0; k < myList.size(); k++) {
                        GameConsole.getInstance().drawImage((int) (myList.get(k).getPosX() * Gem.w + Gem.orgX), (int) (myList.get(k).getPosY() * Gem.h + Gem.orgY + x), myList.get(k).getImage());
                    }
                    console.update();
                }
                for (int k = 0; k < myList.size(); k++) {
                    realSwap(myList.get(k), gem[myList.get(k).getPosX()][myList.get(k).getPosY() + 1]);
                }
                fallGems.playSound();
                updateGame();
            }
            for (int q = 0; q < 8; q++) {
                if ("deleted.png".equals(gem[q][0].getPic())) {
                    gem[q][0].setPic(preview[q][0].getPic());
                    Random randomNum = new Random();
                    String[] color = {"gemBlue.png", "gemGreen.png", "gemOrange.png", "gemPurple.png", "gemRed.png", "gemWhite.png", "gemYellow.png"};
                    preview[q][0] = new Gem(color[randomNum.nextInt(7)], q, -1);
                }
            }
        }
    }



}
