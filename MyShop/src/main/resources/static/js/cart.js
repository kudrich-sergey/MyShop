const btnPreOrder = document.querySelector('.btn_preOrder');
const panePreOrder = document.querySelector('.panel_preOrderCart');
const btnOrder = document.querySelector('.btn_Order');
const inputPrice = document.querySelector('#sum_total');
const inputCount = document.querySelector('#count');
const wrapper = document.querySelector('.panel_productsCart');
const priceElements = wrapper.querySelectorAll('.cart_productSum');
const sumProduct = document.querySelector('#sumProduct');
const sumDelivery = document.querySelector('.sumDelivery');
const sumTotal = document.querySelector('.sumTotal');

 document.querySelector('.btn_preOrder').addEventListener('click', function () {
    panePreOrder.classList.remove('hiddenEff');
    btnPreOrder.parentNode.removeChild(btnPreOrder);
    btnOrder.classList.remove('hidden');
});

function calcCartPriceAndDelivery() {

	let sumProducts = 0;
	let count = 0;

	priceElements.forEach(function (item) {
		const amountEl = item.closest('.cart_productInfo').querySelector('[data-counter]');
		count += parseInt(amountEl.innerText);
		sumProducts += parseInt(item.innerText) * parseInt(amountEl.innerText);
	});
	
	sumProduct.innerText = sumProducts;

	if (sumProducts >= 3000) {
		sumDelivery.classList.add('line-through');
		sumTotal.innerText = sumProducts;
	} else {
		sumDelivery.classList.remove('line-through');
		sumTotal.innerText = sumProducts + parseInt(sumDelivery.innerText);
	}

	inputCount.setAttribute('value', count);
	inputPrice.setAttribute('value', sumTotal.innerText);
}

window.addEventListener('click', function (event) {
    let counter;
	let btnMinus;

    if (event.target.dataset.action === 'plus' || event.target.dataset.action === 'minus') {

		const counterWrapper = event.target.closest('.counter-wrapper');

        counter = counterWrapper.querySelector('[data-counter]');
		btnMinus = counterWrapper.querySelector('.btn-minus');
	}

	if (event.target.dataset.action === 'plus') {
		counter.innerText = ++counter.innerText;
		calcCartPriceAndDelivery();
		
		if (parseInt(counter.innerText) > 1) {
			btnMinus.classList.remove('btn_actionDisable');
			btnMinus.classList.add('btn_actionMinus')
		}
	}

	if (event.target.dataset.action === 'minus') {

		if (parseInt(counter.innerText) > 1) {
			counter.innerText = --counter.innerText;
			calcCartPriceAndDelivery();
		} 

		if (parseInt(counter.innerText) <= 1) {
			btnMinus.classList.remove('btn_actionMinus')
			btnMinus.classList.add('btn_actionDisable');
		}
	}

});


window.onload = function() {
	inputPrice.setAttribute('value', sumTotal.innerText);
};
