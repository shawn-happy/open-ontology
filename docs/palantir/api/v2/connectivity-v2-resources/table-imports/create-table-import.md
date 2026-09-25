`POST /api/v2/connectivity/connections/{connectionRid}/tableImports`

Creates a new TableImport.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:connectivity-table-import-write`.

**OAuth2 scopes**: `api:connectivity-table-import-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `connectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source).<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |

## Request body

```json
{
  "datasetRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
  "importMode": "SNAPSHOT",
  "displayName": "My table import",
  "allowSchemaChanges": true,
  "branchName": "master",
  "config": {
    "type": "jdbcImportConfig",
    "query": "SELECT * FROM table"
  }
}
```

## Response

**TableImport**

The created TableImport

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `TableImport` | object | 是 | The created TableImport<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","importMode":"SNAPSHOT","displayName":"My table import","allowSchemaChanges":true,"connectionRid":"ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b","branchName":"master","rid":"ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158","config":{"type":"jdbcImportConfig","query":"SELECT * FROM table"}}` |
| `TableImport.rid` | string | 是 | The Resource Identifier (RID) of a TableImport (also known as a batch sync).<br>示例: `ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158` |
| `TableImport.connectionRid` | string | 是 | The RID of the Connection (also known as a source) that the Table Import uses to import data.<br>示例: `ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b` |
| `TableImport.datasetRid` | string | 是 | The RID of the output dataset. Can not be modified after the table import is created.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `TableImport.branchName` | string | 否 | The branch name in the output dataset that will contain the imported data. Defaults to `master` for most enrollments. Can not be modified after the table import is created.<br>示例: `master` |
| `TableImport.displayName` | string | 是 | 示例: `My table import` |
| `TableImport.importMode` | enum | 是 | Import mode governs how data is read from an external system, and written into a Foundry dataset.<br>SNAPSHOT: Defines a new dataset state consisting only of data from a particular import execution.<br>APPEND: Purely additive and yields data from previous import executions in addition to newly added data.<br>示例: `SNAPSHOT` |
| `TableImport.allowSchemaChanges` | boolean | 是 | Allow the TableImport to succeed if the schema of imported rows does not match the existing dataset's schema. Defaults to false for new table imports.<br>示例: `true` |
| `TableImport.config` | union | 是 | The import configuration for a specific [connector type](/docs/foundry/data-integration/source-type-overview).<br>示例: `{"type":"jdbcImportConfig","query":"SELECT * FROM table"}` |
| `TableImport.config.databricksImportConfig` | object | 否 | The table import configuration for a [Databricks connection](/docs/foundry/available-connectors/databricks). |
| `TableImport.config.databricksImportConfig.query` | string | 是 | A single SQL query can be executed per sync, which should output a data table<br>and avoid operations like invoking stored procedures.<br>The query results are saved to the output dataset in Foundry.<br>示例: `SELECT * FROM table` |
| `TableImport.config.databricksImportConfig.initialIncrementalState` | union | 否 | The incremental configuration for a table import enables append-style transactions from the same table without duplication of data.<br>You must provide a monotonically increasing column such as a timestamp or id and an initial value for this column.<br>An incremental table import will import rows where the value is greater than the largest already imported.<br>You can use the '?' character to reference the incremental state value when constructing your query.<br>Normally this would be used in a WHERE clause or similar filter applied in order to only sync data with an incremental column value<br>larger than the previously observed maximum value stored in the incremental state. |
| `TableImport.config.databricksImportConfig.initialIncrementalState.stringColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a string data type. |
| `TableImport.config.databricksImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myStringColumn` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the string column to reference in the query. |
| `TableImport.config.databricksImportConfig.initialIncrementalState.dateColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a date type. |
| `TableImport.config.databricksImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myDateColumn` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the date column to reference in the query.<br>示例: `2024-12-31` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.integerColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a numeric integer datatype. |
| `TableImport.config.databricksImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.currentValue` | integer | 是 | The initial incremental state value for the integer column to reference in the query.<br>示例: `1` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState` | object | 否 | — |
| `TableImport.config.databricksImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myTimeColumn` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the timestamp column in UTC to reference in the query.<br>示例: `2020-09-30T14:30:00Z` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.longColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a numeric long datatype. |
| `TableImport.config.databricksImportConfig.initialIncrementalState.longColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.longColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the long column to reference in the query.<br>示例: `1000` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a decimal data type. |
| `TableImport.config.databricksImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myPriceColumn` |
| `TableImport.config.databricksImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the decimal column to reference in the query.<br>示例: `3.25` |
| `TableImport.config.jdbcImportConfig` | object | 否 | The import configuration for a [custom JDBC connection](/docs/foundry/available-connectors/custom-jdbc-sources). |
| `TableImport.config.jdbcImportConfig.query` | string | 是 | A single SQL query can be executed per sync, which should output a data table<br>and avoid operations like invoking stored procedures.<br>The query results are saved to the output dataset in Foundry.<br>示例: `SELECT * FROM table` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState` | union | 否 | The incremental configuration for a table import enables append-style transactions from the same table without duplication of data.<br>You must provide a monotonically increasing column such as a timestamp or id and an initial value for this column.<br>An incremental table import will import rows where the value is greater than the largest already imported.<br>You can use the '?' character to reference the incremental state value when constructing your query.<br>Normally this would be used in a WHERE clause or similar filter applied in order to only sync data with an incremental column value<br>larger than the previously observed maximum value stored in the incremental state. |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.stringColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a string data type. |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myStringColumn` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the string column to reference in the query. |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.dateColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a date type. |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myDateColumn` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the date column to reference in the query.<br>示例: `2024-12-31` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.integerColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a numeric integer datatype. |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.currentValue` | integer | 是 | The initial incremental state value for the integer column to reference in the query.<br>示例: `1` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState` | object | 否 | — |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myTimeColumn` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the timestamp column in UTC to reference in the query.<br>示例: `2020-09-30T14:30:00Z` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.longColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a numeric long datatype. |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.longColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.longColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the long column to reference in the query.<br>示例: `1000` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a decimal data type. |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myPriceColumn` |
| `TableImport.config.jdbcImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the decimal column to reference in the query.<br>示例: `3.25` |
| `TableImport.config.microsoftSqlServerImportConfig` | object | 否 | The import configuration for a [Microsoft SQL Server connection](/docs/foundry/available-connectors/microsoft-sql-server). |
| `TableImport.config.microsoftSqlServerImportConfig.query` | string | 是 | A single SQL query can be executed per sync, which should output a data table<br>and avoid operations like invoking stored procedures.<br>The query results are saved to the output dataset in Foundry.<br>示例: `SELECT * FROM table` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState` | union | 否 | The incremental configuration for a table import enables append-style transactions from the same table without duplication of data.<br>You must provide a monotonically increasing column such as a timestamp or id and an initial value for this column.<br>An incremental table import will import rows where the value is greater than the largest already imported.<br>You can use the '?' character to reference the incremental state value when constructing your query.<br>Normally this would be used in a WHERE clause or similar filter applied in order to only sync data with an incremental column value<br>larger than the previously observed maximum value stored in the incremental state. |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.stringColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a string data type. |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myStringColumn` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the string column to reference in the query. |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.dateColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a date type. |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myDateColumn` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the date column to reference in the query.<br>示例: `2024-12-31` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.integerColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a numeric integer datatype. |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.currentValue` | integer | 是 | The initial incremental state value for the integer column to reference in the query.<br>示例: `1` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState` | object | 否 | — |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myTimeColumn` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the timestamp column in UTC to reference in the query.<br>示例: `2020-09-30T14:30:00Z` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.longColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a numeric long datatype. |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.longColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.longColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the long column to reference in the query.<br>示例: `1000` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a decimal data type. |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myPriceColumn` |
| `TableImport.config.microsoftSqlServerImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the decimal column to reference in the query.<br>示例: `3.25` |
| `TableImport.config.postgreSqlImportConfig` | object | 否 | The import configuration for a [PostgreSQL connection](/docs/foundry/available-connectors/postgresql). |
| `TableImport.config.postgreSqlImportConfig.query` | string | 是 | A single SQL query can be executed per sync, which should output a data table<br>and avoid operations like invoking stored procedures.<br>The query results are saved to the output dataset in Foundry.<br>示例: `SELECT * FROM table` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState` | union | 否 | The incremental configuration for a table import enables append-style transactions from the same table without duplication of data.<br>You must provide a monotonically increasing column such as a timestamp or id and an initial value for this column.<br>An incremental table import will import rows where the value is greater than the largest already imported.<br>You can use the '?' character to reference the incremental state value when constructing your query.<br>Normally this would be used in a WHERE clause or similar filter applied in order to only sync data with an incremental column value<br>larger than the previously observed maximum value stored in the incremental state. |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.stringColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a string data type. |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myStringColumn` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the string column to reference in the query. |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.dateColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a date type. |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myDateColumn` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the date column to reference in the query.<br>示例: `2024-12-31` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.integerColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a numeric integer datatype. |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.currentValue` | integer | 是 | The initial incremental state value for the integer column to reference in the query.<br>示例: `1` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState` | object | 否 | — |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myTimeColumn` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the timestamp column in UTC to reference in the query.<br>示例: `2020-09-30T14:30:00Z` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.longColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a numeric long datatype. |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.longColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.longColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the long column to reference in the query.<br>示例: `1000` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a decimal data type. |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myPriceColumn` |
| `TableImport.config.postgreSqlImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the decimal column to reference in the query.<br>示例: `3.25` |
| `TableImport.config.microsoftAccessImportConfig` | object | 否 | The import configuration for a [Microsoft Access connection](/docs/foundry/available-connectors/microsoft-access). |
| `TableImport.config.microsoftAccessImportConfig.query` | string | 是 | A single SQL query can be executed per sync, which should output a data table<br>and avoid operations like invoking stored procedures.<br>The query results are saved to the output dataset in Foundry.<br>示例: `SELECT * FROM table` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState` | union | 否 | The incremental configuration for a table import enables append-style transactions from the same table without duplication of data.<br>You must provide a monotonically increasing column such as a timestamp or id and an initial value for this column.<br>An incremental table import will import rows where the value is greater than the largest already imported.<br>You can use the '?' character to reference the incremental state value when constructing your query.<br>Normally this would be used in a WHERE clause or similar filter applied in order to only sync data with an incremental column value<br>larger than the previously observed maximum value stored in the incremental state. |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.stringColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a string data type. |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myStringColumn` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the string column to reference in the query. |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.dateColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a date type. |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myDateColumn` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the date column to reference in the query.<br>示例: `2024-12-31` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.integerColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a numeric integer datatype. |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.currentValue` | integer | 是 | The initial incremental state value for the integer column to reference in the query.<br>示例: `1` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState` | object | 否 | — |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myTimeColumn` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the timestamp column in UTC to reference in the query.<br>示例: `2020-09-30T14:30:00Z` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.longColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a numeric long datatype. |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.longColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.longColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the long column to reference in the query.<br>示例: `1000` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a decimal data type. |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myPriceColumn` |
| `TableImport.config.microsoftAccessImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the decimal column to reference in the query.<br>示例: `3.25` |
| `TableImport.config.snowflakeImportConfig` | object | 否 | The table import configuration for a [Snowflake connection](/docs/foundry/available-connectors/snowflake). |
| `TableImport.config.snowflakeImportConfig.query` | string | 是 | A single SQL query can be executed per sync, which should output a data table<br>and avoid operations like invoking stored procedures.<br>The query results are saved to the output dataset in Foundry.<br>示例: `SELECT * FROM table` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState` | union | 否 | The incremental configuration for a table import enables append-style transactions from the same table without duplication of data.<br>You must provide a monotonically increasing column such as a timestamp or id and an initial value for this column.<br>An incremental table import will import rows where the value is greater than the largest already imported.<br>You can use the '?' character to reference the incremental state value when constructing your query.<br>Normally this would be used in a WHERE clause or similar filter applied in order to only sync data with an incremental column value<br>larger than the previously observed maximum value stored in the incremental state. |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.stringColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a string data type. |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myStringColumn` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the string column to reference in the query. |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.dateColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a date type. |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myDateColumn` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the date column to reference in the query.<br>示例: `2024-12-31` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.integerColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a numeric integer datatype. |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.currentValue` | integer | 是 | The initial incremental state value for the integer column to reference in the query.<br>示例: `1` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState` | object | 否 | — |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myTimeColumn` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the timestamp column in UTC to reference in the query.<br>示例: `2020-09-30T14:30:00Z` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.longColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a numeric long datatype. |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.longColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.longColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the long column to reference in the query.<br>示例: `1000` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a decimal data type. |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myPriceColumn` |
| `TableImport.config.snowflakeImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the decimal column to reference in the query.<br>示例: `3.25` |
| `TableImport.config.oracleImportConfig` | object | 否 | The import configuration for an Oracle Database 21 connection. |
| `TableImport.config.oracleImportConfig.query` | string | 是 | A single SQL query can be executed per sync, which should output a data table<br>and avoid operations like invoking stored procedures.<br>The query results are saved to the output dataset in Foundry.<br>示例: `SELECT * FROM table` |
| `TableImport.config.oracleImportConfig.initialIncrementalState` | union | 否 | The incremental configuration for a table import enables append-style transactions from the same table without duplication of data.<br>You must provide a monotonically increasing column such as a timestamp or id and an initial value for this column.<br>An incremental table import will import rows where the value is greater than the largest already imported.<br>You can use the '?' character to reference the incremental state value when constructing your query.<br>Normally this would be used in a WHERE clause or similar filter applied in order to only sync data with an incremental column value<br>larger than the previously observed maximum value stored in the incremental state. |
| `TableImport.config.oracleImportConfig.initialIncrementalState.stringColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a string data type. |
| `TableImport.config.oracleImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myStringColumn` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.stringColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the string column to reference in the query. |
| `TableImport.config.oracleImportConfig.initialIncrementalState.dateColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a date type. |
| `TableImport.config.oracleImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myDateColumn` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.dateColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the date column to reference in the query.<br>示例: `2024-12-31` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.integerColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a numeric integer datatype. |
| `TableImport.config.oracleImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.integerColumnInitialIncrementalState.currentValue` | integer | 是 | The initial incremental state value for the integer column to reference in the query.<br>示例: `1` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState` | object | 否 | — |
| `TableImport.config.oracleImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myTimeColumn` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.timestampColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the timestamp column in UTC to reference in the query.<br>示例: `2020-09-30T14:30:00Z` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.longColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a numeric long datatype. |
| `TableImport.config.oracleImportConfig.initialIncrementalState.longColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myIdColumn` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.longColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the long column to reference in the query.<br>示例: `1000` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState` | object | 否 | The state for an incremental table import using a column with a decimal data type. |
| `TableImport.config.oracleImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.columnName` | string | 是 | 示例: `myPriceColumn` |
| `TableImport.config.oracleImportConfig.initialIncrementalState.decimalColumnInitialIncrementalState.currentValue` | string | 是 | The initial incremental state value for the decimal column to reference in the query.<br>示例: `3.25` |

```json
{
  "datasetRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
  "importMode": "SNAPSHOT",
  "displayName": "My table import",
  "allowSchemaChanges": true,
  "connectionRid": "ri.magritte..source.c078b71b-92f9-41b6-b0df-3760f411120b",
  "branchName": "master",
  "rid": "ri.magritte..extract.27bb4f2b-63b8-44b8-a579-4e2bd65ba158",
  "config": {
    "type": "jdbcImportConfig",
    "query": "SELECT * FROM table"
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INTERNAL | `TableImportTypeNotSupported` | The specified table import type is not yet supported in the Platform API. |
| INTERNAL | `ConnectionDetailsNotDetermined` | Details of the connection (such as which types of import it supports) could not be determined. |
| INVALID_ARGUMENT | `TableImportNotSupportedForConnection` | The specified connection does not support creating or replacing a table import with the specified config. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| PERMISSION_DENIED | `CreateTableImportPermissionDenied` | Could not create the TableImport. |
| NOT_FOUND | `ConnectionNotFound` | The given Connection could not be found. |
