-- 1. 임의의 값을 계산하기 위한 사용자 변수를 설정합니다.
SET @i = 0; -- 계좌번호 구분을 위한 카운터 (id와는 별개)
SET @customer_count = 200; -- 총 고객 수 (200명으로 가정)

INSERT INTO account_balance  (
    -- id는 AUTO_INCREMENT이므로 여기에 포함시키지 않습니다.
    customer_id,
    account_no,
    deposit,
    eval_amount,
    profit_rate
)
SELECT
    -- customerId (200명 중 랜덤 할당)
    CONCAT('CUST_', LPAD(FLOOR(1 + (RAND() * @customer_count)), 4, '0')),

    -- accountNo (계좌번호)
    CONCAT('ACC_', LPAD(@i := @i + 1, 10, '0')),

    -- deposit (예수금: 1백만 ~ 100억 사이)
    @deposit := FLOOR(1000000 + (RAND() * 9999000000)),

    -- evalAmount (평가금액: 예수금의 0.9 ~ 1.2배 사이)
    @evalAmount := FLOOR(@deposit * (0.9 + (RAND() * 0.3))),

    -- profitRate (수익률 계산 및 포맷팅)
    CASE
        WHEN ((@evalAmount - @deposit) / @deposit) * 100 >= 0
        THEN CONCAT('+', FORMAT(((@evalAmount - @deposit) / @deposit) * 100, 2), '%')
        ELSE CONCAT(FORMAT(((@evalAmount - @deposit) / @deposit) * 100, 2), '%')
END
FROM
    -- 1024개의 행을 생성하는 가상 테이블 조합
    (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) a
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
          UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) b
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) c
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) d
LIMIT 1000; -- 정확히 1000건만 삽입



-- 1. 임의의 값을 계산하기 위한 사용자 변수를 설정합니다.
SET @i = 0; -- 상품명 구분을 위한 카운터

-- 2. INSERT INTO ... SELECT 구문을 이용하여 1000건의 데이터를 생성합니다.
INSERT INTO financial_product (
    -- id는 AUTO_INCREMENT이므로 제외
    product_name,
    interest_rate,
    min_amount,
    maturity,
    risk_level
)
SELECT
    -- productName (상품명: "정기예금_0001", "펀드_0002" 등)
    CONCAT(
            CASE FLOOR(RAND() * 3) -- 0: 예금, 1: 적금, 2: 펀드
                WHEN 0 THEN '정기예금_'
                WHEN 1 THEN '자유적금_'
                ELSE '투자펀드_'
                END,
            LPAD(@i := @i + 1, 4, '0')
    ),

    -- interestRate (금리: 0.5% ~ 8.0% 사이 랜덤)
    ROUND(0.5 + (RAND() * 7.5), 2),

    -- minAmount (최소가입금액: 10만, 100만, 500만 중 랜덤)
    CASE FLOOR(RAND() * 3)
        WHEN 0 THEN 100000
        WHEN 1 THEN 1000000
        ELSE 5000000
        END,

    -- maturity (만기: "6개월", "1년", "3년" 중 랜덤)
    CASE FLOOR(RAND() * 3)
        WHEN 0 THEN '6개월'
        WHEN 1 THEN '1년'
        ELSE '3년'
        END,

    -- riskLevel (위험등급: "낮음", "보통", "높음" 중 랜덤)
    CASE FLOOR(RAND() * 3)
        WHEN 0 THEN '낮음'
        WHEN 1 THEN '보통'
        ELSE '높음'
        END
FROM
    -- 1024개의 행을 생성하는 가상 테이블 조합
    (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) a
        JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) b
        JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) c
        JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) d
    LIMIT 1000; -- 정확히 1000건만 삽입



-- 1. 임의의 값을 계산하기 위한 사용자 변수를 설정합니다.
SET @i = 0; -- 카운터 (id와는 별개)
SET @max_ratio = 100.0; -- 최대 비중

-- 2. INSERT INTO ... SELECT 구문을 이용하여 1000건의 데이터를 생성합니다.
INSERT INTO portfolio_summary  (
    asset_type,
    ratio,
    current_value,
    target_ratio,
    rebalance_needed
)
SELECT
    -- assetType (자산군: 주식, 채권, 현금, 대체투자 중 랜덤)
    @assetType := CASE FLOOR(RAND() * 4)
        WHEN 0 THEN '주식'
        WHEN 1 THEN '채권'
        WHEN 2 THEN '현금'
        ELSE '대체투자'
