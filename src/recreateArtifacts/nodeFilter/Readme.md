#Filters regexes according to node definitions

######Input: *fullCorpus.tsv* the corpus of regexes.

######Output: `nodes` folder containing groups and lists of node members to be manually verified.

##Program Summary
For each node, the corresponding filter is used to decide membership.  Then these members are dumped into their corresponding files within the `nodes` folder.
