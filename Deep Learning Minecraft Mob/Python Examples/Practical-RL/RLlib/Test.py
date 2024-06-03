import ray
from ray.rllib.algorithms.ppo import PPOConfig
from ray import tune
import gymnasium as gym #for creating an instance of cartpole

'''This file demonstrates how to test a trained model meant to function on a default environment.
     Note that this is non-trivial, and this process could be improved'''

# Initialize Ray
ray.init()

algo = (
    PPOConfig()
    .rollouts(num_rollout_workers=1)
    .resources(num_gpus=0)
    .environment(env='CartPole-v1')
    .build()
)


# Path to the saved checkpoint (replace with your checkpoint path)
checkpoint_path = "./Checkpoints/Model"

# Load the trained model from checkpoint
algo.restore(checkpoint_path)

#initialize an environment
env = gym.make('CartPole-v1')

# Run the model on the environment
done = False
keep_going = "y"
while keep_going == "y":
    obs, _ = env.reset()
    while not done:
        action = algo.compute_single_action(obs)
        obs, reward, done, truncated, info = env.step(action)
        env.render()
    keep_going = input("do another run? [y,n]")
