package gg.vape.model.models;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

public class IndexedModel {

    private ArrayList<Vector3f> vertices = new ArrayList();
    private ArrayList<Vector2f> texCoords = new ArrayList();
    private ArrayList<Vector3f> normals = new ArrayList();
    private ArrayList<Vector3f> tangents = new ArrayList();
    private ArrayList<Integer> indices = new ArrayList();
    private ArrayList<OBJLoader.OBJIndex> objindices = new ArrayList();

    public IndexedModel() {
    }

    public ArrayList<Vector3f> getPositions() {
        return this.vertices;
    }

    public ArrayList<Vector2f> getTexCoords() {
        return this.texCoords;
    }

    public ArrayList<Vector3f> getNormals() {
        return this.normals;
    }

    public ArrayList<Integer> getIndices() {
        return this.indices;
    }

    public ArrayList<Vector3f> getTangents() {
        return this.tangents;
    }

    public void toMesh(Mesh mesh) {
        ArrayList<Vertex> verticesList = new ArrayList();
        int n = Math.min(this.vertices.size(), Math.min(this.texCoords.size(), this.normals.size()));

        for (int i = 0; i < n; ++i) {
        }

        Integer[] indicesArray = (Integer[]) this.indices.toArray(new Integer[0]);
        Vertex[] verticesArray = (Vertex[]) verticesList.toArray(new Vertex[0]);
        int[] indicesArrayInt = new int[indicesArray.length];

        for (int i = 0; i < indicesArray.length; ++i) {
            indicesArrayInt[i] = indicesArray[i];
        }

        mesh.vertices = verticesArray;
        mesh.indices = indicesArrayInt;
    }

    public void computeTangents() {
        this.tangents.clear();

        int i;
        for (i = 0; i < this.vertices.size(); ++i) {
            this.tangents.add(new Vector3f());
        }

        for (i = 0; i < this.indices.size(); i += 3) {
            int i0 = (Integer) this.indices.get(i);
            int i1 = (Integer) this.indices.get(i + 1);
            int i2 = (Integer) this.indices.get(i + 2);
            double deltaU1 = (double) (((Vector2f) this.texCoords.get(i1)).x - ((Vector2f) this.texCoords.get(i0)).x);
            double deltaU2 = (double) (((Vector2f) this.texCoords.get(i2)).x - ((Vector2f) this.texCoords.get(i0)).x);
            double deltaV1 = (double) (((Vector2f) this.texCoords.get(i1)).y - ((Vector2f) this.texCoords.get(i0)).y);
            double deltaV2 = (double) (((Vector2f) this.texCoords.get(i2)).y - ((Vector2f) this.texCoords.get(i0)).y);
            double dividend = deltaU1 * deltaV2 - deltaU2 * deltaV1;
            double f = dividend == 0.0 ? 0.0 : 1.0 / dividend;
            Vector3f v = null;
            this.tangents.set(i1, v);
            this.tangents.set(i2, v);
        }

        for (i = 0; i < this.tangents.size(); ++i) {
        }

    }

    public ArrayList<OBJLoader.OBJIndex> getObjIndices() {
        return this.objindices;
    }

    public org.lwjgl.util.vector.Vector3f computeCenter() {
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;

        Vector3f position;
        for (Iterator var4 = this.vertices.iterator(); var4.hasNext(); z += position.z) {
            position = (Vector3f) var4.next();
            x += position.x;
            y += position.y;
        }

        x /= (float) this.vertices.size();
        y /= (float) this.vertices.size();
        z /= (float) this.vertices.size();
        return new org.lwjgl.util.vector.Vector3f(x, y, z);
    }
}
