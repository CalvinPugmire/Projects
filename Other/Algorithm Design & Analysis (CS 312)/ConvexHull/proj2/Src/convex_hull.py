from which_pyqt import PYQT_VER
if PYQT_VER == 'PYQT5':
	from PyQt5.QtCore import QLineF, QPointF, QObject
elif PYQT_VER == 'PYQT4':
	from PyQt4.QtCore import QLineF, QPointF, QObject
else:
	raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))



import time

# Some global color constants that might be useful
RED = (255,0,0)
GREEN = (0,255,0)
BLUE = (0,0,255)

# Global variable that controls the speed of the recursion automation, in seconds
#
PAUSE = 0.25

#
# This is the class you have to complete.
#
class ConvexHullSolver(QObject):

# Class constructor
	def __init__( self):
		super().__init__()
		self.pause = False
		
# Some helper methods that make calls to the GUI, allowing us to send updates
# to be displayed.

	def showTangent(self, line, color):
		self.view.addLines(line,color)
		if self.pause:
			time.sleep(PAUSE)

	def eraseTangent(self, line):
		self.view.clearLines(line)

	def blinkTangent(self,line,color):
		self.showTangent(line,color)
		self.eraseTangent(line)

	def showHull(self, polygon, color):
		self.view.addLines(polygon,color)
		if self.pause:
			time.sleep(PAUSE)
		
	def eraseHull(self,polygon):
		self.view.clearLines(polygon)
		
	def showText(self,text):
		self.view.displayStatusText(text)

	def convexHull(self, points):
		#Runs recursive divide and conquer algorithm on points to obtain convex hull points.
		#Uses convex hull points to form a list of lines for said convex hull.
		#Returns convex hull in the form of its outer lines.
		#Time complexity: O(nlog2(n)).
		#Space complexity: O(n).

		points, l, r = self.convexRecurse(points)

		lines = list()
		for i in range(len(points)-1):
			lines.append(QLineF(points[i],points[i+1]))
		lines.append(QLineF(points[len(points)-1],points[0]))

		return lines

	def convexRecurse(self, points):
		#Returns if points has size of 1.
		#Splits points into left and right halves and recurses with both of them to get their convex hulls.
		#Calculates common upper and lower tangents with helper methods.
		#Constructs new convex hull using subhulls and tangents with helper method.
		#Returns new convex hull in the form of ordered points.
		#Time complexity: O(nlog2(n)).
		#Space complexity: O(n).

		if len(points)==1:
			return points, 0, 0

		left, lm, rightmost = self.convexRecurse(points[:int(len(points)/2)])
		right, leftmost, rm = self.convexRecurse(points[int(len(points)/2):])

		upperleft, upperright = self.upperConn(left, right, leftmost, rightmost)
		lowerleft, lowerright = self.lowerConn(left, right, leftmost, rightmost)

		newpoints, newleftmost, newrightmost = self.formHull(left, right, upperright, lowerright, lowerleft, upperleft)
		return newpoints, newleftmost, newrightmost

	def upperConn(self, left, right, leftmost, rightmost):
		#Starts with left subhull's rightmost point and right subhull's leftmost point as upper tangent.
		#Iterates clockwise on right subhull until highest tangent slope is found.
		#Iterates counterclockwise on left subhull until lowest tangent slope is found.
		#Repeats steps 2 and 3 until no changes are being made.
		#Returns common upper tangent in the form of two points.
		#Time complexity: O(n).
		#Space complexity: O(n).

		upperleft = rightmost
		leftstart = rightmost
		upperright = leftmost
		rightstart = leftmost
		upperslope = (right[upperright].y() - left[upperleft].y()) / (right[upperright].x() - left[upperleft].x())
		anychange = 1
		while anychange == 1:
			anychange = 0
			if len(right) > 1:
				i = 0
				rightindex = upperright
				rightchange = 1
				while i < len(right) and rightchange == 1:
					newindex = (rightstart + i) % len(right)
					newslope = (right[newindex].y() - left[upperleft].y()) / (right[newindex].x() - left[upperleft].x())
					if newslope >= upperslope:
						if rightindex != newindex:
							upperslope = newslope
							upperright = newindex
							anychange = 1
						rightindex = newindex
						i = i + 1
					else:
						rightstart = newindex
						rightchange = 0
			if len(left) > 1:
				j = 0
				leftindex = upperleft
				leftchange = 1
				while j < len(left) and leftchange == 1:
					newindex = (leftstart - j) % len(left)
					newslope = (right[upperright].y() - left[newindex].y()) / (right[upperright].x() - left[newindex].x())
					if newslope <= upperslope:
						if leftindex != newindex:
							upperslope = newslope
							upperleft = newindex
							anychange = 1
						leftindex = newindex
						j = j + 1
					else:
						leftstart = newindex
						leftchange = 0
		return upperleft, upperright

	def lowerConn(self, left, right, leftmost, rightmost):
		#Starts with left subhull's rightmost point and right subhull's leftmost point as lower tangent.
		#Iterates counterclockwise on right subhull until lowest tangent slope is found.
		#Iterates clockwise on left subhull until highest tangent slope is found.
		#Repeats steps 2 and 3 until no changes are being made.
		#Returns common lower tangent in the form of two points.
		#Time complexity: O(n).
		#Space complexity: O(n).

		lowerleft = rightmost
		leftstart = rightmost
		lowerright = leftmost
		rightstart = leftmost
		lowerslope = (right[lowerright].y() - left[lowerleft].y()) / (right[lowerright].x() - left[lowerleft].x())
		anychange = 1
		while anychange == 1:
			anychange = 0
			if len(right) > 1:
				i = 0
				rightindex = lowerright
				rightchange = 1
				while i < len(right) and rightchange == 1:
					newindex = (rightstart - i) % len(right)
					newslope = (right[newindex].y() - left[lowerleft].y()) / (right[newindex].x() - left[lowerleft].x())
					if newslope <= lowerslope:
						if rightindex != newindex:
							lowerslope = newslope
							lowerright = newindex
							anychange = 1
						rightindex = newindex
						i = i + 1
					else:
						rightstart = newindex
						rightchange = 0
			if len(left) > 1:
				j = 0
				leftindex = lowerleft
				leftchange = 1
				while j < len(left) and leftchange == 1:
					newindex = (leftstart + j) % len(left)
					newslope = (right[lowerright].y() - left[newindex].y()) / (right[lowerright].x() - left[newindex].x())
					if newslope >= lowerslope:
						if leftindex != newindex:
							lowerslope = newslope
							lowerleft = newindex
							anychange = 1
						leftindex = newindex
						j = j + 1
					else:
						leftstart = newindex
						leftchange = 0
		return lowerleft, lowerright

	def formHull(self, left, right, upperright, lowerright, lowerleft, upperleft):
		#Creates new convex hull in the form of an empty list of points.
		#Adds all of right subhull's points (from upper tangent point to lower tangent point) to hull list.
		#Adds all of left subhull's points (from lower tangent point to upper tangent point) to hull list.
		#Calculates new leftmost and rightmost points by iterating through the new convex hull.
		#Returns new convex hull as a list of points and also returns the new leftmost and rightmost points.
		#Time complexity: O(n).
		#Space complexity: O(n).

		newpoints = list()

		endi = 0
		i = 0
		while endi == 0:
			index = (upperright + i) % len(right)
			newpoints.append(right[index])
			if (index == lowerright):
				endi = 1
			i = i+1

		endj = 0
		j = 0
		while endj == 0:
			index = (lowerleft + j) % len(left)
			newpoints.append(left[index])
			if (index == upperleft):
				endj = 1
			j = j+1

		newleftmost = 0
		newrightmost = 0
		for k in range(len(newpoints)):
			if newpoints[k].x() > newpoints[newrightmost].x():
				newrightmost = k
			if newpoints[k].x() < newpoints[newleftmost].x():
				newleftmost = k

		return newpoints, newleftmost, newrightmost

# This is the method that gets called by the GUI and actually executes
# the finding of the hull
	def compute_hull( self, points, pause, view):
		self.pause = pause
		self.view = view
		assert( type(points) == list and type(points[0]) == QPointF )

		t1 = time.time()
		points.sort(key=QPointF.x)
		t2 = time.time()

		t3 = time.time()
		polygon = self.convexHull(points)
		t4 = time.time()

		# when passing lines to the display, pass a list of QLineF objects.  Each QLineF
		# object can be created with two QPointF objects corresponding to the endpoints
		self.showHull(polygon,RED)
		self.showText('Time Elapsed (Convex Hull): {:3.3f} sec'.format(t4-t3))