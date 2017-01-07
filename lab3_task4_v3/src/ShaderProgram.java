import com.jogamp.opengl.GL2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.Vector;

public class ShaderProgram {
    public static class fixed_pipeline_t {};

    public ShaderProgram(GL2 gl) {
        m_shaders = new Vector<Integer>();
        m_programId = gl.glCreateProgram();
    }

    public ShaderProgram(fixed_pipeline_t t) {
        m_shaders = new Vector<Integer>();
        m_programId = 0;
    }

    public void delete(GL2 gl) {
        freeShaders(gl);
        gl.getGL2().glDeleteProgram(m_programId);
    }

    final void compileShader(GL2 gl, final String srcPath, ShaderType type) {
        ShaderRaii shader = new ShaderRaii(gl, type);
        String[] shaderSrc = readFromFile(srcPath);
        gl.glShaderSource(shader.getId(), 1, shaderSrc, null, 0);
        gl.glCompileShader(shader.getId());
        IntBuffer compileStatusBuffer = IntBuffer.allocate(1);
        gl.glGetShaderiv(shader.getId(), GL2.GL_COMPILE_STATUS, compileStatusBuffer);
        if (compileStatusBuffer.get(0) == GL2.GL_FALSE) {
            System.out.println("Shader compiling failed");
            System.exit(1);
        }
        m_shaders.add(new Integer(shader.release()));
        shader.deleteShaderRaii(gl);
        gl.glAttachShader(m_programId, m_shaders.lastElement());
    }

    final void link(GL2 gl) {
        gl.glLinkProgram(m_programId);
        IntBuffer linkStatusBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(m_programId, GL2.GL_LINK_STATUS, linkStatusBuffer);
        if (linkStatusBuffer.get() == GL2.GL_FALSE){
            System.err.println("Program link error: ");
            int size = linkStatusBuffer.get(0);
            if (size > 0){
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl.glGetProgramInfoLog(m_programId, size, linkStatusBuffer, byteBuffer);
                for (byte b:byteBuffer.array()){
                    System.err.print((char)b);
                }
            } else {
                System.out.println("Unknown");
            }
            System.exit(1);
        }
        freeShaders(gl);
    }

    public final void use(GL2 gl) {
        gl.glUseProgram(m_programId);
    }

    public final int getId(){
        return m_programId;
    }


    private void freeShaders(GL2 gl) {
        for (int shaderId : m_shaders)
        {
            gl.glDetachShader(m_programId, shaderId);
            gl.glDeleteShader(shaderId);
        }
        m_shaders.clear();
    }

    public static String[] readFromFile(String path) {
        File f = new File(path);
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            String result = new String();
            for (byte i: bytes) {
                result += (char)i;
            }
            return new String[]{result};
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    private int m_programId = 0;
    private Vector<Integer> m_shaders;
};
