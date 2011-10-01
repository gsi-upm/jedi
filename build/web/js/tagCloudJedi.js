
$(document).ready(function(){
$("#cloud").tagcloud({type:"list",sizemin:13 }).find("li").tsort();
$("#cloud").tagcloud({sizemin:1, sizemax: 25,height:300});
});

