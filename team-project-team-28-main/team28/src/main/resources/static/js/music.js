document.addEventListener("DOMContentLoaded", function () {
    const musicTableBody = document.getElementById("musicTableBody");

    // Fetch music items
    fetch("/music")
        .then((response) => response.json())
        .then((data) => {
            if (data.length === 0) {
                musicTableBody.innerHTML = `<tr><td colspan="7" class="text-center">No music items available.</td></tr>`;
                return;
            }

            data.forEach((music) => {
                const row = document.createElement("tr");

                row.innerHTML = `
                    <td>${music.id}</td>
                    <td>${music.title}</td>
                    <td>${music.composer}</td>
                    <td>${music.arranger || "N/A"}</td>
                    <td>${music.status}</td>
                    <td>${music.bandInPractice || "N/A"}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="openMusicActionModal(${music.id}, '${music.status}')">Action</button>
                    </td>
                `;

                musicTableBody.appendChild(row);
            });
        })
        .catch((error) => console.error("Error fetching music items:", error));

    // Fetch outstanding orders
    document.getElementById("outstandingOrdersBtn").addEventListener("click", function () {
        fetch("/orders/outstanding")
            .then((response) => response.json())
            .then((orders) => {
                if (orders.length === 0) {
                    alert("No outstanding orders.");
                    return;
                }

                let ordersList = "Outstanding Orders:\n";
                orders.forEach((order) => {
                    ordersList += `Order ID: ${order.id}, Member: ${order.memberName}, Item: ${order.itemName}\n`;
                });

                alert(ordersList);
            })
            .catch((error) => console.error("Error fetching outstanding orders:", error));
    });
});

// Open modal for music action
function openMusicActionModal(musicId, status) {
    document.getElementById("musicIdInput").value = musicId;

    if (status === "STORAGE") {
        document.getElementById("actionSelect").value = "practice";
        document.getElementById("bandSelectField").style.display = "block";
    } else {
        document.getElementById("actionSelect").value = "storage";
        document.getElementById("bandSelectField").style.display = "none";
    }

    $("#musicActionModal").modal("show");
}

// Handle form submission
document.getElementById("musicActionForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const musicId = document.getElementById("musicIdInput").value;
    const action = document.getElementById("actionSelect").value;
    const band = action === "practice" ? document.getElementById("bandSelect").value : null;

    const requestBody = {
        musicId: musicId,
        action: action,
        band: band,
    };

    fetch("/musicAction", {
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
