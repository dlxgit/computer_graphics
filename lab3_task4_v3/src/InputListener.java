import java.awt.event.KeyEvent;
import com.sun.javafx.geom.Vec2f;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


class InputListener implements MouseListener, MouseMotionListener, java.awt.event.KeyListener {
    public static final float ZOOM_STEP = 2.f;
    public static final float MOVE_STEP = 30.f;
    private Vec2f step;
    private boolean isDrag;
    private Vec2f startPos;
    private float zoomValue;
    boolean canZoom;
    Vec2f keyboardMoveDistance;

    InputListener(Window window){
        window.addMouseMotionListener(this);
        window.addMouseListener(this);
        window.addKeyListener(this);

        step = new Vec2f(0,0);
        isDrag = false;
        startPos = new Vec2f(0,0);
        canZoom = true;
        zoomValue = 1;
        keyboardMoveDistance = new Vec2f();
    }

    public final float getScale(){
        return zoomValue;
    }

    final Vec2f getDeltaDistance(){
        return new Vec2f((step.x - keyboardMoveDistance.x) / zoomValue, (step.y - keyboardMoveDistance.y) / zoomValue);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDrag = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!isDrag){
            startPos = new Vec2f(e.getX() - step.x, e.getY() - step.y);
            isDrag = true;
        }
        step = new Vec2f(e.getX()- startPos.x, e.getY() - startPos.y);

        System.out.println(step.toString());

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(canZoom){
            if(e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                zoomValue *= ZOOM_STEP;
                canZoom = false;
            }
            else if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN){
                zoomValue /= ZOOM_STEP;
                canZoom = false;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            keyboardMoveDistance = new Vec2f(keyboardMoveDistance.x + MOVE_STEP, keyboardMoveDistance.y);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP){
            keyboardMoveDistance = new Vec2f(keyboardMoveDistance.x, keyboardMoveDistance.y - MOVE_STEP);
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            keyboardMoveDistance = new Vec2f(keyboardMoveDistance.x - MOVE_STEP, keyboardMoveDistance.y);;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            keyboardMoveDistance = new Vec2f(keyboardMoveDistance.x, keyboardMoveDistance.y + MOVE_STEP);
        }
        else keyboardMoveDistance = new Vec2f();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_PAGE_UP || e.getKeyCode() == KeyEvent.VK_PAGE_DOWN){
            canZoom = true;
        }
    }
}