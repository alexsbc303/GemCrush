/*
 * Project Name: EE2311 Project - Gems Crush
 * Student Name: Bobby Chu Tsz Kit / Sin Bo Chi
 * Student ID: 5440 5635 / 54412731
 * 
 */

package example;

import game.GameConsole;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

/**
 * Sample design of a toggle-enable gem
 * 
 * @author Your Name and ID
 */
public class Gem {

    // the upper-left corner of the board, reference origin point
    public static final int orgX = 240;
    public static final int orgY = 40;
    // the size of the gem
    public static final int w = 65;
    public static final int h = 65;
    // default position in 8x8 grid    
    private int posX = 0;
    private int posY = 0;
    private boolean selected = false;
            
    private Image focus; 
    private String file;
    
    public boolean same = false;
    
    
    Gem(String file, int x, int y) {
        this.focus = new ImageIcon(this.getClass().getResource("/assets/focus.png")).getImage();
        this.file = file;
        this.posX = x;
        this.posY = y;
    }
        
    public void display() {         //                                                      retrieve the image icon from 
        GameConsole.getInstance().drawImage((int)(posX * w + orgX), (int)(posY * h + orgY), new ImageIcon(this.getClass().getResource(Demo1.mode+file)).getImage());
        if(selected)
            GameConsole.getInstance().drawImage((int)(posX * w + orgX), (int)(posY * h + orgY), focus);
    }
    
    public boolean isAt(Point point) {
        if(point != null)
            return (point.x > (posX * w + orgX) && point.x <= ((posX + 1) * w + orgX) && point.y > (posY * h + orgY) && point.y <= ((posY + 1) * h + orgY));   
        else
            return false;
    }
    
    public Image getImage(){    //getImage != getPic 
        return new ImageIcon(this.getClass().getResource(Demo1.mode+file)).getImage();
    }

    public String getPic() {    //getPic = retrieve image file path 
        return file;
    }

    public void setPic(String file) {
        this.file = file;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public void toggleFocus() {
        selected = !selected;
    }

}
