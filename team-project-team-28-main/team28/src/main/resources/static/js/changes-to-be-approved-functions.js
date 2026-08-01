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

function confirmApprove() {
    return confirm("Are you sure you want to approve this request?");
}

function confirmDeny() {
    return confirm("Are you sure you want to deny this request?");
}