END,

    -- ratio (현재 비중: 1.0% ~ 50.0% 사이 랜덤)
    @ratio := ROUND(1.0 + (RAND() * 49.0), 2),

    -- currentValue (현재가치: 100만 ~ 1억 사이 랜덤)
    FLOOR(1000000 + (RAND() * 99000000)),

    -- targetRatio (목표 비중: 5.0% ~ 40.0% 사이 랜덤)
    @targetRatio := ROUND(5.0 + (RAND() * 35.0), 2),

    -- rebalanceNeeded (리밸런싱 필요여부: 현재비중과 목표비중의 차이가 5.0% 이상이면 '필요')
    CASE
        WHEN ABS(@ratio - @targetRatio) >= 5.0 THEN '필요'
        ELSE '필요없음'
END
FROM
    -- 1024개의 행을 생성하는 가상 테이블 조합
    (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) a
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
          UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) b
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) c
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) d
LIMIT 1000; -- 정확히 1000건만 삽입



-- 1. 임의의 값을 계산하기 위한 사용자 변수를 설정합니다.
SET @i = 0; -- 종목코드 및 종목명 구분을 위한 카운터

-- 2. INSERT INTO ... SELECT 구문을 이용하여 1000건의 데이터를 생성합니다.
INSERT INTO stock_status (
    stock_code,
    stock_name,
    current_price,
    change_rate,
    volume
)
SELECT
    -- 1. stock_code (종목코드: A000001 부터 A001000까지)
    CONCAT('A', LPAD(@i := @i + 1, 6, '0')),

    -- 2. stock_name (종목명: '테스트기업_0001', '테스트기업_0002' 등)
    CONCAT('테스트기업_', LPAD(@i, 4, '0')),

    -- 3. current_price (현재가: 1,000원 ~ 500,000원 사이 랜덤)
    @price := FLOOR(1000 + (RAND() * 499000)),

    -- 4. change_rate (전일대비 문자열 포맷팅)
    --    주의: @rate 변수 할당을 이전에 처리해야 합니다.
    --    (MySQL은 변수 할당 순서가 왼쪽에서 오른쪽이 아닐 수 있으므로,
    --     여기서는 @price 할당 옆에 @rate 할당을 숨겨서 순서를 확보하는 트릭을 사용합니다.)
    cast(CASE
        WHEN (@rate := ROUND((RAND() * 10.0) - 5.0, 2)) >= 0
        THEN CONCAT('+', FORMAT(@rate, 2))
        ELSE CONCAT(FORMAT(@rate, 2))
END AS DECIMAL(5,2)),

    -- 5. volume (거래량: 1천 주 ~ 1백만 주 사이 랜덤)
    FLOOR(1000 + (RAND() * 999000))

FROM
    -- 1024개의 행을 생성하는 가상 테이블 조합
    (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) a
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
          UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) b
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) c
    JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) d
LIMIT 1000; -- 정확히 1000건만 삽입






-- 1. 임의의 값을 계산하기 위한 사용자 변수를 설정합니다.
SET @i = 0; -- 종목명 구분을 위한 카운터

-- 2. INSERT INTO ... SELECT 구문을 이용하여 1000건의 데이터를 생성합니다.
INSERT INTO  trade_history  (
    -- id는 AUTO_INCREMENT이므로 제외
    trade_date,
    stock_name,
    trade_type,
    quantity,
    price
)
SELECT
    -- tradeDate (거래일자: 오늘로부터 365일 이내의 랜덤 날짜)
    DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 365) DAY),

    -- stockName (종목명: '테스트기업_0001' 등)
    CONCAT('테스트기업_', LPAD(@i := @i + 1, 4, '0')),

    -- tradeType (구분: '매수' 또는 '매도' 랜덤)
    CASE FLOOR(RAND() * 2)
        WHEN 0 THEN '매수'
        ELSE '매도'
        END,

    -- quantity (수량: 1주 ~ 100주 사이 랜덤)
    FLOOR(1 + (RAND() * 100)),

    -- price (체결가: 1,000원 ~ 100,000원 사이 랜덤)
    FLOOR(1000 + (RAND() * 99000))
FROM
    -- 1024개의 행을 생성하는 가상 테이블 조합
    (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
     UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) a
        JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) b
        JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) c
        JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) d
    LIMIT 1000; -- 정확히 1000건만 삽입