CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_expenses_fk_category ON expenses(fk_category);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_expenses_source ON expenses(source);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_expenses_operation_date ON expenses(operation_date);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_expenses_transaction_date ON expenses(transaction_date);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_expenses_source ON expenses(source);
