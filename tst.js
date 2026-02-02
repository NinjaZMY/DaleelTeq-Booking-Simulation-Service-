
function load(Object)
{

    fetch(`${API_BASE}/${Object.url}`)
            .then(r => r.json())
            .then(data => {
                const list = document.getElementById(Object.listId);
                //the correct way is data.data which is an array which you didn't fix
                if (!Array.isArray(data.data)) {
                    list.innerHTML = '<li style="color:red;">Error loading employees: ' + JSON.stringify(data) + '</li>';
                    return;
                }
                //the correct way is data.data which is an array which you didn't fix
                list.innerHTML = data.data.map(e => `<li>
                    <input type="checkbox" class="${Object.listId}" data-id="${e.id}"> ID ${e.id}: ${e.lib}
                    <button onclick="editEmployee(${Object.entityDotId})" style="margin-left:8px;">Edit</button>
                    <button onclick="deleteEmployee(${Object.entityDotId})" style="margin-left:6px;">Delete</button>
                </li>`).join('');
            })
            .catch(e => { const list = document.getElementById(Object.listId); list.innerHTML = '<li style="color:red;">Error: ' + e.message + '</li>'; });



}

EmployeesObject = {
    url: 'employees',
    listId: 'employeesList',
    entityDotId: 'e.id'
};

loadEmployees = () => {
    load(EmployeesObject);
}

fetch(`${API_BASE}/employees`)
            .then(r => r.json())
            .then(data => {
                const list = document.getElementById('employeesList');
                if (!Array.isArray(data.data)) {
                    list.innerHTML = '<li style="color:red;">Error loading employees: ' + JSON.stringify(data) + '</li>';
                    return;
                }
                list.innerHTML = data.data.map(e => `<li>
                    <input type="checkbox" class="sel-employee" data-id="${e.id}"> ID ${e.id}: ${e.lib}
                    <button onclick="editEmployee(${e.id})" style="margin-left:8px;">Edit</button>
                    <button onclick="deleteEmployee(${e.id})" style="margin-left:6px;">Delete</button>
                </li>`).join('');
            })
            .catch(e => { const list = document.getElementById('employeesList'); list.innerHTML = '<li style="color:red;">Error: ' + e.message + '</li>'; });