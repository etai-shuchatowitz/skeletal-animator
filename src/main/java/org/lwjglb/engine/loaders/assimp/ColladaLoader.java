//package org.lwjglb.engine.loaders.assimp;
//
//import org.joml.Matrix4f;
//import org.joml.Vector4f;
//import org.lwjgl.PointerBuffer;
//import org.lwjgl.assimp.*;
//import org.lwjglb.engine.Utils;
//import org.lwjglb.engine.graph.Material;
//import org.lwjglb.engine.graph.Mesh;
//import org.lwjglb.engine.graph.Texture;
//import org.lwjglb.engine.graph.anim.AnimGameItem;
//import org.lwjglb.engine.loaders.assimp.model.MeshData;
//
//import java.nio.IntBuffer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.lwjgl.assimp.Assimp.*;
//import static org.lwjgl.assimp.Assimp.aiProcess_LimitBoneWeights;
//
//public class ColladaLoader {
//
//    public static AnimGameItem loadAnimGameItem(String resourcePath, String texturesDir)
//            throws Exception {
//        return loadAnimGameItem(resourcePath, texturesDir,
//                aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate
//                        | aiProcess_FixInfacingNormals | aiProcess_LimitBoneWeights);
//    }
//
//
//    public static AnimGameItem loadAnimGameItem(String resourcePath, String texturesDir, int flags)
//            throws Exception {
//        AIScene aiScene = aiImportFile(resourcePath, flags);
//        if (aiScene == null) {
//            throw new Exception("Error loading model");
//        }
//
//        int numMaterials = aiScene.mNumMaterials();
//        PointerBuffer aiMaterials = aiScene.mMaterials();
//        List<Material> materials = new ArrayList<>();
//        for (int i = 0; i < numMaterials; i++) {
//            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
//            processMaterial(aiMaterial, materials, texturesDir);
//        }
//
//        List<Bone> boneList = new ArrayList<>();
//        int numMeshes = aiScene.mNumMeshes();
//        PointerBuffer aiMeshes = aiScene.mMeshes();
//        MeshData[] meshes = new MeshData[numMeshes];
//        for (int i = 0; i < numMeshes; i++) {
//            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
//            MeshData meshData = processMesh(aiMesh, materials, boneList);
//            meshes[i] = meshData;
//        }
//
////        AINode aiRootNode = aiScene.mRootNode();
////        Matrix4f rootTransfromation = AnimMeshesLoader.toMatrix(aiRootNode.mTransformation());
////        Node rootNode = processNodesHierarchy(aiRootNode, null);
////        Map<String, Animation> animations = processAnimations(aiScene, boneList, rootNode, rootTransfromation);
////        AnimGameItem item = new AnimGameItem(meshes, animations);
//
////        return item;
//    }
//
//    private static MeshData processMesh(AIMesh aiMesh, List<Material> materials, List<Bone> boneList) {
//        List<Float> vertices = new ArrayList<>();
//        List<Float> textures = new ArrayList<>();
//        List<Float> normals = new ArrayList<>();
//        List<Integer> indices = new ArrayList<>();
//        List<Integer> boneIds = new ArrayList<>();
//        List<Float> weights = new ArrayList<>();
//
//        processVertices(aiMesh, vertices);
//        processNormals(aiMesh, normals);
//        processTextCoords(aiMesh, textures);
//        processIndices(aiMesh, indices);
//        processBones(aiMesh, boneList, boneIds, weights);
//
//        return new MeshData(floatListToArray(vertices), floatListToArray(textures), floatListToArray(normals),
//                intListToArray(indices), intListToArray(boneIds), floatListToArray(weights));
//    }
//
//    private static void processBones(AIMesh aiMesh, List<Bone> boneList, List<Integer> boneIds,
//                                     List<Float> weights) {
//        Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
//        int numBones = aiMesh.mNumBones();
//        PointerBuffer aiBones = aiMesh.mBones();
//        for (int i = 0; i < numBones; i++) {
//            AIBone aiBone = AIBone.create(aiBones.get(i));
//            int id = boneList.size();
//            Bone bone = new Bone(id, aiBone.mName().dataString(), toMatrix(aiBone.mOffsetMatrix()));
//            boneList.add(bone);
//            int numWeights = aiBone.mNumWeights();
//            AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
//            for (int j = 0; j < numWeights; j++) {
//                AIVertexWeight aiWeight = aiWeights.get(j);
//                VertexWeight vw = new VertexWeight(bone.getBoneId(), aiWeight.mVertexId(),
//                        aiWeight.mWeight());
//                List<VertexWeight> vertexWeightList = weightSet.get(vw.getVertexId());
//                if (vertexWeightList == null) {
//                    vertexWeightList = new ArrayList<>();
//                    weightSet.put(vw.getVertexId(), vertexWeightList);
//                }
//                vertexWeightList.add(vw);
//            }
//        }
//
//        int numVertices = aiMesh.mNumVertices();
//        for (int i = 0; i < numVertices; i++) {
//            List<VertexWeight> vertexWeightList = weightSet.get(i);
//            int size = vertexWeightList != null ? vertexWeightList.size() : 0;
//            for (int j = 0; j < Mesh.MAX_WEIGHTS; j++) {
//                if (j < size) {
//                    VertexWeight vw = vertexWeightList.get(j);
//                    weights.add(vw.getWeight());
//                    boneIds.add(vw.getBoneId());
//                } else {
//                    weights.add(0.0f);
//                    boneIds.add(0);
//                }
//            }
//        }
//    }
//
//    private static void processNormals(AIMesh aiMesh, List<Float> normals) {
//        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
//        while (aiNormals != null && aiNormals.remaining() > 0) {
//            AIVector3D aiNormal = aiNormals.get();
//            normals.add(aiNormal.x());
//            normals.add(aiNormal.y());
//            normals.add(aiNormal.z());
//        }
//    }
//
//    private static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
//        AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
//        int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
//        for (int i = 0; i < numTextCoords; i++) {
//            AIVector3D textCoord = textCoords.get();
//            textures.add(textCoord.x());
//            textures.add(1 - textCoord.y());
//        }
//    }
//
//    private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
//        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
//        while (aiVertices.remaining() > 0) {
//            AIVector3D aiVertex = aiVertices.get();
//            vertices.add(aiVertex.x());
//            vertices.add(aiVertex.y());
//            vertices.add(aiVertex.z());
//        }
//    }
//
//    protected static void processIndices(AIMesh aiMesh, List<Integer> indices) {
//        int numFaces = aiMesh.mNumFaces();
//        AIFace.Buffer aiFaces = aiMesh.mFaces();
//        for (int i = 0; i < numFaces; i++) {
//            AIFace aiFace = aiFaces.get(i);
//            IntBuffer buffer = aiFace.mIndices();
//            while (buffer.remaining() > 0) {
//                indices.add(buffer.get());
//            }
//        }
//    }
//
//    private static void processMaterial(AIMaterial aiMaterial, List<Material> materials,
//                                          String texturesDir) throws Exception {
//        AIColor4D colour = AIColor4D.create();
//
//        AIString path = AIString.calloc();
//        Assimp.aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null,
//                null, null, null, null, null);
//        String textPath = path.dataString();
//        System.out.println(textPath);
//        Texture texture = null;
//        if (textPath != null && textPath.length() > 0) {
//            TextureCache textCache = TextureCache.getInstance();
//            String textureFile = texturesDir + "/" + textPath;
//            textureFile = textureFile.replace("//", "/");
//            texture = textCache.getTexture(textureFile);
//        }
//
//        Vector4f ambient = Material.DEFAULT_COLOUR;
//        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0,
//                colour);
//        if (result == 0) {
//            ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//        }
//
//        Vector4f diffuse = Material.DEFAULT_COLOUR;
//        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0,
//                colour);
//        if (result == 0) {
//            diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//        }
//
//        Vector4f specular = Material.DEFAULT_COLOUR;
//        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0,
//                colour);
//        if (result == 0) {
//            specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//        }
//
//        Material material = new Material(ambient, diffuse, specular, 1.0f);
//        material.setTexture(texture);
//        materials.add(material);
//    }
//
//    private static float[] floatListToArray(List<Float> floatList) {
//        float[] array = new float[floatList.size()];
//        int i = 0;
//
//        for (Float f : floatList) {
//            array[i++] = f;
//        }
//
//        return array;
//    }
//
//    private static int[] intListToArray(List<Integer> intList) {
//        int[] array = new int[intList.size()];
//        int i = 0;
//
//        for (Integer f : intList) {
//            array[i++] = f;
//        }
//
//        return array;
//    }
//
//    private static Matrix4f toMatrix(AIMatrix4x4 aiMatrix4x4) {
//        Matrix4f result = new Matrix4f();
//        result.m00(aiMatrix4x4.a1());
//        result.m10(aiMatrix4x4.a2());
//        result.m20(aiMatrix4x4.a3());
//        result.m30(aiMatrix4x4.a4());
//        result.m01(aiMatrix4x4.b1());
//        result.m11(aiMatrix4x4.b2());
//        result.m21(aiMatrix4x4.b3());
//        result.m31(aiMatrix4x4.b4());
//        result.m02(aiMatrix4x4.c1());
//        result.m12(aiMatrix4x4.c2());
//        result.m22(aiMatrix4x4.c3());
//        result.m32(aiMatrix4x4.c4());
//        result.m03(aiMatrix4x4.d1());
//        result.m13(aiMatrix4x4.d2());
//        result.m23(aiMatrix4x4.d3());
//        result.m33(aiMatrix4x4.d4());
//
//        return result;
//    }
//}
