SELECT i_manufact_id
	,SUM(total_sales) total_sales
FROM (
	SELECT *
	FROM (
		SELECT i_manufact_id
			,SUM(ss_ext_sales_price) total_sales
		FROM store_sales
		JOIN date_dim
		JOIN customer_address
		JOIN item ON i_manufact_id = item.i_manufact_id
		WHERE ss_item_sk = i_item_sk
			AND ss_sold_date_sk = d_date_sk
			AND d_year = 2000
			AND d_moy = 2
			AND ss_addr_sk = ca_address_sk
			AND ca_gmt_offset = - 5
		GROUP BY i_manufact_id
		) ss
	
	UNION ALL
	
	SELECT *
	FROM (
		SELECT i_manufact_id
			,SUM(cs_ext_sales_price) total_sales
		FROM catalog_sales
		JOIN date_dim
		JOIN customer_address
		JOIN item ON i_manufact_id = item.i_manufact_id
		WHERE cs_item_sk = i_item_sk
			AND cs_sold_date_sk = d_date_sk
			AND d_year = 2000
			AND d_moy = 2
			AND cs_bill_addr_sk = ca_address_sk
			AND ca_gmt_offset = - 5
		GROUP BY i_manufact_id
		) cs
	
	UNION ALL
	
	SELECT *
	FROM (
		SELECT i_manufact_id
			,SUM(ws_ext_sales_price) total_sales
		FROM web_sales
		JOIN date_dim
		JOIN customer_address
		JOIN item ON i_manufact_id = item.i_manufact_id
		WHERE ws_item_sk = i_item_sk
			AND ws_sold_date_sk = d_date_sk
			AND d_year = 2000
			AND d_moy = 2
			AND ws_bill_addr_sk = ca_address_sk
			AND ca_gmt_offset = - 5
		GROUP BY i_manufact_id
		) ws
	) tmp1
GROUP BY i_manufact_id
ORDER BY total_sales;
