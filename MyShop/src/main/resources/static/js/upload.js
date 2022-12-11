document.addEventListener('change', function (event) {

  if (event.target.files[0]) {
    var fr = new FileReader();
      fr.addEventListener("load", function () {
        event.target.parentElement.childNodes[1].classList.add('hidden');
        event.target.parentElement.childNodes[3].classList.add('hidden');
        event.target.parentElement.style.backgroundImage = "url(" + fr.result + ")";
    }, false);
      fr.readAsDataURL(event.target.files[0]);
  }

});

document.querySelector('#uploadFile').addEventListener('change', function (event) {

  if (event.target.files[0]) {
    var fr = new FileReader();
      fr.addEventListener("load", function () {
        event.target.parentElement.childNodes[1].classList.add('hidden');
        event.target.parentElement.childNodes[3].classList.add('hidden');
        event.target.parentElement.style.backgroundImage = "url(" + fr.result + ")";
    }, false);
      fr.readAsDataURL(event.target.files[0]);
      const id_product = document.querySelector('#idProduct').value;
      document.getElementById('editProduct').action = '/admin/product/' + id_product + '/image/add';
      document.querySelector('#editProduct').submit();
  }

});
