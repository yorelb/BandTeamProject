function populateEditForm(button) {
    const id = button.getAttribute("data-id");
    const row = button.closest("tr");
    const cells = row.getElementsByTagName("td");
    const inStorage = button.getAttribute('data-inStorage') === 'true';

    document.getElementById("editInstrumentId").value = id;
    document.getElementById("editInstrumentInput").value = cells[0].innerText;
    document.getElementById("editMake").value = cells[1].innerText;
    document.getElementById("editSerialNumber").value = cells[2].innerText;
    document.getElementById("editNote").value = cells[4].innerText;
    document.getElementById('editInStorage').value = inStorage;
    document.getElementById("deleteInstrumentId").value = id;
}
