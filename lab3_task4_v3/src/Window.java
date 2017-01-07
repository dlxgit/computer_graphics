import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.sun.javafx.geom.Vec2f;


public class Window extends GLCanvas implements GLEventListener {
    private GLU m_glu = new GLU();
    private InputListener m_inputListener;
    private final Animator m_animator = new Animator(this);

    private Vec2f m_windowSize;
    private ShaderProgram m_shaderProgram;

    private float m_mandelX = -2;
    private float m_mandelY = -2;
    private float m_mandelHeight = 4;
    private float m_mandelWidth = 4;
    private int m_mandelIterations = 500;

    Window() {
        addGLEventListener(this);
        m_inputListener = new InputListener(this);
        m_animator.start();
        m_glu = new GLU();
        m_windowSize = new Vec2f(800, 800);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        m_shaderProgram = new ShaderProgram(gl);
        gl.glShadeModel(GL2.GL_FLAT);
        try {
            m_shaderProgram.compileShader(gl,
                                        "D:/cg/lab3_task4_v3/src/shader/vshader.glsl",
                                        ShaderType.Vertex);
            m_shaderProgram.compileShader(gl,
                                        "D:/cg/lab3_task4_v3/src/shader/fshader.glsl",
                                        ShaderType.Fragment);
            m_shaderProgram.link(gl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        m_windowSize.x = width;
        m_windowSize.y = height;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        disposeGLEventListener(this, true);
        m_shaderProgram.delete(drawable.getGL().getGL2());
        this.dispose(drawable);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        updateUniformVars(gl);
        gl.glLoadIdentity();

        float zoomValue = m_inputListener.getScale();
        zoom(gl, zoomValue);
        dragNDrop(gl, zoomValue);

        // Draw A Quad
        gl.glColor3f(1,1,0);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-0.5f, 0.5f, 1f);  // Top Left
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(0.5f, 0.5f, 1.0f);   // Top Right
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(0.5f, -0.5f, 1.0f);  // Bottom Right
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-0.5f, -0.5f, 1.0f); // Bottom Left
        }
        gl.glEnd();
        gl.glFlush();
        m_shaderProgram.use(gl);
    }

    private void zoom(GL2 gl, float zoom){
        System.out.println("Zoom: " + zoom);
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        m_glu.gluOrtho2D(-0.5f / zoom, 0.5f / zoom, -0.5f / zoom, 0.5f / zoom);
    }

    private void dragNDrop(GL2 gl, float zoom){
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
        Vec2f dragDistance = m_inputListener.getDeltaDistance();
        gl.glTranslatef((dragDistance.x + dragDistance.x * zoom) / m_windowSize.x, -(dragDistance.y + dragDistance.y * zoom) / m_windowSize.y, 0);
        gl.glViewport( 0, 0, (int) m_windowSize.x, (int) m_windowSize.y);
    }

    private void updateUniformVars(GL2 gl) {
        int mandel_x = gl.glGetUniformLocation(m_shaderProgram.getId(), "mandel_x");
        int mandel_y = gl.glGetUniformLocation(m_shaderProgram.getId(), "mandel_y");
        int mandel_width = gl.glGetUniformLocation(m_shaderProgram.getId(), "mandel_width");
        int mandel_height = gl.glGetUniformLocation(m_shaderProgram.getId(), "mandel_height");
        int mandel_iterations = gl.glGetUniformLocation(m_shaderProgram.getId(), "mandel_iterations");

        assert(mandel_x!=-1);
        assert(mandel_y!=-1);
        assert(mandel_width!=-1);
        assert(mandel_height!=-1);
        assert(mandel_iterations!=-1);

        gl.glUniform1f(mandel_x, m_mandelX);
        gl.glUniform1f(mandel_y, m_mandelY);
        gl.glUniform1f(mandel_width, m_mandelWidth);
        gl.glUniform1f(mandel_height, m_mandelHeight);
        gl.glUniform1f(mandel_iterations, m_mandelIterations);
    }

    public void repaint() {
        while(true){
            repaint();
        }
    }
}

