#!/usr/bin/python3

from which_pyqt import PYQT_VER
if PYQT_VER == 'PYQT5':
	from PyQt5.QtCore import QLineF, QPointF
elif PYQT_VER == 'PYQT4':
	from PyQt4.QtCore import QLineF, QPointF
else:
	raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))


import time
import numpy as np
from TSPClasses import *
from heapq import *
import itertools
import copy


class SearchState:
	def __init__(self, key, lowbound, level, redcostmat, path, city): #Initializes a state for the state tree.
		self.key = key
		self.lowbound = lowbound
		self.level = level
		self.redcostmat = redcostmat
		self.path = path
		self.city = city

	def __gt__(self, other): #Defines the priority sorting rules for the heap queue.
		if self.key > other.key:
			return True
		elif self.key < other.key:
			return False
		elif self.level >= other.level:
			return True
		else:
			return False

	def getPathLength(self): #Returns depth of position in state tree.
		return len(self.path)


class TSPSolver:
	def __init__( self, gui_view ):
		self._scenario = None
		self._bssf = None

	def setupWithScenario( self, scenario ):
		self._scenario = scenario


	''' <summary>
		This is the entry point for the default solver
		which just finds a valid random tour.  Note this could be used to find your
		initial BSSF.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of solution, 
		time spent to find solution, number of permutations tried during search, the 
		solution found, and three null values for fields not used for this 
		algorithm</returns> 
	'''
	
	def defaultRandomTour( self, time_allowance=60.0 ):
		results = {}
		cities = self._scenario.getCities()
		ncities = len(cities)
		foundTour = False
		count = 0
		bssf = None
		start_time = time.time()
		while not foundTour and time.time()-start_time < time_allowance:
			# create a random permutation
			perm = np.random.permutation( ncities )
			route = []
			# Now build the route using the random permutation
			for i in range( ncities ):
				route.append( cities[ perm[i] ] )
			bssf = TSPSolution(route)
			count += 1
			if bssf.cost < np.inf:
				# Found a valid route
				foundTour = True
		end_time = time.time()
		results['cost'] = bssf.cost if foundTour else math.inf
		results['time'] = end_time - start_time
		results['count'] = count
		results['soln'] = bssf
		results['max'] = None
		results['total'] = None
		results['pruned'] = None
		return results


	''' <summary>
		This is the entry point for the greedy solver, which you must implement for 
		the group project (but it is probably a good idea to just do it for the branch-and
		bound project as a way to get your feet wet).  Note this could be used to find your
		initial BSSF.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution, 
		time spent to find best solution, total number of solutions found, the best
		solution found, and three null values for fields not used for this 
		algorithm</returns> 
	'''

	def greedy( self,time_allowance=60.0 ):
		pass
	

	''' <summary>
		This is the entry point for the branch-and-bound algorithm that you will implement
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution, 
		time spent to find best solution, total number solutions found during search (does
		not include the initial BSSF), the best solution found, and three more ints: 
		max queue size, total number of states created, and number of pruned states.</returns> 
	'''

	def lowerBound(self, lowbound, redcostmat): #Reduces reduced cost matrix and finds lower bound.
		for i in range (self.ncities):
			mini = float('inf')
			for j in range (self.ncities):
				if redcostmat[i][j] < mini:
					mini = redcostmat[i][j]
			if mini > 0 and mini != float('inf'):
				for j in range (self.ncities):
					if redcostmat[i][j] != float('inf'):
						redcostmat[i][j] -= mini
				lowbound += mini

		for i in range (self.ncities):
			mini = float('inf')
			for j in range (self.ncities):
				if redcostmat[j][i] < mini:
					mini = redcostmat[j][i]
			if mini > 0 and mini != float('inf'):
				for j in range (self.ncities):
					if redcostmat[j][i] != float('inf'):
						redcostmat[j][i] -= mini
				lowbound += mini

		return lowbound, redcostmat
		
	def branchAndBound( self, time_allowance=60.0 ): #Branch and bound algorithm.
		#Sets up variables.
		results = {}
		cities = self._scenario.getCities()
		self.ncities = len(cities)
		solutioncount = 0
		statecount = 0
		prunedcount = 0
		maxqueuesize = 1
		start_time = time.time()

		#Sets initial variables.
		self._bssf = self.defaultRandomTour().get("soln")
		lowbound = 0
		fredcostmat = [[float('inf') for i in range(self.ncities)] for j in range(self.ncities)]
		for i in range (self.ncities):
			for j in range (self.ncities):
				fredcostmat[i][j] = cities[i].costTo(cities[j])
		lowbound, fredcostmat = self.lowerBound(lowbound, fredcostmat)
		S = []  # Set of active subproblems.
		heappush(S, SearchState(lowbound, lowbound, 0, fredcostmat, [0], 0))
		statecount += 1

		#Searches for solutions until there are no more states or time runs out.
		while len(S) > 0 and time.time() - start_time < time_allowance:
			maxqueuesize = max(maxqueuesize, len(S))
			P = heappop(S)  # Chooses a subproblem and removes it.

			if P.lowbound > self._bssf.cost:
				prunedcount += 1
				continue

			#Expands to smaller subproblems and solves them.
			viableexpansion = False
			for i in range(self.ncities):
				lowbound = P.lowbound + P.redcostmat[P.city][i]
				if lowbound <= self._bssf.cost:
					#Creates new subproblem and defines it.
					viableexpansion = True
					nredcostmat = copy.deepcopy(P.redcostmat)
					for j in range(self.ncities):
						nredcostmat[P.city][j] = float('inf')
					for j in range(self.ncities):
						nredcostmat[j][i] = float('inf')
					nredcostmat[i][P.city] = float('inf')
					lowbound, nredcostmat = self.lowerBound(lowbound, nredcostmat)
					key = lowbound - (P.level+1)*self.ncities*self.ncities
					subproblem = SearchState(key, lowbound, P.level + 1, nredcostmat, P.path.copy()+[i], i)
					statecount += 1

					#Solves whether subproblem is a solution, a heap queue addition, or a fruitless limb.
					#Places subproblem in its proper place and updates variables.
					if subproblem.getPathLength() == self.ncities:
						solution = TSPSolution([cities[j] for j in subproblem.path])
						if solution.cost != float('inf') and solution.cost < self._bssf.cost:
							self._bssf = solution
							solutioncount += 1
						else:
							prunedcount += 1
					elif lowbound <= self._bssf.cost:
						heappush(S, subproblem)
					else:
						prunedcount += 1

			if viableexpansion == False:
				prunedcount += 1

		#Defines results and returns them to main algorithm.
		end_time = time.time()
		results['cost'] = self._bssf.cost
		results['time'] = end_time - start_time
		results['count'] = solutioncount
		results['soln'] = self._bssf
		results['max'] = maxqueuesize
		results['total'] = statecount
		results['pruned'] = prunedcount + len(S)
		return results


	''' <summary>
		This is the entry point for the algorithm you'll write for your group project.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution, 
		time spent to find best solution, total number of solutions found during search, the 
		best solution found.  You may use the other three field however you like.
		algorithm</returns> 
	'''
		
	def fancy( self,time_allowance=60.0 ):
		pass
