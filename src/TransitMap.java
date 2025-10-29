import java.util.List;

public class TransitMap {






    public List<String> calcShortestPath(List<String> currentPath, TransitNode node){
        List<String> shortestPath = null;
        float shortestPathDistance = Float.MAX_VALUE;
        for (TransitNode newNode : node.getConnections()){
            float pathDistance = calcShortestPath(currentPath + self, newNode);
            List<String> newPath = calcShortestPath(currentPath + self, newNode);
            if (pathDistance < shortestPathDistance) shortestPath = newPath;
        }
        if (shortestPath == null) return null;
        return shortestPath;
    }


}
