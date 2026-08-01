function showCreatePerformanceForm() {
    document.getElementById('createPerformanceForm').style.display = 'block';
}

function hideCreatePerformanceForm() {
    document.getElementById('createPerformanceForm').style.display = 'none';
}

function submitPerformance() {
    const name = document.getElementById('name').value;
    const venue = document.getElementById('venue').value;
    const dateTime = document.getElementById('dateTime').value;
    const band = document.querySelector('input[name="band"]:checked')?.value;
    const playlistIds = Array.from(document.getElementById('playlist').selectedOptions).map(option => option.value);

    if (!name || !venue || !dateTime || !band) {
        alert('Please fill all fields.');
        return;
    }

    const payload = { name, venue, dateTime, band, playlistIds };

    // Submit the performance data to the backend
    fetch('/performances', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (response.ok) {
            alert('Performance created successfully!');
            hideCreatePerformanceForm();
            // Refresh the list
        } else {
            alert('Failed to create performance.');
        }
    })
    .catch(error => console.error('Error:', error));
}

document.addEventListener('DOMContentLoaded', () => {
    // Fetch all performances and display
    fetch('/performances')
        .then(response => response.json())
        .then(data => {
            const performanceTableBody = document.querySelector('#performanceTable tbody');
            performanceTableBody.innerHTML = '';

            if (data.length === 0) {
                const noDataRow = document.createElement('tr');
                noDataRow.innerHTML = '<td colspan="6" class="text-center">No performances available.</td>';
                performanceTableBody.appendChild(noDataRow);
            } else {
                data.forEach(performance => {
                    const row = document.createElement('tr');
                    const date = new Date(performance.dateTime);
                    const formattedDate = date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
                    const formattedTime = date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });

                    row.innerHTML = `
                        <td>${performance.name}</td>
                        <td>${performance.venue}</td>
                        <td>${formattedDate}, ${formattedTime}</td>
                        <td>${performance.band}</td>
                        <td>
                        ${performance.playlist && performance.playlist.length > 0 ? performance.playlist.map(music => music.item.nameTypeOrTitle).join(', ') // Replace 'nameTypeOrTitle' with the actual property name
                        : 'No playlist'}
                      </td>
                      <td>
                            <button class="btn btn-info btn-sm" onclick="viewMembers(${performance.id})">View Members</button>
                        </td>
                                          `;
                    performanceTableBody.appendChild(row);
                });
            }
        })
        .catch(error => console.error('Error fetching performances:', error));

    // Fetch music for playlist
    fetch('/music')
        .then(response => response.json())
        .then(musicData => {
            const playlistDropdown = document.getElementById('playlist');
            playlistDropdown.innerHTML = ''; // Clear existing options

            musicData.forEach(music => {
                const option = document.createElement('option');
                option.value = music.id;
                option.textContent = `${music.title} by ${music.composer}`;
                playlistDropdown.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching music:', error));
});

function viewMembers(performanceId) {
    fetch(`/performances/${performanceId}/members`)
        .then(response => response.json())
        .then(members => {
            const playerList = document.getElementById('playerList');
            playerList.innerHTML = '';

            members.forEach(member => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = `${member.firstName} ${member.lastName}`;
                playerList.appendChild(li);
            });

            $('#performanceModal').modal('show');
        })
        .catch(error => console.error('Error fetching members:', error));
}

function viewMembers(performanceId) {
    fetch(`/performances/${performanceId}/members`)
        .then(response => response.json())
        .then(members => {
            console.log(members);
            const playerList = document.getElementById('playerList');
            playerList.innerHTML = ''; // Clear any existing list items

            if (members.length === 0) {
                const noMembersItem = document.createElement('li');
                noMembersItem.className = 'list-group-item text-center';
                noMembersItem.textContent = 'No members found for this performance.';
                playerList.appendChild(noMembersItem);
            } else {
                members.forEach(member => {
                    const li = document.createElement('li');
                    li.className = 'list-group-item';
                    li.innerHTML = `
                        <strong>Full Name:</strong> ${member.firstName} ${member.lastName}<br>
                        <strong>Band:</strong> ${member.bandInPractice}
                    `;
                    playerList.appendChild(li);
                });
            }

            // Show the modal
            $('#performanceModal').modal('show');
        })
        .catch(error => console.error('Error fetching member details:', error));
}