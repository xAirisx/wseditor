 function check() {
    console.log('#InputPassword');
     var pass = document.getElementById('InputPassword');
     var confpass = document.getElementById('ConfirmPassword');
     console.log(pass.value);
     console.log(confpass.value);
     let $but = $('#but');
     if (pass.value == confpass.value) {
        $('#message').html('Matching').css('color', 'green');
        // document.getElementById('but').disabled = true;
        // $but.attr('disabled', 'disabled')
         $but.prop('disabled', false);
    } else {
         $('#message').html('Not Matching').css('color', 'red');
         // $but.removeAttr('disabled');
         $but.prop('disabled', true);
     }
     // document.getElementById('but').disabled = false;
 };