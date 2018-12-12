package parser.collada;

import engine.utils.MyFile;
import parser.XMLParser.XMLNode;
import parser.XMLParser.XMLParser;
import parser.model.*;

public class ColladaLoader {

    public static AnimatedModelData loadColladaModel(MyFile colladaFile, int maxWeights) {
        XMLNode node = XMLParser.loadXmlFile(colladaFile);

        SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
        SkinningData skinningData = skinLoader.extractSkinData();

        SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
        SkeletonData jointsData = jointsLoader.extractBoneData();

        GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
        MeshData meshData = g.extractModelData();

        return new AnimatedModelData(meshData, jointsData);
    }

    public static AnimationData loadColladaAnimation(MyFile colladaFile) {
        XMLNode node = XMLParser.loadXmlFile(colladaFile);
        XMLNode animNode = node.getChild("library_animations");
        XMLNode jointsNode = node.getChild("library_visual_scenes");
        AnimationLoader loader = new AnimationLoader(animNode, jointsNode);
        AnimationData animData = loader.extractAnimation();
        return animData;
    }

}
