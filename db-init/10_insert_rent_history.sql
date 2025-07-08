INSERT INTO book_manager.rent_history (user_id,book_item_id,admin_id,rental_date,expected_return_date,status,description) VALUES
                                                                                                                              (2,1,1,'2025-07-08 12:00:41','2025-07-08','RETURNED','3회 미납으로 3개월간 대여 금지'),
                                                                                                                              (2,6,1,'2025-07-08 12:01:09','2025-07-22','OVERDUE',NULL),
                                                                                                                              (2,13,1,'2025-07-08 12:01:33','2025-07-22','RENTED',NULL),
                                                                                                                              (3,7,1,'2025-07-08 12:01:40','2025-07-22','RENTED',NULL),
                                                                                                                              (3,14,1,'2025-07-08 12:00:16',NULL,'REJECTED','거절'),
                                                                                                                              (3,18,NULL,'2025-07-08 12:00:26',NULL,'REQUESTED',NULL);
