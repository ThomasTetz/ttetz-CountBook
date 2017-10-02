Problems with submission:

I used a custom ListView item and ArrayAdapter. For some reason this caused problems with notifyDataSetChanged() and other similar updating methods to the point where starting the Main activity always displays blank data for the ListView elements until one of the elements is interacted with.

I am aware that this is a problem. My solution to display the data without changing anything was to set a boolean fixClick on the incrementButton of the list items. The first time one of the incrementButtons is clicked the value will be incremented then immediately decremented so that the ListView data fills in but data is retained.

In the future I would like to find out the source of the problem because I found many StackOverflow questions for similar issues but none of the solutions worked for me.
