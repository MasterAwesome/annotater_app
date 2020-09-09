package com.northeastern.annotaterapp.tagger;

import android.content.Context;
import com.northeastern.annotaterapp.ICallback;

public abstract class IAskDefault implements IAsk {
    protected final ICallback callback;
    protected final Context ctx;

    protected IAskDefault(Context ctx, ICallback callback) {
        this.ctx = ctx;
        this.callback = callback;
    }
}
