package com.telus.eas.account.info;

import com.telus.api.account.SearchResults;

import java.io.Serializable;

public class SearchResultsInfo implements SearchResults, Serializable {

   static final long serialVersionUID = 1L;

  private Object[] items;
  private int count;
  private boolean hasMore;

  public Object[] getItems() {
    return items;
  }

  public void setItems(Object[] items) {
    this.items = items;
    this.count = items != null ? items.length : 0;
  }

  public int getCount() {
    return count;
  }

  public boolean hasMore() {
    return hasMore;
  }

  public void setHasMore(boolean hasMore) {
    this.hasMore = hasMore;
  }
}
