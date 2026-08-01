document.addEventListener("DOMContentLoaded", function () {
    const itemsTableBody = document.getElementById("itemsTableBody");

    // Fetch items
    fetch("/items")
        .then((response) => response.json())
        .then((data) => {
            if (data.length === 0) {
                itemsTableBody.innerHTML = `<tr><td colspan="6" class="text-center">No items available.</td></tr>`;
                return;
            }

            data.forEach((item) => {
                const row = document.createElement("tr");

                row.innerHTML = `
                    <td>${item.id}</td>
                    <td>${item.nameTypeOrTitle}</td>
                    <td>${item.makeOrComposer || "N/A"}</td>
                    <td>${item.inStorage ? "Yes" : "No"}</td>
                    <td>${item.loanedTo || "N/A"}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="openLoanReturnModal(${item.id}, '${item.inStorage}')">Loan/Return</button>
                    </td>
                `;

                itemsTableBody.appendChild(row);
            });
        })
        .catch((error) => console.error("Error fetching items:", error));
});

// Open modal for loan/return action
function openLoanReturnModal(itemId, inStorage) {
    document.getElementById("itemIdInput").value = itemId;

    if (inStorage === "true") {
        document.getElementById("actionSelect").value = "loan";
        document.getElementById("memberNameField").style.display = "block";
    } else {
        document.getElementById("actionSelect").value = "return";
        document.getElementById("memberNameField").style.display = "none";
    }

    $("#loanReturnModal").modal("show");
}

// Handle form submission
document.getElementById("loanReturnForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const itemId = document.getElementById("itemIdInput").value;
    const action = document.getElementById("actionSelect").value;
    const memberName = document.getElementById("memberNameInput").value;

    const requestBody = {
        itemId: itemId,
        action: action,
        memberName: action === "loan" ? memberName : null,
    };

    fetch("/loanAction", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
    })
        .then((response) => {
            if (response.ok) {
                alert("Action processed successfully!");
                location.reload();
            } else {
                response.text().then((text) => alert("Error: " + text));
            }
        })
        .catch((error) => console.error("Error processing action:", error));
});
