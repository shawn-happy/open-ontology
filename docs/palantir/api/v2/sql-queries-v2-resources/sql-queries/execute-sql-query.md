`POST /api/v2/sqlQueries/execute`

Executes a new query. Only the user that invoked the query can operate on the query. The size of query
results are limited by default to 1 million rows. Contact your Palantir representative to discuss limit
increases.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:sql-queries-execute`.

**OAuth2 scopes**: `api:sql-queries-execute`

## Request body

```json
{
  "fallbackBranchIds": [
    "master"
  ],
  "serializationFormat": "CSV",
  "query": "SELECT * FROM `/Path/To/Dataset`"
}
```

## Response

**QueryStatus**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `QueryStatus` | union | 是 | — |
| `QueryStatus.running` | object | 否 | — |
| `QueryStatus.running.queryId` | string | 是 | The identifier of a SQL Query.<br>示例: `OikKBfqBaWRjOGQ3YTJkNzMtYjhiZi00ZDMzLTlkOWMtOGQzOW` |
| `QueryStatus.canceled` | object | 否 | — |
| `QueryStatus.failed` | object | 否 | — |
| `QueryStatus.failed.errorMessage` | string | 是 | An error message describing why the query failed.<br>示例: `Failed to execute SQL query` |
| `QueryStatus.succeeded` | object | 否 | — |
| `QueryStatus.succeeded.queryId` | string | 是 | The identifier of a SQL Query.<br>示例: `OikKBfqBaWRjOGQ3YTJkNzMtYjhiZi00ZDMzLTlkOWMtOGQzOW` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `ColumnTypesNotSupported` | The query result contains column types that are not supported by the requested serialization format. |
| PERMISSION_DENIED | `ReadQueryInputsPermissionDenied` | The provided token does not have permission to access the inputs to the query. |
| INVALID_ARGUMENT | `QueryParseError` | The query cannot be parsed. |
| INVALID_ARGUMENT | `QueryCanceled` | The query was canceled. |
| PERMISSION_DENIED | `QueryPermissionDenied` | The provided token does not have permission to access the given query. |
| INTERNAL | `QueryFailed` | The query failed. |
| INVALID_ARGUMENT | `QueryRunning` | The query is running. |
| PERMISSION_DENIED | `ExecuteSqlQueryPermissionDenied` | Could not execute the SqlQuery. |
