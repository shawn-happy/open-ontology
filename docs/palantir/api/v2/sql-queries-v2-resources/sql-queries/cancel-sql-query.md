`POST /api/v2/sqlQueries/{sqlQueryId}/cancel`

Cancels a query. If the query is no longer running this is effectively a no-op.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:sql-queries-execute`.

**OAuth2 scopes**: `api:sql-queries-execute`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sqlQueryId` | string | 是 | The unique identifier for a query. Note that query IDs are not URL-safe and must be URL-encoded when used in API endpoints.<br>示例: `OikKBfqBaWRjOGQ3YTJkNzMtYjhiZi00ZDMzLTlkOWMtOGQzOW` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `QueryPermissionDenied` | The provided token does not have permission to access the given query. |
| INVALID_ARGUMENT | `QueryCanceled` | The query was canceled. |
| INTERNAL | `QueryFailed` | The query failed. |
| INVALID_ARGUMENT | `QueryParseError` | The query cannot be parsed. |
| INVALID_ARGUMENT | `QueryRunning` | The query is running. |
| PERMISSION_DENIED | `ReadQueryInputsPermissionDenied` | The provided token does not have permission to access the inputs to the query. |
| PERMISSION_DENIED | `CancelSqlQueryPermissionDenied` | Could not cancel the SqlQuery. |
