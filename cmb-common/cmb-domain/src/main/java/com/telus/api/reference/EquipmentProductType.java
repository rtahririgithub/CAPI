package com.telus.api.reference;


public interface EquipmentProductType extends Reference{

   String getProductCode() ;
   long   getProductTypeID() ;
   long   getProductGroupTypeID();
}