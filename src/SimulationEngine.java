public class SimulationEngine {
    private float currentTime;
    private float runTime;
    private TransitMap transitMap;
    private ArrivalProcess arrivals;

    public SimulationEngine(String nodeData, float runTime){
        currentTime = 0;
        this.runTime = runTime;
        transitMap = TransitMap.loadNodes(nodeData);
        transitMap.genPathTables();
        arrivals = new ArrivalProcess(1,transitMap);
    }

    public String run(){
        while (currentTime < runTime){
            //TODO: should we use floats or doubles for time purposes?
            float nextArrival = (float) arrivals.getNextArrivalTime();
            //checkNodeEvents(); somehow determine if the next event should be a node event
        }




        return "Output data goes here";
    }

    private boolean checkNodeEvents(){
        for (TransitNode node : transitMap.getNodes()){
            for (TransitConnection connection : node.getConnections()){
                //Check if a departure can be made
            }
        }
        //somehow return the event that needs done
        return false;
    }
}
