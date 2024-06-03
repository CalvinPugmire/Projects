#!/usr/bin/python3

from which_pyqt import PYQT_VER
if PYQT_VER == 'PYQT5':
	from PyQt5.QtCore import QLineF, QPointF
elif PYQT_VER == 'PYQT4':
	from PyQt4.QtCore import QLineF, QPointF
else:
	raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))

import math
import time
import random

# Used to compute the bandwidth for banded version
MAXINDELS = 3

# Used to implement Needleman-Wunsch scoring
MATCH = -3
INDEL = 5
SUB = 1

class GeneSequencing:

	def __init__( self ):
		self.banded = None
	
# This is the method called by the GUI.  _seq1_ and _seq2_ are two sequences to be aligned,
# _banded_ is a boolean that tells you whether you should compute a banded alignment or full alignment, and
# _align_length_ tells you how many base pairs to use in computing the alignment

	def align( self, seq1, seq2, banded, align_length):
		# Pre-algorithm setup.

		self.banded = banded
		self.MaxCharactersToAlign = align_length

		seq1 = ' ' + seq1
		seq2 = ' ' + seq2

		if (banded == False): #Unrestricted alignment algorithm: Time and space complexity of O(nm).
			#Cost and backtrace 2D array setup.

			width = min(align_length + 1, len(seq1))
			height = min(align_length + 1, len(seq2))

			E = [[float('inf') for j in range(width)] for i in range(height)]
			P = [[None for j in range(width)] for i in range(height)]

			#Alignment #1+#2 creation.

			E[0][0] = 0
			P[0][0] = 'b'
			for i in range (1, width):
				E[0][i] = E[0][i-1] + INDEL
				P[0][i] = 'l'
			for j in range(1, height):
				E[j][0] = E[j - 1][0] + INDEL
				P[j][0] = 'u'

			for j in range (1, height):
				for i in range (1, width):
					l = E[j][i-1] + INDEL
					mini = l
					ins = 'l'

					u = E[j-1][i] + INDEL
					if (u < mini):
						mini = u
						ins = 'u'

					if (seq1[i] != seq2[j]):
						d = E[j - 1][i - 1] + SUB
					else:
						d = E[j - 1][i - 1] + MATCH
					if (d < mini):
						mini = d
						ins = 'd'

					E[j][i] = mini
					P[j][i] = ins

			#Score and alignment #1+#2 extraction.

			cost = 0
			i = min(width-1, align_length)
			j = min(height-1, align_length)
			newseq1 = ""
			newseq2 = ""

			while (i > 0 or j > 0):
				if (i > 0 and P[j][i] == 'l'):
					cost += 5
					newseq1 = newseq1 + seq1[i]
					newseq2 = newseq2 + '-'
					i -= 1
				elif (j > 0 and P[j][i] == 'u'):
					cost += 5
					newseq1 = newseq1 + '-'
					newseq2 = newseq2 + seq2[j]
					j -= 1
				elif (i > 0 and j > 0 and P[j][i] == 'd'):
					if (seq1[i] == seq2[j]):
						cost -= 3
					else:
						cost += 1
					newseq1 = newseq1 + seq1[i]
					newseq2 = newseq2 + seq2[j]
					i -= 1
					j -= 1

			score = cost
			alignment1 = newseq1[::-1].format(width, align_length, ',BANDED' if banded else '')
			alignment2 = newseq2[::-1].format(height, align_length, ',BANDED' if banded else '')

		else: #Banded alignment algorithm: Time and space complexity of O(kn).
			#Cost and backtrace 2D array setup.

			if (len(seq1) > len(seq2)):
				width = min(align_length + 1, len(seq1))
				height = min(align_length + 1, len(seq2))
				x = True
			else:
				height = min(align_length + 1, len(seq1))
				width = min(align_length + 1, len(seq2))
				x = False

			if (abs(width - height) > 100):
				score = float('inf')
				alignment1 = "No Alignment Possible"
				alignment2 = "No Alignment Possible"
				return {'align_cost': score, 'seqi_first100': alignment1, 'seqj_first100': alignment2}

			wid = MAXINDELS*2 + 1
			E = [[float('inf') for i in range(wid)] for j in range(height)]
			P = [[None for i in range(wid)] for j in range(height)]

			#Alignment #1+#2 creation.

			E[0][3] = 0
			P[0][3] = 'b'
			for i in range(4, 7):
				E[0][i] = E[0][i - 1] + INDEL
				P[0][i] = 'l'

			for j in range(1, height):
				for i in range(j-MAXINDELS, j+MAXINDELS+1):
					if (i >= 0 and i < width):
						k = i + MAXINDELS - j

						mini = float('inf')

						if (k > 0):
							l = E[j][k - 1] + INDEL
							mini = l
							ins = 'l'

						if (k < 6):
							u = E[j - 1][k + 1] + INDEL
							if (u < mini):
								mini = u
								ins = 'u'

						if (x):
							a = i
							b = j
						else:
							a = j
							b = i

						if (seq1[a] != seq2[b]):
							d = E[j - 1][k] + SUB
						else:
							d = E[j - 1][k] + MATCH
						if (d < mini):
							mini = d
							ins = 'd'

						E[j][k] = mini
						P[j][k] = ins

			#Score and alignment #1+#2 extraction.

			cost = 0
			i = min(width - 1, align_length)
			j = min(height - 1, align_length)
			newseq1 = ""
			newseq2 = ""

			while (i > 0 or j > 0):
				k = i + MAXINDELS - j

				if (x):
					a = i
					b = j
				else:
					a = j
					b = i

				if (i > 0 and P[j][k] == 'l'):
					cost += 5
					if (x):
						newseq1 = newseq1 + seq1[a]
						newseq2 = newseq2 + '-'
					else:
						newseq1 = newseq1 + '-'
						newseq2 = newseq2 + seq2[b]
					i -= 1
				elif (j > 0 and P[j][k] == 'u'):
					cost += 5
					if (x):
						newseq1 = newseq1 + '-'
						newseq2 = newseq2 + seq2[b]
					else:
						newseq1 = newseq1 + seq1[a]
						newseq2 = newseq2 + '-'
					j -= 1
				elif (i > 0 and j > 0 and P[j][k] == 'd'):
					if (seq1[a] == seq2[b]):
						cost -= 3
					else:
						cost += 1
					newseq1 = newseq1 + seq1[a]
					newseq2 = newseq2 + seq2[b]
					i -= 1
					j -= 1

			score = cost
			alignment1 = newseq1[::-1].format(width, align_length, ',BANDED' if banded else '')
			alignment2 = newseq2[::-1].format(height, align_length, ',BANDED' if banded else '')

		return {'align_cost':score, 'seqi_first100':alignment1, 'seqj_first100':alignment2}
