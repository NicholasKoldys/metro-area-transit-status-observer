## How to create the project

The project uses gradle as its build system and bases its structure off Maven package definitions.
```
src
| main
    | java
        | domain.package.name
            | (.java code files)

    | resources
        | view
            | (fxml files)

```

To create the project with the same structure, simply download [Gradle](), run the command..
```
gradle init
```
and see the gradle.build file under ```app/``` directory and copy its structure.

<br />
<br />

To push the project to github as a reposity, follow the following instructions and commands based in the project directory..

* Create a github repository with the desired permisions and name.
* Copy the repository's home link.

In your project directory on your machine, with [Git]() installed, type the following commands..
```
git clone <reposity.link>
```
The project will create the necessary ```init``` files and set the appropriate uplink for commits/pushs/pulls from the repository on [Github]().

<br />
<br />

For a more manual process, the same can be done, with [Git]() installed, type the following commands in the projects directory..
```
git init
```
Create a repository on github.. then..
```
git remote add origin <repository.link>
```
To check the link status..
```
git remote -v 
```
Push initial commit..
```
git push origin <branchToPush>
```
Or, if their are any *different/new* files in the destination repo, then..
```
git pull origin <branchToPull> --allow-unrelated-histories

git push origin <branchToPush>
```