import math
import torch
import torch.nn as nn
from stable_baselines3.common.torch_layers import BaseFeaturesExtractor



class FeatureExtractor(BaseFeaturesExtractor):
    def __init__(self, obs_space, world_width, num_out_nodes):
        super(FeatureExtractor, self).__init__(obs_space, num_out_nodes)

        self.time = 0

        # Defining the convolutional path
        layer_i = 2
        layer_1 = layer_i*16
        layer_2 = layer_1*2
        layer_3 = layer_2*2
        layer_c = layer_3 * math.floor(world_width/2/2)**3
        layer_f = 512
        self.conv_net = nn.Sequential(
            nn.Conv3d(layer_i, layer_1, kernel_size=3, padding=1),
            nn.ReLU(inplace=True),
            nn.MaxPool3d(kernel_size=2, stride=2),
            nn.Conv3d(layer_1, layer_2, kernel_size=3, padding=1),
            nn.ReLU(inplace=True),
            nn.MaxPool3d(kernel_size=2, stride=2),
            nn.Conv3d(layer_2, layer_3, kernel_size=3, padding=1),
            nn.ReLU(inplace=True)
        )
        self.shrtn_layer = nn.Linear(layer_c, layer_f)
        conv_out_nodes = layer_f
        
        # Defining the linear path
        self.lin_net = nn.Sequential(
            nn.Linear(39, 128),
            nn.LeakyReLU(),
            nn.Linear(128, 128),
            nn.LeakyReLU(),
            nn.Linear(128, 128),
            nn.LeakyReLU()
        )
        lin_out_nodes = 128

        # Final layer after concatenation
        self.final_layer = nn.Linear(conv_out_nodes + lin_out_nodes, num_out_nodes)

    def forward(self, observations):
        self.time += 1

        if self.time%100 == 0:
            print("model: ", self.time)
            pass

        # Splitting the input into two parts
        obs_world = observations["world"]
        obs_other = torch.cat((observations["spatial"], observations["inventory"], observations["reward"]), axis=1)

        if self.time%10000 == 0:
            torch.set_printoptions(profile="full")
            torch.set_printoptions(precision=10)
            print(obs_world.shape)
            print(obs_world[0, 0, ...])
            print(obs_world[0, 1, ...])
            print(obs_other.shape)
            print(obs_other[0, ...])
            pass
        
        # Applying the networks
        conv_output = self.conv_net(obs_world)
        conv_output = conv_output.view(conv_output.size(0), -1)
        conv_output = self.shrtn_layer(conv_output)
        lin_output = self.lin_net(obs_other)

        # Combining the outputs
        combined = torch.cat([conv_output, lin_output], dim=1)
        
        # Final projection
        features = self.final_layer(combined)
        return features