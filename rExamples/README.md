##Running example R files

#######These files may be useful to debug your system, if R is not working correctly.

####Get R running
On a mac, you can install R using:
```
brew install R
```

Or download from the [R website](https://www.r-project.org/).  To verify that you have R and rscript installed in `/usr/local/bin` as required, run:
```
>which R
/usr/local/bin/R
>which rscript
/usr/local/bin/rscript
```
-----

####Folder contents:
The following R files should be present:
- *example_wilcox.r*
- *example_kruskal.r*
- *example_ANOVA.r*

And their inputs (respectively):
- *M0R0V0_CC_SEQ_M0R0V1_CC_SNG.tsv*
- *M3R0V0_OCTRNG_M3R0V1_CCC_M3R0V2_HEXRNG.tsv*
- *M0.csv*

##Run the tests

To run *example_kruskal.r*, use:

```
>rscript example_kruskal.r

```

This should perform a kruskal test on the *M3R0V0_OCTRNG_M3R0V1_CCC_M3R0V2_HEXRNG.tsv* input, and output *example_wilcox_output.txt* containing test results to the `output` folder.

The other tests are run the same way.



