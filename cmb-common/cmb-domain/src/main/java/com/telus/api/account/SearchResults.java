package com.telus.api.account;

/**
 * Results of search activities. The user of this interface has information about the
 * number of items returned for the particular serach request and if there are more
 * items that satisfy the search criteria but were not retrieved due to the limit on
 * the size of return array.
 *
 * @author  Vladimir Tsitrin
 * @version 1.0, 07/22/05
 * @since 2.757.0.0
 */

public interface SearchResults {
  /**
   * Returns the array of retrieved items satisfying search criteria. Results should be
   * casted to the particular return type before using them.
   *
   * @return array of retrieved items satisfying search criteria.
   */
  Object[] getItems();

  /**
   * Returns the size of the array of retrieved items satisfying search criteria or
   * <tt>0</tt> if the array is <tt>null</tt>. Thus it's safer and easier to get
   * information about the volume of retrieved data using this method instead of
   * checking if <tt>getItems</tt> returns <tt>null</tt> and casting it to the
   * particular return type.
   *
   * @return size of the array of retrieved items.
   */
  int getCount();

  /**
   * Returns <tt>true</tt> if there are more items satisfying the search criteria in
   * addition to ones retrieved by this search.
   *
   * @return <tt>true</tt> if there are more items satisfying the search criteria in
   * addition to ones retrieved by this search; <tt>false</tt> otherwise.
   */
  boolean hasMore();
}
