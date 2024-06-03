#!/usr/bin/python3


from CS312Graph import *
import time

class ArrayQueue:
    def __init__(self, dist):
        self.arrayList = []
        self.size = 0

        for i in range(len(dist)):
            self.insert(dist[i])
        self.size = len(dist)

    def insert(self, distance):
        self.arrayList.append(distance)
        return

    def deleteMin(self):
        min = float('inf')
        remove = 0
        for i in range(len(self.arrayList)):
            if (self.arrayList[i] != None):
                if (self.arrayList[i] < min):
                    min = self.arrayList[i]
                    remove = i
        self.arrayList[remove] = None
        self.size = self.size-1
        if (self.size == 0):
            self.arrayList.clear()
        return min

    def decreaseKey(self, index, distance):
        self.arrayList[index] = distance
        return

class HeapQueue:
    def __init__(self, dist):
        self.heapList = []
        self.pointerList = []
        self.size = 0

        for i in range(len(dist)):
            self.insert(i, dist[i])

    def insert(self, node, distance):
        self.pointerList.append(len(self.heapList))
        self.heapList.append((node, distance))
        self.size = self.size + 1
        return

    def deleteMin(self):
        if (len(self.heapList) == 1):
            nodef, distancef = self.heapList.pop()
            self.size = self.size - 1
            return nodef

        nodef, distancef = self.heapList[0]
        self.pointerList[nodef] = None
        self.heapList[0] = self.heapList[-1]
        self.heapList.pop()

        nodel, distancel = self.heapList[0]
        self.pointerList[nodel] = 0
        self.bubbleDown(nodel)

        self.size = self.size-1

        return nodef

    def bubbleDown(self, node):
        while (True):
            nodeloc = self.pointerList[node]
            locc1 = round((nodeloc + 0.5) * 2)
            locc2 = (nodeloc + 1) * 2
            node, nodedist = self.heapList[nodeloc]

            locmax = len(self.heapList) - 1
            if (locc1 > locmax and locc2 > locmax):
                break
            else:
                if (locc1 > locmax):
                    distc1 = float('inf')
                    idc2, distc2 = self.heapList[locc2]
                elif (locc2 > locmax):
                    idc1, distc1 = self.heapList[locc1]
                    distc2 = float('inf')
                else:
                    idc1, distc1 = self.heapList[locc1]
                    idc2, distc2 = self.heapList[locc2]

            if (distc1 <= distc2):
                child = idc1
                childdist = distc1
                childloc = locc1
            else:
                child = idc2
                childdist = distc2
                childloc = locc2

            if (nodedist > childdist):
                self.heapList[childloc] = self.heapList[nodeloc]
                self.pointerList[node] = childloc
                self.heapList[nodeloc] = (child, childdist)
                self.pointerList[child] = nodeloc
            else:
                break

    def decreaseKey(self, node, distance):
        location = self.pointerList[node]
        self.heapList[location] = (node, distance)
        self.bubbleUp(node)
        return

    def bubbleUp(self, node):
        while (True):
            nodeloc = self.pointerList[node]
            if (nodeloc == 0):
                break
            locp = (nodeloc - 1) // 2
            node, nodedist = self.heapList[nodeloc]
            idp, distp = self.heapList[locp]
            if (nodedist < distp):
                self.heapList[locp] = self.heapList[nodeloc]
                self.pointerList[node] = locp
                self.heapList[nodeloc] = (idp, distp)
                self.pointerList[idp] = nodeloc
            else:
                break

class NetworkRoutingSolver:
    def __init__( self):
        self.dist = None
        self.prev = None
        self.source = None
        self.dest = None
        self.network = None

    def initializeNetwork( self, network ):
        assert( type(network) == CS312Graph )
        self.network = network

    def getShortestPath( self, destIndex ):
        self.dest = destIndex
        path_edges = []
        total_length = 0

        if (len(self.dist) == 0):
            return {'cost': float('inf'), 'path': path_edges}
        else:
            total_length = self.dist[self.dest]

            currIndex = self.dest
            prevIndex = None
            while (currIndex != self.source):
                prevIndex = self.prev[currIndex]
                currNode = self.network.nodes[currIndex]
                if (prevIndex == None):
                    return {'cost': float('inf'), 'path': path_edges}
                prevNode = self.network.nodes[prevIndex]
                for i in range(len(prevNode.neighbors)):
                    if (prevNode.neighbors[i].dest == currNode):
                        edge = prevNode.neighbors[i]
                        break
                path_edges.append( (currNode.loc, prevNode.loc, '{:.0f}'.format(edge.length)) )
                currIndex = prevIndex

        return {'cost':total_length, 'path':path_edges}

    def computeShortestPaths( self, srcIndex, use_heap=False ):
        self.source = srcIndex
        t1 = time.time()
        self.dist = []
        self.prev = []

        for i in range(len(self.network.nodes)):
            self.dist.append(float('inf'))
            self.prev.append(None)
        self.dist[self.source] = 0

        if (use_heap == False):
            arrayqueue = ArrayQueue(self.dist)
            while (arrayqueue.size > 0):
                min = arrayqueue.deleteMin()
                if (min < float('inf')):
                    i = self.dist.index(min)
                    for j in range(len(self.network.nodes[i].neighbors)):
                        k = self.network.nodes.index(self.network.nodes[i].neighbors[j].dest)
                        if (k >= 0):
                            n = self.dist[i] + self.network.nodes[i].neighbors[j].length
                            if (self.dist[k] > n):
                                self.dist[k] = n
                                self.prev[k] = i
                                arrayqueue.decreaseKey(k, n)
                else:
                    break
        else:
            heapqueue = HeapQueue(self.dist);
            heapqueue.decreaseKey(srcIndex, 0)
            while (heapqueue.size > 0):
                min = heapqueue.deleteMin()
                if (self.dist[min] != float('inf')):
                    for j in self.network.nodes[min].neighbors:
                        k = j.dest.node_id
                        n = self.dist[min] + j.length
                        if (self.dist[k] == float('inf') or n < self.dist[k]):
                            self.dist[k] = n
                            self.prev[k] = min
                            heapqueue.decreaseKey(k, n)
                else:
                    break

        t2 = time.time()
        return (t2-t1)