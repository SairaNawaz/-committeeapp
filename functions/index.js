var admin = require('firebase-admin');
const functions = require('firebase-functions');
admin.initializeApp(functions.config().firebase);


exports.listenForCommitteeRequests = functions.database.ref('/CommitteeRequests/{userId}/{pushId}')
    .onCreate((snapshot, context) => {
//       if (!event.data.exists()) {
//               return;
//       }
       const requestId = JSON.parse(snapshot.val());
//       var type = requestId.type;
       var sentTo = requestId.sentTo;
       var senderIds =  sentTo.split(",");
       var cId = requestId.cid;
       var desc = requestId.desc;
       var c_admin = requestId.admin;
       var payload = {
                data: {
                  title: requestId.title,
                  message:  requestId.message,
                  sentFrom: requestId.sentFrom,
                  type: requestId.type,
                  cid: requestId.cid
                }
        };

       var promises = [];

       var senderTokens = [];
        senderIds.forEach(function (snapshot){
                  var ref = admin.database().ref("users/"+snapshot+"/token");
                  promises.push(ref.once("value" , function(senderTokenVal){
                            senderTokens.push(senderTokenVal);
                  }));
        });

       Promise.all(promises).then(function(){
            var tokenValArray = [];
            senderTokens.forEach(function(tokenVal){
                    tokenValArray.push(tokenVal.val());
            });
            admin.messaging().sendToDevice(tokenValArray, payload)
                    .then(function(response) {
                    senderIds.forEach(function(val){
                        admin.database().ref("user_committees/"+val+"_committees/new_invites/"+cId+"/description").set(desc);
                        admin.database().ref("user_committees/"+val+"_committees/new_invites/"+cId+"/admin").set(c_admin);
                    });
                    console.log("Successfully sent message:", response);
                    return;
            })
             .catch(function(error) {
                      console.log("Error sending message:", error);
            });

          return;
       }).catch(function(error) {
                                                 console.log("Error sending message:", error);
        });
});

exports.listenForGeneralNotifications = functions.database.ref('/generalNotifications/{userId}/{pushId}')
    .onCreate((snapshot, context) => {
//       if (!event.data.exists()) {
//               return;
//       }
       const requestId = JSON.parse(snapshot.val());
       var sentTo = requestId.sentTo;
       var senderIds =  sentTo.split(",");

       var payload = {
                data: {
                  title: requestId.title,
                  message:  requestId.message,
                  sentFrom: requestId.sentFrom,
                  type: requestId.type,
                  cid: requestId.cid
                }
        };

       var promises = [];

       var senderTokens = [];
        senderIds.forEach(function (snapshot){
                  var ref = admin.database().ref("users/"+snapshot+"/token");
                  promises.push(ref.once("value" , function(senderTokenVal){
                            senderTokens.push(senderTokenVal);
                  }));
        });

       Promise.all(promises).then(function(){
            var tokenValArray = [];
            senderTokens.forEach(function(tokenVal){
                    tokenValArray.push(tokenVal.val());
            });
            admin.messaging().sendToDevice(tokenValArray, payload)
                    .then(function(response) {
                    console.log("Successfully sent message:", response);
                    return;
            })
             .catch(function(error) {
                    console.log("Error sending message:", error);
            });
            return;
       }).catch(function(error) {
                                                 console.log("Error sending message:", error);
                               });
});

exports.filterUserContacts = functions.https.onRequest((req, res)=>{

    var body = req.body;
    var contacts = JSON.parse(body.contacts);
    var uid = body.uid;
    var promises = [];

     for(var key in contacts){
         var ref = admin.database().ref("users").child(key);
         promises.push(ref.once("value"));
     }

    Promise.all(promises).then(function(snaps){
        snaps.forEach(function (snap) {
               var qVal = snap.val();
                 if(qVal !== null){
                    contacts[snap.key]["ContactUsingApp"] = 1;
                 }
         });
     admin.database().ref("user_contacts").child(uid+"_contacts").set(contacts);
      res.status(200).send("Success");
      return;
    }).catch(function(error) {
                                              console.log("Error sending message:", error);
                            });
});


exports.listenForNewUsers = functions.database.ref('/users/{pushId}/username')
    .onCreate((snapshot, context) => {
//        const snapshot = event.data;
//        if (!snapshot.exists()) {
//                return;
//        }
//       if (snapshot.previous.exists()) {
//         return;
//       }

       var ref = admin.database().ref("user_contacts");
              ref.once("value", function(querySnapshot1) {
                      querySnapshot1.forEach(function(querySnapshot2) {
                            querySnapshot2.forEach(function(querySnapshot3){
                                if(snapshot.ref.parent.key === querySnapshot3.key){
                                    var pRef = admin.database().ref("users/"+snapshot.ref.parent.key);
                                    pRef.once("value", function(querySnapshot4) {
                                     const snapVal = querySnapshot4.val();
                                     querySnapshot3.ref.child("ContactUsingApp").set(1);
                                    });
                                }

                            });
                      });
              });
});


