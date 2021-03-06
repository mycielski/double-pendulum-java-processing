/*
        _
        \\
         \\    ⬤..
     :    \\  //   :
      :..  \\//     :
         :..⬤

Double pendulum in Java, rendered with Processing library.
 */

import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {

    PGraphics canvas;       // canvas on which the pendulum is to be drawn
    float xOffset, yOffset; // offset of the (0, 0) point on the canvas from top left corner

    float r1, r2;           // radii of the pendulums
    float m1, m2;           // masses of the pendulums
    float theta1, theta2;   // initial angles of the pendulums

    float w1 = 0, w2 = 0;   // initial velocities of the pendulums' pendants
    float e1 = 0, e2 = 0;   // initial accelerations of the pendulums' pendants

    final float G = 0.64f;  // gravitational acceleration; decrease to give the pendulum a "slow motion" appearance

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
        size(900, 900); // size of window
        r1 = random(100, 200);
        r2 = random(100, 200);
        m1 = random(10, 50);
        m2 = random(10, 50);
        //m2 = m1;
        theta1 = random(PI / 2, 3 * PI / 2); // 1st pendulum always starts in the upper half of its circle
        theta2 = random(PI / 2, 3 * PI / 2); // 2nd pendulum always starts in the upper half of its circle
    }

    /**
     * Setup method. Used only to specify the (0, 0) point's offset on the drawing canvas.
     */
    public void setup() {
        xOffset = width / 2;
        yOffset = height / 2;
    }

    /**
     * Drawing method. Contains the equations of motion for the pendulum and specifies how it is to be drawn.
     */
    public void draw() {

        /*
        Calculating angular accelerations in one line of code is too fast for Processing's renderer.
        While the delays from splitting declarations and calculations are miniscule, they are quite sufficient for the
        renderer to "catch up".
        For clarity's sake the one-line equations of motion are left commented in the two lines below. */
        // e1 = (-G * (2 * m1 + m2) * sin(theta1) - m2 * G * sin(theta1 - 2 * theta2) - 2 * sin(theta1 - theta2) * m2 * (w2 * w2 * r2 + w1 * w1 * r1 * cos(theta1 - theta2))) / (r1 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2)));
        // e2 = (2 * sin(theta1 - theta2) * (w1 * w1 * r1 * (m1 + m2) + G * (m1 + m2) * cos(theta1) + w2 * w2 + r2 * m2 * cos(theta1 - theta2))) / (r2 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2)));

        // Angular acceleration of the 1st pendulum
        float factor1 = -G * (2 * m1 + m2) * sin(theta1);
        float factor2 = -m2 * G * sin(theta1 - 2 * theta2);
        float factor3 = -2 * sin(theta1 - theta2) * m2;
        float factor4 = w2 * w2 * r2 + w1 * w1 * r1 * cos(theta1 - theta2);
        float denominator = r1 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2));
        float e1 = (factor1 + factor2 + factor3 * factor4) / denominator;

        // Angular acceleration of the 2nd pendulum
        factor1 = 2 * sin(theta1 - theta2);
        factor2 = (w1 * w1 * r1 * (m1 + m2));
        factor3 = G * (m1 + m2) * cos(theta1);
        factor4 = w2 * w2 * r2 * m2 * cos(theta1 - theta2);
        denominator = r2 * (2 * m1 + m2 - m2 * cos(2 * theta1 - 2 * theta2));
        float e2 = (factor1 * (factor2 + factor3 + factor4)) / denominator;

        // Update angular velocity of the pendulums by adding the calculated accelerations
        w1 += e1;
        w2 += e2;

        // Dampening; use to crudely simulate loss of energy due to friction, air resistance, etc.
        w1 = 0.9999f * w1;
        w2 = 0.9999f * w2;

        // Update the angles of the pendulums by adding the calculated velocities
        theta1 += w1;
        theta2 += w2;

        // Calculate the cartesian coordinates of 1st pendulum's pendant
        float x1 = r1 * sin(theta1);
        float y1 = r1 * cos(theta1);

        // Calculate the cartesian coordinates of 2nd pendulum's pendant
        float x2 = x1 + r2 * sin(theta2);
        float y2 = y1 + r2 * cos(theta2);

        // Apply the (0, 0) point's offset calculated in setup()
        translate(xOffset, yOffset);

        // Set the background to be white, the drawing to be black and its lines 3px wide.
        background(255);
        stroke(0);
        strokeWeight(3);
        fill(0);

        // Draw the 1st pendulum's radius and its pendant.
        // The pendant is drawn proportional to its mass
        line(0, 0, x1, y1);
        ellipse(x1, y1, m1, m1);

        // Draw the 2nd pendulum's radius and its pendant
        // The pendant is drawn proportional to its mass
        line(x1, y1, x2, y2);
        ellipse(x2, y2, m2, m2);

    }

}
