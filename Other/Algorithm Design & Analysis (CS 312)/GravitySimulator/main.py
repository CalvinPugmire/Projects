import random
import arcade
import numpy as np

SCREEN_WIDTH = 800
SCREEN_HEIGHT = 800
G = 6.6743
#S = 0.999


class Mover:
    def __init__(self, m, x, y):
        self.mass = m
        self.position = np.array([x, y])
        self.velocity = np.array([0, 0])
        self.acceleration = np.array([0, 0])

    def constrain(self, val, min_val, max_val):
        if val < min_val:
            return min_val
        if val > max_val:
            return max_val
        return val

    def MoverCalculateAttraction(self, m):
        force = self.position - m.position
        distance = np.linalg.norm(force)
        distance = self.constrain(distance, 5.0, 25.0)
        norm = np.linalg.norm(force)
        if norm != 0:
            force = force / norm
        strength = (G * self.mass * m.mass) / (distance * distance)
        force = force * strength
        return force

    def MoverApplyForce(self, force):
        f = (force / self.mass) #* S
        self.acceleration = (self.acceleration + f) #* S

    def MoverUpdate(self):
        self.velocity = (self.velocity + self.acceleration) #* S
        self.position = self.position + self.velocity
        self.acceleration = 0

    def MoverDisplay(self):
        arcade.draw_circle_filled(self.position[0], self.position[1], self.mass * 10, arcade.color.GRAY)


class MyGame(arcade.Window):
    def __init__(self, width, height):
        super().__init__(width, height)
        arcade.set_background_color(arcade.color.WHITE)
        self.movers = []

    def setup(self):
        for i in range(random.randrange(2, 10)):
            mover = Mover(random.randrange(1, 50) / 10, random.randrange(SCREEN_WIDTH), random.randrange(SCREEN_HEIGHT))
            self.movers.append(mover)

    def on_draw(self):
        arcade.start_render()
        for i in range(len(self.movers)):
            self.movers[i].MoverDisplay()

    def update(self, delta_time):
        for i in range(len(self.movers)):
            for j in range(len(self.movers)):
                if i != j:
                    force = self.movers[j].MoverCalculateAttraction(self.movers[i])
                    self.movers[i].MoverApplyForce(force)
        for i in range(len(self.movers)):
            self.movers[i].MoverUpdate()


def main():
    game = MyGame(SCREEN_WIDTH, SCREEN_HEIGHT)
    game.setup()
    arcade.run()


if __name__ == "__main__":
    main()
