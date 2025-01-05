package simulation;

import com.mechalikh.pureedgesim.datacentersmanager.DefaultComputingNode;
import com.mechalikh.pureedgesim.datacentersmanager.DefaultTopologyCreator;
import com.mechalikh.pureedgesim.locationmanager.DefaultMobilityModel;
import com.mechalikh.pureedgesim.network.DefaultNetworkModel;
import com.mechalikh.pureedgesim.simulationmanager.DefaultSimulationManager;
import com.mechalikh.pureedgesim.simulationmanager.Simulation;
import com.mechalikh.pureedgesim.taskgenerator.DefaultTaskGenerator;
import com.mechalikh.pureedgesim.taskorchestrator.CustomOrchestrator;
import com.mechalikh.pureedgesim.taskorchestrator.DefaultOrchestrator;

// Below is the path for the settings folder of this example


public  class Main {

    public static void main(String[] args) {
        Simulation sim = new Simulation();

        sim.setCustomOutputFolder("PureEdgeSim/simulation/output/");
        sim.setCustomSettingsFolder("PureEdgeSim/simulation/settings/");

        sim.setCustomMobilityModel(CustomStaticMobilityModel.class);

        sim.setCustomEdgeOrchestrator(CustomOrchestrator.class);

        sim.setCustomComputingNode(DefaultComputingNode.class);

        sim.setCustomTaskGenerator(DefaultTaskGenerator.class);

        sim.setCustomNetworkModel(DefaultNetworkModel.class);

        sim.setCustomSimulationManager(DefaultSimulationManager.class);

        sim.setCustomTopologyCreator(DefaultTopologyCreator.class);

        sim.launchSimulation();
    }

}
