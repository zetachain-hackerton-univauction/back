INSERT INTO users (user_id) VALUES
                                (1),
                                (2),
                                (3);

INSERT INTO wallets (wallet_id, user_id, wallet_address, token_name, balance) VALUES
                                                                                  (1, 1, '0xA1B2C3D4E5F6A7B8C9D0E1F2A3B4C5D6E7F8G9H0', 'ETH', 12.5),
                                                                                  (2, 2, '0xB1C2D3E4F5G6H7I8J9K0L1M2N3O4P5Q6R7S8T9U0', 'BTC', 5.7),
                                                                                  (3, 3, '0xC1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0', 'USDT', 100.0);

INSERT INTO ideas (idea_id, user_id, title, summary, category, tags, files_url)
VALUES
    (1, 1, 'AI 기반 NFT 경매 플랫폼', 'AI를 활용한 지능형 NFT 경매 서비스', 'Blockchain', 'AI,NFT,Blockchain', 'https://example.com/nft1.png'),
    (2, 2, '친환경 에너지 거래 서비스', 'P2P 에너지 거래를 위한 블록체인 기반 플랫폼', 'Energy', 'GreenEnergy,P2P', 'https://example.com/energy.png'),
    (3, 3, '메타버스 협업 플랫폼', '3D 환경에서 원격 협업을 지원하는 플랫폼', 'Metaverse', 'VR,AR,Collaboration', 'https://example.com/metaverse.png');

INSERT INTO auctions (
    auction_id, idea_id, seller_id, highest_bidder_id, highest_bid, end_date, status
) VALUES
      (1, 1, 1, NULL, 0.0, NOW() + INTERVAL '7 days', 'OPEN'),
      (2, 2, 2, NULL, 0.0, NOW() + INTERVAL '3 days', 'OPEN'),
      (3, 3, 3, NULL, 0.0, NOW() + INTERVAL '14 days', 'OPEN');

INSERT INTO auctions (
    auction_id, idea_id, seller_id, highest_bidder_id, highest_bid, end_date, status
) VALUES
      (1, 1, 1, NULL, 0.0, NOW() + INTERVAL '7 days', 'OPEN'),
      (2, 2, 2, NULL, 0.0, NOW() + INTERVAL '3 days', 'OPEN'),
      (3, 3, 3, NULL, 0.0, NOW() + INTERVAL '14 days', 'OPEN');

INSERT INTO bids (
    bid_id, auction_id, user_id, bid_amount, status, created_at, token_symbol, bidder_wallet_address, comment
) VALUES
      (1, 1, 2, 1.5, 'PLACED', NOW(), 'ETH', '0xB1C2D3E4F5G6H7I8J9K0L1M2N3O4P5Q6R7S8T9U0', '흥미로운 아이디어네요!'),
      (2, 1, 3, 2.5, 'PLACED', NOW(), 'ETH', '0xC1D2E3F4G5H6I7J8K9L0M1N2O3P4Q5R6S7T8U9V0', '투자하고 싶어요!'),
      (3, 2, 1, 3.0, 'PLACED', NOW(), 'BTC', '0xA1B2C3D4E5F6A7B8C9D0E1F2A3B4C5D6E7F8G9H0', '녹색 에너지의 미래가 기대됩니다.');

