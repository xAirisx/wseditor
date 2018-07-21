 function check() {
    console.log('#InputPassword');
     var pass = document.getElementById('InputPassword');
     var confpass = document.getElementById('ConfirmPassword');
     console.log(pass.value);
     console.log(confpass.value);
    if (pass.value == confpass.value) {
        $('#message').html('Matching').css('color', 'green');
        document.getElementById('but').disabled = true;
    } else
        $('#message').html('Not Matching').css('color', 'red');
        document.getElementById('but').disabled = false;
 };