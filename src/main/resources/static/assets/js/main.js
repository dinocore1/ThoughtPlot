
function isEmpty(str) {
  return (!str || 0 === str.length);
}

class ThoughtPlot {

  currentNote;

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
    window.addEventListener("hashchange", this.onHashChange.bind(this), true);

  }

  onHashChange(event) {
    var noteId = this.getUrlParam();
    if(this.getCurrentNoteId() != noteId) {
      this.loadNote(noteId);
    }
  }

  onNodeClicked(obj) {
    var nodeId = obj.nodes[0];
    var nodeObj = this.nodes.get(nodeId);
    this.loadNote(nodeObj.label);
  }

  getCurrentNoteId() {
    if(this.currentNote) {
      return this.currentNote.id;
    } else {
      return '';
    }
  }

  loadNote(id) {
    var caller = this;

    if (isEmpty(id)) {
      id = 'index';
    }

    if(this.currentNote && this.currentNote.id == id){
      return;
    } else {
      this.currentNote = {
        id: id
      };
    }

    this.network.focus(id, {
      scale: 1.2,
      animation: {
        duration: 1000,
        easingFunction: "easeInOutCubic"
      }
    });

    var url = "api/v1/note/" + encodeURI(id);
    $.getJSON(url, function (data) {
      caller.currentNote = data;
      $('#note').html(data.html);
      //caller.nodes.clear();
      //caller.edges.clear();

      caller.nodes.update(data.graph.nodes);
      caller.edges.update(data.graph.edges);

      /*
      caller.network.focus(id, {
        scale: 1.2,
        animation: {
          duration: 1000,
          easingFunction: "easeInOutCubic"
        }
      });
      */

      window.location.hash = '#' + encodeURI(id);
    });
  }

  getUrlParam() {
    var retval = window.location.hash.substring(1);
    return decodeURI(retval);
  }

};

var thoughtPlot;

$(document).ready(function () {
  thoughtPlot = new ThoughtPlot();
  var noteId = thoughtPlot.getUrlParam();
  thoughtPlot.loadNote(noteId);

  
});
