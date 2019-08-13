// external js: draggabilly.pkgd.js

$(document).ready( function() {
  var $draggable = $('.draggable').draggabilly();
  // Draggabilly instance
  var draggie = $draggable.data('draggabilly');
  $draggable.on( 'dragMove', function() {
    console.log( 'dragMove', draggie.position.x, draggie.position.y );
  });
});