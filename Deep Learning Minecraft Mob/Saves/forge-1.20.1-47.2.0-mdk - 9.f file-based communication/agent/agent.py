import gymnasium as gym
from gymnasium import spaces
import numpy as np

class CustomEnv(gym.Env):
    """Custom Environment that follows gym interface"""
    metadata = {'render.modes': ['human']}

    def __init__(self):
        super(CustomEnv, self).__init__()

        # Define action and state space
        # They must be gym.spaces objects

        self.moveActions = ["none", "forward", "backward", "left", "right"]
        self.lookActions = ["none", "up", "down", "left", "right"]
        self.jumpActions = ["none", "jump"]
        self.handActions = ["none", "hit", "use"]

        # Action space:
        self.action_space = spaces.Dict({
            "move": spaces.Discrete(5), # 0=none, 1=forward, 2=backward, 3=left, 4=right
            "look": spaces.Discrete(5), # 0=none, 1=up, 2=down, 3=left, 4=right
            "jump": spaces.Discrete(2), # 0=none, 1=jump
            "hand": spaces.Discrete(3), # 0=none, 1=hit, 2=use
            "swap": spaces.Box(low=0, high=10, shape=(1,), dtype=np.integer) # indexes 0-9, 10=none
        })

        # State space:
        self.observation_space = spaces.Dict({
            "world": spaces.MultiDiscrete(np.full(24,24,24), 1000), 
            "inventory": spaces.MultiDiscrete(np.full(10), 1000),
            "reward": spaces.Box(low=0.0, high=20.0, shape=(3,), dtype=np.float32)
        })

        # Initialize state
        self.state = None
    
    def reset(self):
        """
        Reset the state of the environment to an initial state and returns an initial state.
        Returns:
        - state (object): the initial state.
        """

        # Reset the state of the environment to an initial state
        # Return the initial state

        #TODO: respawn agent

        return state

    def step(self, action):
        """
        Apply the action to the environment and step the environment forward.
        - action: the action to be executed in the environment
        Returns:
        - state (object): agent's state of the current environment
        - reward (float) : amount of reward returned after previous action
        - done (bool): whether the episode has ended, in which case further step() calls will return undefined results
        - info (dict): contains auxiliary diagnostic information (helpful for debugging, and sometimes learning)
        """

        # Execute one time step within the environment
        # Update self.state
        # Calculate reward
        # Determine if the episode is done
        # Optionally include additional info

        return state, oldReward, done, info
    
    def get_observation(self):
        #TODO here
        return
    
    def render(self, mode='human', close=False):
        """
        Render the environment.
        The set of supported modes varies per environment. (And some environments do not support rendering at all.)
        """

        # Implement rendering logic here

        #TODO later/never.

        pass

    def close(self):
        """
        Perform any necessary cleanup.
        """

        # Close and clean up resources

        #TODO later.
        
        pass


class CubeWorld:
    def __init__(self):
        self.world = np.zeros((24, 24, 24))
        self.shape = self.world.shape
    
    def new_observation(self, ):
        #TODO here
        return



import random

oldSight, oldHear, oldReward = [], [], []
oldData = ""

i = 0
while (True):
    i += 1
    if (i%10000 == 0):
        print("loop ", i)

    with open("../src/main/java/net/calvinpugmire/agiagentmod/entity/custom/AgentInput.txt", "r") as f:
        content = f.read()
    stringContent = content.split(",,,")
    if (len(stringContent) == 5):
        stringSight = stringContent[1].replace("[", "").replace("]", "").split(", ")
        sight = []
        for item in stringSight:
            if (len(item) > 0):
                parts = item.split(" :: ")
                name = parts[0].strip("'")
                coordinates = [float(x) for x in parts[1].split(" ")]
                result = [name] + coordinates
                sight.append(result)

        stringHear = stringContent[2].replace("[", "").replace("]", "").split(", ")
        hear = []
        for item in stringHear:
            if (len(item) > 0):
                parts = item.split(" :: ")
                name = parts[0].strip("'")
                coordinates = [float(x) for x in parts[1].split(" ")]
                result = [name] + coordinates
                hear.append(result)

        reward = stringContent[3].replace("[", "").replace("]", "").split(", ")
        reward = [float(x) for x in reward]

        if (oldSight != sight or oldHear != hear or oldReward != reward):
            oldSight, oldHear, oldReward = sight, hear, reward
            #print(sight)
            #print(hear)
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
    