# ThoughtPlot

A brower-based note taking app. Create mind-maps with Markdown!

### Configure

ThoughtPlot stores all of your notes in regular markdown files (*.md). By default, markdown files are stored in the local dir: `thoughts`. 

ThoughtPlot also can be configured to store the thoughts dir in a git repo. To do so, create an `config/application.properties` file and add the `giturl` property.

```
giturl=git@github.com:dinocore1/Notes.git
```