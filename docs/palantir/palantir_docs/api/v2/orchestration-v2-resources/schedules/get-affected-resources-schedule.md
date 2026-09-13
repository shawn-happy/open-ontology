`POST /api/v2/orchestration/schedules/{scheduleRid}/getAffectedResources`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:orchestration-read`.

**OAuth2 scopes**: `api:orchestration-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**AffectedResourcesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `AffectedResourcesResponse` | object | 是 | — |
| `AffectedResourcesResponse.datasets` | list<BuildableRid> | 否 | — |
| `AffectedResourcesResponse.datasets.BuildableRid` | string | 是 | The Resource Identifier (RID) of a Resource that can be built. For example, this is a Dataset RID, Media Set<br>RID or Restricted View RID. |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetAffectedResourcesSchedulePermissionDenied` | Could not getAffectedResources the Schedule. |
