import gymnasium as gym
from gymnasium.envs.registration import EnvSpec
from gymnasium import spaces
import numpy as np

from WorldModel import WorldModel

WRLD_SZ = 12
SPTL_SZ = 5 #Don't change unless you also change the Java's size?
INV_SZ = 32 #Don't change unless you also change the Java's size.
RWD_SZ = 2 #Don't change unless you also change the Java's size?



class Environment(gym.Env):
    metadata = {'render.modes': ['human']}

    worldSize = WRLD_SZ

    oldData = None

    observation = {
        "world": np.zeros((2, WRLD_SZ, WRLD_SZ, WRLD_SZ)), 
        "spatial": np.zeros((SPTL_SZ)), 
        "inventory": np.zeros((INV_SZ)), 
        "reward": np.zeros((RWD_SZ))
    }

    oldReward = [20.0, 0.0]

    def __init__(self, env_config={}):
        super(Environment, self).__init__()

        # EnvSpec is used for RLlib, if max_episode_steps is not defined it will throw more errors
        self.spec = EnvSpec
        self.spec.max_episode_steps = 5000
        self.spec.id = "Cube-v0"

        # Actions:
        self.actions = ["none",
                        "forward", "backward", "left", "right",
                        "up", "down", "left", "right",
                        "jump", "forward+jump",
                        "hit", "use",
                        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]

        # State:
        self.worldModel = WorldModel()

        # Action space:
        self.action_space = spaces.Discrete(len(self.actions))

        # State space:
        self.observation_space = spaces.Dict({
            "world": spaces.Box(low=-np.inf, high=np.inf, shape=(2, WRLD_SZ, WRLD_SZ, WRLD_SZ)), 
            "spatial": spaces.Box(low=-np.inf, high=np.inf, shape=(SPTL_SZ,)), 
            "inventory": spaces.Box(low=-np.inf, high=np.inf, shape=(INV_SZ,)), 
            "reward": spaces.Box(low=0.0, high=20.0, shape=(RWD_SZ,)),
            "achieved_goal": spaces.Box(low=0.0, high=20.0, shape=(RWD_SZ*2,)),
            "desired_goal": spaces.Box(low=0.0, high=20.0, shape=(RWD_SZ*2,))
        })

        # Initialize state
        self.reset()
    
    def reset(self, *,seed=None, options=None):
        # Reset the state of the environment to an initial state
        # Return the initial state
        self.time = 0
        state = self.get_observation()
        return state, {}

    def step(self, action):
        # Execute one time step within the environment
        self.time+=1

        if self.time%100 == 0:
            print("env: ", self.time)
            pass

        # Update self.state
        self.apply_action(action)

        state = self.get_observation()

        reward = self.compute_reward(state["achieved_goal"], state["desired_goal"], {})[0]

        terminated = self.compute_terminated(state["achieved_goal"], state["desired_goal"], {})

        truncated = self.compute_truncated(state["achieved_goal"], state["desired_goal"], {})
        
        # Optional
        info = {}

        return state, reward, terminated, truncated, info
    
    def apply_action(self, action):
        if action == 0:
            data = "none none none none none"
        elif 1 <= action <= 4:
            data = f"{self.actions[action]} none none none none"
        elif 5 <= action <= 8:
            data = f"none {self.actions[action]} none none none"
        elif action == 9:
            data = "none none jump none none"
        elif action == 10:
            data = "forward none jump none none"
        elif 11 <= action <= 12:
            data = f"none none none {self.actions[action]} none"
        elif 13 <= action <= 22:
            data = f"none none none none {self.actions[action]}"

        if (data != self.oldData):
            self.oldData = data
            with open("../src/main/java/net/calvinpugmire/agiagentmod/entity/custom/AgentOutput.txt", "w") as f:
                f.write(data)
        return
    
    def get_observation(self):
        with open("../src/main/java/net/calvinpugmire/agiagentmod/entity/custom/AgentInput.txt", "r") as f:
            content = f.read()
        stringContent = content.split(",,,")
        if (len(stringContent) == 8):
            newObservation = self.worldModel.update(stringContent)
            self.observation = {
                "world": newObservation["world"], 
                "spatial": newObservation["spatial"], 
                "inventory": newObservation["inventory"], 
                "reward": newObservation["reward"]
            }
            
        observation = {
            "world": self.observation["world"], 
            "spatial": self.observation["spatial"], 
            "inventory": self.observation["inventory"], 
            "reward": self.observation["reward"],
            "achieved_goal": [self.oldReward[0], self.observation["reward"][0], self.oldReward[1], self.observation["reward"][1]],
            "desired_goal": [20.0, 20.0, 0.0, 0.0]
        }
        return observation
    
    def compute_reward(self, achieved_goal, desired_goal, info):
        achievedGoal = achieved_goal

        if isinstance(achievedGoal[0], float):
            achievedGoal = [achievedGoal]

        reward = np.zeros((len(achievedGoal)))

        for i in range(len(achievedGoal)):
            if achievedGoal[i][1] > 0.0 and achievedGoal[i][0] == 0.0:
                change = 0.0
            else:
                change = (achievedGoal[i][1] - achievedGoal[i][0]) + (achievedGoal[i][3] - achievedGoal[i][2])

            if achievedGoal[i][1] == 0.0 and achievedGoal[i][0] > 0.0:
                change -= 5
            elif achievedGoal[i][1] > 0.0:
                change += 0.01

            reward[i] = change

        if any(abs(i) > 0.1 for i in reward):
            print(achievedGoal)
            print(reward)
            pass

        return reward
    
    def compute_terminated(self, achieved_goal, desired_goal, info):
        newReward = self.observation["reward"]
        if newReward[0] > 0.0 and self.oldReward[0] == 0.0:
            self.oldReward = newReward
            return True
        else:
            self.oldReward = newReward
            return False
    
    def compute_truncated(self, achieved_goal, desired_goal, info):
        if self.time >= self.spec.max_episode_steps:
            return True
        else:
            return False

    def render(self, render_mode='human'):
        pass
    