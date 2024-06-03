import torch

print(torch.version.cuda)

if torch.cuda.is_available():
  print("CUDA is available!")
else:
  print("CUDA is not available.")

  
device=torch.device("cuda" if torch.cuda.is_available() else "cpu")
print(device)