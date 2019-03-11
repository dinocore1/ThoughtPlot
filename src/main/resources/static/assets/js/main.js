
function isEmpty(str) {
  return (!str || 0 === str.length);
}

class ThoughtPlot {

  currentNote;
  noteDiv;
  editorDiv;

  constructor() {
    this.isEditMode = false;
    this.nodes = new vis.DataSet();
    this.edges = new vis.DataSet();
    this.network = new vis.Network(
      document.getElementById('notegraph'),
      {
        nodes: this.nodes,
        edges: this.edges
      },
      {
        autoResize: true,
        width: '100%',
        height: '300px',
        layout: {
          randomSeed: 1
        }
      }
    );

    this.noteDiv = $('#noteRow');
    this.editorDiv = $('#editorRow');
    this.editorDiv.hide();

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
      $('#editor').val(data.markdown);

      caller.nodes.update(data.graph.nodes);
      caller.edges.update(data.graph.edges);

      $('.tp-edit').click(function() {
        thoughtPlot.toggleEdit();
      });

      window.location.hash = '#' + encodeURI(id);
    });
  }

  getUrlParam() {
    var retval = window.location.hash.substring(1);
    return decodeURI(retval);
  }

  setEditMode(edit) {
    if(edit) {
      this.noteDiv.hide();
      this.editorDiv.show();
    } else {
      this.noteDiv.show();
      this.editorDiv.hide();
    }
  }

  toggleEdit() {
    this.setEditMode(!this.isEditMode);
  }

};

var thoughtPlot;

$(document).ready(function () {
  thoughtPlot = new ThoughtPlot();
  var noteId = thoughtPlot.getUrlParam();
  thoughtPlot.loadNote(noteId);
});
