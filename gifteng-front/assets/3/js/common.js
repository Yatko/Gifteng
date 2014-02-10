$(function() {
    $(':file').wrap($('<div/>').css({
        height: 0,
        width:0,
        'overflow': 'hidden'
    }));
    
    $('.file').each(function() {
        var $this = $(this);
        var fileInputId = $this.attr('for');
        var fileInput = $('#' + fileInputId);
        
        fileInput.change(function() {
            var fileName = fileInput.val().replace(/C:\\fakepath\\/i, '');
            $this.text(fileName);
        });
        
        $this.click(function() {
            fileInput.click();
        }).show();
    });
});
