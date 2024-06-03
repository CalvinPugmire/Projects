import gym
from gym import spaces
import numpy as np


class CustomEnv(gym.Env):
    metadata = {'render.modes': ['human']}
    def __init__(self):
        super(CustomEnv, self).__init__()
        # Define action and observation space
        # They must be gym.spaces objects. There are a variety of spaces for different environment structures. 
        # Example when using discrete actions: action is one int of value 0 to N. Good if one action can be taken at a time
        N = 4
        self.action_space = spaces.Discrete(N)

        # Example for using image as input (observation space):
        # Note that "Box" is used for continous spaces
        HEIGHT, WIDTH, CHANNELS = 36, 36, 1
        self.observation_space = spaces.Box(low=0, high=255, shape=(HEIGHT, WIDTH, CHANNELS), dtype=np.uint8)

        # Initialize state or observation
        self.state = None


    def step(self, action):
        """
        Apply the action to the environment and step the environment forward.
        - action: the action to be executed in the environment
        Returns:
        - observation (Np Array): agent's observation of the current environment
        - reward (float) : amount of reward returned after previous action
        - done (bool): whether the episode has ended, in which case further step() calls will return undefined results
        - info (dict): contains auxiliary diagnostic information (helpful for debugging, and sometimes learning)
        """
        # Execute one timestep in the environment, retrieving/calculating the following info:


        observation = np.zeros(self.observation_space.shape) #state/observation
        reward = 0.0 #reward generated during that step
        done = False # Ended due to catastrophic failure, achieving goal, etc
        truncated = False # Ended due to time limit
        info = {} #any additional info that is needed 


        return observation, reward, done, truncated, info
    

    def reset(self):
        """
        Reset the state of the environment to an initial state and returns an initial observation.
        Returns:
        - observation (object): the initial observation.
        """
        # Reset the state of the environment to an initial state
        # Return the initial observation
        
        observation = np.zeros(self.observation_space.shape)
        return observation
    

    def render(self, mode='human', close=False):
        """
        Render the environment.
        The set of supported modes varies per environment. (And some environments do not support rendering at all.)
        """
        # Implement rendering logic here. This is where the display is updated
        pass


    def close(self):
        """
        Perform any necessary cleanup.
        """
        # Close and clean up resources. Not necessary for many environments
        pass
