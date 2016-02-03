var JsonRpcContainer=function(E,G,F){opensocial.Container.call(this);
var D={};
for(var B in F){if(F.hasOwnProperty(B)){D[B]={};
for(var C=0;
C<F[B].length;
C++){var A=F[B][C];
D[B][A]=true
}}}this.environment_=new opensocial.Environment(G,D);
this.baseUrl_=E;
this.securityToken_=shindig.auth.getSecurityToken()
};
JsonRpcContainer.inherits(opensocial.Container);
JsonRpcContainer.prototype.getEnvironment=function(){return this.environment_
};
JsonRpcContainer.prototype.requestCreateActivity=function(D,B,A){A=A||{};
var C=opensocial.newDataRequest();
var E=new opensocial.IdSpec({userId:"VIEWER"});
C.add(this.newCreateActivityRequest(E,D),"key");
C.send(function(F){A(F.get("key"))
})
};
JsonRpcContainer.prototype.requestData=function(E,I){I=I||{};
var B=E.getRequestObjects();
var G=B.length;
if(G==0){I(new opensocial.DataResponse({},true));
return 
}var J=new Array(G);
var C=0;
for(var D=0;
D<G;
D++){var H=B[D];
if(!H.key){H.key="systemKey"+C;
while(J[H.key]){C++;
H.key="systemKey"+C
}}J[D]=H.request.rpc;
J[D].id=H.key;
if(H.request.postData){J[D].postData=H.request.postData
}}var A=function(V){if(V.errors[0]){JsonRpcContainer.generateErrorResponse(V,B,I);
return 
}V=V.data;
var L=false;
var U={};
for(var P=0;
P<V.length;
P++){V[V[P].id]=V[P]
}for(var M=0;
M<B.length;
M++){var O=B[M];
var N=V[O.key];
var K=N.data;
var S=N.error;
var R="";
if(S){R=S.message
}var Q=O.request.processResponse(O.request,K,S,R);
L=L||Q.hadError();
U[O.key]=Q
}var T=new opensocial.DataResponse(U,L);
I(T)
};
var F={CONTENT_TYPE:"JSON",METHOD:"POST",AUTHORIZATION:"SIGNED",POST_DATA:gadgets.json.stringify(J)};
gadgets.io.makeNonProxiedRequest(this.baseUrl_+"/rpc?st="+encodeURIComponent(shindig.auth.getSecurityToken()),A,F,"application/json")
};
JsonRpcContainer.generateErrorResponse=function(A,D,F){var B=JsonRpcContainer.translateHttpError(A.errors[0]||A.data.error)||opensocial.ResponseItem.Error.INTERNAL_ERROR;
var E={};
for(var C=0;
C<D.length;
C++){E[D[C].key]=new opensocial.ResponseItem(D[C].request,null,B)
}F(new opensocial.DataResponse(E,true))
};
JsonRpcContainer.translateHttpError=function(A){if(A=="Error 501"){return opensocial.ResponseItem.Error.NOT_IMPLEMENTED
}else{if(A=="Error 401"){return opensocial.ResponseItem.Error.UNAUTHORIZED
}else{if(A=="Error 403"){return opensocial.ResponseItem.Error.FORBIDDEN
}else{if(A=="Error 400"){return opensocial.ResponseItem.Error.BAD_REQUEST
}else{if(A=="Error 500"){return opensocial.ResponseItem.Error.INTERNAL_ERROR
}else{if(A=="Error 404"){return opensocial.ResponseItem.Error.BAD_REQUEST
}}}}}}};
JsonRpcContainer.prototype.makeIdSpec=function(A){return new opensocial.IdSpec({userId:A})
};
JsonRpcContainer.prototype.translateIdSpec=function(A){var D=A.getField("userId");
var C=A.getField("groupId");
if(D=="OWNER"){D="@owner"
}else{if(D=="VIEWER"){D="@viewer"
}else{if(opensocial.Container.isArray(A)){for(var B=0;
B<A.length;
B++){}}}}if(C=="FRIENDS"){C="@friends"
}else{if(!C){C="@self"
}}return{userId:D,groupId:C}
};
JsonRpcContainer.prototype.newFetchPersonRequest=function(D,C){var A=this.newFetchPeopleRequest(this.makeIdSpec(D),C);
var B=this;
return new JsonRpcRequestItem(A.rpc,function(E){return B.createPersonFromJson(E)
})
};
JsonRpcContainer.prototype.newFetchPeopleRequest=function(A,C){var D={method:"people.get"};
D.params=this.translateIdSpec(A);
if(C.profileDetail){D.params.fields=C.profileDetail
}if(C.first){D.params.startIndex=C.first
}if(C.max){D.params.count=C.max
}if(C.sortOrder){D.params.orderBy=C.sortOrder
}if(C.filter){D.params.filterBy=C.filter
}if(A.getField("networkDistance")){D.params.networkDistance=A.getField("networkDistance")
}var B=this;
return new JsonRpcRequestItem(D,function(H){var G;
if(H.list){G=H.list
}else{G=[H]
}var F=[];
for(var E=0;
E<G.length;
E++){F.push(B.createPersonFromJson(G[E]))
}return new opensocial.Collection(F,H.startIndex,H.totalResults)
})
};
JsonRpcContainer.prototype.createPersonFromJson=function(A){return new JsonPerson(A)
};
JsonRpcContainer.prototype.getFieldsList=function(A){if(this.hasNoKeys(A)||this.isWildcardKey(A[0])){return[]
}else{return A
}};
JsonRpcContainer.prototype.hasNoKeys=function(A){return !A||A.length==0
};
JsonRpcContainer.prototype.isWildcardKey=function(A){return A=="*"
};
JsonRpcContainer.prototype.newFetchPersonAppDataRequest=function(A,C,B){var D={method:"appdata.get"};
D.params=this.translateIdSpec(A);
D.params.app="@app";
D.params.fields=this.getFieldsList(C);
if(A.getField("networkDistance")){D.params.networkDistance=A.getField("networkDistance")
}return new JsonRpcRequestItem(D,function(E){return opensocial.Container.escape(E.data,B,true)
})
};
JsonRpcContainer.prototype.newUpdatePersonAppDataRequest=function(D,A,B){var C={method:"appdata.update"};
C.params=this.translateIdSpec(this.makeIdSpec(D));
C.params.app="@app";
C.params.data={};
C.params.data[A]=B;
return new JsonRpcRequestItem(C)
};
JsonRpcContainer.prototype.newRemovePersonAppDataRequest=function(C,A){var B={method:"appdata.delete"};
B.params=this.translateIdSpec(this.makeIdSpec(C));
B.params.app="@app";
B.params.keys=A;
return new JsonRpcRequestItem(B)
};
JsonRpcContainer.prototype.newFetchActivitiesRequest=function(A,B){var C={method:"activities.get"};
C.params=this.translateIdSpec(A);
C.params.app="@app";
if(A.getField("networkDistance")){C.params.networkDistance=A.getField("networkDistance")
}return new JsonRpcRequestItem(C,function(E){E=E.list;
var F=[];
for(var D=0;
D<E.length;
D++){F.push(new JsonActivity(E[D]))
}return new opensocial.Collection(F)
})
};
JsonRpcContainer.prototype.newActivity=function(A){return new JsonActivity(A,true)
};
JsonRpcContainer.prototype.newMediaItem=function(C,A,B){B=B||{};
B.mimeType=C;
B.url=A;
return new JsonMediaItem(B)
};
JsonRpcContainer.prototype.newCreateActivityRequest=function(A,B){var C={method:"activities.create"};
C.params=this.translateIdSpec(A);
C.params.app="@app";
if(A.getField("networkDistance")){C.params.networkDistance=A.getField("networkDistance")
}C.params.activity=B.toJsonObject();
return new JsonRpcRequestItem(C)
};
var JsonRpcRequestItem=function(B,A){this.rpc=B;
this.processData=A||function(C){return C
};
this.processResponse=function(C,F,E,D){return new opensocial.ResponseItem(C,E?null:this.processData(F),E,D)
}
};