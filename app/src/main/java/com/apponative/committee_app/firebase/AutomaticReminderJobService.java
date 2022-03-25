package com.apponative.committee_app.firebase;


import android.os.Bundle;
import android.widget.Toast;

import com.apponative.committee_app.CommitteeApplication;
import com.apponative.committee_app.datamodles.AutomaticReminder;
import com.apponative.committee_app.datamodles.Notification;
import com.apponative.committee_app.datamodles.People;
import com.apponative.committee_app.utils.CommitteeCallBack;
import com.apponative.committee_app.utils.Constants;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;

/**
 * Created by Muhammad Waqas on 7/21/2017.
 */

public class AutomaticReminderJobService extends JobService implements CommitteeCallBack.PaymentPendingPeopleDelegate {

    String admin;

    @Override
    public boolean onStartJob(JobParameters job) {

        admin = job.getExtras().getString("Admin");
        FireBaseDbHandler.getDbHandler(CommitteeApplication.getCurrentActivity()).getAdminCommittees(admin, this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Toast.makeText(this, "Reminder Service Stopped", Toast.LENGTH_LONG).show();
        return false;
    }

    public static void scheduleJobAutomatic(String admin) {
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("Admin", admin);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(CommitteeApplication.getCurrentActivity()));
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(AutomaticReminderJobService.class)
                // uniquely identifies the job
                .setTag("automatic_reminder_service")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 60))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                        , Constraint.DEVICE_IDLE
                ).setExtras(myExtrasBundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }


    public static void cancelAutomaticReminder() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(CommitteeApplication.getCurrentActivity()));
        dispatcher.cancelAll();
    }

    @Override
    public void OnPeopleWithPendingPaymentFilter(ArrayList<AutomaticReminder> automaticReminders, ArrayList<People> peoples) {

        if (peoples.size() > 0) {
            for (int i = 0; i < peoples.size(); i++) {
                FireBaseDbHandler.getDbHandler(CommitteeApplication.getCurrentActivity())
                        .sendAutomaticReminder(admin, new Notification(Constants.NTYPE.P_REMINDER,
                                automaticReminders.get(i).getcId(), "Payment Reminder", "Your Payment for turn "
                                + automaticReminders.get(i).getTurn()
                                + " in committee " + automaticReminders.get(i).getCname() + " is Pending",
                                admin, peoples.get(i).getContactNumber()));
            }
        }
    }
}
