import com.jogamp.opengl.GL2;

public class ShaderRaii {
    public ShaderRaii(GL2 gl, ShaderType shaderType) {
        m_id = gl.glCreateShader(MapShaderType(shaderType));
    }

    public void deleteShaderRaii(GL2 gl) {
        gl.glDeleteShader(m_id);
    }

    public final int getId(){ //вместо того что выше
        return m_id;
    }

    public int release() {
        final int id = m_id;
        m_id = 0;
        return id;
    }

    private int MapShaderType(ShaderType shaderType) {
        return shaderType == ShaderType.Vertex ? GL2.GL_VERTEX_SHADER :
                                                 GL2.GL_FRAGMENT_SHADER;
    }

    private int m_id;
}

