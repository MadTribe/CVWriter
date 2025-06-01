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

## Planned Features

2. Add Role 
3. Tag Role - 
4. Add Project 
5. Tag Project 
6. Add Skill 
7. Tag Skill
8. Add Summary
9. Tag Summary
10. Link CV template
11. Generate CV

