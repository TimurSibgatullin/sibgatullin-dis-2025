<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Тест</title>
</head>
<body>

<h1>Main page</h1>

<h2></h2>

username: ${username}

<script>
    let jwt_token = localStorage.getItem('jwt_token');

    fetch('/api/me', {
        headers: {
            'Authorization': 'Bearer ' + jwt_token
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Unauthorized");
            return res.json();
        })
        .then(data => {
            document.querySelector("h2").innerText = "Hello, " + data.username;
        })
        .catch(() => {
            window.location = "/login";
        });
</script>

</body>
</html>