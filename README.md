# ThoughtPlot

A brower-based note taking app. Create mind-maps with Markdown!


## Note files
Note files are stored in the `thoughts` dir in the project root. Every note file is markdown formated and ends in `.md`. Every note file shows up on the mind-map display as a node.

## Run

Run locally with `gradle bootRun`

## Docker image

Create docker image with `gradle dockerBuildImage` This will create a local docker image `com.devsmart/thoughplot`

You can now run with `docker run --rm -p8080:8080 com.devsmart/thoughplot:0.0.1`

The docker image's working directory is `/app` so you can map the local `thoughts` dir:

`docker run --rm -p8080:8080 -v /full/path/local/thoughts:/app/thoughts com.devsmart/thoughplot:0.0.1`
 
### Configure

ThoughtPlot stores all of your notes in regular markdown files (*.md). By default, markdown files are stored in the local dir: `thoughts`. 

ThoughtPlot also can be configured to store the thoughts dir in a git repo. To do so, create an `config/application.properties` file and add the `giturl` property.

```
giturl=git@github.com:dinocore1/Notes.git
```