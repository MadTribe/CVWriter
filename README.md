## CV Generator

When applying for jobs, it is good practise to tailor your CV or resume to the specific role you are applying to. 

The concept behind this CV generator is to maintain a bank of descriptions that are tailored to emphasize specific skills, roles or industries and then a suitable resume can be quickly assembled from this. 

## Build and Test

```bash
mvn clean package
```

## Usage

```
java -cp target/CVGenerator-1.0-SNAPSHOT.jar org.madtribe.cvgen.App
```

```bash
Usage: <main class> [-eilv] -f=<file> [-n=<fullName>] [-o=<outputFile>]
                    [-q=<filterTags>] [-t=<templateName>]
  -e, --edit          Edit CV
  -f, --file=<file>   project file
  -i, --initialise    Initialize
  -l, --list-tags     List Tags
  -n, --fullName=<fullName>
                      Full Name of CV owner
  -o, --output=<outputFile>
                      Output file to create
  -q, --filter-tags=<filterTags>
                      Filter on Tags comma separated
  -t, --templateName=<templateName>
                      Freemarker Template to use in templates folder. Defaults
                        to cv_default_template.md
  -v, --verbose       Verbose mode

```


## Current Features

1. Create new project - initializes a project file
```bash
java -cp target/CVGenerator-1.0-SNAPSHOT.jar org.madtribe.cvgen.App -f cv2.json -i -n "Mad Tribe"
```

2, Generate CV

Generate markdown based CV with the default template in the templates folder

templates/cv_default_template.md

```bash
 java -cp target/CVGenerator-1.0-SNAPSHOT.jar org.madtribe.cvgen.App -f cv.json -o cv1.md
```

Use -t parameter to reference your own template in the templates folder.

The following features can be accessed through the simple interactive editor  



## Planned Features


