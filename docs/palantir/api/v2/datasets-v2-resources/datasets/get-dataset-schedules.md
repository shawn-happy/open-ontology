`GET /api/v2/datasets/{datasetRid}/getSchedules`

Get the RIDs of the Schedules that target the given Dataset.

Note: It may take up to an hour for recent changes to schedules to be reflected in this response,
especially for schedules managed by Marketplace. This operation will return outdated results in the
meantime.


Third-party applications using this endpoint via OAuth2 must request the following operation scopes: `api:orchestration-read api:datasets-read`.

**OAuth2 scopes**: `api:orchestration-read` `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branchName` | string | 否 | The name of the Branch. If none is provided, the default Branch name - `master` for most enrollments - will be used.<br>示例: `master` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListSchedulesResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListSchedulesResponse` | object | 是 | 示例: `{"data":["ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871"],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListSchedulesResponse.data` | list<ScheduleRid> | 否 | — |
| `ListSchedulesResponse.data.ScheduleRid` | string | 是 | The RID of a Schedule. |
| `ListSchedulesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    "ri.scheduler.main.schedule.5ad5c340-59f3-4a60-9fc6-161bb984f871"
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `BranchNotFound` | The requested branch could not be found, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| PERMISSION_DENIED | `GetDatasetSchedulesPermissionDenied` | Could not getSchedules the Dataset. |
