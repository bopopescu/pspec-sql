SELECT *
FROM (
	SELECT i_manufact_id
		,SUM(ss_sales_price) sum_sales
		,AVG(SUM(ss_sales_price)) AS avg_quarterly_sales
	FROM item
	JOIN store_sales
	JOIN date_dim
	JOIN store
	WHERE ss_item_sk = i_item_sk
		AND ss_sold_date_sk = d_date_sk
		AND ss_store_sk = s_store_sk
		AND d_month_seq IN (
			1195
			,1195 + 1
			,1195 + 2
			,1195 + 3
			,1195 + 4
			,1195 + 5
			,1195 + 6
			,1195 + 7
			,1195 + 8
			,1195 + 9
			,1195 + 10
			,1195 + 11
			)
		AND (
			(
				i_category IN (
					'Books'
					,'Children'
					,'Electronics'
					)
				AND i_class IN (
					'personal'
					,'portable'
					,'reference'
					,'self-help'
					)
				AND i_brand IN (
					'scholaramalgamalg #14'
					,'scholaramalgamalg #7'
					,'exportiunivamalg #9'
					,'scholaramalgamalg #9'
					)
				)
			OR (
				i_category IN (
					'Women'
					,'Music'
					,'Men'
					)
				AND i_class IN (
					'accessories'
					,'classical'
					,'fragrances'
					,'pants'
					)
				AND i_brand IN (
					'amalgimporto #1'
					,'edu packscholar #1'
					,'exportiimporto #1'
					,'importoamalg #1'
					)
				)
			)
	GROUP BY i_manufact_id
		,d_qoy
	) tmp1
WHERE CASE 
		WHEN avg_quarterly_sales > 0
			THEN ABS(sum_sales - avg_quarterly_sales) / avg_quarterly_sales
		ELSE NULL
		END > 0.1
ORDER BY avg_quarterly_sales
	,sum_sales
	,i_manufact_id;
