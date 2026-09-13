`GET /api/v2/sqlQueries/{sqlQueryId}/getResults`

Gets the results of a query. Results are returned in the `serializationFormat` specified at execute time
(defaulting to [Apache Arrow](https://arrow.apache.org/) if no format is provided).

This endpoint implements long polling and requests will time out after one minute. They can be safely
retried while the query is still running.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:sql-queries-read`.

**OAuth2 scopes**: `api:sql-queries-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sqlQueryId` | string | 是 | The unique identifier for a query. Note that query IDs are not URL-safe and must be URL-encoded when used in API endpoints.<br>示例: `OikKBfqBaWRjOGQ3YTJkNzMtYjhiZi00ZDMzLTlkOWMtOGQzOW` |

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `QueryCanceled` | The query was canceled. |
| INTERNAL | `QueryFailed` | The query failed. |
| INVALID_ARGUMENT | `QueryParseError` | The query cannot be parsed. |
| INVALID_ARGUMENT | `QueryRunning` | The query is running. |
| PERMISSION_DENIED | `ReadQueryInputsPermissionDenied` | The provided token does not have permission to access the inputs to the query. |
| PERMISSION_DENIED | `QueryPermissionDenied` | The provided token does not have permission to access the given query. |
| PERMISSION_DENIED | `GetResultsSqlQueryPermissionDenied` | Could not getResults the SqlQuery. |
