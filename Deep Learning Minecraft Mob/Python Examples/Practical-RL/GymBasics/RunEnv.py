import gymnasium as gym
from EnvironmentGridworld import CustomGridWorldEnv

env = gym.make('CartPole-v1', render_mode="human")
#env = CustomGridWorldEnv()

observation = env.reset()

for _ in range(1000):
    env.render() #must pass render mode or this will throw an error
    action = env.action_space.sample()  # take a random action
    observation, reward, done, truncated, info = env.step(action)
    if done or truncated:
        observation = env.reset()

env.close()
