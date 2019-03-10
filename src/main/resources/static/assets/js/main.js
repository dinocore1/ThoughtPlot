
class ThoughtPlot {

  constructor() {
    this.nodes = new vis.DataSet();
    this.edges = new vis.DataSet();
    this.network = new vis.Network(
      document.getElementById('notegraph'), 
      {
        nodes: this.nodes,
        edges: this.edges
      },
      {
        layout: {
          randomSeed: 1
        }
      }
    );


    this.network.on("selectNode", this.onNodeClicked.bind(this));

    this.nodes.add([
      { id: 1, label: 'Node 1' },
      { id: 2, label: 'Node 2' },
      { id: 3, label: 'Node 3' },
      { id: 4, label: 'Node 4' },
      { id: 5, label: 'Node 5' }
    ]);


    this.edges.add([
      { from: 1, to: 3 },
      { from: 1, to: 2 },
      { from: 2, to: 4 },
      { from: 2, to: 5 }
    ]);
  }

  onNodeClicked(obj) {
    this.network.focus(obj.nodes[0], {
      scale: 1.2,
      animation: {
        duration: 1000,
        easingFunction: "easeInOutCubic"
      }
    });
  }
};

var thoughtPlot;

$(document).ready(function() {
  thoughtPlot = new ThoughtPlot();
  thoughtPlot.loadNote();
});
