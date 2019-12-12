#To initilize a node, we need how dangerous it is and a name for it.
class Node:
    def __init__(self, node_name, danger_level):
        self.ID = node_name
        self.danger_level = danger_level
        self.connectedNodes = []
    def addNodeDistance(self, node, distance):
        node_with_distance = [node,distance]
        self.connectedNodes.append(node_with_distance)
    def removeNode(self, node):
        self.connectedNodes.remove(node)
    def printAllDistances(self):
        for key in self.connectedNodes:
            print("Node ID: "+str(key[0].ID)+"\nnode danger: "+str(key[0].danger_level))
            print("Distance to the node: "+str(key[1]))

a = Node('a',5)
b = Node('b',5)
c = Node('c',5)
d = Node('d',5)
e = Node('e',5)

a.addNodeDistance(b, 10)
a.addNodeDistance(c, 3)

b.addNodeDistance(c,1)
b.addNodeDistance(d,2)

c.addNodeDistance(b,4)
c.addNodeDistance(d,8)
c.addNodeDistance(e,2)

d.addNodeDistance(e,7)

e.addNodeDistance(d,9)


graph = [a,b,c,d,e]
#for every node in the graph, increase the distance to the said node by how unsafe/safe it is. Consider the safety of current and next node.
#Do this before calculating the shortest path to find the best overall path (short and safe)

for node in graph:
    for connectedNode in node.connectedNodes:
        connectedNode[1] += 0.1*(connectedNode[0].danger_level + node.danger_level)

def dijkstra(graph,start,goal):
    shortest_distance = {}
    predecessor = {}
    unseenNodes = graph
    infinity = 9999999
    path = []
    for node in unseenNodes:
        shortest_distance[node] = infinity#creates a dictionary of all of the distances of relating nodes. SHORTEST DIST IS STILL A DICT
    shortest_distance[start] = 0#Initilizes the start distance to 0

    while unseenNodes:
        minNode = None
        for node in unseenNodes:
            if minNode is None:
                minNode = node
            elif shortest_distance[node] < shortest_distance[minNode]:
                minNode = node

        #for childNode, weight in graph[minNode].items():
        for node in minNode.connectedNodes:
            #if weight + shortest_distance[minNode] < shortest_distance[childNode]:
            if node[1] + shortest_distance[minNode] < shortest_distance[node[0]]:
                #shortest_distance[childNode] = weight + shortest_distance[minNode]
                shortest_distance[node[0]] = node[1] + shortest_distance[minNode]
                predecessor[node[0]] = minNode
        unseenNodes.remove(minNode)



    currentNode = goal
    while currentNode != start:
        try:
            path.insert(0,currentNode)
            currentNode = predecessor[currentNode]
        except KeyError:
            print('Path not reachable')
            break
    path.insert(0,start)
    if shortest_distance[goal] != infinity:
        print('Shortest distance is ' + str(shortest_distance[goal]))
        for element in path:
            print(element.ID)
        #print('And the path is ' + str(path))


dijkstra(graph, a, b)

#THIS IS FROM YOUTUBE VIDEO (ORIGINAL IMPLEMENTATION)


graph = {'a':{'b':10,'c':3},'b':{'c':1,'d':2},'c':{'b':4,'d':8,'e':2},'d':{'e':7},'e':{'d':9}}

def dijkstra(graph,start,goal):
    shortest_distance = {}
    predecessor = {}
    unseenNodes = graph
    infinity = 9999999
    path = []
    for node in unseenNodes:
        shortest_distance[node] = infinity
    shortest_distance[start] = 0

    while unseenNodes:
        minNode = None
        for node in unseenNodes:
            if minNode is None:
                minNode = node
            elif shortest_distance[node] < shortest_distance[minNode]:
                minNode = node

        for childNode, weight in graph[minNode].items():
            if weight + shortest_distance[minNode] < shortest_distance[childNode]:
                shortest_distance[childNode] = weight + shortest_distance[minNode]
                predecessor[childNode] = minNode
        unseenNodes.pop(minNode)

    currentNode = goal
    while currentNode != start:
        try:
            path.insert(0,currentNode)
            currentNode = predecessor[currentNode]
        except KeyError:
            print('Path not reachable')
            break
    path.insert(0,start)
    if shortest_distance[goal] != infinity:
        print('Shortest distance is ' + str(shortest_distance[goal]))
        print('And the path is ' + str(path))


dijkstra(graph, 'a', 'b')
