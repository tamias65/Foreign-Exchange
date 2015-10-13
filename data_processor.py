import csv
import time
import shutil

from cdecimal import getcontext, Decimal

class dataProcessing(object):

	def __init__(self):
		pass

	# Class to create the base matrix. Take a symbol and add it to a new file 
	# with appropriate headers and keys. 
	def createBaseData(self, symbol):
		listOfFilePaths = self.getAllFilePaths(symbol)
		data = self.addNewFeatures(symbol) 

		target = open(symbol+'.csv', 'wb')
		writer = csv.DictWriter(target, list(data[0].keys()))
		writer.writeheader()

		i = 0
		while i < len(data):
			writer.writerow(data[i])
			i += 1

		print('New base data file created')
		target.close()

	# Consolidates data from various file of the same symbol into a combined
	# data structure to be used in other methods.
	def getAllFilePaths(self, symbol):
		num = 2
		allFilesForSymbol = []
		for i in range(1,num+1):
			allFilesForSymbol.append('.\\'+symbol+'\\'+symbol+'-2015-0'+str(i)+'.csv')
		return allFilesForSymbol

	#  Create an object of a new feature using the avialable data
	def addNewFeatures(self, newSymbol):
		data = []
		quote = 'Bid'

		listOfFilePaths = self.getAllFilePaths(newSymbol)
		fieldnames = ['Symbol', 'Timestamp', 'Bid', 'Ask']

		for filePath in listOfFilePaths:
			print('-------------------------------')
			print(filePath)
			csvFile = open(filePath)
			reader = csv.DictReader(csvFile, fieldnames=fieldnames)
			start = time.time()
			rowOut = [];

			# Go through each row, create new keys, and group entries
			# in specified timed intervals
			for count, row in enumerate(reader):
				symbol = row['Symbol']
				splitTime = row['Timestamp'].split(':')
				# Create one minute intervals
				newTimestamp = splitTime[0]+':'+splitTime[1]
				row['Timestamp'] = newTimestamp

				highKey = symbol+' High'
				lowKey = symbol+' Low'
				openKey = symbol+' Open'
				closeKey = symbol+' Close'
				changeKey = symbol+' Change'

				if not rowOut:
					rowOut = {'Timestamp':row['Timestamp'], 
								  highKey:row[quote],
								   lowKey:row[quote], 
								  openKey:row[quote], 
								 closeKey:row[quote],
								changeKey:0.0}

				if (rowOut['Timestamp'] == row['Timestamp']):
					rowOut[highKey] = max(rowOut[highKey],row[quote])
					rowOut[lowKey] = min(rowOut[lowKey],row[quote])
					rowOut[closeKey] = row[quote]
					rowOut[changeKey] = Decimal(row[quote]) - Decimal(rowOut[openKey])
					# rowOut[changeKey] = float(row[quote]) - float(rowOut[openKey])

				elif (rowOut['Timestamp'] < row['Timestamp']):
					data.append(rowOut)
					rowOut = {'Timestamp':row['Timestamp'], 
								  highKey:row[quote],
								   lowKey:row[quote], 
								  openKey:row[quote], 
								 closeKey:row[quote],
								changeKey:0.0}

				if (count%1000000) == 0 and count >= 1000000:
					elapsed = time.time()-start
					start = time.time()
					print(str(count)+' in '+str(round(elapsed,4)))

			csvFile.close()
			print('New data object created with '+str(len(data))+' rows')
		return data

	# Merge a data object of new features with the base data matrix	 
	def mergeFeaturesToBase(self, baseSymbol, newData):

		baseData = self.readInBase(baseSymbol)

		target = open(baseSymbol+'.csv','wb')
		writer = csv.DictWriter(target, list(set(list(baseData[0].keys())+list(newData[0].keys()))))
		writer.writeheader()

		indBase = 0
		indData = 0

		# Loop through the data and match up the new data object with 
		# corresponding base data matrix entries
		while indBase < len(baseData):
			if indBase == len(baseData):
				break
			if indData == len(newData):
				writer.writerow(baseData[indBase])
				indBase += 1
				continue
			if newData[indData]['Timestamp'] == baseData[indBase]['Timestamp']:
				mergedDict = baseData[indBase].copy()
				writer.writerow(dict(mergedDict, **newData[indData]))
				indData += 1
				indBase += 1
			elif newData[indData]['Timestamp'] < baseData[indBase]['Timestamp']:
				indData += 1
			elif newData[indData]['Timestamp'] > baseData[indBase]['Timestamp']:
				writer.writerow(baseData[indBase])
				indBase += 1
			if (indBase % 100000) == 0:
				print(indBase)
		target.close()

		print('DONE! New features added!')

	# Reading in the data matrix into a base object
	def readInBase(self, symbol):
		csvFile = open(symbol+'.csv','rb')
		data = []
		reader = csv.DictReader(csvFile)
		for row in reader:
			data.append(row)
		csvFile.close()
		return data

	# Creating the label using the base data matrix
	def createLabel(self, symbol):
		data = self.readInBase(symbol)
		# print(data[0].keys())	
		label = 'Label (t+1)'
		target = open(symbol+'.csv','wb')

		if not label in data[0].keys():
			# print('label does not exist, creating label...')
			writer = csv.DictWriter(target, list(data[0].keys())+[label])
			# print(list(data[0].keys())+[label])
			writer.writeheader()
		else:
			# print('label found')
			writer = csv.DictWriter(target, list(data[0].keys()))
			writer.writeheader()

		rowMinusOne = [];
		for row in data:
			if not rowMinusOne:
				rowMinusOne = row
				continue
			if row:
				if row['EUR/USD Close'] >= rowMinusOne['EUR/USD Close']:
					rowMinusOne.update({label:1})
				elif row['EUR/USD Close'] < rowMinusOne['EUR/USD Close']:
					rowMinusOne.update({label:-1})
				else:
					rowMinusOne.update({label:0})
				writer.writerow(rowMinusOne)
				rowMinusOne = row
			else:
				break
		print('-------------------------------')
		print('Done! New labels created!')
		target.close()
			
# baseFilename = ['test.csv']
# dataFilename = ['EURJPY-2015.csv']	

symbol = 'EURUSD'
table = dataProcessing()
table.createBaseData(symbol)
newData = table.addNewFeatures('EURJPY')
table.mergeFeaturesToBase(symbol,newData)
newData = table.addNewFeatures('AUDNZD')
table.mergeFeaturesToBase(symbol,newData)
newData = table.addNewFeatures('GBPUSD')
table.mergeFeaturesToBase(symbol,newData)
newData = table.addNewFeatures('EURGBP')
table.mergeFeaturesToBase(symbol,newData)
shutil.copyfile(symbol+'.csv',symbol+'-backup.csv')
table.createLabel(symbol)


