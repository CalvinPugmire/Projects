from ray.rllib.algorithms.ppo import PPOConfig
import matplotlib.pyplot as plt
from ray.rllib.models import ModelCatalog

from CustomModel import CustomModel

'''This file demonstrates how to train a custom model. Additional functionality present in [Train.py] is also used
    in order to better show the training process and its results. When making custom models, I would recommend
    comparing their effectiveness to the default models in order to ensure that they actually provide better results.
    Note that a microscopic network is needed to properly learn for simple problems like cartpole. Anything too long 
    will simply not learn well.'''

# A simple function for data visualization
def plot(name, x_lab, y_lab, data):
    plt.figure(figsize=(10, 6))
    plt.plot(data)
    plt.xlabel(x_lab)
    plt.ylabel(y_lab)
    plt.title(name)
    plt.show()

#Register your custom model
ModelCatalog.register_custom_model("my_model", CustomModel)

# Initialization of the algorithm. Workers are instances of your environment, lots of other variables you can set here.
# the model dictionary passed to "training()" contains the **kwargs that the model will use. Note how tiny the network
# used here is (policy only has 1 hidden layer). Anything too large will struggle tremendously to learn.
algo = (
        PPOConfig()
        .environment(env='CartPole-v1')
        .resources(num_gpus=0)
        .training(#lr=0.001, grad_clip=30.0, 
                  model={
                        "custom_model": "my_model",
                        "custom_model_config": {
                            "obs_size": 4,
                            "action_size": 2,
                            "num_layers": 1,
                            "hidden_dim": 4
                            },
                        }
        )
        .build()
        )

# I would recommend using a custom directory, otherwise the model will be difficult to track down
custom_checkpoint_dir = "./Checkpoints/CustomModel"

# A few stats that are useful for determining how the training is going. use result.keys() to see other options
print_guide = ["episodes_this_iter", "episode_reward_mean", "episode_reward_max", "episode_reward_min",
               "policy_reward_mean", "done",
               "num_env_steps_sampled_this_iter", ]

#nifty if you want to keep track of how training is going
mean_rewards = []

print("BEGINNING TRAINING")
for i in range(1, 41):
    result = algo.train()
    print(f"Iteration: {i}")
    for key in print_guide:
        print(f'{key}: {result[key]}')

    mean_rewards.append(result["episode_reward_mean"])

    if i % 20 == 0:
        checkpoint_dir = algo.save(custom_checkpoint_dir).checkpoint.path
        print(f"Checkpoint saved in directory {checkpoint_dir}")
    print("\n")

plot("Mean Rewards", "Training Episode", "Mean Rewards", mean_rewards)


