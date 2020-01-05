SRCMF-NLP API
===============

This API regroups different tools to use the SRCMF with Natural Language Processing (NLP) purposes such as :
- lemmatisation
- part-of-speech tagging
- dependency parsing

It can also be used to format your corpus from the Base de Français Medieval files (Tiger XML or TEI XML).

Tested on Linux Mint Cinnamon 18, Ubuntu 16.04 and MacOS.

# Requirements / Environment setup

## TreeTagger
- Download from: [https://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/](https://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/)
- Run script: `sh install-treetagger.sh`
- Download Achim Stein from : 
- export TREETAGGER_HOME=<path-to-treetagger>
- Insert Achim Stein model in treetaggerhome /models/models/file_name

## Wapiti

Wapiti is in the .so library (for Linux), .dylib library (for Mac) or libwapiti.dll file (for Windows).
If a problem occurs: 
- Download wapiti: [https://wapiti.limsi.fr/#download](https://wapiti.limsi.fr/#download)
- Install wapiti: `sudo make install`


# Input format

The input format is derived from CoNLL: a tabular-separated-values (TSV) with an empty line between sentences. Here are the different columns:


| Id | token | lemmaPlaceholder | ttLemma | treetaggerPOS | wapitiPOS | placeholder | morphoPlaceholder | placeholder | head | placeholder | deprel | otherFormPlaceholder | … multiple placeholders |


# Usage from Command Line

## Command Line Usage

The command line basic usage is as follow:

```
java -Xmx[nb RAM]G -jar srcmf-nlp-{version}.jar [command] [subcommands] 
```
It means that you can add multiple java variables. The most useful one is to set the library path to the current folder for it to find the libwapiti files. For instance: 

```
java -Djava.library.path=. -Xmx6G -jar srcmf-nlp-<VERSION>.jar 1on1 -wapitimodel <MODEL-PATH> -matemodel <MATEMODEL-PATH> -test <test-FILE-PATH> -out <OUTPUTFILE-PATH>
```

## CLI for training

You first need to put the file "libwapiti.so" into /lib.
Then:

```
java -Xmx6G -jar srcmf-nlp-{version}.jar 1on1 -train [conll path] -wapitimodel [output model path] -matemodel [output model path]
```

## CLI for testing

You first need to put the file "libwapiti.so" into /lib.
Then:

```
java -Xmx6G -jar srcmf-nlp-{version}.jar 1on1 -test [conll path] -output [file path]
```

## CLI for training and testing in the same time

You first need to put the file "libwapiti.so" into /lib.
Then:

```
java -Xmx6G -jar srcmf-nlp-{version}.jar 1on1 -train [conll path] -test [conll path] -out [file path]
```
Please note for this command models will be stored and used as temporary files ! You can always use the two separate commands for training and testing in a shell script.


## Help section
```
java -jar srcmf-nlp-{version}.jar
```
or
```
java -jar srcmf-nlp-{version}.jar [commands] -help
```

Please note that the -help option will override any commands to prompt the help section, then stop the program.

# Usage as a Java Library

## Build Source Code

This repo is built using maven, and developped using eclipse. In order to build it, please use:

```
mvn clean compile
mvn package
```

Of course, you can also simply use the JAR file : ./srcmf-nlp-{version}.jar as an external library.

## Usage from source

The src/main directory is organized as follow:

![alt text](https://github.com/gguibon/SRCMF-nlp-api/raw/master/srcmfnlp-api-tree.png "Code organization")

Please note that in version 1.0.5-nockeck, data checks are ignored on OneOnOne pipelines. You can always uncomment them but they were made for specific needs in the SRCMF data format.

# Contacts

gael dot guibon at gmail.com

@2020 SRCMF LaTTiCe-CNRS# SRCMF-nlp-api
