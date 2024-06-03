from stable_baselines3 import DQN

from Environment import Environment



# Create the environment
env = Environment()

# Load the agent with the custom policy
model = DQN.load("./Checkpoints/MinecraftDQN", env=env)

# Test the agent
state, info = env.reset()
length = 0
for _ in range(10000):
    action, _states = model.predict(state, deterministic=True)
    state, rewards, terminated, truncated, info = env.step(action)
    length += 1
    if terminated or truncated:
      print(f"Episode Length: {length}")
      state, info = env.reset()
      length = 0

env.close()