DATA FORMAT

	".sensors" and ".labels" files:

		Columns within these files provide the following information:

		1.- Start time of the event, expressed in seconds elapsed since 00:00:00, January 1, 1970 (standard Greenwich time zone).
		2.- Length of the event, expressed in seconds.
		3.- ID of the event. For ".sensors" files, this is the ID of a particular sensor within that house. For ".labels" files, this is the ID of an activity, as defined in the ".activities" file.

	".activities" files:

		Columns within these files provide the following information:

		1.- ID of the activity, as used in the ".labels" file. These IDs must be unique.
		2.- Name of the activity. Equal names across files have as a result all activities being regarded as a single one.


DATA SOURCE

Data from houses A, B and C was obtained from Tim van Kasteren's "Activity Recognition" dataset, which can be found under the following link:

https://sites.google.com/site/tim0306/tlDatasets.zip?attredirects=0

For house C there were two distinct labels having the same name ("use-toilet"). These were conflated into a single label in the provided dataset.

Data from houses D and E was obtained from the MIT's "Activity Recognition in the Home Setting Using Simple and Ubiquitous Sensors" project dataset, which can be found under the following link:

http://courses.media.mit.edu/2004fall/mas622j/04.projects/home/thesis_data_txt.zip

There was a problem with the original data from house E which needed to be fixed. The database included information logged on the 31st of April, which is a date that doesn't exist. Data logged in this period was mapped to the 1st of May instead.
