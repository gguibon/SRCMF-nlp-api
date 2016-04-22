SRCMF-NLP
===============

This API regroups different tools to use the SRCMF with Natural Language Processing (NLP) purposes such as :
- lemmatisation
- part-of-speech tagging
- dependency parsing

It can also be used to format your corpus from the Base de Français Medieval files (Tiger XML or TEI XML).

# Build

This repo is built using maven, and developped using eclipse. In order to build it, please use:

```
mvn clean compile
mvn package
```

Of course, you can also simply use the JAR file : ./srcmf-nlp-1.0.jar

# Usage

```
java -Xmx[nb RAM]G -jar srcmf-nlp-1.0.jar [command] [subcommands] 
```

# Help section
```
java -jar srcmf-nlp-1.0.jar
```
or
```
java -jar srcmf-nlp-1.0.jar [commands] -help
```

Please note that the -help option will override any commands to prompt the help section, then stop the program.

# Usage for training

```
java -Xmx6G -jar srcmf-nlp-1.0.jar 1on1 -train [conll path] -wapitimodel [output model path] -matemodel [output model path]
```

# Usage for testing

```
java -Xmx6G -jar srcmf-nlp-1.0.jar 1on1 -test [conll path] -output [file path]
```

# Usage for training and testing in the same time
```
java -Xmx6G -jar srcmf-nlp-1.0.jar 1on1 -train [conll path] -test [conll path] -out [file path]
```
Please note models will be stored and used as temporary files !

# Contacts

gael dot guibon at gmail.com

@2016 SRCMF LaTTiCe-CNRS# SRCMF-nlp-api
