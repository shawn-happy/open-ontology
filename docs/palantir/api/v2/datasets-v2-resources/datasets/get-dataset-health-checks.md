`GET /api/v2/datasets/{datasetRid}/getHealthChecks`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the RIDs of the Data Health Checks that are configured for the given Dataset.


Third-party applications using this endpoint via OAuth2 must request the following operation scopes: `api:data-health-read api:datasets-read`.

**OAuth2 scopes**: `api:data-health-read` `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch. If none is provided, the default Branch name - `master` for most enrollments - will be used.<br>示例: `master` |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListHealthChecksResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListHealthChecksResponse` | object | 是 | 示例: `{"data":["ri.data-health.main.check.8e27b13a-e21b-4232-ae1b-76ccf5ff42b3"]}` |
| `ListHealthChecksResponse.data` | list<CheckRid> | 否 | — |
| `ListHealthChecksResponse.data.CheckRid` | string | 是 | The unique resource identifier (RID) of a Data Health Check. |

```json
{
  "data": [
    "ri.data-health.main.check.8e27b13a-e21b-4232-ae1b-76ccf5ff42b3"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| PERMISSION_DENIED | `GetDatasetHealthChecksPermissionDenied` | Could not getHealthChecks the Dataset. |
