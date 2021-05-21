import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {

    PGraphics canvas;       // canvas on which the pendulum is to be drawn
    float xOffset, yOffset; // offsets the (0, 0) point so that the whole pendulum can fit in the window

    float r1, r2;           // radii of the pendulums
    float m1, m2;           // masses of the pendulums
    float theta1, theta2;   // initial angles of the pendulums

    float w1 = 0, w2 = 0;   // initial velocities of the pendulums' pendants
    float e1 = 0, e2 = 0;   // initial accelerations of the pendulums' pendants

    final float G = 1;      // gravity constant

    /**
     * Main method.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{"Main"});
    }

    /**
     * Settings method. Used to set the size of the window and assign random initial values to pendulum's variables.
     */
    public void settings() {
        size(1000, 1000); // size of window
        r1 = random(100, 200);
        r2 = random(100, 200);
        m1 = random(10, 50);
        //m2 = m1;
        m2 = random(10, 50);
        theta1 = random(PI / 2, 3 * PI / 2);
        theta2 = random(2 * PI);
    }

    /**
     * Setup method. Used only to specify the (0, 0) point on the canvas, its color and its size.
     */
    public void setup() {
        xOffset = width / 2;
        yOffset = height / 2; // the pendulum will have its origin in the center of the window

        canvas = createGraphics(width, height);
        canvas.beginDraw();
        canvas.background(255); // white background
        canvas.endDraw();
    }

    /**
     * Drawing method. Contains the equations of motion for the pendulum and specifies how it is to be drawn.
     */
    public void draw() {

        e1 = (-G * (2 * m1 + m2) * sin(theta1) - m2 * G * sin(theta1 - 2 * theta2) - 2 * sin(theta1 - theta2) * m2 * (w2 * w2 * r2 + w1 * w1 * r1 * cos(theta1 - theta2))) / (r1 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2)));
        e2 = (2 * sin(theta1 - theta2) * (w1 * w1 * r1 * (m1 + m2) + G * (m1 + m2) * cos(theta1) + w2 * w2 + r2 * m2 * cos(theta1 - theta2))) / (r2 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2)));

        float factor1 = -G * (2 * m1 + m2) * sin(theta1);
        float factor2 = -m2 * G * sin(theta1 - 2 * theta2);
        float factor3 = -2 * sin(theta1 - theta2) * m2;
        float factor4 = w2 * w2 * r2 + w1 * w1 * r1 * cos(theta1 - theta2);
        float denominator = r1 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2));
        float e1 = (factor1 + factor2 + factor3 * factor4) / denominator;

        factor1 = 2 * sin(theta1 - theta2);
        factor2 = (w1 * w1 * r1 * (m1 + m2));
        factor3 = G * (m1 + m2) * cos(theta1);
        factor4 = w2 * w2 * r2 * m2 * cos(theta1 - theta2);
        denominator = r2 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2));
        float e2 = (factor1 * (factor2 + factor3 + factor4)) / denominator;

        float x1 = r1 * sin(theta1);
        float y1 = r1 * cos(theta1); // Cartesian coordinates of the first pendulum's pendant

        float x2 = x1 + r2 * sin(theta2);
        float y2 = y1 + r2 * cos(theta2); // Cartesian coordinates of the second pendulum's pendant

        translate(xOffset, yOffset);

        background(255);
        stroke(0);
        strokeWeight(3);
        fill(0);

        line(0, 0, x1, y1);
        ellipse(x1, y1, m1, m1);

        line(x1, y1, x2, y2);
        ellipse(x2, y2, m2, m2);

        theta1 += w1;
        theta2 += w2;
        w1 += e1;
        w2 += e2;

        // Dampening
        w1 = 0.99f * w1;
        w2 = 0.99f * w2;

    }

}
