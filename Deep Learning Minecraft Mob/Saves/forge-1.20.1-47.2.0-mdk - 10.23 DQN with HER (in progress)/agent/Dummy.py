import random

oldSpawn, oldSight, oldHear, oldInventory, oldReward = [], [], [], [], []
oldData = ""

i = 0
while (True):
    i += 1
    if (i%10000 == 0):
        print("loop ", i)

    with open("../src/main/java/net/calvinpugmire/agiagentmod/entity/custom/AgentInput.txt", "r") as f:
        content = f.read()
    stringContent = content.split(",,,")
    if (len(stringContent) == 7):
        spawn = stringContent[1].replace("[", "").replace("]", "").split(", ")
        spawn = [float(x) for x in spawn]

        stringSight = stringContent[2].replace("[", "").replace("]", "").split(", ")
        sight = []
        for item in stringSight:
            if (len(item) > 0):
                parts = item.split(" :: ")
                name = parts[0].strip("'")
                coordinates = [float(x) for x in parts[1].split(" ")]
                result = [name] + coordinates
                sight.append(result)

        stringHear = stringContent[3].replace("[", "").replace("]", "").split(", ")
        hear = []
        for item in stringHear:
            if (len(item) > 0):
                parts = item.split(" :: ")
                name = parts[0].strip("'")
                coordinates = [float(x) for x in parts[1].split(" ")]
                result = [name] + coordinates
                hear.append(result)

        stringInventory = stringContent[4].replace("[", "").replace("]", "").split(", ")
        inventory = []
        for item in stringInventory:
            if (len(item) > 0):
                parts = item.split(" :: ")
                name = parts[0].strip("'")
                count = float(parts[1])
                result = [name] + [count]
                inventory.append(result)

        reward = stringContent[5].replace("[", "").replace("]", "").split(", ")
        reward = [float(x) for x in reward]

        if (oldSpawn != spawn or oldSight != sight or oldHear != hear or oldInventory != inventory or oldReward != reward):
            oldSpawn, oldSight, oldHear, oldInventory, oldReward = spawn, sight, hear, inventory, reward
            #print(spawn)
            #print(sight)
            #print(hear)
            #print(inventory)
            #print(reward)

    if (i%10000 == 0):
        if (oldReward[0] == 0.0):
            data = "none none none none none";
        else:
            moveAction = random.choice(["none", "forward", "backward", "left", "right"])
            lookAction = random.choice(["none", "up", "down", "left", "right"])
            jumpAction = random.choice(["none", "jump"])
            handAction = random.choice(["none", "hit", "use"])
            swapAction = random.choice(["none","0","1","2","3","4","5","6","7","8","9"])
            data = moveAction+" "+lookAction+" "+jumpAction+" "+handAction+" "+swapAction;
        
        if (data != oldData):
            oldData = data
            with open("../src/main/java/net/calvinpugmire/agiagentmod/entity/custom/AgentOutput.txt", "w") as f:
                f.write(data)
    