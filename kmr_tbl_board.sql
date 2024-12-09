
CREATE SEQUENCE seq_board;
drop table tbl_board;
create table tbl_board(
    bno number(10,0),
    title VARCHAR2(200) not null,
    content VARCHAR2(2000) not null,
    writer VARCHAR2(50) not null,
    regdate date DEFAULT sysdate,
    updatedate date DEFAULT sysdate
);

alter table tbl_board add constraint pk_board primary key (bno);
SELECT * from tbl_board;

INSERT into tbl_board (bno, title, content, writer)
VALUES (seq_board.nextval, '테스트 제목', '테스트 내용', 'user00');

commit;