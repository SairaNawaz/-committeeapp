package com.apponative.committee_app.firebase;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apponative.committee_app.datamodles.AutomaticReminder;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.CommitteeReference;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.datamodles.User;
import com.apponative.committee_app.ui.MainActivity;
import com.apponative.committee_app.ui.dialogs.CustomDialog;
import com.apponative.committee_app.utils.CAUtility;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.apponative.committee_app.utils.DateUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Muhammad Waqas on 4/19/2017.
 */

public class FireBaseDbHandler {
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mDatabase;
    private static FireBaseDbHandler fireBaseDbHandler;
    static Context context;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public static FireBaseDbHandler getDbHandler(Context context) {
        if (fireBaseDbHandler == null) {
            fireBaseDbHandler = new FireBaseDbHandler();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setLogLevel(Logger.Level.DEBUG);
            firebaseDatabase.setPersistenceEnabled(true);
            mDatabase = firebaseDatabase.getReference();
        }
        FireBaseDbHandler.context = context;

        return fireBaseDbHandler;
    }

    static void activateReferences() {
        mDatabase.child(Constants.TBL_USER).child(MainActivity.signedInUser.getUserId());
        mDatabase.child(Constants.TBL_CONTACTS).child(MainActivity.signedInUser.getUserId() + Constants.FLD_CONTACT).keepSynced(true);
        mDatabase.child(Constants.TBL_USERCOMS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_COMS).keepSynced(true);
    }

