`GET /api/v2/sqlQueries/{sqlQueryId}/getStatus`

Gets the status of a query.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:sql-queries-read`.

**OAuth2 scopes**: `api:sql-queries-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sqlQueryId` | string | 是 | The unique identifier for a query. Note that query IDs are not URL-safe and must be URL-encoded when used in API endpoints.<br>示例: `OikKBfqBaWRjOGQ3YTJkNzMtYjhiZi00ZDMzLTlkOWMtOGQzOW` |

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
| PERMISSION_DENIED | `QueryPermissionDenied` | The provided token does not have permission to access the given query. |
| INVALID_ARGUMENT | `QueryCanceled` | The query was canceled. |
| INTERNAL | `QueryFailed` | The query failed. |
| INVALID_ARGUMENT | `QueryParseError` | The query cannot be parsed. |
| INVALID_ARGUMENT | `QueryRunning` | The query is running. |
| PERMISSION_DENIED | `ReadQueryInputsPermissionDenied` | The provided token does not have permission to access the inputs to the query. |
| PERMISSION_DENIED | `GetStatusSqlQueryPermissionDenied` | Could not getStatus the SqlQuery. |
