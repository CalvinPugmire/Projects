from stable_baselines3 import PPO
from EnvironmentGridworld import CustomGridWorldEnv

#Create your gym environment
env = CustomGridWorldEnv()

# Load the model (optional, for demonstration)
model = PPO.load("./Models/ppo_custom_env_model", env=env)

# Test the agent
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