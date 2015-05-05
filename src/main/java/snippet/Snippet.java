package snippet;

public class Snippet {
  $.ajax({
                        url: "http://www.google.com",
                        context: document.body,
                        error: function(jqXHR, exception) {alert('offline')},
                        success: function(){alert('ONline')}
                      })
}

