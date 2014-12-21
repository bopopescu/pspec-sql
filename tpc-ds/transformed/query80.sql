SELECT channel
	,id
	,SUM(sales) AS sales
	,SUM(RETURNS) AS
RETURNS
	,SUM(profit) AS profit
FROM (
	SELECT 'store channel' AS channel
		,CONCAT (
			'store'
			,store_id
			) AS id
		,sales
		,
	RETURNS
		,profit
	FROM (
		SELECT s_store_id AS store_id
			,SUM(ss_ext_sales_price) AS sales
			,SUM(COALESCE(sr_return_amt, 0)) AS
		RETURNS
			,SUM(ss_net_profit - COALESCE(sr_net_loss, 0)) AS profit
		FROM store_sales
		LEFT OUTER JOIN store_returns ON (
				ss_item_sk = sr_item_sk
				AND ss_ticket_number = sr_ticket_number
				)
		JOIN date_dim
		JOIN store
		JOIN item
		JOIN promotion
		WHERE ss_sold_date_sk = d_date_sk
			AND unix_timestamp(d_date, 'yyyy-MM-dd') >= unix_timestamp('1999-08-25', 'yyyy-MM-dd')
			AND unix_timestamp(d_date, 'yyyy-MM-dd') <= unix_timestamp('1999-09-25', 'yyyy-MM-dd')
			AND ss_store_sk = s_store_sk
			AND ss_item_sk = i_item_sk
			AND i_current_price > 50
			AND ss_promo_sk = p_promo_sk
			AND p_channel_tv = 'N'
		GROUP BY s_store_id
		) ssr
	
	UNION ALL
	
	SELECT 'catalog channel' AS channel
		,CONCAT (
			'catalog_page'
			,catalog_page_id
			) AS id
		,sales
		,
	RETURNS
		,profit
	FROM (
		SELECT cp_catalog_page_id AS catalog_page_id
			,SUM(cs_ext_sales_price) AS sales
			,SUM(COALESCE(cr_return_amount, 0)) AS
		RETURNS
			,SUM(cs_net_profit - COALESCE(cr_net_loss, 0)) AS profit
		FROM catalog_sales
		LEFT OUTER JOIN catalog_returns ON (
				cs_item_sk = cr_item_sk
				AND cs_order_number = cr_order_number
				)
		JOIN date_dim
		JOIN catalog_page
		JOIN item
		JOIN promotion
		WHERE cs_sold_date_sk = d_date_sk
			AND unix_timestamp(d_date, 'yyyy-MM-dd') >= unix_timestamp('1999-08-25', 'yyyy-MM-dd')
			AND unix_timestamp(d_date, 'yyyy-MM-dd') <= unix_timestamp('1999-09-25', 'yyyy-MM-dd')
			AND cs_catalog_page_sk = cp_catalog_page_sk
			AND cs_item_sk = i_item_sk
			AND i_current_price > 50
			AND cs_promo_sk = p_promo_sk
			AND p_channel_tv = 'N'
		GROUP BY cp_catalog_page_id
		) csr
	
	UNION ALL
	
	SELECT 'web channel' AS channel
		,CONCAT (
			'web_site'
			,web_site_id
			) AS id
		,sales
		,
	RETURNS
		,profit
	FROM (
		SELECT web_site_id
			,SUM(ws_ext_sales_price) AS sales
			,SUM(COALESCE(wr_return_amt, 0)) AS
		RETURNS
			,SUM(ws_net_profit - COALESCE(wr_net_loss, 0)) AS profit
		FROM web_sales
		LEFT OUTER JOIN web_returns ON (
				ws_item_sk = wr_item_sk
				AND ws_order_number = wr_order_number
				)
		JOIN date_dim
		JOIN web_site
		JOIN item
		JOIN promotion
		WHERE ws_sold_date_sk = d_date_sk
			AND unix_timestamp(d_date, 'yyyy-MM-dd') >= unix_timestamp('1999-08-25', 'yyyy-MM-dd')
			AND unix_timestamp(d_date, 'yyyy-MM-dd') <= unix_timestamp('1999-09-25', 'yyyy-MM-dd')
			AND ws_web_site_sk = web_site_sk
			AND ws_item_sk = i_item_sk
			AND i_current_price > 50
			AND ws_promo_sk = p_promo_sk
			AND p_channel_tv = 'N'
		GROUP BY web_site_id
		) wsr
	) x
GROUP BY channel
	,id
ORDER BY channel
	,id;
