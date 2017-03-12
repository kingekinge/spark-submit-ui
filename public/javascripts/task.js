<!--yarn table-->

  function switchState(state) {
        if(state=='ACCEPTED'){
                return "<span class=\"label label-default\">ACCEPTED</span>";
        }else if(state=='RUNNING'){
                 return "<span class=\"label label-primary\">RUNNING</span>";
        }else if(state=='SUCCESS' || state == 'FINISHED'){
                 return "<span class=\"label label-success\">FINISHED</span>";
        }else if(state=='KILLED'){
                     return "<span class=\"label label-warning\">KILLED</span>";
        }else if(state=='FAILED'){
                     return "<span class=\"label label-danger\">FAILED</span>";
        }
         return state

    }




            $(document).ready(function(){
                $.ajax({
                    type:"GET",
                    url:"/yarn",
                    dataType:"json",
                    success:function(data){
                        var head= "<thead><tr><th>ID</th><th>Name</th><th>App Type</th><th>Queue</th><th>StartTime</th><th>FinishTime</th><th>State</th><th>Kill</th><th>ReRun</th></tr></thead><tbody>"
                        var myobj=eval(data);
                        for (var p in myobj) {
                            var datamid = eval(data[p]);
                            for (var i = 0; i < datamid.length; i++) {
                                head += "<tr><td>" + datamid[i].application_id + "</td><td>" + datamid[i].name + "</td><td>" + datamid[i].apptype + "</td><td>"+datamid[i].queue+ "</td><td>"+new Date(datamid[i].starttime).toLocaleString()+ "</td><td>"+new Date(datamid[i].finishtime).toLocaleString()+ "</td><td>"+switchState(datamid[i].state)+"</td><td><a class=\"edit\" href=\"javascript:;\">" + 'Kill' + "</a></td><td><a class=\"delete\" href=\"javascript:;\">" + 'ReRun' + "</a></td></tr>";
                            };
                        };
                        head += "</tbody>";
                        $('#editable-yarn').html(head);

                            <!--Rerun the task-->
                    $('#editable-yarn a.delete').live('click', function (e) {
                        e.preventDefault();
                            var nRow = $(this).parents('tr')[0];
                              var $id = $(this).parents('tr').find("td:eq(0)").text();


                                        swal({
                                         title: "Determined to run the task?",
                                          text: "Rerun "+$id+" \nMay be coming to an end the running status, whether or not to continue",
                                           type: "warning",
                                             confirmButtonColor: "#DD6B55",
                                            confirmButtonText: "confirm",
                                          cancelButtonText: "cancel",
                                           showCancelButton: true,
                                           closeOnConfirm: false,
                                            showLoaderOnConfirm: true, },
                                          function(){
                                           setTimeout(function(){
                                              $(document).ready(function(){
                                                $.ajax({
                                                    type:"GET",
                                                    url:'/rerun',
                                                    data:{appId:$id},
                                                    success:function(data){

                                                    <!--result process begin-->

                                                     if(data.length>100){
                                                      var $adata=data
                                                            swal({
                                                          title: "Operation is completed",
                                                          text: "Submit Failure",
                                                          type: "error",
                                                          showCancelButton: false,
                                                          confirmButtonText: "confirm",
                                                          closeOnConfirm: true,
                                                          closeOnCancel: false },
                                                          function(isConfirm){
                                                                 if($adata!=null){
                                                                     $adata=$adata.replace(/\n/g,"<br/>");
                                                                 }
                                                                        zeroModal.show({
                                                                              title: 'Spark submited error logs,please check your parameters',
                                                                              content: $adata,
                                                                              width: '50%',
                                                                              height: '80%',
                                                                              overlay: true
                                                                        });


                                                                });
                                                     }else{

                                                                      swal({
                                                          title: "Operation is completed!",
                                                          text: "The task has been submitted,the new generation task id "+data,
                                                          type: "success",
                                                          showCancelButton: false,
                                                          confirmButtonText: "confirm",
                                                          closeOnConfirm: true,
                                                          closeOnCancel: false },
                                                          function(isConfirm){
                                                                    window.location="/tasklist";
                                                                });


                                                     }

                                                    <!--result process end-->



                                                    }
                                                });
                                            });


                                            },
                                           2000); });







                   });




                <!--KILL TASK-->
                $('#editable-yarn a.edit').live('click', function (e) {
                   e.preventDefault();
                            var nRow = $(this).parents('tr')[0];
                              var $id = $(this).parents('tr').find("td:eq(0)").text();


                                     swal({
                                        title: "please confirm?",
                                        text: "determined to end "+$id+"?",
                                        type: "warning",
                                        confirmButtonColor: "#DD6B55",
                                        confirmButtonText: "confirm",
                                        cancelButtonText: "cancel",
                                        showCancelButton: true,
                                        closeOnConfirm: false,
                                         },
                                        function(isConfirm){
                                        if (isConfirm) {

                                            $(document).ready(function(){
                                                $.ajax({
                                                    type:"GET",
                                                    url:"/kill",
                                                    data:{appId:$id},
                                                    success:function(data){
                                                                  swal({
                                                          title: "Operation is completed!",
                                                          text: "Kill Success!",
                                                          type: "success",
                                                          showCancelButton: false,
                                                          confirmButtonText: "confirm",
                                                          closeOnConfirm: true,
                                                          closeOnCancel: false },
                                                          function(isConfirm){
                                                                    window.location="/tasklist";
                                                                });
                                                    }
                                                });
                                            });

                                           }  });
                   });






                    }
                });
            });


