package com.example.maiquynhtruong.heathyreminders.Services;

import android.os.AsyncTask;

import com.example.maiquynhtruong.heathyreminders.ReminderTask;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ReminderFirebaseJobService extends JobService {
    AsyncTask mBackgroundTask;
    @Override
    public boolean onStartJob(final JobParameters job) {
         mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                // issue a reminder
//                ReminderTask.executeTask(ReminderFirebaseJobService.this, job.getTag()); // job.getTag() is later to tell which task is which
                ReminderTask.executeTask(ReminderFirebaseJobService.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute(job);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
