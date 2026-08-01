function confirmDelete() {
    const instrumentId = document.getElementById("editInstrumentId").value;
    const deleteForm = document.getElementById("deleteForm");

    // Test
    if (!deleteForm){
        console.log("Form not found!")
    }

    // Confirm with the user before deleting
    if (confirm("Are you sure you want to delete this instrument? This action cannot be undone.")) {
        console.log("Confirmed")
        // Set the instrument ID in the delete form hidden input field
        document.getElementById("deleteInstrumentId").value = instrumentId;
        // Submit the form
        deleteForm.submit();
    } else {
        console.log("Canceled")
    }
}