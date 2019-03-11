
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
    var caller = this;
    if(id === undefined) {
      id = '';
    }
    var url = "api/v1/note/" + encodeURI(id);
    $.getJSON( url, function( data ) {
      $('#note').html(data.html);
      //caller.nodes.clear();
      //caller.edges.clear();

      caller.nodes.update(data.graph.nodes);
      caller.edges.update(data.graph.edges);
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
