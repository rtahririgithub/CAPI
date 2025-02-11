CREATE OR REPLACE PACKAGE BODY data_usage_pkg
AS
--################################################################
-- Procedures/Functions
--################################################################
--------------------------------------------------------------------------
   PROCEDURE getdatausagedetails (
      pi_ban             IN       NUMBER,
      pi_phone_nbr       IN       VARCHAR2,
      pi_service_type    IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_details   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      IF pi_postpaid = numeric_true
      THEN
         OPEN po_usage_details
          FOR
             SELECT   event_time, event_type, event_name, units, amount,
                      NVL (title_nm, ''), NVL (artist_nm, ''),
                      NVL (album_nm, ''), content_size_no,
                      NVL (content_size_uom_cd, ''),
                      NVL (unit_of_measure, ''), event_source,
                      NVL (to_char(mcc_country_cd), ''),
                      NVL (receiver_phone_number, '')
                 FROM rated_ipdr_events_pos
                WHERE ban = pi_ban
                  AND phone_number = pi_phone_nbr
                  AND event_type = pi_service_type
                  AND event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                  AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
             ORDER BY event_time DESC;
      ELSE
         OPEN po_usage_details
          FOR
             SELECT   event_time, event_type, event_name, units, amount,
                      NVL (title_nm, ''), NVL (artist_nm, ''),
                      NVL (album_nm, ''), content_size_no,
                      NVL (content_size_uom_cd, ''),
                      NVL (unit_of_measure, ''), event_source,
                      NVL (to_char(mcc_country_cd), ''),
                      NVL (receiver_phone_number, '')
                 FROM rated_ipdr_events_pre
                WHERE ban = pi_ban
                  AND phone_number = pi_phone_nbr
                  AND event_type = pi_service_type
                  AND event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                  AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
             ORDER BY event_time DESC;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_usage_details%ISOPEN)
         THEN
            CLOSE po_usage_details;
         END IF;

         -- return NULL cursor
         OPEN po_usage_details
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_usage_details%ISOPEN)
         THEN
            CLOSE po_usage_details;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Details. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausagedetails;

--------------------------------------------------------------------------
   PROCEDURE getdatausagesummary (
      pi_ban             IN       NUMBER,
      pi_phone_nbr       IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_summary   OUT      refcursor
   )
   IS
   BEGIN
------------------------------------------------------------------
-- Create reference to a cursor  using SQL statement
------------------------------------------------------------------
      IF pi_postpaid = numeric_true
      THEN
         OPEN po_usage_summary
          FOR
             SELECT   TRUNC (event_time), event_type, SUM (units),
                      SUM (amount), MAX (unit_of_measure)
                 FROM rated_ipdr_events_pos
                WHERE event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                  AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
                  AND ban = pi_ban
                  AND phone_number = pi_phone_nbr
             GROUP BY TRUNC (event_time), event_type
             ORDER BY TRUNC (event_time) DESC, event_type;
      ELSE
         OPEN po_usage_summary
          FOR
             SELECT   TRUNC (event_time), event_type, SUM (units),
                      SUM (amount), MAX (unit_of_measure)
                 FROM rated_ipdr_events_pre
                WHERE event_time >= TO_DATE (pi_from_date, 'MM/DD/YYYY')
                  AND event_time < TO_DATE (pi_to_date, 'MM/DD/YYYY')
                  AND ban = pi_ban
                  AND phone_number = pi_phone_nbr
             GROUP BY TRUNC (event_time), event_type
             ORDER BY TRUNC (event_time) DESC, event_type;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         -- return NULL cursor
         OPEN po_usage_summary
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Summary. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausagesummary;

