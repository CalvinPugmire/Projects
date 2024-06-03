import torch.nn as nn
from stable_baselines3.common.torch_layers import BaseFeaturesExtractor
from stable_baselines3 import PPO
from stable_baselines3.common.policies import ActorCriticPolicy
import gymnasium as gym

'''A simple illustration of how to make a custom model for use with Stable Baseline. Note that for 
    PPO, there are several key parts of the network. On a macro scale, you have the actor and the critic.
    In this case, both of these draw from the same feature extraction, which is defined as 
    CustomFeatureExtraction. Note that the heads of both the actor and critic (feature -> action/value)
    are modified using the [net_arch] variable in [policy_kwargs]. You can edit the policy and critic
    more specifically by redifining the overarching model class used for PPO, but that is extremely tricky
    and significantly more time consuming. '''

class CustomFeatureExtraction(BaseFeaturesExtractor):
    def __init__(self, observation_space, hidden_dim= 4, feature_dim=2):
        super(CustomFeatureExtraction, self).__init__(observation_space, feature_dim)
        # Define your network structure here
        self.net = nn.Sequential(
            nn.Linear(observation_space.shape[0], hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, feature_dim),
        )

    def forward(self, observations):
        return self.net(observations)
    

    
# Create the environment
env = gym.make('CartPole-v1')

# Create the policy with the custom network
policy_kwargs = dict(
    features_extractor_class=CustomFeatureExtraction,
    features_extractor_kwargs=dict(hidden_dim=4, feature_dim=5),
    net_arch = dict(pi=[16], vf=[16, 16])
)

# Instantiate the agent with the custom policy
model = PPO(ActorCriticPolicy, env, verbose=1, policy_kwargs=policy_kwargs,
            )

# Train the agent
model.learn(total_timesteps=10000)

# Test the agent
env.close()
env = gym.make('CartPole-v1', render_mode="human") #new env, now in the render mode for humans
obs = env.reset()[0] #outputs obs and info, only need obs
length = 0
for _ in range(10000):
    action, _states = model.predict(obs, deterministic=True)
    obs, rewards, done, truncated, info = env.step(action)
    env.render()
    length += 1
    if done or truncated:
      print(f"Episode Length: {length}")
      obs = env.reset()[0]
      length = 0

model.save("./Models/ppo_custom_model")

env.close()