CREATE OR REPLACE FUNCTION ean_get_ean_number_range_by_id(
  p_number_range_id integer
)
returns SETOF ean_number_range as
/*
-- $Id$
-- $Id$
-- $HeadURL$
*/
/**
 * Get ean ranges of type or all if type is null.
 *
 * @ExpectedExecutionTime 10 ms
 * @ExpectedExecutionFrequency 10.000 per day
 */
/**  Test
  set search_path=zcat_api_r13_00_07,public;
  select * from ean_get_number_range_for_id(1);
*/
$BODY$
BEGIN
  RETURN QUERY
    SELECT enr_id,
           enr_name,
           enr_active,
           enr_ean_number_range_type,
           enr_start_ean::text,
           enr_end_ean::text,
           enr_increase_by,
           enr_last_ean::text
      FROM zcat_commons.ean_number_range
 WHERE enr_id = p_number_range_id;
END
$BODY$
language plpgsql
    volatile security definer
    cost 100;