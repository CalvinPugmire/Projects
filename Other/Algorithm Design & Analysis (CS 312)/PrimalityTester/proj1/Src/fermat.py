import random


def prime_test(N, k):
	# This is main function, that is connected to the Test button. You don't need to touch it.
	return fermat(N,k), miller_rabin(N,k)


def mod_exp(x, y, N):
    # Calculates whether given iteration of Fermat/Miller-Rabin test process
    # will indicate submitted number N as being prime or composite using
    # modular exponentiation with base x, exponent y, and mod value N.
    # Time complexity: O(n^3).
    # Space complexity: O(n^2).
    if (y == 0):
        return 1
    z = mod_exp(x, int(y / 2), N)
    if (y % 2 == 0):
        return (z ** 2) % N
    else:
        return (x * (z ** 2)) % N


def fprobability(k):
    # Calculates chance of Fermat test process being correct when using
    # k iterations.
    # Time complexity: O(k^2).
    # Space complexity: O(log2(k)).
    return 1 - 1 / (2 ** k)


def mprobability(k):
    # Calculates chance of Miller-Rabin test process being correct when
    # using k iterations.
    # Time complexity: O(k^2).
    # Space complexity: O(log2(k)).
    return 1 - 1 / (4 ** k)


def fermat(N, k):
    # Calculates whether submitted number N is prime using k iterations
    # through the Fermat test process.
    # Time complexity: O(k*n^3).
    # Space complexity: O(n^2).
    if (N == 0 or N == 1):
        return 'prime'
    for i in range(k):
        a = random.randint(1, N - 1)
        if (mod_exp(a, N - 1, N) != 1 % N):
            return 'composite'
    return 'prime'


def miller_rabin(N, k):
    # Calculates whether submitted number N is prime using k iterations
    # through the Miller-Rabin test process.
    # Time complexity: O(k*n^4).
    # Space complexity: O(n^2).
    if (N == 0 or N == 1):
        return 'prime'
    for i in range(k):
        a = random.randint(1, N - 1)
        evenexpo = 1
        y = N - 1;
        while (evenexpo):
            m = mod_exp(a, y, N)
            if (m == (-1) % N):
                break
            if (m != 1 % N and m != (-1) % N):
                return 'composite'
            if (y % 2 != 0):
                evenexpo = 0
            y = int(y / 2)
    return 'prime'


# I experimented with Carmichael numbers and low k values. I found that the two algorithms sometimes disagree
# when one or both are used. This is likely due to the fact that Carmichael numbers can pass through one or both
# tests when the number of iterations used is low and the chances of that happening are higher for the Fermat test.