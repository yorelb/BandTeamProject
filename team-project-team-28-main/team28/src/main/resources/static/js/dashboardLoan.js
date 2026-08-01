// binds the action and instrument id to the hidden input fields in the form so they're passed to the dto
function setAction(button) {
    const action = button.getAttribute('data-action');
    const instrumentId = button.getAttribute('data-id');

    if (action === "loan") {
        document.getElementById('loanAction').value = action;
        document.getElementById('loanInstrumentId').value = instrumentId;
    } else if (action === "return") {
        document.getElementById('returnAction').value = action;
        document.getElementById('returnInstrumentId').value = instrumentId;
    }
}

// assigns the values of the item on loan dynamically so that they can be viewed in the modal
function showLoanDetails(button) {
    const itemName = button.getAttribute('data-itemName');
    const make = button.getAttribute('data-make');
    const note = button.getAttribute('data-note');
    const loanDate = button.getAttribute('data-loanDate');
    const memberFirstName = button.getAttribute('data-memberFirstName');
    const memberLastName = button.getAttribute('data-memberLastName');

    document.getElementById('modalInstrumentName').innerText = itemName;
    document.getElementById('modalMake').innerText = make;
    document.getElementById('modalNote').innerText = note;
    document.getElementById('modalLoanDate').innerText = loanDate;
    document.getElementById('modalMemberName').innerText = memberFirstName + " " + memberLastName;
}