                                var FILENAME="";
                                jQuery(document).ready(function($)
                                {
                                    var i = 1,
                                            $example_dropzone_filetable = $("#example-dropzone-filetable"),
                                            example_dropzone = $("#advancedDropzone").dropzone({
                                                url: '/sparkjar',

                                                // Events
                                                addedfile: function(file)

                                                {
                                                    FILENAME = file.name;
                                                    var location = "/tmp/file/" + FILENAME;
                                                    $("#jarlocation").val(location).removeClass("alert-success").removeClass("alert-danger").addClass("alert-success");

                                                    if(i == 1)
                                                    {
                                                        $example_dropzone_filetable.find('tbody').html('');
                                                    }

                                                    var size = parseInt(file.size/1024, 10);
                                                    size = size < 1024 ? (size + " KB") : (parseInt(size/1024, 10) + " MB");

                                                    var $el = $('<tr>\
                                                    <td class="text-centers">'+(i++)+'</td>\
                                                    <td>'+file.name+'</td>\
                                                    <td><div class="progress progress-striped"><div class="progress-bar progress-bar-warning"></div></div></td>\
                                                    <td>'+size+'</td>\
                                                    <td>Uploading...</td>\
                                                </tr>');


                                                    $example_dropzone_filetable.find('tbody').append($el);
                                                    file.fileEntryTd = $el;
                                                    file.progressBar = $el.find('.progress-bar');
                                                },

                                                uploadprogress: function(file, progress, bytesSent)
                                                {
                                                    file.progressBar.width(progress + '%');
                                                },

                                                success: function(file)
                                                {
                                                    file.fileEntryTd.find('td:last').html('<span class="alert-success">Uploaded successfully!</span>');
                                                    file.progressBar.removeClass('progress-bar-warning').addClass('progress-bar-success');
                                                },

                                                error: function(file)
                                                {
                                                    file.fileEntryTd.find('td:last').html('<span class="alert-danger">Upload failed!</span>');
                                                    file.progressBar.removeClass('progress-bar-warning').addClass('progress-bar-red');
                                                    $("#jarlocation").val("Invalid file!Upload failed!!");
                                                    $("#jarlocation").addClass("alert-danger");
                                                }
                                            });

                                    $("#advancedDropzone").css({
                                        minHeight: 200
                                    });


                                    $("#submit").click(function(){
                                        var $adata=null;
                                        //$("#jarForm").submit();
                                             var $mode = $("#masters").find("option:selected").val();
                                            swal({
                                            title: "submit the jar?",
                                            text: "The current submit mode "+$mode,
                                            type: "info",
                                            confirmButtonText: "confirm",
                                            cancelButtonText: "cancel",
                                            showCancelButton: true,
                                            closeOnConfirm: false,
                                            showLoaderOnConfirm: true,
                                             }, function(){
                                              setTimeout(function(){
                                                 $('#jarForm').ajaxSubmit(
                                                  function(data){
                                                     if(data!="Task submitted successfully!"){
                                                      $adata=data
                                                            swal({
                                                          title: "Operation is completed",
                                                          text: "failure",
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
                                                          title: "Operation is completed",
                                                          text: data+" /Task Management  See the task details",
                                                          type: "success",
                                                          showCancelButton: false,
                                                          confirmButtonText: "confirm",
                                                          closeOnConfirm: true,
                                                          closeOnCancel: false },
                                                          function(isConfirm){
                                                                    window.location="/tasklist";
                                                                });

                                                     }

                                                    }
                                                    );


                                                }, 5000);

                                               });




                                      });
                                });