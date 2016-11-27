import com.sun.javafx.geom.Vec2f;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Andrey on 20.11.2016.
 */
public class MouseEventListener implements MouseListener, MouseMotionListener {
    private Vec2f step;
    private boolean isDrag;
    private Vec2f startPos;

    public MouseEventListener(){
        step = new Vec2f(0,0);
        isDrag = false;
        startPos = new Vec2f(0,0);
    }

    public Vec2f getDeltaDistance(){
        return step;
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
        step = new Vec2f(e.getX()- startPos.x , e.getY() - startPos.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
