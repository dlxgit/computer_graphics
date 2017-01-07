uniform float mandel_x;
uniform float mandel_y;
uniform float mandel_width;
uniform float mandel_height; 
uniform float mandel_iterations;

float calculateMandelbrotIterations(float x, float y) {
	float xx = 0.0;
    float yy = 0.0;
    float iter = 0.0;
    while (xx * xx + yy * yy <= 4.0 && iter < mandel_iterations) {
        float temp = xx * xx - yy * yy + x;
        yy = 2.0 * xx * yy + y;
        xx = temp;
        iter++;
    }
    return iter;
}

vec4 getColor(float iterations) {
	float oneThirdMandelIterations = mandel_iterations / 3.0;
	float green = iterations / oneThirdMandelIterations;
	float blue = (iterations - 1.3 * oneThirdMandelIterations) / oneThirdMandelIterations;
	float red = (iterations - 2.2 * oneThirdMandelIterations) / oneThirdMandelIterations;
	return vec4(red, green, blue, 1.0);
}

void main()
{
    float x = mandel_x + gl_TexCoord[0].x * mandel_width;
    float y = mandel_y + gl_TexCoord[0].y * mandel_height;
    float iterations = calculateMandelbrotIterations(x, y);
    gl_FragColor = getColor(iterations);
}