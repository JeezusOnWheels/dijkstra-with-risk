# Dijkstra with Risk
Implementation of bi-objective dykstra's algorithm inspired by the paper "Safe Navigation in Urban Environments" by Esther et. al.

For an explanation of the algorithm and the entire group project please see the accompanying video presentation https://youtu.be/KdDu_tS5Q2g?t=491.

# Run Instructions
```
git clone https://github.com/henrijsprincis/dijkstra-with-risk
javac shortestPathWithRisk.java
java shortestPathWithRisk -s MapRisk.txt 0 2 0
```
## Commandline Parameters
```
java RoadMap -s <MapFile> <StartVertexIndex> <EndVertexIndex> <mode>
```

\<MapFile\> - location of the map file e.g. MapRisk.txt

\<StartVertexIndex\> - Index (line number) of the starting node in the MapFile.

\<EndVertexIndex\> - Index (line number) of the ending node in the MapFile.

\<mode\> - an integer between 0 and 2. 

0 = shortest length, 1 = lowest risk, 2 = lowest risk and length.

## Map file format
First line indicates the number of nodes and the number of roads respectively.

Every node has a name that is displayed in a new line.

Every edge has the following structure:

\<Vertex1Idx\> \<Vertex2Idx\> \<pathLength\> \<riskLength\>

Note that the current implementation assumes that edges are bi-directional.

# Project Description
This code was written as a part of the CM2305 coursework where the aim was to developed a mobile application which enables safe navigation at night.
To do this, we looked at crime density in Cardiff city and estimated the risk of travelling along each road.
We then displayed three paths to the user: shortest, least risky, and a combination of both.

## Example
<img src=https://github.com/henrijsprincis/dijkstra-with-risk/assets/38922533/d2ea0a5c-e7b9-4040-bb9c-eda95781b395, width = 400, height = 400>

Suppose you wanted to travel from point A to point C.

1. The shortest path is A->B->C (length = 20)
2. The least risky path is A->D->C (risk = 20)
3. Path that balances risk with length is A->C (risk + length = 50)


