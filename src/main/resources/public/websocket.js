/**
 * PeaceGatch
 * Created by luknw on 17.01.2017
 */

//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/");
webSocket.onopen = function () {
    var username = prompt("Choose nick:", "Username");
    webSocket.send("/newUser:" + username);
    // document.cookie = "username=" + username;
};
webSocket.onmessage = function (msg) {
    updateChat(msg);
};
webSocket.onclose = function () {
    alert("Disconnected")
};

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) {
        sendMessage(e.target.value);
    }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    var data = JSON.parse(msg.data);
    id("channelName").innerHTML = data.currentChannel;
    insert("chat", data.userMessage);
    id("userlist").innerHTML = "";

    data.userList.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}