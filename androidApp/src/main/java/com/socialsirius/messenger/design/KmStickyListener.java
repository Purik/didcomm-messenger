package com.socialsirius.messenger.design;

import android.view.View;

public interface KmStickyListener {
    Integer getHeaderPositionForItem(Integer itemPosition);

    Integer getHeaderLayout(Integer headerPosition);

    void bindHeaderData(View header, Integer headerPosition);

    Boolean isHeader(Integer itemPosition);
}