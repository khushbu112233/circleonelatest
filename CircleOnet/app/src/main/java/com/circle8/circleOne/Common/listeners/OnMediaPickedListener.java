package com.circle8.circleOne.Common.listeners;

import com.quickblox.q_municate_db.models.Attachment;

public interface OnMediaPickedListener {

    void onMediaPicked(int requestCode, Attachment.Type attachmentType, Object attachment);

    void onMediaPickError(int requestCode, Exception e);

    void onMediaPickClosed(int requestCode);
}