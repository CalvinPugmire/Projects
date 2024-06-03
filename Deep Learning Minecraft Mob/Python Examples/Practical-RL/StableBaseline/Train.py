from stable_baselines3 import PPO
from EnvironmentGridworld import CustomGridWorldEnv

#Create your gym environment
env = CustomGridWorldEnv()

# Initialize the agent (a Multi-Layer Perceptron in this case)
# You can customize the policy architecture by defining a new policy network
model = PPO('MlpPolicy', env, verbose=1)

# Train the agent
model.learn(total_timesteps=500000)

# Save the model
model.save("./Models/ppo_custom_env_model")

