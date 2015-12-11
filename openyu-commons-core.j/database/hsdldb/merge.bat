@echo off
if exist all.sql @echo --- Deleting all.sql ---
if exist all.sql del all.sql

@echo --- Merging *.sql to all.sql ---
copy *.sql all.sql

pause