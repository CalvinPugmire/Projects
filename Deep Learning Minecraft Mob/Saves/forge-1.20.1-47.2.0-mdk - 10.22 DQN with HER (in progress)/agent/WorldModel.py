import math, mmh3
import numpy as np

WRLD_SZ = 12
SPTL_SZ = 5 #Don't change unless you also change the Java's size?
INV_SZ = 32 #Don't change unless you also change the Java's size.
RWD_SZ = 2 #Don't change unless you also change the Java's size?



class WorldModel:
    stringSpawn, stringSpatial, stringSight, stringHear, stringInventory, stringReward = "", "", "", "", "", ""
    spawn = []
    idDictionary = dict()

    def __init__(self):
        self.world = np.zeros((2, WRLD_SZ, WRLD_SZ, WRLD_SZ))
        self.worldShape = self.world.shape
        self.spatial = np.zeros((SPTL_SZ))
        self.spatialShape = self.spatial.shape
        self.inventory = np.zeros((INV_SZ))
        self.inventoryShape = self.inventory.shape
        self.reward = np.zeros((RWD_SZ))
        self.rewardShape = self.reward.shape
    
    def hashShift(self, text):
        return mmh3.hash(text.encode())/1000000000

    def rotShift(self, x, y):
        x_s = math.radians(x%360)
        y_s = math.radians(y%360)
        return x_s, y_s

    def coordShift(self, x, y, z):
        x_s = int(x - self.spawn[0] + math.ceil(WRLD_SZ/2))
        y_s = int(y - self.spawn[1] + math.ceil(WRLD_SZ/2))
        z_s = int(z - self.spawn[2] + math.ceil(WRLD_SZ/2))
        return x_s, y_s, z_s
    
    def coordValid(self, coord):
        if (0 <= coord[0] <= WRLD_SZ) and (0 <= coord[1] <= WRLD_SZ) and (0 <= coord[2] <= WRLD_SZ):
            return True
        else:
            return False
    
    def update(self, stringContent):
        if stringContent[1] != self.stringSpawn:
            spawn = stringContent[1].replace("[", "").replace("]", "").split(", ")
            spawn = [float(x) for x in spawn]
            
            self.stringSpawn = stringContent[1]
            self.spawn = spawn
            
        if stringContent[2] != self.stringSpatial:
            spatial = stringContent[2].replace("[", "").replace("]", "").split(", ")
            if (len(spatial) == SPTL_SZ):
                spatial = [float(x) for x in spatial]
                spatial[0], spatial[1], spatial[2] = self.coordShift(spatial[0], spatial[1], spatial[2])
                spatial[3], spatial[4] = self.rotShift(spatial[3], spatial[4])
                self.spatial = spatial
            
            self.stringSpatial = stringContent[2]

        if stringContent[3] != self.stringSight:
            splitSight = stringContent[3].replace("[", "").replace("]", "").split(", ")
            sight = []
            for item in splitSight:
                if (len(item) > 0):
                    parts = item.split(" :: ")
                    name = parts[0].strip("'")
                    coordinates = [float(x) for x in parts[1].split(" ")]
                    result = [name] + coordinates
                    sight.append(result)

            for i in sight:
                if "block." in i[0]: #Replace with moving-target tracking AI.
                    if self.coordValid(self.coordShift(i[1], i[2], i[3])):
                        if i[0] not in self.idDictionary:
                            self.idDictionary[i[0]] = self.hashShift(i[0])
                        x, y, z = self.coordShift(i[1], i[2], i[3])
                        self.world[0, x-1, y-1, z-1] = self.idDictionary.get(i[0])
                
            self.stringSight = stringContent[3]

        if stringContent[4] != self.stringHear:
            splitHear = stringContent[4].replace("[", "").replace("]", "").split(", ")
            hear = []
            for item in splitHear:
                if (len(item) > 0):
                    parts = item.split(" :: ")
                    name = parts[0].strip("'")
                    coordinates = [float(x) for x in parts[1].split(" ")]
                    result = [name] + coordinates
                    hear.append(result)

            self.world[1, :, :, :] = 0.0
            for i in hear:
                if self.coordValid(self.coordShift(i[1], i[2], i[3])):
                    if i[0] not in self.idDictionary:
                        self.idDictionary[i[0]] = self.hashShift(i[0])
                    x, y, z = self.coordShift(i[1], i[2], i[3])
                    self.world[1, x-1, y-1, z-1] = self.idDictionary.get(i[0])
                
            self.stringHear = stringContent[4]

        if stringContent[5] != self.stringInventory:
            splitInventory = stringContent[5].replace("[", "").replace("]", "").split(", ")
            inventory = []
            for item in splitInventory:
                if (len(item) > 0):
                    parts = item.split(" :: ")
                    name = parts[0].strip("'")
                    count = float(parts[1])
                    result = [name] + [count]
                    inventory.append(result)

            c = 0
            for i in inventory:
                if i[0] not in self.idDictionary:
                    self.idDictionary[i[0]] = self.hashShift(i[0])
                self.inventory[c] = self.idDictionary.get(i[0])
                c += 1
                self.inventory[c] = i[1]
                c += 1
                
            self.stringInventory = stringContent[5]

        if stringContent[6] != self.stringReward:
            reward = stringContent[6].replace("[", "").replace("]", "").split(", ")
            reward = [float(x) for x in reward]
            
            self.stringReward = stringContent[6]
            self.reward = reward

        observation = {
            "world": self.world, 
            "spatial": self.spatial, 
            "inventory": self.inventory,
            "reward": self.reward
        }

        return observation