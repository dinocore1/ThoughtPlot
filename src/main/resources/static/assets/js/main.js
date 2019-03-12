
function isEmpty(str) {
  return (!str || 0 === str.length);
}

class ThoughtPlot {

  //status can be: idle, loading
  loadStatus;
  currentNote;
  noteDiv;
  editorDiv;

  constructor() {
    this.loadStatus = 'idle';
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

    $('#editSave').click((event) => {
      event.preventDefault();
      this.saveMarkdown($('#editor').val());
    });

    this.network.on('selectNode', (obj) => {
      var id = obj.nodes[0];
      window.location.hash = '#' + encodeURI(id);
    });

    this.onHashChange = () => {
      var noteId = this.getUrlParam();
      this.loadNote(noteId);
    }

    window.addEventListener('hashchange', this.onHashChange);

  }

  getCurrentNoteId() {
    if(this.currentNote) {
      return this.currentNote.id;
    } else {
      return '';
    }
  }

  loadNote(id) {
    if (isEmpty(id)) {
      id = 'index';
    }

    this.network.focus(id, {
      scale: 1.2,
      animation: {
        duration: 1000,
        easingFunction: "easeInOutCubic"
      }
    });

    var onSuccess = (data) => {
      this.currentNote = data;
      $('#note').html(data.html);
      $('#editor').val(data.markdown);

      this.nodes.update(data.graph.nodes);
      this.edges.update(data.graph.edges);

      $('.tp-edit').click(function(event) {
        event.preventDefault();
        thoughtPlot.toggleEdit();
      });

      window.removeEventListener('hashchange', this.onHashChange);
      window.location.hash = '#' + encodeURI(id);
      window.addEventListener('hashchange', this.onHashChange);
    };

    var onError = (jqxhr) => {
      if(jqxhr.status == 404) {
        this.currentNote = {
          id: id,
          html: '',
          markdown: ''
        };
        $('#note').html(this.currentNote.html);
        $('#editor').val(this.currentNote.markdown);
        this.setEditMode(true);
      }
    };

    var url = "api/v1/note/" + encodeURI(id);
    $.ajax({
      url: url,
      method: 'GET',
      dataType: 'json',
      success: onSuccess,
      error: onError
    });
  }

  saveMarkdown(markdown) {
    this.currentNote.markdown = markdown;

    var url = "/api/v1/note/" + encodeURI(this.currentNote.id) + "/markdown";

    var onSuccess = (stuff) => {
      this.toggleEdit();
      this.loadNote(this.currentNote.id);
    };

    var jqxhr = $.ajax({
      url: url,
      method: 'POST',
      data: this.currentNote.markdown,
      contentType: 'application/json',
      success: onSuccess
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
    this.isEditMode = edit;
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
