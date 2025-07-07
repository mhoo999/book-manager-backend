INSERT INTO user (
    email, name, phone, pwd, is_deleted, created_at
) VALUES ('example@email.com1','홍길동','010-1234-5678','$2a$12$R6Uu24.vh/XB2ASdxXbWsun6uBYXBNrNUQtGc9S6wn91o/uzaXPDq',0,NOW()), --hashed_password
         ('example@email.com2','홍길동2','010-1234-5678','$2a$12$R6Uu24.vh/XB2ASdxXbWsun6uBYXBNrNUQtGc9S6wn91o/uzaXPDq',0,NOW()) --user1234