package gg.vape.model.models;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class TessellatorModel extends ObjModel {

    public TessellatorModel(String string) {
        super(string);

        try {
            String content = new String(this.read(Model.class.getResourceAsStream(string)), StandardCharsets.UTF_8);
            String startPath = string.substring(0, string.lastIndexOf(47) + 1);
            HashMap<ObjObject, IndexedModel> map = new OBJLoader().loadModel(startPath, content);
            this.objObjects.clear();
            Set<ObjObject> keys = map.keySet();

            for (ObjObject object : keys) {
                Mesh mesh = new Mesh();
                object.mesh = mesh;
                this.objObjects.add(object);
                map.get(object).toMesh(mesh);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }

    public void renderImpl() {
        this.objObjects.sort((a, b) -> {
            Vec3d v = Minecraft.getMinecraft().getRenderViewEntity().getPositionVector();
            double aDist = v.distanceTo(new Vec3d(a.center.x, a.center.y, a.center.z));
            double bDist = v.distanceTo(new Vec3d(b.center.x, b.center.y, b.center.z));
            return Double.compare(aDist, bDist);
        });

        for (ObjObject object : this.objObjects) {
            this.renderGroup(object);
        }

    }

    public void renderGroupsImpl(String group) {

        for (ObjObject object : this.objObjects) {
            if (object.getName().equals(group)) {
                this.renderGroup(object);
            }
        }

    }

    public void renderGroupImpl(ObjObject obj) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder renderer = tess.getBuffer();
        if (obj.mesh != null) {
            if (obj.material != null) {
                GlStateManager.bindTexture(obj.material.diffuseTexture);
            }

            int[] indices = obj.mesh.indices;
            Vertex[] vertices = obj.mesh.vertices;
            renderer.begin(4, DefaultVertexFormats.POSITION_TEX_NORMAL);

            for (int i = 0; i < indices.length; i += 3) {
                int i0 = indices[i];
                int i1 = indices[i + 1];
                int i2 = indices[i + 2];
                Vertex v0 = vertices[i0];
                Vertex v1 = vertices[i1];
                Vertex v2 = vertices[i2];
            }

            tess.draw();
        }
    }

    public boolean fireEvent(ObjEvent event) {
        return true;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void regenerateNormals() {
    }
}
