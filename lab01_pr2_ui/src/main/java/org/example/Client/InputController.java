package org.example.Client;
import java.awt.event.*;

public class InputController implements KeyListener, MouseMotionListener, MouseListener {

    private final GameClient client;

    public boolean up, down, left, right, one, two, three, four, five, six;

    public InputController(GameClient client) {
        this.client = client;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_1 -> one = true;
            case KeyEvent.VK_2 -> two = true;
            case KeyEvent.VK_3 -> three = true;
            case KeyEvent.VK_4 -> four = true;
            case KeyEvent.VK_5 -> five = true;
            case KeyEvent.VK_6 -> six = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_1 -> one = false;
            case KeyEvent.VK_2 -> two = false;
            case KeyEvent.VK_3 -> three = false;
            case KeyEvent.VK_4 -> four = false;
            case KeyEvent.VK_5 -> five = false;
            case KeyEvent.VK_6 -> six = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        client.updateMouse(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        client.updateMouse(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        client.sendShootStart();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        client.sendShootStop();
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
}