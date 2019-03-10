
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
      { id: 'Android', label: 'Android' },
      { id: 2, label: 'Node 2' },
      { id: 3, label: 'Node 3' },
      { id: 4, label: 'Node 4' },
      { id: 5, label: 'Node 5' }
    ]);


    this.edges.add([
      { from: 'Android', to: 3 },
      { from: 'Android', to: 2 },
      { from: 2, to: 4 },
      { from: 2, to: 5 }
    ]);
  }

  onNodeClicked(obj) {
    var nodeId = obj.nodes[0];
    this.network.focus(nodeId, {
      scale: 1.2,
      animation: {
        duration: 1000,
        easingFunction: "easeInOutCubic"
      }
    });
    var nodeObj = this.nodes.get(nodeId);
    this.loadNote(nodeObj.label);
  }

  loadNote(id) {
    if(id === undefined) {
      id = '';
    }
    var url = "api/v1/note/" + encodeURI(id);
    $.getJSON( url, function( data ) {
      $('#note').html(data.html);
      //history.pushState(null, id, "/?" + encodeURI(id));
    });
  }

  getUrlParam() {
    var retval = window.location.search.substring(1);
    return decodeURI(retval);
  }

};

var thoughtPlot;

$(document).ready(function() {
  thoughtPlot = new ThoughtPlot();
  var noteId = thoughtPlot.getUrlParam();
  thoughtPlot.loadNote(noteId);
});
