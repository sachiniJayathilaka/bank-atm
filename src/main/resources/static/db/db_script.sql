CREATE
database hsbc_atm;
------------------------
INSERT INTO atm(id)
VALUES (1);

INSERT INTO atm_funds(id, cash_note, no_of_cash_notes, atm_id)
VALUES (1, 'FIFTY', 10, 1);

INSERT INTO atm_funds(id, cash_note, no_of_cash_notes, atm_id)
VALUES (2, 'TWENTY', 30, 1);

INSERT INTO atm_funds(id, cash_note, no_of_cash_notes, atm_id)
VALUES (3, 'TEN', 30, 1);

INSERT INTO atm_funds(id, cash_note, no_of_cash_notes, atm_id)
VALUES (4, 'FIVE', 20, 1);