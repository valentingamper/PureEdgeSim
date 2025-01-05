/**
 *     PureEdgeSim:  A Simulation Framework for Performance Evaluation of Cloud, Edge and Mist Computing Environments
 *
 *     This file is part of PureEdgeSim Project.
 *
 *     PureEdgeSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     PureEdgeSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with PureEdgeSim. If not, see <http://www.gnu.org/licenses/>.
 *
 *     @author Charafeddine Mechalikh
 **/
package com.mechalikh.pureedgesim.taskorchestrator;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mechalikh.pureedgesim.datacentersmanager.ComputingNode;
import com.mechalikh.pureedgesim.scenariomanager.SimulationParameters;
import com.mechalikh.pureedgesim.simulationengine.Event;
import com.mechalikh.pureedgesim.simulationmanager.SimulationManager;
import com.mechalikh.pureedgesim.taskgenerator.Task;
import jdk.jshell.spi.ExecutionControl;

public class CustomOrchestrator extends Orchestrator {
    protected Map<Integer, Integer> historyMap = new LinkedHashMap<>();

    public CustomOrchestrator(SimulationManager simulationManager) {
        super(simulationManager);
        // Initialize the history map
        for (int i = 0; i < nodeList.size(); i++)
            historyMap.put(i, 0);
    }

    protected int findComputingNode(String[] architecture, Task task) {
        switch(algorithmName){
            case "ROUND_ROBIN":
                return roundRobin(architecture, task);
            case "TRADE_OFF":
                return tradeOff(architecture, task);
            case "ALGORITHM_1":
                throw new RuntimeException("Algorithm1 not implemented yet");
            case "ALGORITHM_2":
                throw new RuntimeException("Algorithm2 not implemented yet");
            case "ALGORITHM_3":
                throw new RuntimeException("Algorithm3 not implemented yet");
            case "ALGORITHM_4":
                throw new RuntimeException("Algorith4m not implemented yet");
            case "ALGORITHM_5":
                throw new RuntimeException("Algorithm5 not implemented yet");
            default:
                throw new IllegalArgumentException(getClass().getSimpleName() + " - Unknown orchestration algorithm_'"
                        + algorithmName + "', please check the simulation parameters file...");
        }
    }

    protected int tradeOff(String[] architecture, Task task) {
        int selected = -1;
        double min = -1;
        double newMin;// the computing node with minimum weight;
        ComputingNode node; // get best computing node for this task
        for (int i = 0; i < nodeList.size(); i++) {
            node = nodeList.get(i);
            if (offloadingIsPossible(task, node, architecture)) {
                // the weight below represent the priority, the less it is, the more it is //
                // suitable for offlaoding, you can change it as you want
                double weight = 1.2;
                // // this is an
                // edge server 'cloudlet', the latency is slightly high then edge // devices
                if (node.getType() == SimulationParameters.TYPES.CLOUD) {
                    weight = 1.8; // this
                    // is the cloud, it consumes more energy and results in high latency, so //
                    // better to avoid it
                } else if (node.getType() == SimulationParameters.TYPES.EDGE_DEVICE) {
                    weight = 1.3;// this is an edge
                    // device, it results in an extremely low latency, but may // consume more
                    // energy.
                }
                newMin = (historyMap.get(i) + 1) * weight * task.getLength() / node.getMipsPerCore();
                if (min == -1 || min > newMin) { // if it is the first
                    // iteration, or if this computing node has more // cpu mips and // less waiting
                    // tasks
                    min = newMin; // set the first computing node as the best one
                    selected = i;
                }
            }
        }
        if (selected != -1)
            historyMap.put(selected, historyMap.get(selected) + 1); // assign the tasks to the selected computing
        // node
        return selected;
    }

    public int roundRobin(String[] architecture, Task task) {
        int selected = -1;
        int minTasksCount = -1; // Computing node with minimum assigned tasks.
        for (int i = 0; i < nodeList.size(); i++) {
            if (offloadingIsPossible(task, nodeList.get(i), architecture)
                    && (minTasksCount == -1 || minTasksCount > historyMap.get(i))) {
                minTasksCount = historyMap.get(i);
                // if this is the first time,
                // or new min found, so we choose it as the best computing node.
                selected = i;
            }
        }
        // Assign the tasks to the obtained computing node.
        historyMap.put(selected, minTasksCount + 1);

        return selected;
    }

    @Override
    public void resultsReturned(Task task) {
        // Do something with the task that has been finished

    }

    @Override
    public void processEvent(Event e) {
        // Process the scheduled events, if any.

    }

}
