$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();
	$.post(
        "/community/addPost",
        {"title":title,"content":content},
        function(data){
            data = $.parseJSON(data);
            $("#hintBody").text(data.msg);
            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                if(data.code==1){
                    window.location.reload();
                }else{
                    $("#publishModal").modal("show");
                }
            }, 2000);
        }
	)


}