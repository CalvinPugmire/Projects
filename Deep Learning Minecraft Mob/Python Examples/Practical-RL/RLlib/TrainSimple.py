from ray.rllib.algorithms.ppo import PPOConfig


'''This file demonstrates the most simple usage of RLlib for training on a default environment. 
    See [Train.py] for additional functions that are useful for the training process.'''

algo = (
    PPOConfig()
    .rollouts(num_rollout_workers=1)
    .resources(num_gpus=0)
    .environment(env='CartPole-v1')
    .build()
)

for i in range(1, 21):
    result = algo.train()
    if i % 20 == 0:
        checkpoint_dir = algo.save().checkpoint.path
