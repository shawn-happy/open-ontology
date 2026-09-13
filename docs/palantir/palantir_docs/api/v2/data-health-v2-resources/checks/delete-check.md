`DELETE /api/v2/dataHealth/checks/{checkRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Delete the Check with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:data-health-write`.

**OAuth2 scopes**: `api:data-health-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `checkRid` | string | 是 | The unique resource identifier (RID) of a Data Health Check.<br>示例: `ri.data-health.main.check.8e27b13a-e21b-4232-ae1b-76ccf5ff42b3` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `DeleteCheckPermissionDenied` | Could not delete the Check. |
| NOT_FOUND | `CheckNotFound` | The given Check could not be found. |
