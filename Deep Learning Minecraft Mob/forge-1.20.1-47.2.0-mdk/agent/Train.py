import torch
from stable_baselines3 import DQN, HerReplayBuffer

from Environment import Environment
from Model import FeatureExtractor



# Create the environment
env = Environment()

# Create the policy with the custom network
policy_kwargs = dict(
    net_arch = [64, 64], 
    features_extractor_class=FeatureExtractor, 
    features_extractor_kwargs=dict(world_width=env.worldSize, num_out_nodes=64)
)

# Instantiate the agent with the custom policy
model = DQN(
    policy="MultiInputPolicy", 
    env=env, 
    device=torch.device("cuda" if torch.cuda.is_available() else "cpu"),
    learning_rate=0.001, 
    buffer_size=1000000, 
    learning_starts=10000, 
    batch_size=32, 
    replay_buffer_class=HerReplayBuffer, 
    exploration_initial_eps=1.0, 
    exploration_final_eps=0.1, 
    policy_kwargs=policy_kwargs, 
    verbose=1)

# Train the agent
model.learn(total_timesteps=10000000)

model.save("./Checkpoints/MinecraftDQN")
env.close()