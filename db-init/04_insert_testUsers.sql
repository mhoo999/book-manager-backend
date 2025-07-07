INSERT INTO user (
    email, name, phone, pwd, is_deleted, created_at
) VALUES ('example@email.com1','홍길동','010-1234-5678','$2a$12$R6Uu24.vh/XB2ASdxXbWsun6uBYXBNrNUQtGc9S6wn91o/uzaXPDq',0,NOW()),  /* hashed_password */
         ('example@email.com2','홍길동2','010-1234-5628','$2a$10$nLjPphIzVRvsxtcPy5BYmeKxVrlmKDjeLZwQtgeazw6FKRj.kv4Uy',0,NOW()),/* user1234 */
         ('example@email.com3','홍길동3','010-1234-5278','$2a$10$nLjPphIzVRvsxtcPy5BYmeKxVrlmKDjeLZwQtgeazw6FKRj.kv4Uy',0,NOW());  /* user1234 */