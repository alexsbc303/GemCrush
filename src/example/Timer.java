/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

public class Timer {

    private long initTime;

    public void start() {
        initTime = System.currentTimeMillis();
    }

    public String getGameTimeString() {

        int time = (int) (System.currentTimeMillis() - initTime) / 1000;
        int hour = time / 3600;
        int minute = time % 3600 / 60;
        int second = time % 3600 % 60;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
    
    public String getInitialTimeString() {
        return String.format("%02d:%02d:%02d", 00, 00, 00);
    }
}
