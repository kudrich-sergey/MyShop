const preview_active = document.querySelectorAll('.product_preview-mini');
const preview = document.querySelector('#change_image');

document.addEventListener('click', function (event) {

    if (event.target.parentElement.classList[0] === "product_preview-mini") {
        preview.setAttribute('src', event.target.getAttribute('src'));
        preview_active.forEach(function (item) {
            item.classList.remove('active_preview-mini');
        });
        event.target.parentElement.classList.add('active_preview-mini');
    }

});