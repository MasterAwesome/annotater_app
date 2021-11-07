/*
 * Copyright (C) 2020 Arvind Mukund <armu30@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.northeastern.annotaterapp.workers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.northeastern.annotaterapp.services.AskService;

/**
 * Nice little worker that starts a foreground service and then it's entire purpose in life is
 * over.
 */
public class ListenWorker extends Worker {
    private final Context mContext;
    private static final String LOG_TAG = ListenWorker.class.getSimpleName();

    /**
     * Constructs a ListenWorker object
     *
     * @param context      context for starting a foreground service.
     * @param workerParams voodoo parameter.
     */
    public ListenWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        Log.d(LOG_TAG, "ListenWorker");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(LOG_TAG, "doWork");
        Intent listenerService = new Intent(mContext, AskService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(listenerService);
        } else {
            mContext.startService(listenerService);
        }
        return Result.success();
    }
}