--------------------------------------------------------------------------------------------------------------------------
   PROCEDURE getdatausagesummary (
      pi_service_types   IN       t_service_type_array,
      pi_ban             IN       NUMBER,
      pi_phone_nbr       IN       VARCHAR2,
      pi_from_date       IN       VARCHAR2,
      pi_to_date         IN       VARCHAR2,
      pi_postpaid        IN       NUMBER,
      po_usage_summary   OUT      refcursor
   )
   IS
      v_cursor_text    VARCHAR2 (32767);
      v_service_type   VARCHAR2 (256);                                --- ???
      v_table_name     VARCHAR2 (25);
      i_index          BINARY_INTEGER;
   BEGIN
      IF (pi_postpaid = numeric_true)
      THEN
         v_table_name := 'rated_ipdr_events_pos';
      ELSE
         v_table_name := 'rated_ipdr_events_pre';
      END IF;

      v_cursor_text :=
            'SELECT  trunc(event_time),'
         || ' event_type, SUM (units), SUM (amount), MAX (unit_of_measure)'
         || ' FROM '
         || v_table_name
         || ' WHERE event_time >= TO_DATE('''
         || pi_from_date
         || ''', ''MM/DD/YYYY'')'
         || ' AND event_time < TO_DATE('''
         || pi_to_date
         || ''', ''MM/DD/YYYY'')'
         || ' AND ban = '
         || pi_ban
         || ' AND phone_number = '''
         || pi_phone_nbr
         || ''''
         || ' and event_type IN (';
      i_index := 1;

      WHILE i_index < pi_service_types.COUNT
      LOOP
         v_service_type := '''' || pi_service_types (i_index) || ''' ';
         v_cursor_text := v_cursor_text || v_service_type || ', ';
         i_index := i_index + 1;
      END LOOP;

      v_service_type := '''' || pi_service_types (i_index) || ''' ';
      v_cursor_text := v_cursor_text || v_service_type;
      v_cursor_text :=
            v_cursor_text
         || ') '
         || ' GROUP BY trunc(event_time), event_type'
         || ' ORDER BY trunc(event_time) DESC, event_type ';

      OPEN po_usage_summary
       FOR v_cursor_text;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         -- return NULL cursor
         OPEN po_usage_summary
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_usage_summary%ISOPEN)
         THEN
            CLOSE po_usage_summary;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Summary. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausagesummary;

   PROCEDURE getdatausageservicetypes (
      po_data_usage_service_types   OUT   refcursor
   )
   IS
   BEGIN
      OPEN po_data_usage_service_types
       FOR
          SELECT service_type, description, french_des, service_type_group_cd
            FROM infranet_service_type;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_data_usage_service_types%ISOPEN)
         THEN
            CLOSE po_data_usage_service_types;
         END IF;

         -- return NULL cursor
         OPEN po_data_usage_service_types
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_data_usage_service_types%ISOPEN)
         THEN
            CLOSE po_data_usage_service_types;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Service Types. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausageservicetypes;

   PROCEDURE getdatausageunits (po_data_usage_units OUT refcursor)
   IS
   BEGIN
      OPEN po_data_usage_units
       FOR
          select dr.DATA_RECORD_UOM_CD, event.unit_of_measure_en_des, event.unit_of_measure_fr_des
          from event_unit_of_measure event, data_record_event_uom dr
          where event.UNIT_OF_MEASURE_CD = dr.UNIT_OF_MEASURE_CD;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         IF (po_data_usage_units%ISOPEN)
         THEN
            CLOSE po_data_usage_units;
         END IF;

         -- return NULL cursor
         OPEN po_data_usage_units
          FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_data_usage_units%ISOPEN)
         THEN
            CLOSE po_data_usage_units;
         END IF;

         raise_application_error (-20160,
                                     'Get Data Usage Units. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getdatausageunits;

   PROCEDURE getMobileCountries (po_mobile_countries OUT refcursor)
   IS
   BEGIN
   	OPEN po_mobile_countries
   		FOR
   			SELECT distinct mcc_country_cd,
   											country.country_nm,
   											country.country_nm_french
   			FROM country,
   			     mobile_country_code
				WHERE  country.country_cd = mobile_country_code.country_cd
								AND mcc_country_cd IS NOT NULL;
		EXCEPTION
			WHEN NO_DATA_FOUND
			THEN
				IF (po_mobile_countries%ISOPEN)
				THEN
					CLOSE po_mobile_countries;
				END IF;

				OPEN po_mobile_countries
				FOR
             SELECT NULL
               FROM DUAL
              WHERE 1 = 0;
      WHEN OTHERS
      THEN
         IF (po_mobile_countries%ISOPEN)
         THEN
            CLOSE po_mobile_countries;
         END IF;

         raise_application_error (-20160,
                                     'Get Mobile Countries. Oracle:(['
                                  || SQLCODE
                                  || '] ['
                                  || SQLERRM
                                  || '])'
                                 );
   END getMobileCountries;

--------------------------------------------------------------------------------------------------------------------------
END;
-- Data_Usage_pkg
/