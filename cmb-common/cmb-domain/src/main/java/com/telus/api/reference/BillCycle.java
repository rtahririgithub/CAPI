package com.telus.api.reference;

/**
  * Represents a billing cycle.
  */
public interface BillCycle extends Reference {

   /**
     * Returns the cycle code.  The code identifies this cycle (it is the primary key).
     *
     * @return String
     */
   String getCode();

   /**
     * Returns the close day.  This is the day of the month the bill will be closed (the last
     * day usage and other charges will get associated with the bill).
     *
     * @return int
     */
   int getCloseDay();

   /**
     * Returns the day the bill is due.
     *
     * @return int
     */
   int getDueDay();

   /**
     * Returns the cycle description.
     *
     * @return String
     */
   String getDescription();

   /**
     * Returns the allocation indicator code
     *
     * @return String
     */
   String getAllocationIndicator();

   /**
     * Returns the population code.
     *
     * @return String
     */
   String getPopulationCode();

   /**
     * Returns the bill day
     *
     * @return int
     */
   int getBillDay();

   /**
     * Returns the number of accounts allocated to this cycle.
     *
     * @return long
     */
   long getNumberOfAllocatedAccounts();

   /**
     * Returns the number of subscribers allocated to this cycle.
     *
     * @return long
     */
   long getNumberOfAllocatedSubscribers();

   /**
     * Returns the weight of this cycle.
     *
     * long
     */
   long getWeight();

}