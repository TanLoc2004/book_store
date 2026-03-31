$(document).ready(function () {

    $('.quantity').on('input', function () {

        let quantity = $(this).val();

        let id = $(this).data('id');

        let row = $(this).closest('tr');

        let price = parseFloat(row.find('.price').text());

        let itemTotal = price * quantity;

        row.find('.item-total').text(itemTotal);

        updateTotal();

        $.ajax({
            url: '/cart/updateCart/' + id + '/' + quantity,
            type: 'GET'
        });

    });

});


function updateTotal() {

    let total = 0;

    $('.item-total').each(function () {

        total += parseFloat($(this).text());

    });

    $('#totalPrice').text(total);

}