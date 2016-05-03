#Finding significance of understandability differences

######Input: contents of `comprehensionData`

######Output: *testedEdges.tex*

##Program Summary
Reads the matching input data from *pairs_from_2.csv* and *pairs_from_3.csv*, and the composition input data from *RenamingRegexes.tsv* and *compositionAnswers.tsv*.  Then read in what regexes are associated with what edges from *EXP_EDG_LST.tsv*.  The data for each edge is grouped and a wilcox test is performed using R to find a p-value.  The edges are sorted by smallest of either p-value, and all this is used to create the table.
