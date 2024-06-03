import ray
from ray.rllib.algorithms.ppo import PPOConfig
from ray import tune
from EnvironmentGridworld import CustomGridWorldEnv

'''This file demonstrates how to test a trained model meant to function on the custom gridworld environment.
     Note that this is non-trivial, and this process could be improved'''

# Initialize Ray
ray.init()

tune.register_env("Grid-v0", CustomGridWorldEnv)

agent = (
    PPOConfig()
    .rollouts(num_rollout_workers=0)
    .resources(num_gpus=1)
    .environment(env='Grid-v0')
    .build()
)


# Path to the saved checkpoint (replace with your checkpoint path)
checkpoint_path = "./Checkpoints/GridworldModel"

# Load the trained model from checkpoint
agent.restore(checkpoint_path)

#initialize an environment
env = CustomGridWorldEnv()

# Run the model on the environment
done = False
keep_going = "y"
while keep_going == "y":
    obs, _ = env.reset()
    while not done:
        action = agent.compute_single_action(obs)
        obs, reward, done, truncated, info = env.step(action)
        env.render()
    keep_going = input("do another run? [y,n]")