<!--standalone table-->

function MillisecondToDate(msd) {
    var time = parseFloat(msd) / 1000;
    if (null != time && "" != time) {
        if (time > 60 && time < 60 * 60) {
            time = parseInt(time / 60.0) + "minutes" + parseInt((parseFloat(time / 60.0) -
                parseInt(time / 60.0)) * 60) + "seconds";
        }
        else if (time >= 60 * 60 && time < 60 * 60 * 24) {
            time = parseInt(time / 3600.0) + "hous" + parseInt((parseFloat(time / 3600.0) -
                parseInt(time / 3600.0)) * 60) + "minutes" +
                parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
                parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
        }
        else {
            time = parseInt(time) + "seconds";
        }
    }
    return time;
}


            $(document).ready(function(){
                $.ajax({
                    type:"GET",
                    url:"/standalone",
                    dataType:"json",
                    success:function(data){
                        var head= "<thead><tr><th>Application ID</th><th>Name</th><th>Cores</th><th>Memory per Node</th><th>Submitted Time</th><th>State</th><th>Duration</th><th>Kill</th><th>ReRun</th></tr></thead><tbody>"
                        var myobj=eval(data);
                        for (var p in myobj) {
                            var datamid = eval(data[p]);
                            for (var i = 0; i < datamid.length; i++) {
                                head += "<tr><td>" + datamid[i].app_id + "</td><td>" + datamid[i].name + "</td><td>" + datamid[i].cores + "</td><td>"+datamid[i].memoryperslave+ "</td><td>"+new Date(datamid[i].starttime).toLocaleString()+ "</td><td>"+switchState(datamid[i].state)+ "</td><td>"+MillisecondToDate(datamid[i].duration)+"</td><td><a class=\"edit\" href=\"javascript:;\">" + 'Kill' + "</a></td><td><a class=\"delete\" href=\"javascript:;\">" + 'ReRun' + "</a></td></tr>";
                            };
                        };
                        head += "</tbody>";
                        $('#editable-standalone').html(head);

                                                <!--KILL TASK-->
                                        $('#editable-standalone a.edit').live('click', function (e) {
                   e.preventDefault();
                            var nRow = $(this).parents('tr')[0];
                              var $id = $(this).parents('tr').find("td:eq(0)").text();


                                     swal({
                                        title: "please confirm?",
                                        text: "determined to end "+$id+"?",
                                        type: "warning",
                                        confirmButtonColor: "#DD6B55",
                                        confirmButtonText: "confirm",
                                        cancelButtonText: "cancel",
                                        showCancelButton: true,
                                        closeOnConfirm: false,
                                         },
                                        function(isConfirm){
                                        if (isConfirm) {

                                            $(document).ready(function(){
                                                $.ajax({
                                                    type:"GET",
                                                    url:'/kill',
                                                    data:{appId:$id},
                                                    complete:function(data){
                                                              swal({
                                                          title: "Operation is completed!",
                                                          text: "Kill Success!",
                                                          type: "success",
                                                          showCancelButton: false,
                                                          confirmButtonText: "confirm",
                                                          closeOnConfirm: true,
                                                          closeOnCancel: false },
                                                          function(isConfirm){
                                                                    window.location="/tasklist";
                                                                });
                                                    }
                                                });
                                            });

                                           }  });
                   });




                    <!--Rerun the task-->
                    $('#editable-standalone a.delete').live('click', function (e) {
                        e.preventDefault();
                            var nRow = $(this).parents('tr')[0];
                              var $id = $(this).parents('tr').find("td:eq(0)").text();

                                        swal({
                                         title: "Determined to run the task?",
                                          text: "Rerun "+$id+" \nMay be coming to an end the running status, whether or not to continue",
                                           type: "warning",
                                             confirmButtonColor: "#DD6B55",
                                            confirmButtonText: "confirm",
                                          cancelButtonText: "cancel",
                                           showCancelButton: true,
                                           closeOnConfirm: false,
                                            showLoaderOnConfirm: true, },
                                          function(){
                                           setTimeout(function(){
                                              $(document).ready(function(){
                                                $.ajax({
                                                    type:"GET",
                                                    url:'/rerun',
                                                    data:{appId:$id},
                                                    success:function(data){
                                                            <!--result process begin-->

                                                     if(data.length>100){
                                                      var $adata=data
                                                            swal({
                                                          title: "Operation is completed",
                                                          text: "submit failure",
                                                          type: "error",
                                                          showCancelButton: false,
                                                          confirmButtonText: "confirm",
                                                          closeOnConfirm: true,
                                                          closeOnCancel: false },
                                                          function(isConfirm){
                                                                 if($adata!=null){
                                                                     $adata=$adata.replace(/\n/g,"<br/>");
                                                                 }
                                                                        zeroModal.show({
                                                                              title: 'Spark submited error logs,please check your parameters',
                                                                              content: $adata,
                                                                              width: '50%',
                                                                              height: '80%',
                                                                              overlay: true
                                                                        });


                                                                });
                                                     }else{

                                                                      swal({
                                                          title: "Operation is completed!",
                                                          text: "The task has been submitted, the new generation taskid "+data,
                                                          type: "success",
                                                          showCancelButton: false,
                                                          confirmButtonText: "confirm",
                                                          closeOnConfirm: true,
                                                          closeOnCancel: false },
                                                          function(isConfirm){
                                                                    window.location="/tasklist";
                                                                });


                                                     }

                                                    <!--result process end-->



                                                    }
                                                });
                                            });


                                            },
                                           2000); });


                   });




                    }
                });
            });

