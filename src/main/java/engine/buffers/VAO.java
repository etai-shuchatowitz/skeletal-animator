package engine.buffers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class VAO {

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_INT = 4;
    public final int id;
    private List<VBO> dataVbos = new ArrayList<VBO>();
    private VBO indexVbo;
    private int indexCount;

    public static VAO create() {
        int id = GL30.glGenVertexArrays();
        return new VAO(id);
    }

    private VAO(int id) {
        this.id = id;
    }

    public int getIndexCount(){
        return indexCount;
    }

    public void bind(int... attributes){
        bind();
        for (int i : attributes) {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    public void unbind(int... attributes){
        for (int i : attributes) {
            GL20.glDisableVertexAttribArray(i);
        }
        unbind();
    }

    public void createIndexBuffer(int[] indices){
        this.indexVbo = VBO.create(GL15.GL_ELEMENT_ARRAY_BUFFER);
        indexVbo.bind();
        indexVbo.storeData(indices);
        this.indexCount = indices.length;
    }

    public void createAttribute(int attribute, float[] data, int attrSize){
        VBO dataVbo = VBO.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
        dataVbo.unbind();
        dataVbos.add(dataVbo);
    }

    public void createIntAttribute(int attribute, int[] data, int attrSize){
        VBO dataVbo = VBO.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, attrSize * BYTES_PER_INT, 0);
        dataVbo.unbind();
        dataVbos.add(dataVbo);
    }

    public void delete() {
        GL30.glDeleteVertexArrays(id);
        for(VBO vbo : dataVbos){
            vbo.delete();
        }
        indexVbo.delete();
    }

    private void bind() {
        GL30.glBindVertexArray(id);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

}