    public void setUserProfile(String child, final User object, final CommitteeCallBack.ProfileChangeDelegate profileChangeDelegate) {
        MainActivity.signedInUser = new User();
        MainActivity.signedInUser.setUserId(child);
        mDatabase.child(Constants.TBL_USER).child(child).setValue(object).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                profileChangeDelegate.onProfileSaved(CAUtility.mergeObjects(MainActivity.signedInUser, object));
            }
        });
    }

    public void getUserContactList(final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        Query getContacts = mDatabase.child(Constants.TBL_CONTACTS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_CONTACT).orderByChild("contactUsingApp");
        getContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryResultDelegate.OnQueryResult(dataSnapshot,dataSnapshot.getRef().toString() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getConfirmedUsers(String key, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        Query getConfirmedUsers = mDatabase.child(Constants.TBL_COMS).child(key).child("members_confirmed");
        getConfirmedUsers.keepSynced(true);
        getConfirmedUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryResultDelegate.OnQueryResult(dataSnapshot,dataSnapshot.getRef().toString() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getJoinedUsers(String key, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        Query getJoinedUsers = mDatabase.child(Constants.TBL_COMS).child(key).child("members_joined");
        getJoinedUsers.keepSynced(true);
        getJoinedUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryResultDelegate.OnQueryResult(dataSnapshot,dataSnapshot.getRef().toString() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getMembersbyCommitteeKey(String key, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        Query getJoinedUsers = mDatabase.child(Constants.TBL_COM_MEMBERS).child(key);
        getJoinedUsers.keepSynced(true);
        getJoinedUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryResultDelegate.OnQueryResult(dataSnapshot,dataSnapshot.getRef().toString() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCommitteeMembersByTurn(String key, String turn, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        Query getJoinedUsers = mDatabase.child(Constants.TBL_COM_MEMBERS).child(key).child(turn);
        getJoinedUsers.keepSynced(true);
        getJoinedUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryResultDelegate.OnQueryResult(dataSnapshot, dataSnapshot.getRef().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getCommitteesByStatus(String status, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        DatabaseReference databaseReference = mDatabase.child(Constants.TBL_USERCOMS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_COMS).child(status);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryResultDelegate.OnQueryResult(dataSnapshot, dataSnapshot.getRef().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCommitteeDetails(String key, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        Query getCommitteeDetails = mDatabase.child(Constants.TBL_COMS).child(key);
        getCommitteeDetails.keepSynced(true);
        getCommitteeDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryResultDelegate.OnQueryResult(dataSnapshot,dataSnapshot.getRef().toString() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void declineCommittee(String key, String status, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        mDatabase.child(Constants.TBL_COMS).child(key).child("members_confirmed").child(MainActivity.signedInUser.getUserId()).removeValue();
        mDatabase.child(Constants.TBL_COMS).child(key).child("members_joined").child(MainActivity.signedInUser.getUserId()).removeValue();
        mDatabase.child(Constants.TBL_USERCOMS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_COMS).child(status).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                queryResultDelegate.OnQueryResult(null,Constants.TBL_USERCOMS );
            }
        });
    }

    public void joinCommittee(final String key, final CommitteeReference committeeReference, final CommitteeCallBack.QueryResultDelegate queryResultDelegate) {
        mDatabase.child(Constants.TBL_USERCOMS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_COMS).child(Constants.NEWINVITES).child(key).removeValue();
        mDatabase.child(Constants.TBL_USERCOMS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_COMS).child(Constants.PENDING).child(key).setValue(committeeReference).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                queryResultDelegate.OnQueryResult(null,Constants.TBL_USERCOMS );
                mDatabase.child(Constants.TBL_COMS).child(key).child("members_joined").child(MainActivity.signedInUser.getUserId())
                        .setValue(new People(MainActivity.signedInUser.getUsername(), MainActivity.signedInUser.getUserId(), ""));
                sendGeneralNotification(new Notification(Constants.NTYPE.JOIN, key,
                        "Joined", MainActivity.signedInUser.getUsername() + " has Joined Committee " + committeeReference.getDescription()
                        , MainActivity.signedInUser.getUserId(), committeeReference.getAdmin()));
            }
        });
    }

    public void confirmMember(String key, String cname, People people) {
        mDatabase.child(Constants.TBL_COMS).child(key).child("members_confirmed").child(people.getContactNumber()).setValue(people);
        mDatabase.child(Constants.TBL_COMS).child(key).child("members_joined").child(people.getContactNumber()).removeValue();
        sendGeneralNotification(new Notification(Constants.NTYPE.CONFIRMED, key, "Confirmed", "You are Confirmed in Committee " + cname, MainActivity.signedInUser.getUserId(), people.getContactNumber()));
    }

    public void unConfirmMember(String key, String cname, People people) {
        mDatabase.child(Constants.TBL_COMS).child(key).child("members_confirmed").child(people.getContactNumber()).removeValue();
        mDatabase.child(Constants.TBL_COMS).child(key).child("members_joined").child(people.getContactNumber()).setValue(people);

        sendGeneralNotification(new Notification(Constants.NTYPE.UNCONFIRMED, key, "UnConfirmed", "You are UnConfirmed in Committee " + cname, MainActivity.signedInUser.getUserId(), people.getContactNumber()));
    }

    public void setPaymentStatus(String key, String committeeName, People people, String turn) {
        mDatabase.child(Constants.TBL_COM_MEMBERS).child(key)
                .child(turn).child(people.getContactNumber()).setValue(people);
        sendGeneralNotification(new Notification(Constants.NTYPE.P_STATUS, key,
                "Payment Status", people.isPaymentStatus() ? "Your Payment is Received for turn " + turn + " in committee " + committeeName
                : "Your Payment is Pending for turn " + turn + " in committee "
                + committeeName, MainActivity.signedInUser.getUserId(), people.getContactNumber()));
    }

    public void userProfileCheck(final String currentUserId, final CommitteeCallBack.FireBaseAuthCallBack fireBaseAuthCallBack) {

        mDatabase.child(Constants.TBL_USER).child(currentUserId + "/username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.signedInUser = new User();
                MainActivity.signedInUser.setUserId(currentUserId);

                activateReferences();

                if (dataSnapshot.getValue() != null) {
                    MainActivity.signedInUser.setUsername((String) dataSnapshot.getValue());
                    fireBaseAuthCallBack.SignInSuccess(MainActivity.signedInUser);
                } else {
                    fireBaseAuthCallBack.NewUserRegisteration();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("User Profile", "onCancelled", databaseError.toException());
            }
        });
    }

    public void setUserProfile(final String username, String profileImageUrl,
                               final CommitteeCallBack.ProfileChangeDelegate profileChangeDelegate) {
        if (profileImageUrl != null && !profileImageUrl.toString().equalsIgnoreCase("")) {
            Uri file = Uri.fromFile(new File(profileImageUrl));
            final StorageReference riversRef = mStorageRef.child(Constants.ST_PP + MainActivity.signedInUser.getUserId() + Constants.PP_TYPE);
            riversRef.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        User user = new User(username);
                        setUserProfile(MainActivity.signedInUser.getUserId(), user, profileChangeDelegate);
                    }else{
                        final CustomDialog dialog = new CustomDialog(context, new CommitteeCallBack.DialogInteractionListener() {
                            @Override
                            public void OnButton1Click(Dialog dialog) {

                            }

                            @Override
                            public void OnButton2Click(Dialog dialog) {
                                profileChangeDelegate.onProfileSaved(null);
                            }
                        });
                        dialog.setContent("No Internet", "Please Check Your internet Connection and try again", "", "Ok");
                        dialog.show();
                    }
                }
            });
        } else {
            User user = new User(username);
            setUserProfile(MainActivity.signedInUser.getUserId(), user, profileChangeDelegate);
        }
    }

    public void createCommittee(final Committee committee, final CommitteeCallBack.CreateCommitteCallBacks createCommitteCallBacks) {
        final String pushId = mDatabase.child(Constants.TBL_COMS).push().getKey();
        mDatabase.child(Constants.TBL_COMS).child(pushId).setValue(committee);
        Query createCommittee = mDatabase.child(Constants.TBL_COMS).child(pushId);
        createCommittee.keepSynced(true);
        createCommittee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createCommitteCallBacks.OnCommitteeCreated(pushId, committee.getAdmin(), committee.getC_desc());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateCommitteeStatus(final CommitteeCallBack.CreateCommitteCallBacks createCommitteCallBacks, String status, final String key, final CommitteeReference committeeReference) {
        mDatabase.child(Constants.TBL_USERCOMS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_COMS).child(status).child(key).setValue(committeeReference);
        Query updateStatus = mDatabase.child(Constants.TBL_USERCOMS).child(MainActivity.signedInUser.getUserId()
                + Constants.FLD_COMS).child(status).child(key);
        updateStatus.keepSynced(true);
        updateStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                committeeReference.setCid(key);
                createCommitteCallBacks.OnCommitteeAdminCreated(committeeReference);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUserRegisterationToken(String token) {
        if (MainActivity.signedInUser != null) {
            mDatabase.child(Constants.TBL_USER).child(MainActivity.signedInUser.getUserId()).child(Constants.FLD_TOKEN).setValue(token);
        }
    }

    public void sendNotificationToUser(final CommitteeCallBack.InvitesDelegate createCommitteCallBacks, Notification notification) {
        mDatabase.child(Constants.TBL_INVITENOTICE).child(MainActivity.signedInUser.getUserId()).push().setValue(new Gson().toJson(notification));
        mDatabase.child(Constants.TBL_INVITENOTICE).child(MainActivity.signedInUser.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createCommitteCallBacks.OnInvitesSent();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendGeneralNotification(Notification notification) {
        mDatabase.child(Constants.TBL_NOTIFICATION).child(MainActivity.signedInUser.getUserId())
                .push().setValue(new Gson().toJson(notification));
        mDatabase.child(Constants.TBL_NOTIFICATION).child(MainActivity.signedInUser.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendAutomaticReminder(String admin, Notification notification) {
        mDatabase.child(Constants.TBL_NOTIFICATION).child(admin).push().setValue(new Gson().toJson(notification));
    }
    //Methods for Automatic Reminders

    public void getAdminCommittees(final String admin, final CommitteeCallBack.PaymentPendingPeopleDelegate queryResultDelegate) {

        mDatabase.child(Constants.TBL_USERCOMS).child(admin + Constants.FLD_COMS).child(Constants.ONGOING).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<CommitteeReference> committeeReferences = new ArrayList<CommitteeReference>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommitteeReference committeeReference = dataSnapshot1.getValue(CommitteeReference.class);
                    if (committeeReference.getAdmin().equalsIgnoreCase(admin)) {
                        committeeReference.setCid(dataSnapshot1.getKey());
                        committeeReferences.add(committeeReference);
                    }
                }
                getCommitteeMembers(committeeReferences, queryResultDelegate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void getCommitteeMembers(final ArrayList<CommitteeReference> committeeReferences
            , final CommitteeCallBack.PaymentPendingPeopleDelegate queryResultDelegate) {

        final ArrayList<People> peopleWithPendingPayment = new ArrayList<>();
        final ArrayList<AutomaticReminder> automaticReminders = new ArrayList<>();
        FirebaseMultiQuery firebasemultiquery = new FirebaseMultiQuery();
        for (CommitteeReference committeeReference : committeeReferences) {
            firebasemultiquery.add(mDatabase.child(Constants.TBL_COM_MEMBERS).child(committeeReference.getCid())
                    .child(DateUtils.getDateInFormatedString(new Date())));
        }

        final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebasemultiquery.start();
        allLoad.addOnCompleteListener(new OnCompleteListener<Map<DatabaseReference, DataSnapshot>>() {
            @Override
            public void onComplete(@NonNull Task<Map<DatabaseReference, DataSnapshot>> task) {
                Map<DatabaseReference, DataSnapshot> result = task.getResult();
                for (Map.Entry<DatabaseReference, DataSnapshot> entry : result.entrySet()) {
                    DataSnapshot dataSnapshot = entry.getValue();
                    String key = String.valueOf(entry.getKey()).replaceAll("https://committeeapp-da749.firebaseio.com/committee_members/-", "");
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            People people = dataSnapshot1.getValue(People.class);
                            if (people != null && !people.isPaymentStatus()) {
                                peopleWithPendingPayment.add(people);
                                final String cid = key.replace("/"+dataSnapshot.getKey(), "");
                                ArrayList<CommitteeReference> filtername = new ArrayList<>(Collections2.filter(committeeReferences, new Predicate<CommitteeReference>() {
                                    @Override
                                    public boolean apply(CommitteeReference candidate) {
                                        return candidate.getCid().contains(cid);
                                    }
                                }));
                                String cname = filtername.get(0).getDescription().split("\n")[0];
                                automaticReminders.add(new AutomaticReminder(cid, dataSnapshot.getKey(), cname));
                            }
                        }
                    }
                }
                queryResultDelegate.OnPeopleWithPendingPaymentFilter(automaticReminders,peopleWithPendingPayment);
            }
        });
    }
}
