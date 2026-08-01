document.addEventListener('DOMContentLoaded', () => {
    // Fetch member participations
    fetch('/performances/participation')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const participationTableBody = document.querySelector('#participationTable tbody');
            participationTableBody.innerHTML = '';

            if (data.length === 0) {
                const noDataRow = document.createElement('tr');
                noDataRow.innerHTML = '<td colspan="5" class="text-center">No performances available.</td>';
                participationTableBody.appendChild(noDataRow);
            } else {
                data.forEach(participation => {
                    const row = document.createElement('tr');
            
                    const date = new Date(participation.dateTime);
                    const formattedDate = date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
                    const formattedTime = date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
            
                    row.innerHTML = `
                        <td>${participation.name}</td>
                        <td>${participation.venue}</td>
                        <td>${formattedDate}, ${formattedTime}</td>
                        <td>${participation.participating ? 'Yes' : 'No'}</td>
                        <td>
                            <button class="btn ${participation.participating ? 'btn-danger' : 'btn-success'} btn-sm" 
                                    onclick="toggleParticipation(${participation.performanceId}, ${participation.participating})">
                                ${participation.participating ? 'Opt Out' : 'Opt In'}
                            </button>
                        </td>
                    `;
                    participationTableBody.appendChild(row);
                });
            }
        })
        .catch(error => console.error('Error fetching participations:', error));
});

function toggleParticipation(performanceId, isParticipating) {
    const action = isParticipating ? 'opt-out' : 'opt-in';
    const confirmationMessage = isParticipating
        ? 'Are you sure you want to opt out of this performance?'
        : 'Are you sure you want to opt in to this performance?';

    const confirmAction = confirm(confirmationMessage);
    if (confirmAction) {
        fetch(`/performances/participation/${action}/${performanceId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (response.ok) {
                    alert(`Successfully ${isParticipating ? 'opted out of' : 'opted into'} the performance.`);
                    // Refresh the participations list
                    document.dispatchEvent(new Event('DOMContentLoaded'));
                } else {
                    alert(`Failed to ${isParticipating ? 'opt out of' : 'opt into'} the performance.`);
                }
            })
            .catch(error => console.error(`Error toggling participation:`, error));
    }
}