exports.shareTurnWithMembers = functions.https.onRequest((req, res)=>{

    var body = req.body;
    var members = JSON.parse(body.members);
    var cid = body.cid;
    var details = JSON.parse(body.committeedetails);
    var description = details.description;
    var cname = description.split("\n")[0];
    var sender = body.sender;
    var type = body.type;
    var promises = [];
    var regTokens = [];
    var payloadList = [];
    members.forEach(function (snapshot){
          var cnumber = snapshot.ContactNumber;
          var turn = snapshot.Turn;
          turn = turn.replace(/\//g, "-");

          members.forEach(function (turnSnapshot){
                  var all_cref = admin.database().ref("committee_members/"+cid+"/"+turn+"/"+turnSnapshot.ContactNumber);
                promises.push(all_cref.set(turnSnapshot));
          });

          var user_crefPending =  admin.database().ref("user_committees/"+cnumber+"_committees/pending/"+cid);
          var user_crefOnGoing =  admin.database().ref("user_committees/"+cnumber+"_committees/on_going/"+cid);

          promises.push(user_crefPending.set(null));
          promises.push(user_crefOnGoing.set(details));

           if(cnumber !== sender){
                var tokenref = admin.database().ref("users/"+cnumber+"/token");
                      promises.push(tokenref.once("value" , function(senderTokenVal){
                           regTokens.push(senderTokenVal.val());
                     }));

                     var payload =  {
                                     data: {
                                             title: "Congratulations! "+cname+" Turned Out",
                                             message:  "Congratulations! Your Committee "+cname+" turn out date is "+snapshot.Turn,
                                             sentFrom: sender,
                                             type: type,
                                             cid: cid
                              }
                     };

                     payloadList.push(payload);
           }

   });

     promises.push(admin.database().ref("user_committees/"+sender+"_committees/pending/"+cid).set(null));
     promises.push(admin.database().ref("user_committees/"+sender+"_committees/on_going/"+cid).set(details));

    Promise.all(promises).then(function(){
            for(var i=0; i<regTokens.length; i++){
                admin.messaging().sendToDevice(regTokens[i], payloadList[i])
                                  .then(function(response) {
                                  console.log("Successfully sent message:", response);
                                  return;
                     })
                      .catch(function(error) {
                                  console.log("Error sending message:", error);
                     });
            }
            res.status(200).send("Success");
            return;
    }).catch(function(error) {
                                              console.log("Error sending message:", error);
                            });
});

exports.completeCommittee = functions.https.onRequest((req, res)=>{

    var body = req.body;
    var members = JSON.parse(body.members);
    var cid = body.cid;
    var details = JSON.parse(body.committeedetails);
    var description = details.description;
    var cname = description.split("\n")[0];
    var type = body.type;
    var sender = body.sender;
    var promises = [];
    var regTokens = [];
    var payload =  {
                     data: {
                            title: "Comittee Completed",
                            message: "Your Committee " +cname+" is completed. Stay in touch for the next time.",
                            sentFrom: sender,
                            type: type,
                            cid: cid
                     }
                   };
    members.forEach(function (snapshot){
          var cnumber = snapshot;
          var user_crefOnGoing =  admin.database().ref("user_committees/"+cnumber+"_committees/on_going/"+cid);
          var user_crefCompleted =  admin.database().ref("user_committees/"+cnumber+"_committees/completed/"+cid);
          var tokenref = admin.database().ref("users/"+cnumber+"/token");

          promises.push(user_crefOnGoing.set(null));
          promises.push(user_crefCompleted.set(details));

           promises.push(tokenref.once("value" , function(senderTokenVal){
                regTokens.push(senderTokenVal.val());
          }));

    });

     promises.push(admin.database().ref("user_committees/"+sender+"_committees/on_going/"+cid).set(null));
     promises.push(admin.database().ref("user_committees/"+sender+"_committees/completed/"+cid).set(details));
     promises.push(admin.database().ref("allcommittees/"+cid+"/status").set(1));

    Promise.all(promises).then(function(){
            for(var i=0; i<regTokens.length; i++){
                admin.messaging().sendToDevice(regTokens[i], payload)
                                  .then(function(response) {
                                  console.log("Successfully sent message:", response);
                                  return;
                })
                      .catch(function(error) {
                                  console.log("Error sending message:", error);
                });
            }
            res.status(200).send("Success");
            return;
    }).catch(function(error) {
                                        console.log("Error sending message:", error);
                      });
});