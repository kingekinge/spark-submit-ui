 var host = window.location.host
 var wsUri = "ws://" + host + "/";
 var websocket = null
  var msg;


   function initWebSocket()
   {
     count = document.getElementById("count")
     createWebSocket();
   }

   function createWebSocket()
   {
     websocket = new WebSocket(wsUri+"startpush");
     websocket.onopen = function(evt) { onOpen(evt) };
     websocket.onclose = function(evt) { onClose(evt) };
     websocket.onmessage = function(evt) { onMessage(evt) };
     websocket.onerror = function(evt) { onError(evt) };
   }

   function onOpen(evt)
   {
     writeToScreen("CONNECTED");
     doSend("WebSocket rocks");
   }

   function onClose(evt)
   {
     writeToScreen("DISCONNECTED");
   }

   function onMessage(evt)
   {
     writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data+'</span>');
      showmessage();
   }

   function showmessage(){
                  $.ajax({
                                     type:"GET",
                                     url:"/msglist",
                                     dataType:"json",
                                     success:function(data){
                                     var head= ""
                                     var $count = 0;
                                     var myobj=eval(data);
                                     for (var p in myobj) {
                                         var datamid = eval(data[p]);
                                         for (var i = 0; i < datamid.length; i++) {
                                             $count++;
                                             head +="<li class=\"notification-warning\"><a  href=\"/read?appId="+datamid[i].id+"\"><i class=\"fa-envelope-o\"></i><span class=\"line\">"+"任务运行结束,当前状态 "+datamid[i].state+"</span><span class=\"line small time\">"+"ID "+datamid[i].id+"</span></a></li>"

                                         };
                                     };

                                     $('#msglist').html(head);
                                     if($count!=0){
                                      $('#count').html($count);
                                       }
                                      $('#mes').html($count);


                                     }
                                 });

   }

   function onError(evt)
   {
     writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
   }

   function doSend(message)
   {
     writeToScreen("SENT: " + message);
     websocket.send(message);
   }

   function writeToScreen(message)
   {
//     var pre = document.createElement("p");
//     pre.style.wordWrap = "break-word";
//     pre.innerHTML = message;
//     output.appendChild(pre);
   }

  window.addEventListener("load", initWebSocket, false);