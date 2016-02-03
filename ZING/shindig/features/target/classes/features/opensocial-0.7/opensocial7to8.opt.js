opensocial.Activity.MediaItem=opensocial.MediaItem;
opensocial.newActivityMediaItem=opensocial.newMediaItem;
opensocial.DataRequest.PersonId=opensocial.IdSpec.PersonId;
opensocial.DataRequest.Group={OWNER_FRIENDS:"OWNER_FRIENDS",VIEWER_FRIENDS:"VIEWER_FRIENDS"};
opensocial.DataRequest.prototype.newFetchPeopleRequest_v08=opensocial.DataRequest.prototype.newFetchPeopleRequest;
opensocial.DataRequest.prototype.newFetchPeopleRequest=function(A,B){return this.newFetchPeopleRequest_v08(translateIdSpec(A),B)
};
opensocial.DataRequest.prototype.newFetchPersonAppDataRequest_v08=opensocial.DataRequest.prototype.newFetchPersonAppDataRequest;
opensocial.DataRequest.prototype.newFetchPersonAppDataRequest=function(A,C,B){return this.newFetchPersonAppDataRequest_v08(translateIdSpec(A),C,B)
};
opensocial.DataRequest.prototype.newFetchActivitiesRequest_v08=opensocial.DataRequest.prototype.newFetchActivitiesRequest;
opensocial.DataRequest.prototype.newFetchActivitiesRequest=function(A,C){var B=this.newFetchActivitiesRequest_v08(translateIdSpec(A),C);
B.isActivityRequest=true;
return B
};
opensocial.ResponseItem.prototype.getData_v08=opensocial.ResponseItem.prototype.getData;
opensocial.ResponseItem.prototype.getData=function(){var A=this.getData_v08();
if(this.getOriginalDataRequest()&&this.getOriginalDataRequest().isActivityRequest){return{activities:A}
}return A
};
opensocial.Environment.ObjectType.ACTIVITY_MEDIA_ITEM=opensocial.Environment.ObjectType.MEDIA_ITEM;
opensocial.Person.prototype.getField_v08=opensocial.Person.prototype.getField;
opensocial.Person.prototype.getField=function(A,B){var C=this.getField_v08(A,B);
if(A=="lookingFor"&&C){return C.getDisplayValue()
}else{return C
}};
function translateIdSpec(A){if(A=="OWNER_FRIENDS"){return new opensocial.IdSpec({userId:"OWNER",groupId:"FRIENDS"})
}else{if(A=="VIEWER_FRIENDS"){return new opensocial.IdSpec({userId:"VIEWER",groupId:"FRIENDS"})
}else{return new opensocial.IdSpec({userId:A})
}}};