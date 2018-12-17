package engine.graph.anim;

import org.joml.Matrix4f;

import java.util.Arrays;

public class AnimatedFrame {

    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();

    public static final int MAX_JOINTS = 150;

    private Matrix4f[] jointMatrices;

    public Matrix4f[] getInterpolatedJointMatrices() {
        return interpolatedJointMatrices;
    }

    private Matrix4f[] interpolatedJointMatrices;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    private double time;

    public AnimatedFrame() {
        jointMatrices = new Matrix4f[MAX_JOINTS];
        interpolatedJointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(jointMatrices, IDENTITY_MATRIX);
        Arrays.fill(interpolatedJointMatrices, IDENTITY_MATRIX);
    }

    public Matrix4f[] getJointMatrices() {
        return jointMatrices;
    }

    public void setMatrix(int pos, Matrix4f jointMatrix) {
        jointMatrices[pos] = jointMatrix;
        updateInterpolatedJointMatrix(pos, jointMatrix);
    }

    public void updateInterpolatedJointMatrix(int pos, Matrix4f jointMatrix) {
        interpolatedJointMatrices[pos] = jointMatrix;
    }

}

