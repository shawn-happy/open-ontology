`POST /api/v2/models/liveDeployments/{liveDeploymentRid}/disable`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Disables the live deployment and removes its running replicas while retaining its model and runtime configuration.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-write`.

**OAuth2 scopes**: `api:models-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `liveDeploymentRid` | string | 是 | The Resource Identifier (RID) of a Live Deployment.<br>示例: `ri.foundry-ml-live.main.live-deployment.f351c142-0e4c-4b12-adc2-6e1539737ae9` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `LiveDeploymentNotFound` | The specified live deployment was not found. |
| PERMISSION_DENIED | `DisableLiveDeploymentPermissionDenied` | Could not disable the LiveDeployment. |
