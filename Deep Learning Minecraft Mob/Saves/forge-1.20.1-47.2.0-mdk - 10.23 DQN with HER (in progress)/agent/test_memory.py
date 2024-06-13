import psutil

def get_available_memory():
    mem = psutil.virtual_memory()
    return mem.available / (1024 ** 3)  # Convert bytes to GB

print(f"Available memory: {get_available_memory():.2f} GB")