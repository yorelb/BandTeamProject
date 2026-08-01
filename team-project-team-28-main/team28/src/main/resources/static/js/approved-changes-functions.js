document.addEventListener('DOMContentLoaded', () => {
    const alertSuccess = document.querySelector('.alert-success');
    const alertDanger = document.querySelector('.alert-danger');

    if (alertSuccess) {
        alertSuccess.classList.add('show');
    }

    if (alertDanger) {
        alertDanger.classList.add('show');
    }
});

function confirmProceed() {
    return confirm("Are you sure you want to proceed with this change?");
}

function confirmDiscard() {
    return confirm("Are you sure you want to discard this request?");
}