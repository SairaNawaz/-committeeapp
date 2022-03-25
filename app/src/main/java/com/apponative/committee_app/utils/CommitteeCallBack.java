package com.apponative.committee_app.utils;

import android.app.Dialog;
import android.os.Bundle;

import com.apponative.committee_app.datamodles.AutomaticReminder;
import com.apponative.committee_app.datamodles.Committee;
import com.apponative.committee_app.datamodles.CommitteeReference;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.datamodles.User;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * Created by Muhammad Waqas on 4/19/2017.
 */

public class CommitteeCallBack {

    public interface FireBaseAuthCallBack {
        void SignInSuccess(User user);

        void NewUserRegisteration();
    }

    public interface ProfileChangeDelegate {
        void onProfileSaved(User user);
    }

    public interface FragmentDelegate {
        void OnMenuItemSelected(int tagId, Bundle b);
    }

    public interface QueryResultDelegate {
        void OnQueryResult(DataSnapshot dataSnapshot, String queryRef);
    }

    public interface CreateCommitteCallBacks {

        void OnC1Next(Committee committee);

        void OnC2Next(String selectedContacts);

        void OnC3Submit();

        void OnCommitteeCreated(String key, String admin, String description);

        void OnCommitteeAdminCreated(CommitteeReference committeeReference);
    }

    public interface InvitesDelegate {
        void OnInvitesSent();
    }

    public interface CommitteeUpdateCallBacks {
        void OnUserUpdate(boolean isConfirmed, ArrayList<People> members);
    }

    public interface CommitteeRandomDrawCallBacks {
        void OnTurnShared(boolean result);
    }

    public interface CommitteeCompleteCallBack {
        void OnAllMembersTurned(boolean AllTurned);

        void OnCommitteeComplete(boolean result);
    }

    public interface NotificationCallBack {
        void OnNotificationReceived();
    }

    public interface DialogInteractionListener {

        void OnButton1Click(Dialog dialog);

        void OnButton2Click(Dialog dialog);
    }

    public interface DialogDayChooserListener {
        void OnDaySelected(String dayid);
    }

    public interface NotificationInterfaceListener {
        void OnNotificationSelected(int id);

        void OnClearAllClick();
    }

    public interface PaymentPendingPeopleDelegate {
        void OnPeopleWithPendingPaymentFilter(ArrayList<AutomaticReminder> automaticReminders, ArrayList<People> peoples);
    }
}
