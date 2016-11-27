import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

import javax.swing.*;

/**
 * Created by Andrey on 13.11.2016.
 */

public class Frame implements GLEventListener {
    private static final Vec2f CAMERA_POS = new Vec2f(250, 275);
    final static Vec2f WINDOW_START_SIZE = new Vec2f(510, 590);
    private Vec2f windowSize;

    private GLCanvas glcanvas;
    private JFrame frame;
    private GLU glu;
    private MouseEventListener mouseListener;

    private Shape shapes[];


    public Frame(String title) {
        windowSize = WINDOW_START_SIZE;

        shapes = new Json_Reader().getShapes();
        mouseListener = new MouseEventListener();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        glu = new GLU();

        glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(this);
        glcanvas.setSize((int)WINDOW_START_SIZE.x, (int)WINDOW_START_SIZE.y);
        glcanvas.addMouseMotionListener(mouseListener);
        glcanvas.addMouseListener(mouseListener);
        glcanvas.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);

        frame = new JFrame (title);
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
        frame.setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        glcanvas.disposeGLEventListener(this, true);
        frame.dispose();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClearColor(0, 0, 0, 0);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
        drawShapes(gl2);
        DragNDrop(gl2);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        windowSize.x = width;
        windowSize.y = height;
    }

    private void drawShapes(GL2 gl) {
        for(Shape shape : shapes) {
            gl.glColor3f(shape.m_color.x, shape.m_color.x, shape.m_color.x);
            if(shape.m_radius > 0){
                drawFilledCircle(gl, shape.m_points.get(0).x, shape.m_points.get(0).y, (float)shape.m_radius, shape.m_color);
                continue;
            }
            gl.glBegin(GL2.GL_TRIANGLE_FAN);
            gl.glColor3f(shape.m_color.x, shape.m_color.y, shape.m_color.z);
            for (Vec2f coordinatePair : shape.m_points) {
                gl.glVertex2d(coordinatePair.x, coordinatePair.y);
            }
            gl.glEnd();
        }
    }

    private void DragNDrop(GL2 gl2){
        gl2.glMatrixMode( GL2.GL_PROJECTION );
        gl2.glLoadIdentity();
        glu.gluOrtho2D( -windowSize.x/2 + CAMERA_POS.x, windowSize.x /2 + CAMERA_POS.x, windowSize.y /2 + CAMERA_POS.y, -windowSize.y/2 + CAMERA_POS.y);
        gl2.glMatrixMode( GL2.GL_MODELVIEW );
        gl2.glLoadIdentity();
        Vec2f dragDistance = mouseListener.getDeltaDistance();
        gl2.glTranslatef(dragDistance.x, dragDistance.y, 0);
        gl2.glViewport( 0, 0, (int)windowSize.x, (int)windowSize.y);
        gl2.glFlush();
    }

    public void repaint() {
        while (true) {
            glcanvas.repaint();
        }
    }

    private void drawFilledCircle(GL2 gl, float x, float y, float radius, Vec3f color){
        int i;
        int triangleAmount = 20; //# of triangles used to draw circle
        float twicePi = 2.0f * (float)Math.PI;

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glColor3f( color.x, color.y, color.z);
        gl.glVertex2f(x, y); // center of circle
        for(i = 0; i <= triangleAmount;i++) {
            gl.glVertex2d(
                    x + (radius * Math.cos(i *  twicePi / triangleAmount)),
                    y + (radius * Math.sin(i * twicePi / triangleAmount))
            );
        }
        gl.glEnd();
    }
}