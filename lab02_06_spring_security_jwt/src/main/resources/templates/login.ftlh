<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Тест</title>
</head>
<body>
<div id="login_section" style="display:block">
<form id="form" method="post">
    <div><input name="username" type="text" placeholder="username"/></div>
    <div><input name="password" type="password" placeholder="password"/></div>
    <div><input type="button" value="OK" onclick="login()"/></div>
</form>
</div>

<div id="content_section" style="display:none">
    <h>Данные по бронированиям в гостинице:</h>
    <div>
        <table id="booking_table">

        </table>
    </div>
</div>

<div id="booking_data"></div>

<div id="edit_form" style="display:none">
    <h3>Edit booking</h3>

    <input type="hidden" id="booking_id"/>

    <div>
        Arrival: <input type="date" id="arrivaldate"/>
    </div>

    <div>
        Staying: <input type="date" id="stayingdate"/>
    </div>

    <div>
        Departure: <input type="date" id="departuredate"/>
    </div>

    <div>
        Room: <input type="text" id="room"/>
    </div>

    <button onclick="update_booking()">Save</button>
</div>

<script>
    let jwt_token;
    localStorage.setItem('jwt_token', token);
    window.location = '/app';
    function login() {

        const formDataToJson = (formData) => JSON.stringify(Object.fromEntries(formData));
        const formElement = document.getElementById('form');
        const jsonData = formDataToJson(new FormData(formElement));
        console.log(jsonData)

        fetch('http://localhost:8090/api/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json;charset=utf-8'
          },
          body: jsonData
        })
        .then(response => response.json())
        .then(json => {
            let token = json.token;
            jwt_token = token;
            document.getElementById("login_section").style.display="none";
            document.getElementById("content_section").style.display="block";

            show_bookings();
        });
    }

    function show_bookings() {
        fetch('http://localhost:8090/api/booking/allview', {
          method: 'GET',
          headers: {
            'Authorization': 'Bearer ' + jwt_token
          }
        })
        .then(response => response.json())
        .then(json => {
               make_booking_table(json);
        });
    }

    function make_booking_table(data) {
        let tbl = document.getElementById("booking_table");
       for (let i = 0; i < data.bookings.length; i++) {
          let tr = document.createElement('tr');
          let td_room = document.createElement('td');
          td_room.innerHTML = "<span onclick='open_booking("
                + data.bookings[i].id + ")'>" + data.bookings[i].room + "</span>";
          tr.append(td_room);
          let td_arrivaldate = document.createElement('td');
          td_arrivaldate.innerHTML = data.bookings[i].arrivaldate;
          tr.append(td_arrivaldate);
          let td_stayingdate = document.createElement('td');
          td_stayingdate.innerHTML = data.bookings[i].stayingdate;
          tr.append(td_stayingdate);

          tbl.append(tr);
       }
    }

    function open_booking(booking_id) {
        fetch('http://localhost:8090/api/booking/get/' + booking_id, {
          method: 'GET',
          headers: {
            'Authorization': 'Bearer ' + jwt_token
          }
        })
        .then(response => response.json())
        .then(json => {
              fill_booking(json);
        });
    }

    function fill_booking(data) {
        document.getElementById("booking_data").innerHTML = JSON.stringify(data);
    }

    function fill_booking(data) {
        document.getElementById("edit_form").style.display = "block";

        document.getElementById("booking_id").value = data.id;
        document.getElementById("arrivaldate").value = data.arrivaldate;
        document.getElementById("stayingdate").value = data.stayingdate;
        document.getElementById("departuredate").value = data.departuredate;
        document.getElementById("room").value = data.room;
    }

    function update_booking() {

        let id = document.getElementById("booking_id").value;

        let body = {
            arrivaldate: document.getElementById("arrivaldate").value,
            stayingdate: document.getElementById("stayingdate").value,
            departuredate: document.getElementById("departuredate").value,
            room: document.getElementById("room").value
        };

        fetch('http://localhost:8090/api/booking/update/' + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + jwt_token
            },
            body: JSON.stringify(body)
        })
            .then(response => response.text())
            .then(text => {
                alert(text);
                refresh_table();
            });
    }

    function refresh_table() {
        document.getElementById("booking_table").innerHTML = "";
        show_bookings();
    }

</script>


</body>
</html>