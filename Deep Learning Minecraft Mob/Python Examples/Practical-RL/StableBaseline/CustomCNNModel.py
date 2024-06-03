import torch.nn as nn
import torch
from stable_baselines3.common.torch_layers import BaseFeaturesExtractor
from stable_baselines3 import PPO
from stable_baselines3.common.policies import ActorCriticPolicy
import gymnasium as gym

'''Basically [CustomModel.py], except using a significantly more complex feature extractor. Be warned that using this on a 
    simple/unsuitable environment will result in little to no learning'''

class CustomFeatureExtraction(BaseFeaturesExtractor):
    def __init__(self, observation_space, world_model_width, conv_layers, other_obs, lin_layers, hidden_dim=4, feature_dim=2):
        super(CustomFeatureExtraction, self).__init__(observation_space, feature_dim)

        self.world_model_width = world_model_width

        # Defining the convolutional path
        conv_modules = []
        current_channels = world_model_width  # Assuming initial channels equals world_model_width, adjust if different
        for _ in range(conv_layers):
            conv_modules.append(nn.Conv3d(current_channels, current_channels * 2, kernel_size=3, stride=1, padding=1))
            conv_modules.append(nn.ReLU())
            current_channels *= 2
        self.conv_net = nn.Sequential(*conv_modules)

        # Defining the linear path
        lin_modules = []
        current_dims = other_obs
        for _ in range(lin_layers):
            lin_modules.append(nn.Linear(current_dims, hidden_dim))
            lin_modules.append(nn.LeakyReLU())
            current_dims = hidden_dim
        self.lin_net = nn.Sequential(*lin_modules)

        # Final layer after concatenation
        self.final_layer = nn.Linear(current_channels * world_model_width ** 3 + hidden_dim, feature_dim)

    def forward(self, observations):
        # Splitting the input into two parts
        obs_3d = observations[:, :self.world_model_width**3].reshape(-1, self.world_model_width, self.world_model_width, self.world_model_width)  # Adjust the reshape parameters as per actual observation details
        obs_other = observations[:, self.world_model_width**3:]

        # Applying the networks
        conv_output = self.conv_net(obs_3d)
        lin_output = self.lin_net(obs_other)

        # Flattening the conv_output
        conv_output = conv_output.view(conv_output.size(0), -1)

        # Combining the outputs
        combined = torch.cat([conv_output, lin_output], dim=1)

        # Final projection
        features = self.final_layer(combined)
        return features

    

    
# Create the environment
env = gym.make('CartPole-v1')

# Create the policy with the custom network
policy_kwargs = dict(
    features_extractor_class=CustomFeatureExtraction,
    features_extractor_kwargs=dict(world_model_width=25, conv_layers=2, other_obs=8, lin_layers=3, hidden_dim=6, feature_dim=8),
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