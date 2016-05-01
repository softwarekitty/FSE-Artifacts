##Artifacts prepared for FSE

The tools in this repo can be used to recreate the results in the FSE submission, [**Refactoring Regular Expressions**](https://github.com/softwarekitty/ISSTA-16-AE-6Artifacts/blob/master/pdf/comingSoon.pdf).

Contents of important folders are described below:
____

####```artifacts```

The artifacts folder contains key objects used in recreating the results of the paper:

- **merged_report.db** is an SQLite3 database file containing all the data mined from GitHub.
- **projectInfo.tsv** contains a list of the projects mined for regexes (only those that contained regexes are included).
- **fullCorpus.tsv** is a dump of all the patterns (and the project sets associated with them) from the corpus in the paper.
- ```patternTracking``` contains three files, accounting for patterns excluded from analysis due to unicode errors, rare features and other errors, as mentioned in the paper.
- ```nodes``` is the result of running the node counting program on the corpus.
- ```manuallyReviewedNodes``` is the contents of `nodes` after a manual review.
- **nodeCounts.tex** summarizes the number of regexes in each equivalence class, determined by examining the contents of the ```manuallyReviewedNodes``` folder.
- `comprehensionData` contains all of the data about user comprehension gathered from Mechanical Turk and transformed using Google Sheets.
- **testedEdges.tex** summarizes matching and composition results for tested edges, and the results of running the wilcox test in R on both measurements for all tested edges.

_____

####```src/recreateArtifacts```

**Readme files containing instructions for how to reproduce the artifacts** are in the following paths:

- ```miningDataSources``` generates the *projectInfo.tsv* file from the database.
- ```corpus``` generates the *fullCorpus.tsv* file from the database, and error tracking files in ```patternTracking```.
- `nodeFilter` filters regexes in the corpus to produce the populated `nodes` folder.
- `nodeFinal` summarizes the contents of `manuallyReviewedNodes` in *nodeCounts.tex*.
- `edgeTests` uses data from the `comprehensionData` folder to analyze and summarize results of the comprehension study in the *testedEdges.tex* table.

######For all programs, inputs are taken from the ```artifacts``` folder and outputs are produced in an ```output``` folder within the folder for recreating the artifact.

######This repo is a groomed version of the original [regex_readability_study](https://github.com/softwarekitty/regex_readability_study) repository.

-----

####```src/main``` and ```src/test```

These folders contain the core code used to reason about regular expressions, and the test suite protecting and specifying that code.

-----

####```rExamples```

This folder contains examples of the **R** functions used in this analysis, with setup and running instructions.

_____

####Input format
A tab-separated-values (tsv) file with Python patterns and a CSV list of project IDs, like:

```
"ab*c"  1,2
"(?:\\d+)\.(\\d+)"   2,3,5
u'[^a-zA-Z0-9_]' 1,5
'^[-\\w]+$' 2
'^\\s*\\n'  1,3,4
```

At this time, all patterns must be followed by a tab and at least one project ID.

Patterns should be valid in Python - raw Python Strings are not supported at this time.

No extra lines or whitespace in input files, please.  No dulplicate patterns, please.


_____

##Setup Eclipse


1. Create a new Java Project in Eclipse (use Java 1.7) using this repo as the project directory.
2. Add the jar files in the `lib` directory to the build path.
3. To run tests of the core code, set up JUnit4.

-----


##F.A.Q.
####why Python?
It was not an arbitrary choice, but it was not the only option, either.  JavaScript would have been a reasonable alternative using our rationalle.  Consider first that regular expression languages have different feature sets, and doing this analysis takes some time.  In order to maximize the impact of the research, we wanted a language that *includes* common features (features shared by other languages) and *excludes* rare features (features not shared by many other languaes).  Python fits this description, as can be seen by looking at [a comparison of language feature sets](https://github.com/softwarekitty/ISSTA-16-AE-6Artifacts/pdf/blob/master/languageTables.pdf) from [my thesis](https://github.com/softwarekitty/ISSTA-16-AE-6Artifacts/pdf/blob/master/thesis.pdf).

####where is the mining code?
It can be found in the [tour_de_source](https://github.com/softwarekitty/tour_de_source) repo, but it is not groomed for public consumption, and is probably not an optimal mining solution.

####why not use formal tools for behavioral analysis?
Because the tools we found cannot handle regexes using certain common features, like '$'.

####how can I submit an error report, bug report or pull requrest?
Please open an issue if you find any problems or want to be a contributor.

